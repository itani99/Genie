package com.example.handymanapplication.ui.Profile

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.ServiceAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_services.*

class ServicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        getServices()
    }


    private fun getServices() {
        var adapter = ServiceAdapter(this!!)
        // recycler_view.layoutManager=GridLayoutManager(context!!,30)

        val mLayoutManager = GridLayoutManager(this!!, 2)
        services_recycler.setLayoutManager(mLayoutManager)
        services_recycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        services_recycler.setItemAnimator(DefaultItemAnimator())
        services_recycler.setAdapter(adapter)


        services_recycler.setLayoutManager(mLayoutManager)
        services_recycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        services_recycler.setItemAnimator(DefaultItemAnimator())


        Fuel.get(Utils.API_Services)
            .header(
                "accept" to "application/json"
            )
            .responseJson { _, _, result ->

                result.success {
                    //
                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        //     var services = res.getJSONObject("services")
                        runOnUiThread {


                            val items = res.getJSONArray("services")

                            for (i in 0 until items.length()) {
                                adapter.setItem(items.getJSONObject(i))
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
