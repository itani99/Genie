package com.example.handymanapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Constants
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener { login() }
//this is a comment
        //hey
    }

    private fun login() {
        val email = edt_email.text.toString()
        val password = edt_password.text.toString()

        Fuel.post(Utils.API_LOGIN, listOf("email" to email, "password" to password))
            .header("accept" to "application/json")
            .responseJson { _, _, result ->
                result.success {
                    var res = it.obj()
                    if (res.getBoolean("status")) {

                        Toast.makeText(this, "Success.", Toast.LENGTH_SHORT).show()

                        var user = res.getJSONObject("user")

                        SharedPreferences.setPrefernces(
                            this@MainActivity, Constants.FILE_USER,
                            Constants.USER_EMAIL, user.getString("email")
                        )
                        SharedPreferences.setPrefernces(
                            this@MainActivity, Constants.FILE_USER,
                            Constants.USER_NAME, user.getString("name")
                        )
//                       SharedPreferences.clearPreferences(this@MainActivity, Constants.FILE_USER)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            res.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }

    fun OpenSignupPage(view: View) {
        startActivity(Intent(this@MainActivity, signupActivity::class.java))
    }
}
