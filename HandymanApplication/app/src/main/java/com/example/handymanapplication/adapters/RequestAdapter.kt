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
import kotlinx.android.synthetic.main.user_chat_row.view.*
import org.json.JSONObject

class RequestAdapter(context: Context) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    var list : ArrayList<Any> = ArrayList()
    private val unfoldedIndexes = HashSet<Int>()
    var defaultRequestBtnClickListener: View.OnClickListener? = null


    fun setItem( ob: Any){
        list.add(ob)
        notifyItemInserted(list.size -1)
    }
    fun getItem( index : Int) = list[index]

    fun removeItem (index: Int){
        list.removeAt(index)
        notifyItemRemoved( index )
    }
    fun removeItems (){
        list.clear()
        notifyDataSetChanged()
    }

    fun getItems() = list



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.user_chat_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.itemView.setOnClickListener {
//          //  if ((list[position] as JSONObject).)
//        }
        //holder.itemView.request_description.text= (list[position] as JSONObject).optString("description", "title")
//
//      //
//        if ( query == null )
//        holder.itemView.text1.text = (_list[position] as JSONObject).optString("name","unknown")
//        else
//        holder.itemView.text1.text = (list[position] as JSONObject).optString("name","unknown")
    }

    fun registerToggle(position: Int) {
        if (unfoldedIndexes.contains(position))
            registerFold(position)
        else
            registerUnfold(position)
    }

    fun registerFold(position: Int) {
        unfoldedIndexes.remove(position)
    }

    fun registerUnfold(position: Int) {
        unfoldedIndexes.add(position)
    }
    override fun getItemCount(): Int {
       return list.size
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)

}

