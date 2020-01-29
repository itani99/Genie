package com.example.handymanapplication.ui.Profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.HomeAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import androidx.appcompat.app.AppCompatActivity
import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.ImageView
import com.example.handymanapplication.Utils.IOnBackPressed
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment(), IOnBackPressed {

    private var edit = false
    private var image: String? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        viewProfile()
        (activity as AppCompatActivity).supportActionBar!!.hide()

        return inflater.inflate(
            com.example.handymanapplication.R.layout.fragment_profile,
            container,
            false
        )
    }

    override fun onBackPressed(): Boolean {
        if (edit) {
            activity?.runOnUiThread {
                AlertDialog.Builder(activity)
                    .setTitle(" Discard Changes?")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", { dialog, _ ->
                        // close profile page
                        dialog.dismiss()
                    })
                    .setNegativeButton("No", { dialog, _ ->
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


        edit_btn.setOnClickListener {
            if (edit) {
                // complete saving
                saveProfile()
            } else {
                //open edit
                edit_btn.setBackgroundResource(R.drawable.ic_menu_save)
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
            selectphoto_imageview_register.setImageBitmap(bitmap)
            selectphoto_button.alpha = 0f
            // when image is not null so u have an image
            image = Utils.encodeToBase64(bitmap)

        }
    }
    // ma3e yeha bel utils :p 3zkrout

    private fun saveProfile() {

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

                      //  profile_email.text = profile.getString("email")

                        activity?.runOnUiThread {
                            Toast.makeText(activity, profile.toString(), Toast.LENGTH_LONG).show()
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