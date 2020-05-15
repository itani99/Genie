package com.example.handymanapplication.ui.ChatLog


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Models.ChatItems
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Constants
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.ChatAdapter
import com.example.handymanapplication.adapters.ChatLogAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_chat_log.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatLogActivity : AppCompatActivity() {
    var adapter: ChatLogAdapter? = null
    var request_id: String? = null

    var image: String? = null

    var flag: Boolean? = false
    private val aLBReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                var msg: JSONObject =
                    JSONObject(intent!!.extras!!.getString(Constants.PARAM_NOTIFICATION_INFO))
                var dataJson = msg.getString("type")

                val ob = JSONObject()
                val id = msg.getString("request_id")
                val from = msg.getJSONObject("from")
                val client_image = from.optString("image", "services/service_1585417538.png")

                setTitle(from.optString("name"))
                if (id.equals(request_id)) {
                    adapter!!.setItem(
                        ChatItems.From(
                            msg.optString("date", "date"),
                            "",
                            msg.optString("message", "message"),
                            client_image

                        )
                    )
                }
                recyclerview_chat_log.smoothScrollToPosition(adapter!!.itemCount - 1)


            } catch (e: Exception) {

            }

        }


    }

    override fun onPause() {
        unregisterReceiver(aLBReceiver)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(aLBReceiver, IntentFilter().apply {
            addAction(Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_EVENT)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        registerReceiver(aLBReceiver, IntentFilter().apply {
            addAction(Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_EVENT)
        })

        loadImage()
        send_button_chat_log.setOnClickListener {
            sendMessage()

            Utils.hideSoftKeyBoard(this, send_button_chat_log)

            runOnUiThread {
                recyclerview_chat_log.smoothScrollToPosition(adapter!!.itemCount - 1)
            }
        }
        adapter = ChatLogAdapter(this)
        recyclerview_chat_log.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )


        recyclerview_chat_log.adapter = adapter!!
        var object2: JSONObject? = null

        var obje: JSONObject = JSONObject(intent.extras?.getString("object").toString())
        if (obje.has("nameValuePairs")) {


            object2 = JSONObject(obje.getString("nameValuePairs"))
            request_id = object2.getString("_id")
            Toast.makeText(this, request_id, Toast.LENGTH_LONG).show()
        }
        if (object2!!.has("messages")) {


            var object3 = JSONObject(object2.optString("messages", "messages"))
            var object4: JSONArray = (object3.getJSONArray("values"))
            for (items in 0 until object4.length()) {


                val messageItem: JSONObject = (object4.getJSONObject(items))
                val message = messageItem.getJSONObject("nameValuePairs")
                val messageItemFrom = message.getJSONObject("from")
                val from = messageItemFrom.getJSONObject("nameValuePairs")
                if (from.optString("_id") == SharedPreferences.getID(this).toString()) {

                    adapter!!.setItem(
                        ChatItems.To(
                            message.optString("date", "date"),
                            from.optString("name", "name"),
                            message.optString("message", "message"),
                            from.optString("image", "image")
                        )
                    )
                } else {
                    setTitle(from.optString("name"))

                    adapter!!.setItem(
                        ChatItems.From(
                            message.optString("date", "date"),
                            from.optString("name", "name"),
                            message.optString("message", "message"),
                            from.optString("image", "image")
                        )
                    )


                }

            }
        }

        recyclerview_chat_log.layoutManager = LinearLayoutManager(
            this@ChatLogActivity,
            LinearLayoutManager.VERTICAL, false
        )

        if (adapter!!.itemCount > 2) {
            recyclerview_chat_log.smoothScrollToPosition(adapter!!.itemCount - 1)
        }

    }


    fun sendMessage() {

        flag = true
        var message = edittext_chat_log.text.toString()
        adapter!!.setItem(
            ChatItems.To(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "any",
                message,
                image!!
            )
        )
        edittext_chat_log.text.clear()
        Fuel.post(
            Utils.API_SEND_MESSAGE.plus(request_id), listOf("message" to message)
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
        ).responseJson { _, _, result ->

            result.success {


            }
            recyclerview_chat_log.smoothScrollToPosition(adapter!!.itemCount - 1)

            result.failure {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun loadImage() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        var profile = res.getJSONObject("profile")
                        this!!.runOnUiThread {


                            if (profile.has("image")) {

                                image = profile.optString(
                                    "image",
                                    "ic_user.png"
                                )


                            } else {
                                image = "services/service_1585417538.png"

                            }

//
                        }
                    } else {

                        Toast.makeText(
                            this,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }


    }
}