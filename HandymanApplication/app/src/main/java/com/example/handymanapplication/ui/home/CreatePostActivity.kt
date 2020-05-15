package com.example.handymanapplication.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.nfc.Tag
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.activities.HomePageActivity
import com.example.handymanapplication.adapters.PostImagesAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_create_post.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.log

class CreatePostActivity : AppCompatActivity() {
    private var context: Context? = null

    var PICK_IMAGE_MULTIPLE = 1
    lateinit var imagePath: String
    var counter = 0
    var listOfImages = ArrayList<String>()
    val adapter = PostImagesAdapter(this)
    var imagesPathList: MutableList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        initAdapter()
        select_post_images.setOnClickListener {
            if (counter == 5) {
                Toast.makeText(this, "You are allowed to upload 5 images", Toast.LENGTH_LONG)
                    .show()
            } else {


                if (Build.VERSION.SDK_INT < 19) {

                    var intent = Intent()
                    intent.type = "image/*"
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(
                        Intent.createChooser(intent, "Select Picture")
                        , PICK_IMAGE_MULTIPLE
                    )
                } else {
                    var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "image/*"
                    startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                }
            }
        }
        submit_post.setOnClickListener {
            savePost()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
            && null != data
        ) {

            adapter.setItem(data.data!!)
            counter++
            val bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            listOfImages.add(Utils.encodeToBase64(bitmap))
            if (data.getClipData() != null) {

                var count = data.clipData!!.itemCount
                for (i in 0..count - 1) {
                    var imageUri: Uri = data!!.clipData!!.getItemAt(i).uri
                    getPathFromURI(imageUri)
                }
            } else if (data.getData() != null) {
                var imagePath: String? = data!!.data!!.path
                Log.e("imagePath", imagePath);
            }

            //   displayImageData()
        }
    }

    fun savePost() {
        val post_title = create_post_title.text.toString()
        val post_body = create_post_body.text.toString()
        var params = HashMap<String , String>()
        params.put("title", post_title)
        params.put("body", post_body)
        for ( x in 0 until listOfImages.size){
            params.put("images[$x]", listOfImages.get(x))
        }
        Fuel.post(
            Utils.API_POST, params.toList()
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
        )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        val intent = Intent(this, HomePageActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
                result.failure {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }


    private fun getPathFromURI(uri: Uri) {
        var path: String? = uri.path // uri = any content Uri

        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (path!!.contains("/document/image:")) { // files selected from "Documents"
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else { // files selected from all other sources, especially on Samsung devices
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = contentResolver.query(
                databaseUri,
                projection, selection, selectionArgs, null
            )
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                imagePath = cursor.getString(columnIndex)
                // Log.e("path", imagePath);
                imagesPathList.add(imagePath)


            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("exception", e.message, e)
        }
    }

    fun initAdapter() {

        val mLayoutManager = GridLayoutManager(this, 2)
        recycler_post_images.setLayoutManager(mLayoutManager)
        recycler_post_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recycler_post_images.setItemAnimator(DefaultItemAnimator())
        recycler_post_images.setAdapter(adapter)


        recycler_post_images.setLayoutManager(mLayoutManager)
        recycler_post_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recycler_post_images.setItemAnimator(DefaultItemAnimator())


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
