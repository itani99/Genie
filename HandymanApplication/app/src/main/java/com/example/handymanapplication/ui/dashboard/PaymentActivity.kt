package com.example.handymanapplication.ui.dashboard

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Models.TimeLineItem
import com.example.handymanapplication.Models.receiptItem
import com.example.handymanapplication.R
import com.example.handymanapplication.adapters.ReceiptAdapter
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.payment_toolbar.*
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.activities.HomePageActivity
import com.example.handymanapplication.adapters.PostImagesAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_create_post.*
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode


class PaymentActivity : AppCompatActivity() {

    var PICK_IMAGE_MULTIPLE = 111
    var round_counter: Int? = 0
    var request_id: String? = null
    private var recyclerAdapter: ReceiptAdapter? = null

    lateinit var imagePath: String
    var listOfImages = ArrayList<String>()
    val images_adapter = PostImagesAdapter(this)
    var imagesPathList: MutableList<String> = arrayListOf()


    lateinit var imagePath2: String
    var listOfImages2 = ArrayList<String>()
    val images_adapter2 = PostImagesAdapter(this)
    var imagesPathList2: MutableList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        initAdapter()
        initAdapter2()
        supportActionBar!!.hide()
        var obje = JSONObject(intent!!.extras!!.getString("object"))
        var object2 = JSONObject(obje.getString("nameValuePairs"))
        request_id = (object2).optString("request_id")
        receipt_recycler!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        result_images!!.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        add_receipt_item.setOnClickListener {
            withEditText(it)

        }
        reset_btn.setOnClickListener {
            total_receipt.text = 00.00.toString()
            recyclerAdapter!!.removeItems()

            round_counter = 0

            color_round_1.setBackgroundResource(R.drawable.ic_rounds)

            color_round_2.setBackgroundResource(R.drawable.ic_rounds)

            color_round_3.setBackgroundResource(R.drawable.ic_rounds)

            color_round_4.setBackgroundResource(R.drawable.ic_rounds)

            color_round_5.setBackgroundResource(R.drawable.ic_rounds)

            color_round_6.setBackgroundResource(R.drawable.ic_rounds)

        }
        submit_receipt.setOnClickListener {
            sendReceipt()

        }
        add_images_btn_result.setOnClickListener {
            if (Build.VERSION.SDK_INT < 19) {

                var intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture")
                    , 10011
                )
            } else {
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, 10011);
            }
        }


        add_images_btn.setOnClickListener {
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


        recyclerAdapter = ReceiptAdapter(this)

        receipt_recycler.adapter = recyclerAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
            && null != data
        ) {

            images_adapter.setItem(data.data!!)

            val bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            listOfImages.add(Utils.encodeToBase64(bitmap))
            if (data.getClipData() != null) {

                var count = data.clipData!!.itemCount
                for (i in 0..count - 1) {
                    var imageUri: Uri = data!!.clipData!!.getItemAt(i).uri
                    getPathFromURI(imageUri, 0)
                }
            } else if (data.getData() != null) {
                var imagePath: String? = data!!.data!!.path
                Log.e("imagePath", imagePath)
            }

            //   displayImageData()
        } else if (requestCode == 10011 && resultCode == Activity.RESULT_OK
            && null != data
        ) {
            images_adapter2.setItem(data.data!!)

            val bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            listOfImages2.add(Utils.encodeToBase64(bitmap))
            if (data.getClipData() != null) {

                var count = data.clipData!!.itemCount
                for (i in 0..count - 1) {
                    var imageUri: Uri = data!!.clipData!!.getItemAt(i).uri
                    getPathFromURI(imageUri, 1)
                }
            } else if (data.getData() != null) {
                var imagePath: String? = data!!.data!!.path
            }
        }
    }

    private fun getPathFromURI(uri: Uri, id: Int) {
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
                if (id == 0) {
                    imagePath = cursor.getString(columnIndex)
                    // Log.e("path", imagePath);
                    imagesPathList.add(imagePath)
                } else {
                    imagePath2 = cursor.getString(columnIndex)
                    // Log.e("path", imagePath);
                    imagesPathList2.add(imagePath)
                }

            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("exception", e.message, e)
        }
    }

    fun withEditText(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("item description")
        val dialogLayout = inflater.inflate(R.layout.custom_dialog, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.item_name)
        val editText2 = dialogLayout.findViewById<EditText>(R.id.item_quantity)
        val editText3 = dialogLayout.findViewById<EditText>(R.id.item_price)


        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            round_counter = round_counter!! + 1
            if (round_counter == 1)
                color_round_1.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 2)
                color_round_2.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 3)
                color_round_3.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 4)
                color_round_4.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 5)
                color_round_5.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 6)
                color_round_6.setBackgroundResource(R.drawable.ic_round)

            total_receipt.text =
                (
                        total_receipt.text.toString().toDouble()
                                + ((editText2.text.toString().toInt()
                                * editText3.text.toString().toDouble()))
                        ).toString()
            val ob: JSONObject = JSONObject()
            ob.put(
                "name", editText.text.toString()
            )
            ob.put(
                "price",
                editText3.text.toString().toDouble()
            )
            ob.put(
                "qty",
                editText2.text.toString().toInt()
            )
            recyclerAdapter!!.setItem(
                ob
            )


        }
        builder.show()
    }


    fun sendReceipt() {
        var params = HashMap<String, String>()

        for (x in 0 until listOfImages.size) {
            params.put("images[$x]", listOfImages.get(x))
        }


        Fuel.post(
            Utils.API_RECEIPT_IMAGES.plus(request_id),
            params.toList()
        )

            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                }
                result.failure {

                    Toast.makeText(this!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }




        var params2 = HashMap<String, String>()

        for (x in 0 until listOfImages2.size) {
            params2.put("images[$x]", listOfImages2.get(x))
        }


        Fuel.post(
            Utils.API_RESULT_IMAGES.plus(request_id),
            params2.toList()
        )

            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                }
                result.failure {

                    Toast.makeText(this!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }


        Fuel.post(
            Utils.API_RECEIPT.plus(request_id), listOf(
                "receipt" to recyclerAdapter!!.getList(),
                "total" to total_receipt.text.toString()
            )
        )
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {
                    runOnUiThread {
                        Toast.makeText(this!!, "Receipt Sent", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this, HomePageActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                    }
                }
                result.failure {

                    Toast.makeText(this!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    fun initAdapter2() {

        val mLayoutManager = GridLayoutManager(this, 2)
        result_images.setLayoutManager(mLayoutManager)
        result_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        result_images.setItemAnimator(DefaultItemAnimator())
        result_images.setAdapter(images_adapter2)


        result_images.setLayoutManager(mLayoutManager)
        result_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        result_images.setItemAnimator(DefaultItemAnimator())


    }
    fun initAdapter() {

        val mLayoutManager = GridLayoutManager(this, 2)
        receipt_images.setLayoutManager(mLayoutManager)
        receipt_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        receipt_images.setItemAnimator(DefaultItemAnimator())
        receipt_images.setAdapter(images_adapter)


        receipt_images.setLayoutManager(mLayoutManager)
        receipt_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        receipt_images.setItemAnimator(DefaultItemAnimator())


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
