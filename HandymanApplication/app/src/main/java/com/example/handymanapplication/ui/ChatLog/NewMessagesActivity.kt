package com.example.handymanapplication.ui.ChatLog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.handymanapplication.R

class NewMessagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchUsers()
        setContentView(R.layout.activity_new_message)
    }

    fun fetchUsers(){

    }
}
