package com.example.handymanapplication.ui.Scheduler

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.handymanapplication.Models.EventItem
import java.util.*
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.EventDialogAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_calendar.*
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import ru.cleverpumpkin.calendar.extension.getColorInt
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class MyScheduleFragment : Fragment() {

    val eventItems = mutableListOf<EventItem>()
    var list = ArrayList<Any>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jobs()

        return inflater.inflate(
            R.layout.activity_calendar,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        val calendar = Calendar.getInstance()

        calendar.set(2020, Calendar.MARCH, 21)
        val initialDate = CalendarDate(calendar.time)

        val preselectedDates = mutableListOf<CalendarDate>()

        calendar.set(2018, Calendar.JUNE, 13)
        preselectedDates += CalendarDate(calendar.time)

        calendar.set(2018, Calendar.JUNE, 16)
        preselectedDates += CalendarDate(calendar.time)

        calendar.set(2018, Calendar.JUNE, 19)
        preselectedDates += CalendarDate(calendar.time)

        calendar.set(2018, Calendar.MAY, 28)
        val minDate = CalendarDate(calendar.time)

        calendar.set(2018, Calendar.JULY, 2)
        val maxDate = CalendarDate(calendar.time)

        calendarView.setupCalendar(

            initialDate = initialDate,
            minDate = minDate,
            maxDate = maxDate,
            selectionMode = CalendarView.SelectionMode.MULTIPLE
            //, selectedDates = preselectedDates
        )
//        calendarView.dateSelectionFilter = { date ->
//            date.dayOfWeek != Calendar.SUNDAY
//        }

        with(calendarView) {
            setDrawGridOnSelectedDates(drawGrid = true)
            setGridColorRes(R.color.custom_calendar_grid_color)

            setYearSelectionBarBackgroundColorRes(R.color.custom_calendar_year_selection_background)
            setYearSelectionBarArrowsColorRes(R.color.custom_calendar_year_selection_arrows_color)
            setYearSelectionBarTextColorRes(R.color.custom_calendar_year_selection_text_color)

            setDaysBarBackgroundColorRes(R.color.custom_calendar_days_bar_background)
            setDaysBarTextColorRes(R.color.custom_calendar_days_bar_text_color)

            setMonthTextColorRes(R.color.custom_calendar_month_text_color)

            setDateCellBackgroundRes(R.drawable.custom_date_bg_selector)
        }



        calendarView.onDateClickListener = { date ->
            showDialogWithEventsForSpecificDate(date)
        }

        if (savedInstanceState == null) {
            calendarView.setupCalendar(selectionMode = CalendarView.SelectionMode.NONE)
        }

    }


    fun showDialogWithEventsForSpecificDate(date: CalendarDate) {

        val eventItems = calendarView.getDateIndicators(date)
            .filterIsInstance<EventItem>()
            .toTypedArray()

        if (eventItems.isNotEmpty()) {
            val adapter = EventDialogAdapter(context!!, eventItems)

            val builder = AlertDialog.Builder(context!!)
                .setTitle("$date")
                .setAdapter(adapter, null)

            val dialog = builder.create()
            dialog.show()
        }
    }


    private fun generateEventItems(): List<EventItem> {


        val calendar = Calendar.getInstance()
        val ex4 = CalendarDate(Utils.stringToCalendar("3/4/2020", "dd/MM/yyyy").time)

        calendar.set(2020, 4, 16)
        val ex = CalendarDate(calendar.time)
        calendar.set(2020, Calendar.MARCH, 17)
        val ex2 = CalendarDate(calendar.time)

        // call the fun
//        eventItems += EventItem(
//            eventName = "Event #1",
//            date = ex4,
//            color = activity!!.getColorInt(R.color.event_1_color)
//        )
//        eventItems += EventItem(
//            eventName = "Event #1",
//            date = ex2,
//            color = activity!!.getColorInt(R.color.event_2_color)
//        )
//        eventItems += EventItem(
//            eventName = "Event #3",
//            date = ex,
//            color = activity!!.getColorInt(R.color.event_1_color)
//        )
//        eventItems += EventItem(
//            eventName = "Event #5",
//            date = ex2,
//            color = activity!!.getColorInt(R.color.event_5_color)
//        )

//        }

        return eventItems
    }

    private fun jobs() {

        Fuel.get(Utils.API_SCHEDULE).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
        ).responseJson { _, _, result ->

            result.success {
                val res = it.obj()
                if (res.optString("status", "error") == "success") {
                    activity!!.runOnUiThread {


                        val requests = res.getJSONArray("requests")


                        for (i in 0 until requests.length()) {
                            val request = requests.getJSONObject(i)
                            val date_ =
                                CalendarDate(
                                    Utils.stringToCalendar(
                                        request.optString(
                                            "date",
                                            "2020-4-03"
                                        ), "yyyy-MM-dd"
                                    ).time
                                )
                            var color: Int? = null
                            val service = request.getJSONObject("service")
                            when (service.optString("indicator", "default")) {
                                "red" -> {
                                    color = R.color.event_1_color
                                }
                                "yellow" -> {
                                    color = R.color.event_4_color
                                }
                                "black" -> {
                                    color = R.color.event_7_color
                                }
                                "blue" -> {
                                    color = R.color.event_2_color
                                }
                                "green" -> {
                                    color = R.color.event_3_color
                                }
                                "default" -> {
                                    color = R.color.event_5_color
                                }
                            }


                            eventItems += EventItem(
                                eventName = "Event #$i",
                                date = date_,
                                color = activity!!.getColorInt(color!!),
                                from = request.optString("from", "00"),
                                to = request.optString("to", "00")
                            )

                        }
                        calendarView.datesIndicators = generateEventItems()


                    }
                }

            }
            result.failure {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                    .show()

            }

        }
    }

}