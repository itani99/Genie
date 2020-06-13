package com.example.handymanapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handymanapplication.Models.ReviewRatingItem
import com.example.handymanapplication.Models.ServiceHeaderItem
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.feedback_row.view.*
import kotlinx.android.synthetic.main.post_row.view.*
import kotlinx.android.synthetic.main.service_header_item.view.*
import org.json.JSONArray

class FeedbacksAdapter(var context: Context) : RecyclerView.Adapter<FeedbacksAdapter.ViewHolder>() {
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
    } override fun getItemViewType(position: Int): Int {
        return if (list[position] is ServiceHeaderItem) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.service_header_item, parent, false)
            )
            else -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.feedback_row, parent, false)
            )
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list[position] is ReviewRatingItem) {
            holder.itemView.feedback_text.text = (list[position] as ReviewRatingItem).feedback
            holder.itemView.rating_item.rating =
                (list[position] as ReviewRatingItem).rating.toFloat()

            holder.itemView.client_name.text = (list[position] as ReviewRatingItem).client_name
            val url = (list[position] as ReviewRatingItem).client_image
            Glide
                .with(holder.itemView)
                .load(Utils.BASE_IMAGE_URL.plus(url))
                .into(holder.itemView.client_image)



        } else {
            holder.itemView.service_name_header.text = (list[position] as ServiceHeaderItem).name
//            holder.itemView.txt_to.text = (list[position] as TimeLineItem).to
//            holder.itemView.rmv_item.setOnClickListener {

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

