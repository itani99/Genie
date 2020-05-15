package com.example.handymanapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.Models.receiptItem
import com.example.handymanapplication.R
import kotlinx.android.synthetic.main.activity_payment.view.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.receipt_item.view.*
import org.json.JSONObject

public class ReceiptAdapter(context: Context) : RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {
    var list: ArrayList<JSONObject> = ArrayList()

    fun setItem(ob: JSONObject) {
        list.add(ob)
        notifyItemInserted(list.size - 1)
    }

    fun getList(): List<Any> {
        return list!!

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
                .inflate(R.layout.receipt_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val count = position + 1


        holder.itemView.item_counter.text = "Item $count :"
        holder.itemView.receipt_name_item.text = (list[position]).getString("name")

        holder.itemView.receipt_item_price.text =
            (list[position]).get("price").toString().plus(" $")
        holder.itemView.receipt_item_qty.text = (list[position]).get("qty").toString()

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

