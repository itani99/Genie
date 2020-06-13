package com.example.handymanapplication.ui.home


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.PostAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.posts_recycler.*
class HomeFragment : Fragment() {
    private var recyclerAdapter: PostAdapter? = null

    var handler: Handler = Handler()
    val tags = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.hide()
        //activity!!.actionBar!!.hide()

        return inflater.inflate(
            R.layout.posts_recycler,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTgas()
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
                    if (progressStatus === 100) {
                        // Hide the progress bar from layout after finishing task
                        progress.setVisibility(View.GONE)
                        post_recycler.visibility = View.VISIBLE
                        search_input.visibility = View.VISIBLE
                        create_post_btn.visibility = View.VISIBLE
                    }
                }
            }
        }).start()

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, tags)
        (search_input)?.setAdapter(adapter)

        val create =
            view!!.findViewById(com.example.handymanapplication.R.id.create_post_btn) as Button
        create.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                val intent = Intent(context!!, CreatePostActivity::class.java)
                startActivity(intent)
            }
        })

        post_recycler!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        recyclerAdapter = PostAdapter(context!!)
        init()
        post_recycler.adapter = recyclerAdapter

        // adapter.filter("name", input)
        search_input!!.addTextChangedListener(object : TextWatcher {
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
                Utils.AUTHORIZATION to SharedPreferences.getToken(activity!!.baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {


                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {


                        //     var services = res.getJSONObject("services")
                        activity!!.runOnUiThread {


                            val items = res.getJSONArray("posts")

                            for (i in 0 until items.length()) {
                                    recyclerAdapter!!.setItem(items.getJSONObject(i))
                                }
//                            recyclerAdapter!!.notifyDataSetChanged()




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
                        requireActivity().runOnUiThread {


                            val items = res.getJSONArray("services")

                            for (i in 0 until items.length()) {
                                tags.add(items.optJSONObject(i).optString("name"))
                                //  adapter.setItem(items.getJSONObject(i))
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

