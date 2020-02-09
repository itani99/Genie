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
import kotlinx.android.synthetic.main.fragment_home.*
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


        var homeAdapter = HomeAdapter(context!!)

        recycler_ongoing_notifications_id.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recycler_ongoing_notifications_id.adapter = homeAdapter

        homeAdapter.setItem(JSONObject().apply {
            put("name", "Ahmad")
        })

        homeAdapter.setItem(JSONObject().apply {
            put("name", "Mohammad")
        })
        homeAdapter.setItem(JSONObject().apply {
            put("name", "jad").put("family", "itani ")
        })
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