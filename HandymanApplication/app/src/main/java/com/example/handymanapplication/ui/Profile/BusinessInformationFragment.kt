package com.example.handymanapplication.ui.Profile

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.handymanapplication.Models.TimeLineHeader
import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.adapters.TimelineAdapter
import kotlinx.android.synthetic.main.fragment_bussinessinformation.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.R
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*
import com.google.android.libraries.places.widget.AutocompleteActivity
import android.content.Intent
import android.app.Activity
import android.widget.Toast
import com.google.android.libraries.places.api.Places


class BusinessInformationFragment : Fragment(), IOnBackPressed {

    private var edit = false
    private var image: String? = null
    private var flag = false
    private var AUTOCOMPLETE_REQUEST_CODE = 1
    private var fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
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
        adapter.setItem(TimeLineHeader(0, "Monday"))
        adapter.setItem(TimeLineHeader(1, "Tuesday"))
        adapter.setItem(TimeLineHeader(2, "wed"))
        adapter.setItem(TimeLineHeader(3, "Thurs"))
        adapter.setItem(TimeLineHeader(4, "Fri"))
        adapter.setItem(TimeLineHeader(5, "Sat"))
        adapter.setItem(TimeLineHeader(6, "Sun"))
        timeline.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        timeline.adapter = adapter


        txt_address.setOnClickListener {
            if (!Places.isInitialized()) {
                Places.initialize(
                    activity!!.baseContext,
                    "AIzaSyCayBS9KPayvC2NTQ6JVz4Mef4UoaS5eQs",
                    Locale.US
                )
            }
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(activity!!)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

        }


    }


    override fun onBackPressed(): Boolean {
        if (edit) {
            activity?.runOnUiThread {
                AlertDialog.Builder(activity!!)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Toast.makeText(activity!!, place.name, Toast.LENGTH_LONG).show()

                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data!!)
                }

                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }
    }


}
