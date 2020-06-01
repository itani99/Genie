package com.example.handymanapplication.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handymanapplication.Helpers.ServiceHelper
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Utils
import kotlinx.android.synthetic.main.service_card.view.*
import org.json.JSONArray
import org.json.JSONObject

class PostImagesCardsAdapter(var context: Context) :
    RecyclerView.Adapter<PostImagesCardsAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()


    // var context:Context = context
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
                .inflate(R.layout.service_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val image_url = (list[position]).toString()

        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(image_url)).into(holder.itemView.service_image)

    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }

}

/*

 */