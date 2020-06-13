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
import com.example.handymanapplication.Utils.Constants
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.service_card.view.*
import org.json.JSONArray
import org.json.JSONObject

class ServiceAdapter(var context: Context) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
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
holder.itemView.service_title.visibility=View.VISIBLE
        var ids: JSONArray? = null
        if ((list[position] as JSONObject).has("user_ids")) {

            ids = (list[position] as JSONObject).getJSONArray("user_ids")
            for (i in 0 until ids!!.length()) {
                if (ids!![i].toString().equals(SharedPreferences.getID(context))) {

                    holder.itemView.overflow.setImageResource(R.drawable.ic_remove_black_24dp)
                    break
                } else {

                    holder.itemView.overflow.setImageResource(R.drawable.ic_add)
                }
            }
        }

        holder.itemView.overflow.setOnClickListener {

            var flag: Boolean = true
            for (i in 0 until ids!!.length()) {

                if (ids[i].toString().equals(SharedPreferences.getID(context!!))) {
                    flag = false
                    break
                } else {
                    flag = true
                }
            }

            var id_to_action = (list[position] as JSONObject).get("_id").toString()
            if (!flag) {

                var index: Int = -1
                for (i in 0 until ids!!.length()) {
                    if (SharedPreferences.getID(context!!).equals(ids[i].toString())) {
                        index = i
                        break
                    }
                }
                ServiceHelper.deleteService(id_to_action, context!!)
                holder.itemView.overflow.setImageResource(R.drawable.ic_add)
                ids.remove(index)


            } else {
                ServiceHelper.saveService(id_to_action, context!!)
                holder.itemView.overflow.setImageResource(R.drawable.ic_remove_black_24dp)
                ids.put(SharedPreferences.getID(context!!))

            }

        }


        holder.itemView.service_title.text = (list[position] as JSONObject).getString("name")
        val image_url = (list[position] as JSONObject).optString("image", "image.png")

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