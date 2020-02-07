package com.example.handymanapplication.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.R
import com.example.handymanapplication.adapters.HomeAdapter
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.ongoing_notifications.*
import kotlinx.android.synthetic.main.row_layout.view.*
import org.json.JSONObject

class OngoingRequestsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ongoing_notifications, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder>()
        var request1 = Request("123", "mhmd", "ee")
        var request2 = Request("123", "mhmd", "ee")
        var request3 = Request("123", "mhmd", "ee")
        var request4 = Request("123", "mhmd", "ee")
        adapter.add(RequestItem(request1))
        adapter.add(RequestItem(request2))
        adapter.add(RequestItem(request3))
        adapter.add(RequestItem(request4))

        adapter.setOnItemClickListener { item, view ->
            //TODO
            val intent = Intent(view.context, ViewRequestFragment::class.java)
            startActivity(intent)
        }
        recycler_ongoing_notifications_id.adapter = adapter
    }

    class Request(val uid: String, val username: String, val profileImageUrl: String) {
        constructor() : this("", "", "")

    }

    private class RequestItem(val request: Request) : Item() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            viewHolder.itemView.text1.text = request.username

            Picasso.get().load(request.profileImageUrl).into(viewHolder.itemView.imageView)
        }

        override fun getLayout(): Int {
            return R.layout.row_layout
        }
    }

}