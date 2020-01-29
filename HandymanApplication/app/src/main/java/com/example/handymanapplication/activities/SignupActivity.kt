package com.example.handymanapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.handymanapplication.Utils.Constants
import com.example.handymanapplication.Utils.SharedPreferences
import android.view.View
import android.widget.Toast
import com.example.handymanapplication.activities.HomePageActivity
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.concurrent.TimeUnit

class SignupActivity : AppCompatActivity() {
var code: String=""
    var callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//code=p0.smsCode.toString()
            edt_verify_code.setText( p0.smsCode )
            btn_register.performClick()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d("OnCode", p0.localizedMessage)

        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            Log.d("OnCode Sent", p0)
            ll_signup_form.visibility = View.GONE
            ll_verify_code.visibility = View.VISIBLE
            authToken = p0
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            Log.d("OnCode", "Time out")
            super.onCodeAutoRetrievalTimeOut(p0)

        }


    }
    var fauth = PhoneAuthProvider.getInstance()
    var authToken : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_signup)
        getActionBar()?.hide()
        super.onCreate(savedInstanceState)

        //  register_btn.setOnClickListener { register() }

        btn_register.setOnClickListener {
            if ( ll_signup_form.visibility == View.VISIBLE){
                //make registration

                if (edt_phone.text.toString().isEmpty() ||
                    edt_name.text.toString().isEmpty() ||
                    edt_email.text.toString().isEmpty() ||
                    edt_password.text.toString().isEmpty() ||
                    edt_confirm_password.text.toString().isEmpty() ){
                    return@setOnClickListener
                }

                fauth.verifyPhoneNumber(
                    edt_phone.text.toString(), // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    this@SignupActivity, // Activity (for callback binding)
                    callback) // OnVerificationStateChangedCallbacks
            }else{
                //verify code
                var credential = PhoneAuthProvider.getCredential( authToken!! , edt_verify_code.text.toString())

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                    if ( it.isSuccessful ){
                        register()
                    }else{
                        runOnUiThread {
                            ll_signup_form.visibility = View.VISIBLE
                            ll_verify_code.visibility = View.GONE
                        }

                    }
                }
            }


        }

    }


    private fun register() {
        val name = edt_name.text.toString()
        val email = edt_email.text.toString()
        val phone = edt_phone.text.toString()
        val password = edt_password.text.toString()
        val passwordConfirmation = edt_confirm_password.text.toString()

        //

        Fuel.post(
            Utils.API_Register,
            listOf(
                "password_confirmation"    to passwordConfirmation,
                "name" to name, "email" to email,
                "password" to password,"phone" to phone ,"role" to "employee"

            )
        )
            .header("accept" to "application/json")
            .responseJson { _, _, result ->
                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {

                       Utils.sendRegistrationToServer(this) //  Toast.makeText(this, "Success.", Toast.LENGTH_SHORT).show()

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

    fun openSignUpPage(view: View) {
        val intent = Intent(this@SignupActivity, MainActivity::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}