package com.example.handymanapplication.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.Utils.putExtraJson
import com.example.handymanapplication.ui.dashboard.ViewImagesActivity
import kotlinx.android.synthetic.main.post_row.view.*
import kotlinx.android.synthetic.main.post_row_lay.view.*
import org.json.JSONObject

class PostAdapter(var context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    var _list: ArrayList<Any> = ArrayList()
    var list: ArrayList<Any> = ArrayList()
    var query: String? = null

    fun setItem(ob: Any) {
        _list.add(ob)
        notifyItemInserted(_list.size - 1)
    }

    fun getItem(index: Int) = _list[index]

    fun removeItem(index: Int) {
        _list.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeItems() {
        _list.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_row_lay, parent, false)
        )
    }

    fun filter(key: String, query: String) {
        this.query = query
        this.list.clear()

        for (items in 0 until _list.size) {
            val services = (_list[items] as JSONObject).optJSONArray("services")
            for (j in 0 until services.length()) {
                if ((services!!.optJSONObject(j).optString("name")).toLowerCase().contains(query.toLowerCase())) {
                    list.add(_list[items])
                    break
                }
            }
        }

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (query == null) {
            if ((_list[position] as JSONObject).has("images"))
                holder.itemView.setOnClickListener {
                    val ob: JSONObject? = JSONObject()
                    ob!!.put("images", ((_list[position]) as JSONObject).optJSONArray("images"))

                    val intent = Intent(
                        context!!,
                        com.example.handymanapplication.ui.home.ViewImagesActivity::class.java
                    )

                    intent!!.putExtraJson("object", ob)
                    context!!.startActivity(intent)
                }

            if ((_list[position] as JSONObject).has("services")) {
                var tags: String = ""

                val services = (_list[position] as JSONObject).optJSONArray("services")
                for (items in 0 until services.length()) {
                    if (items == services.length() - 1) {
                        tags = tags.plus(services.optJSONObject(items).optString("name"))

                    } else {
                        tags = tags.plus(services.optJSONObject(items).optString("name").plus(", "))

                    }
                }
                holder.itemView.tags_txt.text = tags
            }
            val handyman = (_list[position] as JSONObject).optJSONObject("handyman")
            holder.itemView.newsTitle.text =handyman.optString("name").plus("\n ").plus(
                (_list[position] as JSONObject).optString("title", "title"))
            holder.itemView.newsInfo.text =
                (_list[position] as JSONObject).optString("body", "content")
            holder.itemView.post_date_time.text =
                (_list[position] as JSONObject).optString("created_at", "date")

            if ((_list[position] as JSONObject).has("images")) {
                var images_array = (_list[position] as JSONObject).getJSONArray("images")
                val url = images_array.get(0).toString()


                Glide
                    .with(holder.itemView)
                    .load(Utils.BASE_IMAGE_URL.plus(url))
                    .into(holder.itemView.thumbnail)
            }

        } else {

            if ((list[position] as JSONObject).has("images"))
                holder.itemView.setOnClickListener {
                    val ob: JSONObject? = JSONObject()
                    ob!!.put("images", ((list[position]) as JSONObject).optJSONArray("images"))

                    val intent = Intent(
                        context!!,
                        com.example.handymanapplication.ui.home.ViewImagesActivity::class.java
                    )

                    intent!!.putExtraJson("object", ob)
                    context!!.startActivity(intent)
                }

            if ((list[position] as JSONObject).has("services")) {
                var tags: String = ""

                val services = (list[position] as JSONObject).optJSONArray("services")
                for (items in 0 until services.length()) {
                    if (items == services.length() - 1) {
                        tags = tags.plus(services.optJSONObject(items).optString("name"))

                    } else {
                        tags = tags.plus(services.optJSONObject(items).optString("name").plus(", "))

                    }
                }
                holder.itemView.tags_txt.text = tags
            }
            val handyman = (list[position] as JSONObject).optJSONObject("handyman")
            holder.itemView.newsTitle.text =handyman.optString("name").plus("\n ").plus(
                (list[position] as JSONObject).optString("title", "title"))
            holder.itemView.newsInfo.text =
                (list[position] as JSONObject).optString("body", "content")
            holder.itemView.post_date_time.text =
                (list[position] as JSONObject).optString("created_at", "date")
            if ((list[position] as JSONObject).has("images")) {
                var images_array = (list[position] as JSONObject).getJSONArray("images")
                val url = images_array.get(0).toString()

                Glide
                    .with(holder.itemView)
                    .load(Utils.BASE_IMAGE_URL.plus(url))
                    .into(holder.itemView.thumbnail)
            }


        }
    }

    override fun getItemCount(): Int {
        return if (query == null) _list.size else list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

