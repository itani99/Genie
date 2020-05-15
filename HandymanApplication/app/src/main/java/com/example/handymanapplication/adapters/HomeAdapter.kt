package com.example.handymanapplication.adapters


import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.R
import kotlinx.android.synthetic.main.row_layout.view.*
import org.json.JSONObject

class HomeAdapter(context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // holder.itemView.text1.text = (list[position] as JSONObject).optString("name","unknown")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

