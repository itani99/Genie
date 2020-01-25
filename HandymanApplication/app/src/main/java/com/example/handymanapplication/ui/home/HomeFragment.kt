package com.example.handymanapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.R
import com.example.handymanapplication.adapters.HomeAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var homeAdapter = HomeAdapter( context!! )

        recycler_main_id.layoutManager = LinearLayoutManager( context , LinearLayoutManager.VERTICAL , false )

        recycler_main_id.adapter = homeAdapter

        homeAdapter.setItem(JSONObject().apply {
            put("name", "Ahmad")
        })

        homeAdapter.setItem(JSONObject().apply {
            put("name", "Mohammad")
        })


    }

    fun viewHandyman(){

    }
}