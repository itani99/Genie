package com.example.handymanapplication.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.Utils.putExtraJson
import com.example.handymanapplication.ui.ChatLog.ChatLogActivity
import kotlinx.android.synthetic.main.user_chat_row.view.*
import org.json.JSONArray
import org.json.JSONObject

class ChatAdapter(var context: Context) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_chat_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//check
        if (!(list[position] as JSONObject).has("messages")) {
            holder.itemView.client_latest_message.text = "Click to send a new message.."
        } else {
            var msgArray: JSONArray = (list[position] as JSONObject).optJSONArray("messages")!!

            holder.itemView.client_latest_message.text =
                msgArray.getJSONObject(msgArray.length() - 1).getString("message")

            val client = (list[position] as JSONObject).getJSONObject("client")
            holder.itemView.client_name_msg.text = client.optString("name", "name")
            if (client.has("image")) {

                val client_img = client.optString("image", "")
                if (client_img == "null") {
                    Glide
                        .with(holder.itemView!!)
                        .load(Utils.BASE_IMAGE_URL.plus("services/service_1585417538.png"))
                        .into(holder.itemView.client_msg_image)
                } else {


                    Glide
                        .with(holder.itemView!!)
                        .load(Utils.BASE_IMAGE_URL.plus(client_img)).into(holder.itemView.client_msg_image)
                }
            }

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context!!, ChatLogActivity::class.java)
            intent.putExtraJson("object", list[position] as JSONObject)
            // intent.putExtra()
            context!!.startActivity(intent)
        }
//
//      //
//        if ( query == null )
//        holder.itemView.text1.text = (_list[position] as JSONObject).optString("name","unknown")
//        else
//        holder.itemView.text1.text = (list[position] as JSONObject).optString("name","unknown")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

