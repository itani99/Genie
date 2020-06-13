package com.example.handymanapplication.ui.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.activities.HomePageActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        savePassword.setOnClickListener {


            val pass = change_password_txt.text.toString()
            val pass2 = confim_change_password_txt.text.toString()
            if (pass == pass2 && pass.length >= 8) {
                savePassword(pass.toString())
            } else {
                change_password_txt.text!!.clear()
                change_password_txt.text!!.clear()
                if (pass.length < 8) {
                    Toast.makeText(
                        this,
                        "Password should at least be 8 characters",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Passwords are not the same",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    fun savePassword(password: String) {
        Fuel.post(Utils.API_PASSWORD, listOf("password" to password!!))
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this!!.baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {


                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        val i = Intent(this!!, ProfileActivity::class.java)
                        startActivity(i)
                    }
                }
                result.failure {

                }

            }
    }


    //API_PASSWORD
}
