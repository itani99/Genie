package com.example.handymanapplication.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.handymanapplication.Models.ItemCell
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.*
import com.example.handymanapplication.activities.MainActivity
import com.example.handymanapplication.adapters.FoldingCellListAdapter
import com.example.handymanapplication.adapters.OutgoingFoldingCellListAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.ramotion.foldingcell.FoldingCell
import kotlinx.android.synthetic.main.fragment_request.*
import kotlinx.android.synthetic.main.outgoing_notifications.*
import org.json.JSONObject


class OutgoingRequestsFragment : Fragment(), IUpdate {

    var items: ArrayList<ItemCell>? = ArrayList<ItemCell>()
    var adapter_outgoing: OutgoingFoldingCellListAdapter? = null
    var theListView: ListView? = null
    var mcontext: Context? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mcontext == null) {
            mcontext = context!!
        }
        return inflater.inflate(R.layout.outgoing_notifications, container, false)
    }

    fun newInstance(): OutgoingRequestsFragment {
        return OutgoingRequestsFragment()
    }


    override fun update(context: Context) {
        // welcome()
        Toast.makeText(context, "hello", Toast.LENGTH_LONG)
            .show()
        mcontext = context!!
        //updateItems(mcontext!!)

//        if (adapter_outgoing!=null){
//                  items!!.add(item)
//        adapter_outgoing!!.notifyDataSetChanged()
//    }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }


    fun init() {

        theListView = out_mainListView

        getRequests(mcontext!!)

        adapter_outgoing = OutgoingFoldingCellListAdapter(mcontext!!, items!!


            , object :
                IActionsOutgoing {
                override fun onListClick(list: ItemCell) {
                    var index: Int? = null
                    for (i in 0 until items!!.size) {
                        if (items!!.get(i).equals(list)) {
                            index = i
                        }
                    }
                    adapter_outgoing!!.registerToggle(index!!)
                    adapter_outgoing!!.notifyDataSetChanged()
                }

                override fun onItemPay(list: ItemCell) {
                    val ob: JSONObject? = JSONObject()
                    ob!!.put("request_id", list.pos)

                    val intent = Intent(context!!, PaymentActivity::class.java)

                    intent!!.putExtraJson("object", ob)
                    startActivity(intent)
                }

                override fun onViewImageClick(list: ItemCell) {
                    val ob: JSONObject? = JSONObject()
                    if (list.image.length > 0){
                        ob!!.put("images", list.images)

                    val intent = Intent(context!!, ViewImagesActivity::class.java)

                    intent!!.putExtraJson("object", ob)
                    startActivity(intent)
                }}

                override fun onRescheduleCllick(list: ItemCell) {

                    Toast.makeText(
                        mcontext,
                       "Reschedule",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        )

        theListView!!.setAdapter(adapter_outgoing)
        // set on click event listener to list view
        theListView!!.setOnItemClickListener(
            AdapterView.OnItemClickListener
            { adapterView, view, pos, _ ->
                // toggle clicked cell state
                (view as FoldingCell).toggle(false)
                // register in adapter that state for selected cell is toggled
                adapter_outgoing!!.registerToggle(pos)
            }

        )
    }

    private fun getRequests(mcontext: Context) {
        Fuel.get(Utils.API_OUTGOING_REQUESTS.plus(SharedPreferences.getID(mcontext!!)))
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(mcontext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        activity!!.runOnUiThread {
                            var list = res.getJSONArray("requests")

                            for (i in 0 until list.length()) {
                                var request = list.getJSONObject(i)
                                var client = request.getJSONObject("client")
                                var service = request.getJSONObject("service")
                           //     val location = request.getJSONArray("location")
                                var flag = false
                                if (request.has("receipt")) {
                                    flag = true
                                }
                                items!!.add(
                                    ItemCell(
                                        request.optString("_id", ""),
                                        client.optString("name", ""),
                                        client.optString("image", "image"),
                                        request.optString("created_at", ""),
                                        request.optString("date", "")
                                        ,
                                        "",
                                        service.optString("image", ""),
                                        request.optString("title", ""),
                                        request.optString("description", "")
                                        ,
                                        "", request.optJSONArray("images"), flag,
                                        "",
                                        -122.0839998498559,
                                        37.42199952052943
                                        , request.optString("from", "").plus(":00"),
                                        request.optString("to", "").plus(":00"),
                                        service.optString("name", "service name")
                                    )

                                )


                            }

                        }

                        activity!!.runOnUiThread {

                            adapter_outgoing!!.notifyDataSetChanged()


                        }
                    } else {

                        Toast.makeText(
                            mcontext,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(mcontext, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }


            }

    }

}