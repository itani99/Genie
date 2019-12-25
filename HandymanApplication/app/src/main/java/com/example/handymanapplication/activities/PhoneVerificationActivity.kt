package com.example.handymanapplication.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.handymanapplication.R
import kotlinx.android.synthetic.main.activity_phone_verification.*


class PhoneVerificationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_phone_verification)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fbtn_verify.setOnClickListener {
            var intent = Intent()
            intent.putExtra("code",edt_code.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

}
