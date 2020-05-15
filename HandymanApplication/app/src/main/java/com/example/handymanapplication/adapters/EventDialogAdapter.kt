package com.example.handymanapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.handymanapplication.Models.EventItem
import com.example.handymanapplication.R

class EventDialogAdapter(
    context: Context,
    events: Array<EventItem>
) : ArrayAdapter<EventItem>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dialog_event, parent, false)

        val eventItem = getItem(position)

        if (eventItem != null) {
            view.findViewById<View>(R.id.event_color).setBackgroundColor(eventItem.color)
            view.findViewById<TextView>(R.id.event_title).text = eventItem.eventName

            view.findViewById<TextView>(R.id.event_from).text = eventItem.from

            view.findViewById<TextView>(R.id.event_to).text = eventItem.to

        }

        return view
    }

}