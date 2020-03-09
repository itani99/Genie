package com.example.handymanapplication.ui.Profile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Models.TimeLineHeader
import com.example.handymanapplication.Models.TimeLineItem
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.TimelineAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.fragment_bussinessinformation.*
import org.json.JSONObject
import java.util.*


class BusinessInformationFragment : Fragment(), IOnBackPressed {

    private var edit = false
    private var image: String? = null
    private var flag = false
    private var AUTOCOMPLETE_REQUEST_CODE = 1
    private var fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
    private var longitude: String? = null

    private var latitude: String? = null
    private val PLACE_PICKER_REQUEST = 1
//    private var mGoogleApiClient: GoogleApiClient? = null


    val RequestPermissionCode = 1
    var mLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        getLastLocation()
        (activity as AppCompatActivity).supportActionBar!!.show()

        return inflater.inflate(
            R.layout.fragment_bussinessinformation,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        edit=true
        initTimeline()
        btn_save_changes.setOnClickListener {
            onBackPressed()
        }
        txt_address.setOnClickListener {
            if (!Places.isInitialized()) {
                Places.initialize(
                    activity!!.baseContext,
                    getString(R.string.google_api_key),
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

    fun initTimeline() {
        var adapter = TimelineAdapter(context!!)
        Toast.makeText(
            activity,
            SharedPreferences.getID(activity!!.baseContext).toString(),
            Toast.LENGTH_LONG
        )
            .show()

        Fuel.get(Utils.API_Timeline.plus(SharedPreferences.getID(activity!!.baseContext).toString()))
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(activity!!.baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        activity!!.runOnUiThread {
                            var items = res.getJSONArray("timeline")

                            for (i in 0 until items.length()) {
                                when (i) {
                                    0 -> {

                                        adapter.setItem(TimeLineHeader(0, "Monday"))
                                        var monday = items[i] as JSONObject
                                        filler(i, adapter, monday)
                                    }

                                    1 -> {
                                        adapter.setItem(TimeLineHeader(1, "Tuesday"))
                                        var tuesday = items[i] as JSONObject
                                        filler(i, adapter, tuesday)

                                    }
                                    2 -> {
                                        adapter.setItem(TimeLineHeader(2, "Wednesday"))
                                        var wednesday = items[i] as JSONObject
                                        filler(i, adapter, wednesday)
                                    }

                                    3 -> {


                                        adapter.setItem(TimeLineHeader(3, "Thursday"))
                                        var thursday = items[i] as JSONObject
                                        filler(i, adapter, thursday)
                                    }
                                    4 -> {


                                        adapter.setItem(TimeLineHeader(4, "Friday"))
                                        var friday = items[i] as JSONObject
                                        filler(i, adapter, friday)
                                    }
                                    5 -> {

                                        adapter.setItem(TimeLineHeader(5, "Saturday"))
                                        var saturday = items[i] as JSONObject
                                        filler(i, adapter, saturday)
                                    }
                                    6 -> {

                                        adapter.setItem(TimeLineHeader(6, "Sunday"))
                                        var sunday = items[i] as JSONObject
                                        filler(i, adapter, sunday)
                                    }


                                }
                            }


                        }
                    } else {

                        Toast.makeText(
                            activity,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        timeline.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        timeline.adapter = adapter
    }

    fun filler(i: Int, adapter: TimelineAdapter, day: JSONObject) {
        day.put("1111", false)
        var from = ""
        var to = ""
        var newSlot = false
        for (index in 0..23) {
            var item = String.format("%02d", index) + "00"
            if (!newSlot && day.optBoolean(item, false)) {
                from = item
                newSlot = true
            } else if (newSlot && !day.optBoolean(item, false)) {
                newSlot = false
                to = item
                adapter.setItem(TimeLineItem(from, to))
            } else {
                continue
            }

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

                    //   var latitude :Double=  (place.getLatLng()!!.latitude)
                    Toast.makeText(
                        activity!!,
                        (place!!.name.toString().plus("\n".plus(latitude)).plus("\n".plus(longitude))),
                        Toast.LENGTH_LONG
                    ).show()

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

    fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    mLocation = location
                    if (location != null) {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                    }
                }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            RequestPermissionCode
        )
    }

}

/*
if (ActivityCompat.checkSelfPermission(getContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
    } else {
        Log.e("DB", "PERMISSION GRANTED");
    }
 */