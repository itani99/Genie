package com.example.handymanapplication.adapters


import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handymanapplication.Models.ChatItems
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Utils
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_to.view.*
import kotlin.collections.ArrayList

class ChatLogAdapter(var context: Context) : RecyclerView.Adapter<ChatLogAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()

    fun setItem(ob: Any) {
        list.add(ob)
        notifyItemInserted(list.size - 1)
    }

    fun getItem(index: Int) = list[index]

    fun removeItem(index: Int) {
        list.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeItems() {
        list.clear()
        notifyDataSetChanged()
    }

    fun getItems() = list


    override fun getItemViewType(position: Int): Int {
        return if (list[position] is ChatItems.From) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_from, parent, false)
            )
            else -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_to, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position] is ChatItems.From) {

            holder.itemView.message_from.text = (list[position] as ChatItems.From).message.plus("\n\n".plus((list[position] as ChatItems.From).date))
            var url = Utils.BASE_IMAGE_URL.plus((list[position] as ChatItems.From).image)

            Glide
                .with(holder.itemView!!)
                .load(url).into(holder.itemView.imageFrom)

        } else {

            holder.itemView.message_to.text = (list[position] as ChatItems.To).message.plus("\n\n".plus((list[position] as ChatItems.To).date))
            var url = Utils.BASE_IMAGE_URL.plus((list[position] as ChatItems.To).image)

            Glide
                .with(holder.itemView!!)
                .load(url).into(holder.itemView.image_to)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

