package com.example.handymanapplication.ui.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.MyPostsAdapter
import com.example.handymanapplication.ui.home.CreatePostActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_my_posts.*
import kotlinx.android.synthetic.main.posts_recycler.*


class MyPostsActivity : AppCompatActivity() {

    private var recyclerAdapter: MyPostsAdapter? = null

    var handler: Handler = Handler()
    val tags = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        getTgas()
        var progress = my_progress_bar
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
                    if (progressStatus === 100) {
                        // Hide the progress bar from layout after finishing task
                        progress.setVisibility(View.GONE)
                        my_post_recycler.visibility = View.VISIBLE
                        search_input_my.visibility = View.VISIBLE
                    }
                }
            }
        }).start()

        val adapter = ArrayAdapter(this, R.layout.list_item, tags)
        (search_input)?.setAdapter(adapter)


        my_post_recycler!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



        recyclerAdapter = MyPostsAdapter(this!!)
        init()
        my_post_recycler.adapter = recyclerAdapter

        // adapter.filter("name", input)
        search_input_my!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                recyclerAdapter!!.filter("title", charSequence.toString())
            }

            //stripe
            override fun afterTextChanged(editable: Editable) {}
        })


    }


    fun init() {
        Fuel.get(Utils.API_POST)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
            .responseJson { _, _, result ->

                result.success {


                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {


                        //     var services = res.getJSONObject("services")
                        this!!.runOnUiThread {


                            val items = res.getJSONArray("posts")

                            for (i in 0 until items.length()) {
                                val item = (items.getJSONObject(i)).optJSONObject("handyman")
                                if (item.optString("_id").equals(SharedPreferences.getID(this))) {
                                    recyclerAdapter!!.setItem(items.getJSONObject(i))

                                }
                            }

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

    fun getTgas() {
        Fuel.get(Utils.API_Services)
            .header(
                "accept" to "application/json"
            )
            .responseJson { _, _, result ->

                result.success {
                    //
                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        //     var services = res.getJSONObject("services")
                        this.runOnUiThread {


                            val items = res.getJSONArray("services")

                            for (i in 0 until items.length()) {
                                tags.add(items.optJSONObject(i).optString("name"))
                                //  adapter.setItem(items.getJSONObject(i))
                            }


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



