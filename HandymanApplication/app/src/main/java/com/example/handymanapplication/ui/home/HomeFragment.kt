package com.example.handymanapplication.ui.home


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            com.example.handymanapplication.R.layout.posts_recycler,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

