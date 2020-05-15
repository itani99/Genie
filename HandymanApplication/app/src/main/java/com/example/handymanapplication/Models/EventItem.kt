package com.example.handymanapplication.Models

import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView

class EventItem(
    override val date: CalendarDate,
    override val color: Int,
    val eventName: String,
    val from:String,
    val to:String

) : CalendarView.DateIndicator