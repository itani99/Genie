package com.example.handymanapplication.ui.Profile

import android.view.LayoutInflater


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import androidx.appcompat.app.AppCompatActivity

import android.text.InputType
import com.example.handymanapplication.Utils.IOnBackPressed
import kotlinx.android.synthetic.main.fragment_bussinessinformation.*
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Models.TimeLineHeader
import com.example.handymanapplication.adapters.TimelineAdapter
import com.squareup.picasso.Picasso
import java.net.URL


class BusinessInformationFragment : Fragment(), IOnBackPressed {

    private var edit = false
    private var image: String? = null
    private var flag = false

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        (activity as AppCompatActivity).supportActionBar!!.show()

        return inflater.inflate(
            com.example.handymanapplication.R.layout.fragment_bussinessinformation,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var adapter = TimelineAdapter(context!!)
        adapter.setItem(TimeLineHeader(0,"Monday"))
        adapter.setItem(TimeLineHeader(1,"Tuesday"))
        adapter.setItem(TimeLineHeader(2,"wed"))
        adapter.setItem(TimeLineHeader(3,"Thurs"))
        adapter.setItem(TimeLineHeader(4,"Fri"))
        adapter.setItem(TimeLineHeader(5,"Sat"))
        adapter.setItem(TimeLineHeader(6,"Sun"))
        timeline.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL, false)


        timeline.adapter = adapter

    }


    override fun onBackPressed(): Boolean {
        if (edit) {
            activity?.runOnUiThread {
                AlertDialog.Builder(activity)
                    .setTitle("Discard Changes?")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", { dialog, _ ->
                        dialog.dismiss()
                    })
                    .setNegativeButton("No", { dialog, _ ->
                        dialog.dismiss()
                    }).create().show()
            }
            return false
        }
        return true
    }

    }
