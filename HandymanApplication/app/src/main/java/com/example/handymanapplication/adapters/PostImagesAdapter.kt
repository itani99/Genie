package com.example.handymanapplication.adapters


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.R
import kotlinx.android.synthetic.main.post_image.view.*
import kotlinx.android.synthetic.main.row_layout.view.*
import org.json.JSONObject

class PostImagesAdapter(var context: Context) :
    RecyclerView.Adapter<PostImagesAdapter.ViewHolder>() {
    var list: ArrayList<Uri> = ArrayList()

    fun setItem(ob: Uri) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.crt_post_title.text = "text"
        val bitmap =
            MediaStore.Images.Media.getBitmap(
                context.contentResolver, list[position]
            )

        holder.itemView.create_post_image.setImageBitmap(bitmap)
        //(list[position] as JSONObject).optString("name","unknown")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

