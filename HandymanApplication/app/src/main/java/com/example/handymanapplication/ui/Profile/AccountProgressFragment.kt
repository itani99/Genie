package com.example.handymanapplication.ui.Profile

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.Models.ReviewRatingItem
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.FeedbacksAdapter
import com.example.handymanapplication.adapters.TagsAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.fragment_credit_card.*
import kotlinx.android.synthetic.main.fragment_credit_card.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text


class AccountProgressFragment : Fragment() {
    var adapter: FeedbacksAdapter? = null
    var tags: ArrayList<String>? = ArrayList()

    var tags_adapter: TagsAdapter? = null
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(
            com.example.handymanapplication.R.layout.fragment_credit_card,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FeedbacksAdapter(context!!)
        feedback_recycler!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tags_adapter = TagsAdapter(context!!)
        tags()
        viewProfile()
       // initTagsAdapter()


        feedback_recycler.adapter = adapter
        // rating_bar.rating=2.5!!.toFloat()
    }

    fun initTagsAdapter() {
        val mLayoutManager = GridLayoutManager(context!!, 2)
        services_tagged.setLayoutManager(mLayoutManager)
        services_tagged.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        services_tagged.setItemAnimator(DefaultItemAnimator())
        services_tagged.setAdapter(tags_adapter)


        services_tagged.setLayoutManager(mLayoutManager)
        services_tagged.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        services_tagged.setItemAnimator(DefaultItemAnimator())

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

                                    adapter!!.setItem(
                                        ReviewRatingItem(
                                            1,
                                            (arr.getJSONObject(k).optString("feedback")),
                                            (arr.getJSONObject(k).optDouble("rating")),
                                            (arr.getJSONObject(k).optJSONObject("client").optString(
                                                "name"
                                            )),
                                            (arr.getJSONObject(k).optJSONObject("client").optString(
                                                "image"
                                            ))
                                        )
                                    )
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
                                tags_adapter!!.setItem(tags_.getJSONObject(i))

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


    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right =
                    (column) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }
}

