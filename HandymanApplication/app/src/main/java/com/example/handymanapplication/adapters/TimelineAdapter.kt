package com.example.handymanapplication.adapters


import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.Models.TimeLineHeader
import com.example.handymanapplication.Models.TimeLineItem
import com.example.handymanapplication.R
import kotlinx.android.synthetic.main.layout_timeline_header.view.*
import kotlinx.android.synthetic.main.layout_timeline_item.view.*
import kotlinx.android.synthetic.main.layout_timeline_price.view.*
import kotlinx.android.synthetic.main.row_layout.view.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class TimelineAdapter(var context: Context) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
    var list : ArrayList<Any> = ArrayList()

    fun setItem( ob: Any){
        list.add(ob)
        notifyItemInserted(list.size -1)
    }
    fun getItem( index : Int) = list[index]

    fun removeItem (index: Int){
        list.removeAt(index)
        notifyItemRemoved( index )
    }
    fun removeItems (){
        list.clear()
        notifyDataSetChanged()
    }

    fun getItems() = list


    override fun getItemViewType(position: Int): Int {
        return if (list[position] is TimeLineHeader) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when( viewType ){
            0->ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_timeline_header,parent,false))
            else->ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_timeline_item,parent,false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if ( list[position] is TimeLineHeader ){
            holder.itemView.textview.text = (list[position] as TimeLineHeader).name

        }else{
            holder.itemView.txt_from.text = ( list[position] as TimeLineItem).from
            holder.itemView.txt_to.text = ( list[position] as TimeLineItem).to
            holder.itemView.txt_price.text =  (list[position] as TimeLineItem).price.toString()

        }
        holder.itemView.tag = list[position]
        holder.itemView.setOnClickListener {
            if ( it.tag is TimeLineHeader){
                var from_hour: Int? =null
                var from_minute: Int? =null
                var to_hour: Int? =null
                var to_minute: Int? =null
                val cal = Calendar.getInstance()

                TimePickerDialog(context, TimePickerDialog.OnTimeSetListener{
                    view, hour,minute->
                    from_hour = hour
                    from_minute = minute
                    TimePickerDialog(context, TimePickerDialog.OnTimeSetListener{
                            to_view, thour,tminute->
                        to_hour = thour
                        to_minute = tminute
                        val v = LayoutInflater.from(context).inflate(R.layout.layout_timeline_price, null)

                        val price = v.findViewById(R.id.price) as EditText

                        AlertDialog.Builder(context)
                            .setTitle("Set Price")
                            .setView(v)
                            .setNegativeButton(android.R.string.cancel){
                                dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(android.R.string.ok){
                                vdialog, _ ->
                                list.add(list.indexOf(it.tag)+1,
                                    TimeLineItem("${from_hour}:${from_minute}",
                                        "${to_hour}:${to_minute}", price.text.toString().toDouble() ))
                                notifyDataSetChanged()
                                vdialog.dismiss()
                            }
                            .create().show()
                    }, hour +1,
                        minute, true).show()
                }, cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE), true).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)

}

