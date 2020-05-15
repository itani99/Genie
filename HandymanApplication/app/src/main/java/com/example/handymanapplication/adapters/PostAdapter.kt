package com.example.handymanapplication.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Utils
import kotlinx.android.synthetic.main.post_row.view.*
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
                .inflate(R.layout.post_row, parent, false)
        )
    }

    fun filter(key: String, query: String) {
        this.query = query
        this.list.clear()
        this.list.addAll(this._list.filter {
            (it as JSONObject).optString(key).contains(query)
        })
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        /*
         if ( query == null )
        holder.itemView.text1.text = (_list[position] as JSONObject).optString("name","unknown")
        else
        holder.itemView.text1.text = (list[position] as JSONObject).optString("name","unknown")

         */
        holder.itemView.post_img.setAnimation(
            AnimationUtils.loadAnimation(
                context!!,
                R.anim.fade_transition_animation
            )
        )
/*

 */
        holder.itemView.setAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.fade_scale_animation
            )
        )
        if (query == null) {
            holder.itemView.post_title.text =
                (_list[position] as JSONObject).optString("title", "title")
            holder.itemView.post_description.text =
                (_list[position] as JSONObject).optString("body", "content")
            holder.itemView.post_date.text =
                (_list[position] as JSONObject).optString("created_at", "date")

            if ((_list[position] as JSONObject).has("images")) {
                var images_array = (_list[position] as JSONObject).getJSONArray("images")
                val url = images_array.get(0).toString()


                Glide
                    .with(holder.itemView)
                    .load(Utils.BASE_IMAGE_URL.plus(url))
                    .into(holder.itemView.post_img)
            }
        } else {
            holder.itemView.post_title.text =
                (list[position] as JSONObject).optString("title", "title")
            holder.itemView.post_description.text =
                (list[position] as JSONObject).optString("body", "content")
            holder.itemView.post_date.text =
                (list[position] as JSONObject).optString("created_at", "date")

            //Mond


        }
    }

    override fun getItemCount(): Int {
        return if (query == null) _list.size else list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

