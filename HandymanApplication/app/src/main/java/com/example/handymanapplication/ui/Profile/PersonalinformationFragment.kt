package com.example.handymanapplication.ui.Profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.handymanapplication.R

import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_personalinformation.*


class PersonalinformationFragment : Fragment(), IOnBackPressed {


    private var edit = false
    private var image: String? = null
    private var flag = false

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        viewProfile()
        (activity as AppCompatActivity).supportActionBar!!.show()

        return inflater.inflate(
            com.example.handymanapplication.R.layout.fragment_personalinformation,
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


        selectphoto_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
// full name , email, password, porifle pic, bio , gender ,bank account, phone , dob , balance , address
        // service. , certificates , price ,criminal record , cv , certficiates . avaialable time

        edit_btn.setOnClickListener {
            if (edit) {
                // complete saving
                edit_btn.setBackgroundResource(com.example.handymanapplication.R.drawable.icons8_writeprofile)
                Utils.hideSoftKeyBoard(activity!!.baseContext, profile_email)
                edit = false
                profile_email.isFocusable = false
                profile_email.isFocusableInTouchMode = false

                profile_phone.isFocusable = false
                profile_phone.isFocusableInTouchMode = false

                profile_biography.isFocusable = false
                profile_biography.isFocusableInTouchMode = false
                saveProfile()
            } else {
                //open edit
                profile_email.isFocusable = true
                profile_email.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                profile_email.isFocusableInTouchMode = true

                profile_phone.isFocusable = true
                profile_phone.inputType = InputType.TYPE_CLASS_PHONE
                profile_phone.isFocusableInTouchMode = true

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
        var email = profile_email.text.toString()
        var phone = profile_phone.text.toString()
        var biography = profile_biography.text.toString()
        Fuel.put(
            Utils.API_EDIT_PROFILE, listOf(
                "email" to email, "phone" to phone

            )
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(activity!!.baseContext).toString()
        )
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
                            profile_name.setText(profile.getString("name"))
                            profile_email.setText(profile.getString("email"))
                            profile_phone.setText(profile.getString("phone"))
                            val url: String =
                                "https://handiman.club/public/storage/uploads/dzYci2r374tKkI7NdBtNu3L5K.png"

                            Glide
                                .with(this)
                                .load(url).into(profile_picture)
                            activity?.runOnUiThread {
                                Toast.makeText(activity, profile.toString(), Toast.LENGTH_LONG)
                                    .show()
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
}
