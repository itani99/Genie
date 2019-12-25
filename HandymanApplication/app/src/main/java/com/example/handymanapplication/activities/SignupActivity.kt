package com.example.handymanapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.handymanapplication.Utils.Constants
import com.example.handymanapplication.Utils.SharedPreferences
import android.view.View
import android.widget.Toast
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_verification.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.concurrent.TimeUnit

class SignupActivity : AppCompatActivity() {

    var callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(p0: FirebaseException) {


        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            Log.d("OnCode Sent", p0)
            startActivityForResult( Intent( this@SignupActivity , PhoneVerificationActivity::class.java), 1000)
        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_signup)
        super.onCreate(savedInstanceState)

        //  register_btn.setOnClickListener { register() }

        btn_register.setOnClickListener {
            if (edt_phone.text.toString().isEmpty()){
                return@setOnClickListener
            }
            var fauth = PhoneAuthProvider.getInstance()
            fauth.verifyPhoneNumber(
                edt_phone.text.toString(), // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (for callback binding)
                callback) // OnVerificationStateChangedCallbacks

        }

    }


    private fun register() {
        val name = edt_code.text.toString()
        val email = edt_email.text.toString()
        val password = edt_password.text.toString()
        val passwordConfirmation = edt_confirm_password.text.toString()

        //

        Fuel.post(
            Utils.API_Register,
            listOf(
                "password_confirmation"    to passwordConfirmation,
                "name" to name, "email" to email, "password" to password

            )
        )
            .header("accept" to "application/json")
            .responseJson { _, _, result ->
                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {

                        //  Toast.makeText(this, "Success.", Toast.LENGTH_SHORT).show()

                        var user = res.getJSONObject("user")

                        SharedPreferences.setPrefernces(
                            this@SignupActivity, Constants.FILE_USER,
                            Constants.USER_EMAIL, user.getString("email")
                        )
                        SharedPreferences.setPrefernces(
                            this@SignupActivity, Constants.FILE_USER,
                            Constants.USER_NAME, user.getString("name")
                        )
                        SharedPreferences.setPrefernces(
                            this@SignupActivity, Constants.FILE_USER,
                            Constants.USER_TOKEN, res.getString("token")
                        )
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                SharedPreferences.getToken(this@SignupActivity).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        val intent = Intent(this, HomePageActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)


//                       SharedPreferences.clearPreferences(this@MainActivity, Constants.FILE_USER)
                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            res.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {
                    runOnUiThread {
                        Toast.makeText(this@SignupActivity, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }

    fun openMainActivity(view: View) {
        val intent = Intent(this@SignupActivity, MainActivity::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}