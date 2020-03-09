package com.example.handymanapplication.ui.ChatLog

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Constants
import org.json.JSONObject

class ChatLogActivity : AppCompatActivity() {


    private val aLBReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                var msg = JSONObject( intent!!.extras!!.getString(Constants.PARAM_NOTIFICATION_INFO))

               // the activity is notified that there is a new message in the intent
            }catch (e:Exception){

            }

        }

    }


    override fun onPause() {
        unregisterReceiver(aLBReceiver)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(aLBReceiver,IntentFilter().apply {
            addAction(Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_EVENT)
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_chat_row)

        registerReceiver(aLBReceiver,IntentFilter().apply {
            addAction(Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_EVENT)
        })
    }


}
