package com.example.handymanapplication.ui.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Models.TimeLineHeader
import com.example.handymanapplication.Models.TimeLineItem
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.activities.HomePageActivity
import com.example.handymanapplication.adapters.TimelineAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_time_line.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class TimeLineActivity : AppCompatActivity() {

    var adapter: TimelineAdapter? = null
    var edit: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        adapter = TimelineAdapter(this!!)
        initTimeline()


    }

    override fun onBackPressed() {
        if (adapter!!.edited) {
            android.app.AlertDialog.Builder(this)
                .setTitle("Discard Changes?")
                .setMessage("Are You Sure?")
                .setPositiveButton("Yes", { dialog, _ ->
                    saveTimeline()
                    val intent = Intent(this!!, ProfileActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    dialog.dismiss()
                }).setNegativeButton("No", { dialog, _ ->
                    dialog.dismiss()
                }).create().show()

        } else {
            val intent = Intent(this!!, ProfileActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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
            Utils.AUTHORIZATION to SharedPreferences.getToken(this!!).toString()
        )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {

                    }
                }
                result.failure {
                    Toast.makeText(this!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }

    fun initTimeline() {


        Fuel.get(Utils.API_Timeline.plus(SharedPreferences.getID(this).toString()))
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this!!.baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        runOnUiThread {

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
                            this,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        timeline.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


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

}
