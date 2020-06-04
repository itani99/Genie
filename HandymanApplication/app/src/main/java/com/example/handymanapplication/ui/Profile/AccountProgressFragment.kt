package com.example.handymanapplication.ui.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.FeedbacksAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_credit_card.*
import kotlinx.android.synthetic.main.fragment_credit_card.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text


class AccountProgressFragment : Fragment() {
    var adapter: FeedbacksAdapter? = null
    var tags: ArrayList<String>? = ArrayList()
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.show()
        return inflater.inflate(
            com.example.handymanapplication.R.layout.fragment_credit_card,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedback_recycler!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        adapter = FeedbacksAdapter(context!!)
        tags()
        viewProfile()
        feedback_recycler.adapter = adapter
        // rating_bar.rating=2.5!!.toFloat()
    }

    private fun viewProfile() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(activity!!.baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        var profile = res.getJSONObject("profile")
                        activity!!.runOnUiThread {

                            val items = profile.optJSONObject("feedback_object")
                            val ratings = profile.optJSONObject("rating_object")



                                for (j in 0 until tags!!.size) {
                                    var arr: JSONArray =
                                        items.getJSONArray(tags!!.get(j).toString())
                                    for (k in 0 until arr.length()) {

                                        adapter!!.setItem(arr.getJSONObject(k))

                                }


                            }

//
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


    }

    fun tags() {
        Fuel.get(Utils.API_TAGS)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {


                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        activity!!.runOnUiThread {
                            var tags_ = res.optJSONArray("tags")
                            for (i in 0 until tags_.length()) {
                                tags!!.add((tags_.getJSONObject(i).getString("_id")))
                            }
                        }

                    } else {

                        Toast.makeText(
                            context!!,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

                result.failure {

                    Toast.makeText(context!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()

                }
            }

    }

}
