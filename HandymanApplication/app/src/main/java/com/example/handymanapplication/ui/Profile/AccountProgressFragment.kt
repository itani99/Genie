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
import org.json.JSONObject
import org.w3c.dom.Text


class AccountProgressFragment : Fragment() {
    var adapter: FeedbacksAdapter? = null
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.show()
//init()
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
      //  viewProfile()
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
                            rating_bar.rating = profile.optDouble("rating").toFloat()
                            total_rating.text= profile.optDouble("rating").toString()
                            rating_bar.isFocusableInTouchMode = false
                            rating_bar.isFocusable = false
                            val items = profile.optJSONArray("feedbacks")
                            val ratings = profile.optJSONArray("ratings")
                            if (items != null) {
                                number_of_requests.text =
                                    (items.length().toString())
                            } else {
                                number_of_requests.text=0.toString()
                            }
                            if (items!=null) {
                                for (i in 0 until items.length()) {
                                    var ob = JSONObject()
                                    ob.put("feedback", items.getString(i))
                                    ob.put("rating", ratings.getDouble(i))
                                    adapter!!.setItem(ob)
                                }
                            }
//                            activity?.runOnUiThread {
//                                Toast.makeText(activity, profile.toString(), Toast.LENGTH_LONG)
//                                    .show()
//                            }
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


}
