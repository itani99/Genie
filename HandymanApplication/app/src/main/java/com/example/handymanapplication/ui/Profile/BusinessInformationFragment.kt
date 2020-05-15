package com.example.handymanapplication.ui.Profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Models.TimeLineHeader
import com.example.handymanapplication.Models.TimeLineItem
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.activities.HomePageActivity
import com.example.handymanapplication.activities.MapsActivity
import com.example.handymanapplication.adapters.TimelineAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.JsonArray
import com.rtchagas.pingplacepicker.PingPlacePicker
import kotlinx.android.synthetic.main.fragment_bussinessinformation.*
import kotlinx.android.synthetic.main.fragment_bussinessinformation.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.reflect.Array
import java.net.URI
import java.net.URL
import java.net.URLConnection
import java.sql.Time
import java.util.*
import kotlin.math.log


class BusinessInformationFragment : Fragment(), IOnBackPressed {

    var adapter: TimelineAdapter? = null
    private var edit = false
    private var image: String? = null
    private var flag = false
    private var AUTOCOMPLETE_REQUEST_CODE = 1
    private var fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
    private var longitude: String? = null

    private var latitude: String? = null
    private val PLACE_PICKER_REQUEST = 1
    private val pingActivityRequestCode = 1001
    private val cvActivityRequestCode = 1002
    private val certificateActivityRequestCode = 1003
    private val criminalRecordActivityRequestCode = 1004
    private var file_base64: String? = null


    val RequestPermissionCode = 1
    var mLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        adapter = TimelineAdapter(context!!)
        (activity as AppCompatActivity).supportActionBar!!.show()

        return inflater.inflate(
            R.layout.fragment_bussinessinformation,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        edit = true
        initTimeline()

        btn_save_changes.setOnClickListener {
            onBackPressed()

        }
        txt_address.setOnClickListener {
            // getLastLocation()
            showPlacePicker()

        }

        cv.setOnClickListener {
            checkStoragePermission(0)
        }
        criminal_record.setOnClickListener {
            checkStoragePermission(1)
        }
        certificate.setOnClickListener {
            checkStoragePermission(2)
        }
    }

    private fun checkStoragePermission(id: Int) {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0
            )

        } else {
            if (id == 0) {
                val intent = getFileChooserIntentForImageAndPdf()
                startActivityForResult(intent, cvActivityRequestCode)

            } else if (id == 1) {
                val intent = getFileChooserIntentForImageAndPdf()
                startActivityForResult(intent, criminalRecordActivityRequestCode)

            } else if (id == 2) {
                val intent = getFileChooserIntentForImageAndPdf()
                startActivityForResult(intent, certificateActivityRequestCode)

            }
        }
    }

    fun getFileChooserIntentForImageAndPdf(): Intent {
        val mimeTypes = arrayOf("application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("application/pdf")
            .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == pingActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {

            val place: Place? = PingPlacePicker.getPlace(data!!)

            saveLocation(place!!.latLng!!.latitude, place.latLng!!.longitude)


        } else if ((requestCode == cvActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {
            var pdfFile: File? = null
            if (data != null) {
                data.data?.let {
                    if (it.scheme.equals("content")) {
                        val pdfBytes =
                            (activity!!.contentResolver?.openInputStream(it))?.readBytes()
                        pdfFile = File(
                            activity!!.getExternalFilesDir(null),
                            "Lesson ${Calendar.getInstance().time}t.pdf"
                        )

                        if (pdfFile!!.exists())
                            pdfFile!!.delete()
                        try {
                            val fos = FileOutputStream(pdfFile!!.path)
                            fos.write(pdfBytes)
                            fos.close()
                        } catch (e: Exception) {

                        }
                    } else {
                        pdfFile = it.toFile()
                    }

                }
            }


            Toast.makeText(
                activity,
                data!!.data.toString(),
                Toast.LENGTH_LONG
            ).show()
            file_base64 = Utils.convertFileToBase64(pdfFile!!)
            savePDF(file_base64.toString(), 0)
            Log.e("bittttt", file_base64.toString())
        } else if ((requestCode == criminalRecordActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {
            var pdfFile: File? = null
            if (data != null) {
                data.data?.let {
                    if (it.scheme.equals("content")) {
                        val pdfBytes =
                            (activity!!.contentResolver?.openInputStream(it))?.readBytes()
                        pdfFile = File(
                            activity!!.getExternalFilesDir(null),
                            "Lesson ${Calendar.getInstance().time}t.pdf"
                        )

                        if (pdfFile!!.exists())
                            pdfFile!!.delete()
                        try {
                            val fos = FileOutputStream(pdfFile!!.path)
                            fos.write(pdfBytes)
                            fos.close()
                        } catch (e: Exception) {

                        }
                    } else {
                        pdfFile = it.toFile()
                    }

                }
            }


            Toast.makeText(
                activity,
                data!!.data.toString(),
                Toast.LENGTH_LONG
            ).show()
            file_base64 = Utils.convertFileToBase64(pdfFile!!)
            savePDF(file_base64.toString(), 1)
            Log.e("bittttt", file_base64.toString())
        } else if ((requestCode == certificateActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {
            var pdfFile: File? = null
            if (data != null) {
                data.data?.let {
                    if (it.scheme.equals("content")) {
                        val pdfBytes =
                            (activity!!.contentResolver?.openInputStream(it))?.readBytes()
                        pdfFile = File(
                            activity!!.getExternalFilesDir(null),
                            "Lesson ${Calendar.getInstance().time}t.pdf"
                        )

                        if (pdfFile!!.exists())
                            pdfFile!!.delete()
                        try {
                            val fos = FileOutputStream(pdfFile!!.path)
                            fos.write(pdfBytes)
                            fos.close()
                        } catch (e: Exception) {

                        }
                    } else {
                        pdfFile = it.toFile()
                    }

                }
            }


            Toast.makeText(
                activity,
                data!!.data.toString(),
                Toast.LENGTH_LONG
            ).show()
            file_base64 = Utils.convertFileToBase64(pdfFile!!)
            savePDF(file_base64.toString(), 2)
            Log.e("bittttt", file_base64.toString())
        }
    }

    fun savePDF(document: String, id: Int) {
        var pdf_type: String? = null
        when (id) {
            0 -> {
                pdf_type = "cv"
            }
            1 -> {
                pdf_type = "criminal_record"
            }
            2 -> {
                pdf_type = "certificate"
            }
        }

        Fuel.post(
            Utils.API_EDIT_PROFILE, listOf(pdf_type!! to document)
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
        )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        activity!!.runOnUiThread {
                            Toast.makeText(context!!, "saved", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
                result.failure {
                    Toast.makeText(context!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }

    private fun initTimeline() {
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

                            if (res.has("timeline")) {
                                val items = res.getJSONArray("timeline")

                                for (i in 0 until items.length()) {
                                    when (i) {
                                        0 -> {

                                            adapter!!.setItem(TimeLineHeader(0, "Monday"))
                                            var monday = items[i] as JSONArray
                                            filler(i, adapter!!, monday, 0)
                                        }

                                        1 -> {
                                            adapter!!.setItem(TimeLineHeader(1, "Tuesday"))
                                            var tuesday = items[i] as JSONArray
                                            filler(i, adapter!!, tuesday, 1)

                                        }
                                        2 -> {
                                            adapter!!.setItem(TimeLineHeader(2, "Wednesday"))
                                            var wednesday = items[i] as JSONArray
                                            filler(i, adapter!!, wednesday, 2)
                                        }

                                        3 -> {


                                            adapter!!.setItem(TimeLineHeader(3, "Thursday"))
                                            var thursday = items[i] as JSONArray
                                            filler(i, adapter!!, thursday, 3)
                                        }
                                        4 -> {


                                            adapter!!.setItem(TimeLineHeader(4, "Friday"))
                                            var friday = items[i] as JSONArray
                                            filler(i, adapter!!, friday, 4)
                                        }
                                        5 -> {

                                            adapter!!.setItem(TimeLineHeader(5, "Saturday"))
                                            var saturday = items[i] as JSONArray
                                            filler(i, adapter!!, saturday, 5)
                                        }
                                        6 -> {

                                            adapter!!.setItem(TimeLineHeader(6, "Sunday"))
                                            var sunday = items[i] as JSONArray
                                            filler(i, adapter!!, sunday, 6)
                                        }


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

    private fun filler(i: Int, adapter: TimelineAdapter, day: JSONArray, id: Int) {
        // day.put("1111", false)
        var from = ""
        var to = ""
        var newSlot = false
        for (index in 0..23) {
            val item = index
            var time_formt = String.format("%02d", index) + ":00"
            if (!newSlot && day.optBoolean(item, false)) {
                from = time_formt
                newSlot = true
            } else if (newSlot && !day.optBoolean(item, false)) {
                newSlot = false
                to = time_formt
                adapter.setItem(TimeLineItem(from, to, id))
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
                        saveTimeline()
                    })
                    .setNegativeButton("No", { dialog, _ ->
                        dialog.dismiss()
                    }).create().show()
            }
            return false
        }
        return true
    }

    private fun showPlacePicker() {

        val builder = PingPlacePicker.IntentBuilder()

        builder.setAndroidApiKey("AIzaSyD39zDc1HFT-tA6gvOtYb6agwT9XEfpN-U")
            .setMapsApiKey("AIzaSyAWLbffIXqHauCs-scP0xJ3YK77Ea1IUs8")
            .setLatLng(LatLng(37.4219999, -122.0862462))

        try {
            val placeIntent = builder.build(activity!!)
            startActivityForResult(placeIntent, pingActivityRequestCode)
        } catch (ex: Exception) {
            Toast.makeText(context!!, "Google Play Services is not Available", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getLastLocation() {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: kotlin.Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.size > 0 && grantResults!![0] == PackageManager.PERMISSION_GRANTED
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // pdf.setEnabled(true);
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

    private fun saveLocation(latitude: Double, longitude: Double) {


        var params = HashMap<String, Double>()


        params.put("latitude", latitude)
        params.put("longitude", longitude)

        Fuel.post(
            Utils.API_EDIT_PROFILE, params.toList()
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
        )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        activity!!.runOnUiThread {
                            Toast.makeText(context!!, "saved", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
                result.failure {
                    Toast.makeText(context!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }

    fun saveTimeline() {
        //Toast.makeText(context!!, adapter!!.getItems().toString(), Toast.LENGTH_LONG).show()
        var _list: List<Any> = adapter!!.getItems()
        var week_arr: JSONArray = JSONArray()
//        for (i in 0..7) {
//            var day_arr: JSONArray = JSONArray()
//            var id = (_list as TimeLineHeader).id
//            for (j in 0..23) {

        var day0: JSONArray = JSONArray()
        var day1: JSONArray = JSONArray()
        var day2: JSONArray = JSONArray()
        var day3: JSONArray = JSONArray()
        var day4: JSONArray = JSONArray()
        var day5: JSONArray = JSONArray()
        var day6: JSONArray = JSONArray()
        for (item in 0 until _list.size) {
            var ob = _list.get(item)

            if (ob is TimeLineItem) {
                if (ob.from.length == 4) {
                    ob.from = Utils.addChar(ob.from, '0', 0)
                }
                if (ob.to.length == 4) {
                    ob.to = Utils.addChar(ob.to, '0', 0)

                }
                if (ob.id == 0) {

                    var obje = JSONObject()
                    obje.put("from", ob.from)
                    obje.put("to", ob.to)
                    day0.put(obje)

                }
                if (ob.id == 1) {

                    var obje = JSONObject()
                    obje.put("from", ob.from)
                    obje.put("to", ob.to)
                    day1.put(obje)

                }
                if (ob.id == 2) {

                    var obje = JSONObject()
                    obje.put("from", ob.from)
                    obje.put("to", ob.to)
                    day2.put(obje)

                }
                if (ob.id == 3) {

                    var obje = JSONObject()
                    obje.put("from", ob.from)
                    obje.put("to", ob.to)
                    day3.put(obje)

                }
                if (ob.id == 4) {

                    var obje = JSONObject()
                    obje.put("from", ob.from)
                    obje.put("to", ob.to)
                    day4.put(obje)

                }
                if (ob.id == 5) {

                    var obje = JSONObject()
                    obje.put("from", ob.from)
                    obje.put("to", ob.to)
                    day5.put(obje)

                }
                if (ob.id == 6) {

                    var obje = JSONObject()
                    obje.put("from", ob.from)
                    obje.put("to", ob.to)
                    day6.put(obje)

                }
            }

        }

        var day0_upgrade = JSONArray()

        var day1_upgrade = JSONArray()

        var day2_upgrade = JSONArray()

        var day3_upgrade = JSONArray()

        var day4_upgrade = JSONArray()

        var day5_upgrade = JSONArray()

        var day6_upgrade = JSONArray()
        for (t in 0..23) {
            day0_upgrade.put(t, false)
            day1_upgrade.put(t, false)
            day2_upgrade.put(t, false)
            day3_upgrade.put(t, false)
            day4_upgrade.put(t, false)
            day5_upgrade.put(t, false)
            day6_upgrade.put(t, false)

        }

        for (item in 0 until day0.length()) {
            var obj: JSONObject = (day0.getJSONObject(item))
            var from = obj.get("from")
            var to = obj.get("to")
            for (j in 0..23) {
                var time_formt = String.format("%02d", j) + ":00"
                if (time_formt >= from.toString() && time_formt < to.toString()) {
                    day0_upgrade.put(j, true)
                }
            }
        }

        for (item in 0 until day1.length()) {
            var obj: JSONObject = (day1.getJSONObject(item))
            var from = obj.get("from")
            var to = obj.get("to")
            for (j in 0..23) {
                var time_formt = String.format("%02d", j) + ":00"
                if (time_formt >= from.toString() && time_formt < to.toString()) {
                    day1_upgrade.put(j, true)
                }
            }

        }
        for (item in 0 until day2.length()) {
            var obj: JSONObject = (day2.getJSONObject(item))
            var from = obj.get("from")
            var to = obj.get("to")
            for (j in 0..23) {
                var time_formt = String.format("%02d", j) + ":00"
                if (time_formt >= from.toString() && time_formt < to.toString()) {
                    day2_upgrade.put(j, true)
                }
            }

        }
        for (item in 0 until day3.length()) {
            var obj: JSONObject = (day3.getJSONObject(item))
            var from = obj.get("from")
            var to = obj.get("to")
            for (j in 0..23) {
                var time_formt = String.format("%02d", j) + ":00"
                if (time_formt >= from.toString() && time_formt < to.toString()) {
                    day3_upgrade.put(j, true)
                }
            }


        }
        for (item in 0 until day4.length()) {
            var obj: JSONObject = (day4.getJSONObject(item))
            var from = obj.get("from")
            var to = obj.get("to")
            for (j in 0..23) {
                var time_formt = String.format("%02d", j) + ":00"
                if (time_formt >= from.toString() && time_formt < to.toString()) {
                    day4_upgrade.put(j, true)
                }
            }

        }
        for (item in 0 until day5.length()) {
            var obj: JSONObject = (day5.getJSONObject(item))
            var from = obj.get("from")
            var to = obj.get("to")
            for (j in 0..23) {
                var time_formt = String.format("%02d", j) + ":00"
                if (time_formt >= from.toString() && time_formt < to.toString()) {
                    day5_upgrade.put(j, true)
                }
            }

        }
        for (item in 0 until day6.length()) {
            var obj: JSONObject = (day6.getJSONObject(item))
            var from = obj.get("from")
            var to = obj.get("to")
            for (j in 0..23) {
                var time_formt = String.format("%02d", j) + ":00"
                if (time_formt >= from.toString() && time_formt < to.toString()) {
                    day6_upgrade.put(j, true)
                }
            }

        }
        Log.d("day0_upgrade", day0_upgrade.toString())

        Log.d("day1_upgrade", day1_upgrade.toString())

        week_arr.put(day0_upgrade)
        week_arr.put(day1_upgrade)
        week_arr.put(day2_upgrade)
        week_arr.put(day3_upgrade)
        week_arr.put(day4_upgrade)
        week_arr.put(day5_upgrade)
        week_arr.put(day6_upgrade)
        Log.d("week", week_arr.toString())
        sendTimeLineToServer(week_arr!!)
//
//            }
//            week_arr.put(day_arr)
//
//        }
    }

    fun sendTimeLineToServer(arr: JSONArray) {
        var params = HashMap<String, JSONArray>()

        for (x in 0 until arr.length()) {
            params.put("timeline[$x]", arr!!.getJSONArray(x))
        }
        Fuel.post(
            Utils.API_EDIT_PROFILE, params.toList()
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
        )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        val intent = Intent(context!!, HomePageActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
                result.failure {
                    Toast.makeText(context!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }
}



