package com.example.handymanapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.R
import kotlinx.android.synthetic.main.tags_item.view.*
import org.json.JSONObject


class TagsAdapter(var context: Context) :
    RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    var tags: ArrayList<Any> = ArrayList()

    fun setItem(ob: Any) {
        list.add(ob!!)
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

    fun getTags() = tags.toArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tags_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.service_name_txt.text = (list[position] as JSONObject).optString("name")
        holder.itemView.is_service_checked.setOnClickListener {

            if (holder.itemView.is_service_checked.isChecked == true) {
                tags.add((list[position] as JSONObject).optString("_id"))
            } else {
                tags.remove((list[position] as JSONObject).optString("_id"))
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

