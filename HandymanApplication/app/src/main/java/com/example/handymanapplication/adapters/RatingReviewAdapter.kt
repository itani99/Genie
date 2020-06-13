package com.example.handymanapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.R
import kotlinx.android.synthetic.main.activity_ratings_reviews.view.*
import org.json.JSONObject

class RatingReviewAdapter(var context: Context) :
    RecyclerView.Adapter<RatingReviewAdapter.ViewHolder>() {
    var list: ArrayList<JSONObject> = ArrayList()

    fun setItem(ob: JSONObject) {
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
                .inflate(R.layout.activity_ratings_reviews, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.editText.setText(list[position].optDouble("total", 0.0).toString())
        var max =
            (list[position].optInt("5")) + (list[position].optInt("4")) + (list[position].optInt("3"))
        +(list[position].optInt("2")) + (list[position].optInt("1"))
        holder.itemView.progressBar1.max = max
        holder.itemView.progressBar2.max = max
        holder.itemView.progressBar3.max = max
        holder.itemView.progressBar4.max = max
        holder.itemView.progressBar5.max = max
        holder.itemView.service_name_rated.text = list[position].optString("name")
        holder.itemView.progressBar1.setProgress((list[position] as JSONObject).optInt("1"))
        holder.itemView.progressBar2.setProgress((list[position] as JSONObject).optString("2").toInt())
        holder.itemView.progressBar3.setProgress((list[position] as JSONObject).optString("3").toInt())
        holder.itemView.progressBar4.setProgress((list[position] as JSONObject).optString("4").toInt())
        holder.itemView.progressBar5.setProgress((list[position] as JSONObject).optInt("5"))
        holder.itemView.textView2_feedback.text = (list[position].optString("feedback"))
        holder.itemView.ratingBar.rating = list[position].optDouble("total").toFloat()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

