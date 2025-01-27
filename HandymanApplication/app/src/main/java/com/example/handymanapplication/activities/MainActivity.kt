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

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide()
        setContentView(R.layout.activity_login)

        if (SharedPreferences.getToken(this@MainActivity) != null) {
            startActivity(Intent(this@MainActivity, HomePageActivity::class.java))
            finish()
        }

        btn_login.setOnClickListener { login() }

    }

    private fun login() {

        btn_login.isEnabled = false
        val email = edt_email.text.toString()
        val password = edt_password.text.toString()
        val role = "employee"

        if (password.length < 8) {
            Toast.makeText(
                this,
                "Wrong password",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        Fuel.post(
            Utils.API_LOGIN, listOf("email" to email, "password" to password, "role" to role)
        )
            .header("accept" to "application/json")
            .responseJson { _, _, result ->
                result.success {

                    var res = it.obj()
                    if (res.optString("status", "errorss") == "success") {

                        // Toast.makeText(this, "Success.", Toast.LENGTH_SHORT).show()

                        var user = res.getJSONObject("user")

                        SharedPreferences.setPrefernces(
                            this@MainActivity, Constants.FILE_USER,
                            Constants.USER_EMAIL, user.getString("email")
                        )
                        SharedPreferences.setPrefernces(
                            this@MainActivity, Constants.FILE_USER,
                            Constants.USER_NAME, user.getString("name")
                        )
                        SharedPreferences.setPrefernces(
                            this@MainActivity, Constants.FILE_USER,
                            Constants.USER_ID, user.getString("_id")
                        )
                        SharedPreferences.setPrefernces(
                            this@MainActivity, Constants.FILE_USER,
                            Constants.USER_TOKEN, res.getString("token")
                        )

                        Utils.sendRegistrationToServer(this)

                        val intent = Intent(this, HomePageActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)


                    } else {

                        btn_login.isEnabled = true
                        Toast.makeText(
                            this@MainActivity,
                            res.toString()
                            ,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
                result.failure {

                    runOnUiThread {

                        btn_login.isEnabled = true
                        Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }

    fun openSignUpPage(view: View) {
        val intent = Intent(this@MainActivity, SignUpActivity::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }


}
