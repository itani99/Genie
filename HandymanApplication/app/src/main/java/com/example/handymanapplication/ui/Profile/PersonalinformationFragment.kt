package com.example.handymanapplication.ui.Profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.ServiceAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_personalinformation.*
import android.os.Build
import android.widget.RadioButton
import androidx.fragment.app.FragmentTransaction
import com.example.handymanapplication.R


class PersonalinformationFragment : Fragment(), IOnBackPressed {


    private var edit = false
    private var image: String? = null
    private var flag = false
    private var gender: String? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        viewProfile()

        (activity as AppCompatActivity).supportActionBar!!.show()



        return inflater.inflate(
            R.layout.fragment_personalinformation,
            container,
            false
        )
    }

    override fun onBackPressed(): Boolean {
        if (edit) {
            activity?.runOnUiThread {
                AlertDialog.Builder(activity)
                    .setTitle("Discard Changes?")
                    .setMessage("Are You Sure?")
                    .setPositiveButton("Yes", { dialog, _ ->
                        dialog.dismiss()
                    }).setNegativeButton("No", { dialog, _ ->
                        dialog.dismiss()
                    }).create().show()
            }
            return false
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

              add_services.setOnClickListener {
            getServices()
            services_recycler.visibility = View.VISIBLE
            //prepare all the services

        }



        selectphoto_button.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        edit_btn.setOnClickListener {
            if (edit) {
                // complete saving
                edit_btn.setBackgroundResource(R.drawable.icons8_writeprofile)
                // Utils.hideSoftKeyBoard(activity!!.baseContext, profile_email)
                edit = false
                profile_name.isFocusable = false
                profile_name.isFocusableInTouchMode = false



                profile_biography.isFocusable = false
                profile_biography.isFocusableInTouchMode = false
                saveProfile()
            } else {
                //open edit
                profile_name.isFocusable = true
                profile_name.inputType = InputType.TYPE_CLASS_TEXT
                profile_name.isFocusableInTouchMode = true

//                profile_phone.isFocusable = true
//                profile_phone.inputType = InputType.TYPE_CLASS_PHONE
//                profile_phone.isFocusableInTouchMode = true

                profile_biography.isFocusable = true
                profile_biography.inputType = InputType.TYPE_CLASS_TEXT
                profile_biography.isFocusableInTouchMode = true




                edit_btn.setBackgroundResource(R.drawable.ic_save_black_24dp)
                edit = true

            }
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhotoUri)
            profile_picture.setImageBitmap(bitmap)
            selectphoto_button.alpha = 0f
            // when image is not null so u have an image
            image = Utils.encodeToBase64(bitmap)

        }
    }

    private fun saveProfile() {
        onRadioButtonClicked(profile_female)
        var biography = profile_biography.text.toString()
        Fuel.post(
            Utils.API_EDIT_PROFILE, listOf(
                "image" to image,
                "biography" to biography,
                "gender" to gender


            )
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(activity!!.baseContext).toString()
        ).responseJson { _, _, result ->

            result.success {

                var res = it.obj()
                if (res.optString("status", "error") == "success") {

                    var profile = res.getJSONObject("user")

//                    activity?.runOnUiThread {
//                        Toast.makeText(activity, profile.toString(), Toast.LENGTH_LONG)
//                            .show()
//                    }
                }
            }
            result.failure {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }

        }
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

                            profile_name.setText(profile.optString("name", ""))
                            profile_biography.setText(profile.optString("biography", ""))
                            if (profile.has("gender")) {
                                var gen = profile.getString("gender")
                                if (gen == "female") {
                                    profile_female.isChecked = true
                                } else {
                                    profile_male.isChecked = true
                                }
                            }
                            profile_balance.setText((profile.optString("balance", "")).plus("$"))

                            if (profile.optString("isApproved", "false") == "true") {
                                profile_status_background.setBackgroundColor(Color.GREEN)
                                profile_status.setText("Online")
                            }
                            if (profile.has("image")) {


                                val url =
                                    Utils.BASE_IMAGE_URL.plus(
                                        profile.optString(
                                            "image",
                                            "ic_user.png"
                                        )
                                    )

                                Glide
                                    .with(this)
                                    .load(url).into(profile_picture)
                            } else {
                                Glide
                                    .with(this)
                                    .load(Utils.BASE_IMAGE_URL.plus("services/service_1585417538.png"))
                                    .into(profile_picture)
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

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            if (view.getId() == R.id.profile_female) {
                if (checked)
                    gender = "female"
                else
                    gender = "male"
            }

        }
    }

    private fun getServices() {
        var adapter = ServiceAdapter(context!!)
        // recycler_view.layoutManager=GridLayoutManager(context!!,30)

        val mLayoutManager = GridLayoutManager(context!!, 2)
        services_recycler.setLayoutManager(mLayoutManager)
        services_recycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        services_recycler.setItemAnimator(DefaultItemAnimator())
        services_recycler.setAdapter(adapter)


        services_recycler.setLayoutManager(mLayoutManager)
        services_recycler.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        services_recycler.setItemAnimator(DefaultItemAnimator())


        //TODO
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
                        activity!!.runOnUiThread {


                            val items = res.getJSONArray("services")

                            for (i in 0 until items.length()) {
                                adapter.setItem(items.getJSONObject(i))
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
