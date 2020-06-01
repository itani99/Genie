package com.example.handymanapplication.ui.home

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
import com.example.handymanapplication.adapters.PostImagesCardsAdapter
import kotlinx.android.synthetic.main.activity_view_images2.*
import org.json.JSONArray
import org.json.JSONObject

class ViewImagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_images2)
        var obje = JSONObject(intent!!.extras!!.getString("object"))
        var object2 = JSONObject(obje.getString("nameValuePairs"))
        var objee = JSONObject(object2.getString("images"))
        var objArray = objee.get("values") as JSONArray
        var adapter = PostImagesCardsAdapter(this!!)
        // recycler_view.layoutManager=GridLayoutManager(context!!,30)

        val mLayoutManager = GridLayoutManager(this!!, 2)
        post_images_recycler.setLayoutManager(mLayoutManager)
        post_images_recycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        post_images_recycler.setItemAnimator(DefaultItemAnimator())
        post_images_recycler.setAdapter(adapter)


        post_images_recycler.setLayoutManager(mLayoutManager)
        post_images_recycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        post_images_recycler.setItemAnimator(DefaultItemAnimator())

        for (i in 0 until objArray.length()) {
            adapter.setItem(objArray[i].toString())
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
