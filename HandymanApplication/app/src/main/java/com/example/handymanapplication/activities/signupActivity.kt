package com.example.handymanapplication.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.handymanapplication.R
import kotlinx.android.synthetic.main.activity_signup.*

class signupActivity : AppCompatActivity() {
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);
        val name = HandymanNameRegister.text.toString()
        val email = HandymanEmailRegister.text.toString()
        val password = HandymanPasswordRegister.text.toString()
         }

    fun OpenMainActivity(view: View) {
        startActivity(Intent(this@signupActivity, MainActivity::class.java))
    }
}