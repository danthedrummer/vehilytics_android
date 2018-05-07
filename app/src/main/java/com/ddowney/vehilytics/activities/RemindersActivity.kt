package com.ddowney.vehilytics.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.ddowney.vehilytics.R
import com.ddowney.vehilytics.adapters.RemindersAdapter
import com.ddowney.vehilytics.helpers.DanCompatActivity
import com.ddowney.vehilytics.helpers.callbacks.VehilyticsCallback
import com.ddowney.vehilytics.helpers.listeners.RecyclerViewClickListener
import com.ddowney.vehilytics.models.Reminder
import com.ddowney.vehilytics.services.ServiceManager
import kotlinx.android.synthetic.main.activity_reminders.*
import retrofit2.Call
import retrofit2.Response
import java.util.*

/**
 * The reminders activity provides a way for a user to quickly create
 * recurring reminders in their calendar to perform vehicle maintenance
 */
class RemindersActivity : DanCompatActivity() {

    companion object {
        private const val LOG_TAG = "RemindersActivity"
        private const val CREATE_REMINDER = 1
    }

    private lateinit var remindersAdapter: RemindersAdapter
    private var reminderList: List<Reminder> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)
        setSupportActionBar(reminders_toolbar)

        getRemindersList()

        reminders_recycler.setHasFixedSize(true)
        reminders_recycler.layoutManager = LinearLayoutManager(this)

    }

    /**
     * Hides loading bars and displays main content
     */
    private fun displayMainContent() {
        updateAdapter()
        reminders_content_loading.visibility = View.GONE
        reminders_recycler.visibility = View.VISIBLE
    }

    /**
     * Updates the recycler view adapter with the new reminderList
     */
    private fun updateAdapter() {
        remindersAdapter = RemindersAdapter(reminderList, object: RecyclerViewClickListener {
            override fun onItemClicked(position: Int) {
                createCalendarReminder(reminderList[position])
            }

        })
        reminders_recycler.adapter = remindersAdapter
        reminders_recycler.hasPendingAdapterUpdates()
    }

    /**
     * Triggers the calendar app to open for creating a new reminder populated
     * with fields from the selected option
     *
     * @param reminder: The selected reminder to populate the Calendar entry
     */
    private fun createCalendarReminder(reminder: Reminder) {
        val calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE), 10, 0, 0)
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.timeInMillis+60*60*1000)
        intent.putExtra(Events.ALL_DAY, false)
        intent.putExtra(Events.RRULE, "FREQ=WEEKLY;INTERVAL=${reminder.weeklyFrequency}")
        intent.putExtra(Events.TITLE, reminder.title)
        intent.putExtra(Events.DESCRIPTION, reminder.description)
        startActivityForResult(intent, CREATE_REMINDER)
    }

    /**
     * Makes a request to get a suggested reminder list from the web service
     */
    private fun getRemindersList() {
        ServiceManager.remindersService.getRemindersList()
                .enqueue(object : VehilyticsCallback<List<Reminder>>(baseContext) {
                    override fun onResponse(call: Call<List<Reminder>>?,
                                            response: Response<List<Reminder>>?) {
                        super.onResponse(call, response)
                        reminderList = response?.body() ?: listOf()
                        displayMainContent()
                    }
                })
    }

    /**
     * Creates a snackbar message that pops up from the bottom of
     * the screen
     *
     * @param message: The message to be displayed
     */
    private fun makeSnackText(message: String) {
        val snack = Snackbar.make(reminders_layout, message, Snackbar.LENGTH_LONG)
        snack.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                .setTextColor(Color.WHITE)
        snack.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_REMINDER) {
            if (resultCode == Activity.RESULT_OK) {
                makeSnackText("Reminder created")
            } else if (resultCode == Activity.RESULT_CANCELED) {
                makeSnackText("Reminder cancelled")
            }
        }
    }
}
