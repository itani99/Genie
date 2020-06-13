package com.example.handymanapplication.ui.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_information.*

class InformationActivity : AppCompatActivity() {
    var gender: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        val bio = intent!!.extras!!.getString("biography").toString()
        profile_biography.setText(bio)
        val price = intent!!.extras!!.get("price").toString()
        price_hour.setText(price)
        val name = intent!!.extras!!.get("name").toString()
        profile_name.setText(name)
        val gender = intent!!.extras!!.get("gender").toString()
        if (gender == "male") {
            profile_male.isChecked = true
        } else {
            profile_female.isChecked = true
        }
    }

    override fun onBackPressed() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Save Changes?")
            .setMessage("Are You Sure?")
            .setPositiveButton("Yes", { dialog, _ ->
                save()
                val intent = Intent(this!!, ProfileActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }).setNegativeButton("No", { dialog, _ ->
                val intent = Intent(this!!, ProfileActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            })
            .create().show()


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

    fun save() {
        onRadioButtonClicked(profile_female)
        Fuel.post(
            Utils.API_EDIT_PROFILE, listOf(
                "biography" to profile_biography.text.toString(),
                "price" to price_hour.text.toString(),
                "gender" to gender!!,
                "name" to profile_name.text.toString()
            )
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

}

