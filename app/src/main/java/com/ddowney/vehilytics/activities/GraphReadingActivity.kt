package com.ddowney.vehilytics.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.Vehilytics
import com.ddowney.vehilytics.helpers.DanCompatActivity
import com.ddowney.vehilytics.helpers.callbacks.VehilyticsCallback
import com.ddowney.vehilytics.models.Reading
import com.ddowney.vehilytics.models.ReadingsResponse
import com.ddowney.vehilytics.models.Sensor
import com.ddowney.vehilytics.services.ServiceManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_graph_reading.*
import retrofit2.Call
import retrofit2.Response

/**
 * This is the graphing activity. It will display the given readings for a
 * sensor in a readable way along with ranges (if applicable) and any
 * additional information
 */
class GraphReadingActivity : DanCompatActivity() {

    companion object {
        private const val LOG_TAG = "GraphActivity"
    }

    private lateinit var sensor: Sensor

    private var readings: List<Reading> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_reading)

        if (!intent.hasExtra("sensor") || intent.extras["sensor"] !is Sensor) {
            finish()
            return
        }

        sensor = intent.extras["sensor"] as Sensor

        graph_reading_toolbar.title = sensor.name
        setSupportActionBar(graph_reading_toolbar)

        getReadingsForSensor()
    }

    /**
     * Hides the loading bar and displays the real content
     */
    private fun displayMainContent() {
        graph_content_loading.visibility = View.GONE
        graph_wrapper.visibility = View.VISIBLE
        readings_info_wrapper.visibility = View.VISIBLE
    }

    /**
     * Makes a request to get all readings for the selected sensor
     */
    private fun getReadingsForSensor() {
        ServiceManager.readingsService.getReadingsForSensor(Vehilytics.user.email, Vehilytics.user.token,
                sensor.shortname).enqueue(object: VehilyticsCallback<ReadingsResponse>(baseContext) {
            override fun onResponse(call: Call<ReadingsResponse>?, response: Response<ReadingsResponse>?) {
                super.onResponse(call, response)
                readings = response?.body()?.readings ?: listOf()
                readings_info.text = response?.body()?.info ?: "No info provided"
                populateGraph(response?.body()?.upperRange, response?.body()?.lowerRange)
                displayMainContent()
            }
        })
    }

    /**
     * Populates the graph with the information retrieved from the server
     */
    private fun populateGraph(upperRange: Float?, lowerRange: Float?) {
        val graphEntries: MutableList<Entry> = mutableListOf()
        val upperRangeEntries: MutableList<Entry> = mutableListOf()
        val lowerRangeEntries: MutableList<Entry> = mutableListOf()

        Log.d(LOG_TAG, "upper = $upperRange")
        Log.d(LOG_TAG, "lower = $lowerRange")

        readings.forEach { reading ->
            graphEntries.add(Entry(graphEntries.size.toFloat(), reading.value.toFloat()))
            if (upperRange != null) {
                upperRangeEntries.add(Entry(upperRangeEntries.size.toFloat(), upperRange))
            }
            if (lowerRange != null) {
                lowerRangeEntries.add(Entry(lowerRangeEntries.size.toFloat(), lowerRange))
            }
        }

        Log.d(LOG_TAG, "${graphEntries.size}, ${upperRangeEntries.size}, ${lowerRangeEntries.size}")

        val setActual = LineDataSet(graphEntries, "Actual")
        setActual.axisDependency = YAxis.AxisDependency.LEFT
        setActual.color = ContextCompat.getColor(this, R.color.colorPrimary)
        setActual.circleColors = listOf(ContextCompat.getColor(this, R.color.colorAccent))
        setActual.lineWidth = 2f
        setActual.circleRadius = 3f
        setActual.fillAlpha = 90

        val setUpper = LineDataSet(upperRangeEntries, "Upper Limit")
        setUpper.axisDependency = YAxis.AxisDependency.LEFT
        setUpper.color = ContextCompat.getColor(this, R.color.colorError)
        setUpper.circleColors = listOf(ContextCompat.getColor(this, R.color.colorError))
        setUpper.lineWidth = 1f
        setUpper.circleRadius = 1f
        setUpper.fillAlpha = 45

        val setLower = LineDataSet(lowerRangeEntries, "Lower Limit")
        setLower.axisDependency = YAxis.AxisDependency.LEFT
        setLower.color = ContextCompat.getColor(this, R.color.colorErrorLight)
        setLower.circleColors = listOf(ContextCompat.getColor(this, R.color.colorErrorLight))
        setLower.lineWidth = 1f
        setLower.circleRadius = 1f
        setLower.fillAlpha = 45

        val data = if (upperRange == null && lowerRange == null) {
            LineData(setActual)
        } else if (upperRange == null) {
            LineData(setActual, setLower)
        } else if (lowerRange == null) {
            LineData(setActual, setUpper)
        } else {
            LineData(setActual, setUpper, setLower)
        }

        data.setValueTextColor(R.color.colorDivider)
        data.setValueTextSize(9f)

        readings_chart.clear()
        readings_chart.data = data
        val description = Description()
        description.text = "${sensor.name} (${sensor.unit})"
        readings_chart.description = description

        var min = Float.MAX_VALUE
        var max = Float.MIN_VALUE
        graphEntries.forEach { entry ->
            min = Math.min(min, entry.y)
            max = Math.max(max, entry.y)
        }

        readings_chart.axisLeft.axisMinimum = min - ((max - min) / 2)
        readings_chart.axisRight.setDrawLabels(false)
        readings_chart.xAxis.setDrawLabels(false)
    }
}
