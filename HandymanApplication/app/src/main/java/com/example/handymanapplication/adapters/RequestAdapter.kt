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
import org.json.JSONObject

class RequestAdapter(context: Context) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {
    var _list : ArrayList<Any> = ArrayList()
    var list : ArrayList<Any> = ArrayList()
    var query: String? = null

    fun setItem( ob: Any){
        _list.add(ob)
        notifyItemInserted(_list.size -1)
    }
    fun getItem( index : Int) = _list[index]

    fun removeItem (index: Int){
        _list.removeAt(index)
        notifyItemRemoved( index )
    }
    fun removeItems (){
        _list.clear()
        notifyDataSetChanged()
    }

    fun filter( key : String , query : String ){
        this.query = query
        this.list.clear()
        this.list.addAll( this._list.filter {
            (it as JSONObject).optString(key).contains(query)
        })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
          //  if ((list[position] as JSONObject).)
        }
      //
        if ( query == null )
        holder.itemView.text1.text = (_list[position] as JSONObject).optString("name","unknown")
        else
        holder.itemView.text1.text = (list[position] as JSONObject).optString("name","unknown")
    }

    override fun getItemCount(): Int {
        return if ( query == null ) _list.size else list.size
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)

}

