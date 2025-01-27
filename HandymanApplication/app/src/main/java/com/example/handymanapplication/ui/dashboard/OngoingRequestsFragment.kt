package com.example.handymanapplication.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ramotion.foldingcell.FoldingCell
import com.example.handymanapplication.Models.ItemCell
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.adapters.FoldingCellListAdapter
import com.example.handymanapplication.Utils.IActionsOngoing
import com.example.handymanapplication.Utils.putExtraJson
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.ongoing_notifications.*
import org.json.JSONObject
import java.util.*

import kotlin.collections.ArrayList


class OngoingRequestsFragment : Fragment() {

    var items: ArrayList<ItemCell>? = ArrayList<ItemCell>()
    var adapter: FoldingCellListAdapter? = null
    var mcontext: Context? = null


    var handler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mcontext = context!!
        return inflater.inflate(
            com.example.handymanapplication.R.layout.ongoing_notifications,
            container,
            false
        )
    }

    fun newInstance(): OngoingRequestsFragment {
        return OngoingRequestsFragment()
    }

    override fun onResume() {
        super.onResume()
        //getRequests()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context!!
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
                        mainListView.visibility=View.VISIBLE
                        // Set a message of completion
//                        tv.setText("Operation completed...")
                    }
                }
            }
        }).start()

        val theListView = mainListView
        getRequests()



        adapter = FoldingCellListAdapter(mcontext!!, items!!,
            object :
                IActionsOngoing {
                override fun onListClick(list: ItemCell) {
                    var index: Int? = null
                    for (i in 0 until items!!.size) {
                        if (items!!.get(i).equals(list)) {
                            index = i
                        }
                    }
                    adapter!!.registerToggle(index!!)
                    adapter!!.notifyDataSetChanged()
                }

                override fun onViewImageClick(list: ItemCell) {
                    val ob: JSONObject? = JSONObject()
                    ob!!.put("images", list.images)

                    val intent = Intent(context!!, ViewImagesActivity::class.java)

                    intent!!.putExtraJson("object", ob)
                    startActivity(intent)
                }

                override fun onAccept(item: ItemCell) {
                    replyToRequest(item.pos, "accepted")

                    var index: Int? = null
                    for (i in 0 until items!!.size) {
                        if (items!!.get(i).equals(item)) {
                            index = i
                        }
                    }
                    adapter!!.registerToggle(index!!)
                    items!!.remove(item)
                    adapter!!.notifyDataSetChanged()

                    activity!!.runOnUiThread {
                        Toast.makeText(mcontext!!, "accept", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onDelete(item: ItemCell) {


                    replyToRequest(item.pos, "rejected")
                    var index: Int? = null
                    for (i in 0 until items!!.size) {
                        if (items!!.get(i).equals(item)) {
                            index = i
                        }
                    }
                    adapter!!.registerToggle(index!!)
                    items!!.remove(item)
                    adapter!!.notifyDataSetChanged()

                    activity!!.runOnUiThread {
                        Toast.makeText(mcontext!!, "Rejected", Toast.LENGTH_LONG).show()
                    }
                }
            })
        theListView.setAdapter(adapter)

        // set on click event listener to list view
        theListView.setOnItemClickListener(AdapterView.OnItemClickListener
        { adapterView, view, pos, _ ->
            // toggle clicked cell state
            (view as FoldingCell).toggle(false)
            // register in adapter that state for selected cell is toggled
            adapter!!.registerToggle(pos)
        })

    }

    private fun replyToRequest(id: String, status: String) {
        Fuel.post(
            Utils.API_APPROVE_REQUESTS.plus(id), listOf("status" to status)
        )
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(mcontext!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {
                    activity!!.runOnUiThread {
                        Toast.makeText(mcontext!!, "Approved Successfully", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                result.failure {

                    Toast.makeText(mcontext!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun getRequests() {
        Fuel.get(Utils.API_ONGOING_REQUESTS)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(mcontext!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    val res = it.obj()

                    if (res.optString("status", "error") == "success") {
                        var list = res.getJSONArray("requests")

                        for (i in 0 until list.length()) {
                            val request = list.getJSONObject(i)
                            val client = request.getJSONObject("client")
                            val service = request.getJSONObject("service")
                            val address = request.optJSONObject("client_address")
                            val location = address!!.optJSONArray("location")


                            activity!!.runOnUiThread {

                                items!!.add(
                                    ItemCell(
                                        request.optString("_id", ""),
                                        client.optString("name", ""),
                                        client.optString(
                                            "image",
                                            "services/service_1585417538.png"
                                        ),
                                        request.optString("created_at", ""),
                                        request.optString("date", "")
                                        ,
                                        "",
                                        service.optString("image", ""),
                                        request.optString("subject", ""),
                                        request.optString("description", "")
                                        ,
                                        address.optString("street", "street"),
                                        request.optJSONArray("images"),
                                        false,
                                        "",
                                        location.getDouble(0),
                                        location.getDouble(1)
                                        ,
                                        request.optString("from", "").plus(":00"),
                                        request.optString("to", "").plus(":00"),
                                        service.optString("name", "service name"),
                                        address.optString("building", "building"),
                                        address.optString("floor", "floor")
                                    )

                                )
                                adapter!!.notifyDataSetChanged()

                                //                                items!!.add(
//                                    ItemCell(
//                                        request.get("_id").toString(), client.optString("name"), "", "", "", "", "", ""
//                                        , "", "", ""
//                                    )
//                                )
//                            adapter.setItem(items.getJSONObject(i))
                            }

                        }

                        activity!!.runOnUiThread {
                            adapter!!.notifyDataSetChanged()
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
