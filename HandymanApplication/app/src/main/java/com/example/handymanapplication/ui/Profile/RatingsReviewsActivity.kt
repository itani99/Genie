package com.example.handymanapplication.ui.Profile

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.Utils.getJsonExtra
import com.example.handymanapplication.adapters.MyPostsAdapter
import com.example.handymanapplication.adapters.RatingReviewAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_my_posts.*
import kotlinx.android.synthetic.main.activity_ratings_reviews.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.reviews_ratings_custom.*
import org.json.JSONArray
import org.json.JSONObject

class RatingsReviewsActivity : AppCompatActivity() {
    var ratings: JSONObject? = null
    var feedback: JSONObject? = null
    var services: JSONArray? = null
    var adapter: RatingReviewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RatingReviewAdapter(this)
        setContentView(R.layout.reviews_ratings_custom)

        recycler_overview!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        initProfile()

//        for ( i in 0 until feedback!!.length()){
//            adapter.setItem("")
//        }

        recycler_overview.adapter = adapter!!

    }

    fun initProfile() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        var profile = res.getJSONObject("profile")
                        runOnUiThread {

                            if (profile.has("rating_object")) {
                                ratings = profile.optJSONObject("rating_object")
                            }
                            if (profile.has("service_ids")) {
                                services = profile.optJSONArray("service_ids")
                            }
                            if (profile.has("feedback_object")) {
                                feedback = profile.optJSONObject("feedback_object")
                                for (i in 0 until feedback!!.length()) {

                                    var total_rating_per_service: Any? = null
                                    var count1: Any? = 0
                                    var count2: Any? = 0
                                    var count3: Any? = 0
                                    var count4: Any? = 0
                                    var count5: Any? = 0
                                    var max = 0
                                    var flag = false
                                    var name_ser = ""
                                    var service_items =
                                        feedback!!.optJSONArray(services!!.get(i).toString())
                                    if (service_items == null) {
                                        name_ser =
                                            feedback!!.optString(services!!.get(i).toString())
                                        flag = true
                                    }
                                    var feedbaks_ = ""
                                    val rating_per_service =
                                        ratings!!.optJSONArray(services!!.get(i).toString())
                                    if (rating_per_service != null) {
                                        total_rating_per_service = rating_per_service[0]
                                        count1 = rating_per_service[1]
                                        count2 = rating_per_service[2]
                                        count3 = rating_per_service[3]
                                        count4 = rating_per_service[4]
                                        count5 = rating_per_service[5]

                                        if (flag == false) {
                                            for (j in 0 until service_items.length()) {
                                                val item = service_items.optJSONObject(j)
                                                feedbaks_ =
                                                    feedbaks_.plus(

                                                        item.optJSONObject("client").optString("name").plus(
                                                            ": "
                                                        ).plus(
                                                            item.optString("feedback")
                                                        )
                                                    )
                                                        .plus("\n")
                                                name_ser = item.optString("service_name")

                                            }
                                        }
                                    }

                                    var ob = JSONObject()

                                    ob.put("1", count1)
                                    ob.put("2", count2)
                                    ob.put("3", count3)
                                    ob.put("4", count4)
                                    ob.put("5", count5)
                                    ob.put("name", name_ser)

                                    ob.put("total", total_rating_per_service)
                                    ob.put("max", max)
                                    ob.put("feedback", feedbaks_)
                                    adapter!!.setItem(ob)

                                }

                            }


                        }

//
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

    }
}
