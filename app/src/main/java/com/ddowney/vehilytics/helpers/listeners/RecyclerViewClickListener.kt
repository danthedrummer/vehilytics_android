package com.ddowney.vehilytics.helpers.listeners

/**
 * Provides a means for a recycler view to callback into the activity to
 * be used as a trigger for methods requiring context etc.
 */
interface RecyclerViewClickListener {
    fun onItemClicked(position: Int)
}