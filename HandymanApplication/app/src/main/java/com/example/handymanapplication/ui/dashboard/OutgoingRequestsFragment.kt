package com.example.handymanapplication.ui.dashboard

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.handymanapplication.Models.ItemCell
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.*
import com.example.handymanapplication.activities.HomePageActivity
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


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
                    if (list.image.length > 0) {
                        ob!!.put("images", list.images)

                        val intent = Intent(context!!, ViewImagesActivity::class.java)

                        intent!!.putExtraJson("object", ob)
                        startActivity(intent)
                    }
                }

                override fun onRescheduleCllick(list: ItemCell) {

                    val cal = Calendar.getInstance()
                    val c = Calendar.getInstance()
                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)
                    var from_hour: Any? = null
                    var stringDate1: Any? = null
                    var to_hour: Any? = null

                    var date: Any? = null
                    DatePickerDialog(
                        context!!,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                            c!!.set(year, monthOfYear, dayOfMonth)
                            var dateofl: String? = (dayOfMonth.toString())
                            var monthofk: String? = ((monthOfYear + 1).toString())
                            if (dateofl!!.length == 1) {
                                dateofl = "0".plus((dayOfMonth.toString()))
                            }
                            if (monthofk!!.length == 1) {
                                monthofk = "0".plus(((monthOfYear + 1).toString()))
                            }
                            var ex =
                                year.toString().plus("-").plus(monthofk).plus("-").plus(dateofl)
                            stringDate1 = LocalDate.parse(ex).toString()


                            var timepicker = TimePickerDialog(

                                context, TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                                    from_hour = hour

                                    TimePickerDialog(
                                        context,
                                        TimePickerDialog.OnTimeSetListener { to_view, thour, tminute ->
                                            to_hour = thour


                                            AlertDialog.Builder(context)
                                                .setTitle("Please Confirm")

                                                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                .setPositiveButton(android.R.string.ok) { vdialog, _ ->
                                                    vdialog.dismiss()

                                                    Toast.makeText(
                                                        context!!,
                                                        ex.toString().plus(" \n").plus(from_hour).plus(
                                                            ""
                                                        )
                                                            .plus(to_hour)
                                                        ,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    reschedule(list, ex, from_hour!!, to_hour!!)

                                                }
                                                .create().show()
                                        },
                                        hour + 1,
                                        minute,
                                        true
                                    ).show()
                                },
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            )
                            timepicker.show()


                        },
                        year,
                        month,
                        day
                    ).show()









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

    private fun reschedule(item: ItemCell, date: Any, from: Any, to: Any) {
        Fuel.post(
            Utils.API_RESCHEDULE.plus(item.pos),
            listOf(
                "date" to date, "from" to from, "to" to to
            )
        )
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {
                    val intent = Intent(context!!, HomePageActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                result.failure {

                    Toast.makeText(context!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
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
                                val address = request.getJSONObject("client_address")
                                var location = address.getJSONArray("location")

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
                                        request.optString("subject", ""),
                                        request.optString("description", "")
                                        ,
                                        address.optString("street","street"),
                                        request.optJSONArray("images"),
                                        flag,
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