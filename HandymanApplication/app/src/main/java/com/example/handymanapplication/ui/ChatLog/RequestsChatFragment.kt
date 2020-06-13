package com.example.handymanapplication.ui.ChatLog

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.ChatAdapter
import com.example.handymanapplication.adapters.PostAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.requests_chat.*

class RequestsChatFragment : Fragment() {

    private var chat_request: ChatAdapter? = null

    var handler: Handler = Handler()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            com.example.handymanapplication.R.layout.requests_chat,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var progress = progress_bar
        var progressStatus = 0
        Thread(Runnable {
            while (progressStatus < 100) {
                // Update the progress status
                progressStatus += 1

                // Try to sleep the thread for 20 milliseconds
                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                // Update the progress bar
                handler.post {
                    // Toast.makeText(context!!, progressStatus, Toast.LENGTH_LONG).show()

//                    pb.setProgress(progressStatus)
                    // Show the progress on TextView
//                    tv.setText(progressStatus + "")
                    // If task execution completed
                    if (progressStatus === 100) {
                        // Hide the progress bar from layout after finishing task
                        progress.setVisibility(View.GONE)
                        // Set a message of completion
//                        tv.setText("Operation completed...")
                    }
                }
            }
        }).start()
        chat_request_recycler!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        chat_request = ChatAdapter(context!!)

        chat_request_recycler.adapter = chat_request
        fetchRequests()
    }

    fun fetchRequests() {
        Fuel.get(Utils.API_CHAT_REQUESTS)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(activity!!.baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {
                        var list = res.getJSONArray("requests")

                        for (i in 0 until list.length()) {
                            var request = list.getJSONObject(i)
                            activity!!.runOnUiThread {
                                chat_request!!.setItem(request)
                            }
                        }
                    } else {

                        Toast.makeText(
                            activity,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }

    }
}