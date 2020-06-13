package com.example.handymanapplication.ui.Profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.handymanapplication.Models.ReviewRatingItem
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.activities.MainActivity
import com.example.handymanapplication.adapters.DashboardPagerAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_settings.*
import android.widget.CompoundButton
import android.widget.Switch
import com.example.handymanapplication.Utils.putExtraJson
import com.example.handymanapplication.activities.HomePageActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.rtchagas.pingplacepicker.PingPlacePicker
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap


class ProfileActivity : AppCompatActivity() {

    var handler: Handler = Handler()
    var lat: Double? = null
    var lng: Double? = null
    var cv_pdf: String? = null
    var certificate_pdf: String? = null
    var criminal_record_pdf: String? = null
    var image: String? = null
    var selectedPhotoUri: Uri? = null
    var name: String? = null
    var biography: String? = null
    var price: Double? = null
    var gender: String? = null
    var rating_object: JSONObject? = null
    var feedback_object: JSONObject? = null
    var services:JSONArray?=null
    private val pingActivityRequestCode = 1001
    override fun onBackPressed() {
        val intent = Intent(this, HomePageActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()



        if (SharedPreferences.loadNightModeState(this) == true) {
            setTheme(R.style.darkTheme)
        } else
            setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_settings)
        var myswitch = findViewById<View>(R.id.darkModeSwitch) as Switch
        if (SharedPreferences.loadNightModeState(this) == true) {
            myswitch.setChecked(true)
        }
        myswitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                SharedPreferences.setNightModeState(this, true)
                restartApp()
            } else {
                SharedPreferences.setNightModeState(this, false)
                restartApp()
            }
        })


        initProfile()

        var progress = profile_progress
        var progressStatus = 0
        timeline_layout.setOnClickListener {
            val intent = Intent(this, TimeLineActivity::class.java)

            startActivity(intent)
        }
        Thread(Runnable {
            while (progressStatus < 100) {
                progressStatus += 1

                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                handler.post {

                    if (progressStatus == 100) {
                        progress.setVisibility(View.GONE)
                        layout_progress.visibility = View.GONE
                        profile_view.visibility = View.VISIBLE

                    }
                }
            }
        }).start()
        logout_btn.setOnClickListener {
            Utils.logout(this!!)
        }
        address_picker.setOnClickListener {
            showPlacePicker()
        }
        user_profile_picture.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        documents_txt.setOnClickListener {

            val i = Intent(this!!, DocumentsActivity::class.java)
            i.putExtra("url", cv_pdf)
            startActivity(i)

        }
        certificate_txt.setOnClickListener {
            val i = Intent(this!!, CertificateActivity::class.java)
            i.putExtra("url", certificate_pdf)
            startActivity(i)
        }
        criminal_Record_txt.setOnClickListener {
            val i = Intent(this!!, CriminalRecordActivity::class.java)
            i.putExtra("url", criminal_record_pdf)
            startActivity(i)
        }
        services_txt.setOnClickListener {
            val i = Intent(this!!, ServicesActivity::class.java)
            startActivity(i)
        }
        change_pass.setOnClickListener {
            val i = Intent(this!!, PasswordActivity::class.java)
            startActivity(i)
        }
        reviews_ratings.setOnClickListener {
            val i = Intent(this!!, RatingsReviewsActivity::class.java)
            i.putExtraJson("rating", rating_object!!)
            i.putExtraJson("feedback",feedback_object!!)
            i.putExtraJson("services",services!!)
            startActivity(i)
        }
        posts_txt.setOnClickListener {
            val i = Intent(this!!, MyPostsActivity::class.java)
            startActivity(i)
        }
        profile_txt.setOnClickListener {
            val i = Intent(this!!, InformationActivity::class.java)
            i.putExtra("biography", biography)
            i.putExtra("name", name)
            i.putExtra("gender", gender)
            i.putExtra("price", price)
            startActivity(i)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == pingActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {

            val place: Place? = PingPlacePicker.getPlace(data!!)

            saveLocation(place!!.latLng!!.latitude, place.latLng!!.longitude)

        }

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(this?.contentResolver, selectedPhotoUri)
            user_profile_picture.setImageBitmap(bitmap)
            user_profile_picture.alpha = 0f
            // when image is not null so u have an image
            image = Utils.encodeToBase64(bitmap)
            saveImage(image!!)

        }
    }

    fun saveImage(image: String) {
        Fuel.post(
            Utils.API_EDIT_PROFILE, listOf("image" to image)
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(this!!).toString()
        )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        val intent = Intent(this!!, ProfileActivity::class.java)
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


    private fun showPlacePicker() {

        val builder = PingPlacePicker.IntentBuilder()

        builder.setAndroidApiKey("AIzaSyD39zDc1HFT-tA6gvOtYb6agwT9XEfpN-U")
            .setMapsApiKey("AIzaSyAWLbffIXqHauCs-scP0xJ3YK77Ea1IUs8")
            .setLatLng(LatLng(lat!!, lng!!))


        try {
            val placeIntent = builder.build(this!!)
            startActivityForResult(placeIntent, pingActivityRequestCode)
        } catch (ex: Exception) {
            Toast.makeText(this!!, "Google Play Services is not Available", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun saveLocation(latitude: Double, longitude: Double) {


        var params = HashMap<String, Double>()


        params.put("latitude", latitude)
        params.put("longitude", longitude)

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
                        val intent = Intent(this!!, ProfileActivity::class.java)
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
                                    .load(url).into(user_profile_picture)
                            } else {
                                Glide
                                    .with(this)
                                    .load(Utils.BASE_IMAGE_URL.plus("services/service_1585417538.png"))
                                    .into(user_profile_picture)
                            }
                            if (profile.has("phone")) {
                                phone_txt.text = profile.optString("phone")
                            }
                            if (profile.has("name")) {
                                user_name_txt.text = profile.optString("name")
                            }
                            if (profile.has("isApproved")) {
                                if (profile.optBoolean("isApproved")) {
                                    user_profile_picture.borderColor = Color.GREEN
                                }
                            }
                            if (profile.has("visits")) {
                                number_visits.text =
                                    "Views: ".plus(profile.optInt("visits").toString())
                            }
                            if (profile.has("location")) {
                                val location = profile.optJSONArray("location")
                                lng = location!!.optDouble(0, 33.8938)
                                lat = location!!.optDouble(1, 35.5018)
                            } else {
                                lng = (35.5018)
                                lat = (33.8938)
                            }
                            if (profile.has("cv")) {
                                cv_pdf = profile.optString("cv")
                            }
                            if (profile.has("criminal_record")) {
                                criminal_record_pdf = profile.optString("criminal_record")
                            }
                            if (profile.has("certificate")) {
                                certificate_pdf = profile.optString("certificate")
                            }
                            if (profile.has("price")) {
                                price = profile.optDouble("price", 0.0)
                            }
                            if (profile.has("biography")) {
                                biography = profile.optString("biography", "")
                            }
                            if (profile.has("gender")) {
                                gender = profile.optString("gender")
                            }
                            if (profile.has("name")) {
                                name = profile.optString("name")
                            }
                            if (profile.has("rating_object")) {
                                rating_object = profile.optJSONObject("rating_object")
                            }
                            if (profile.has("feedback_object")) {
                                feedback_object = profile.optJSONObject("feedback_object")
                            }
                            if(profile.has("service_ids")){
                                services=profile.optJSONArray("service_ids")
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

    fun restartApp() {
        val i = Intent(applicationContext, ProfileActivity::class.java)
        startActivity(i)
        finish()
    }
}