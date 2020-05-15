package com.example.handymanapplication.ui.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handymanapplication.Models.TimeLineItem
import com.example.handymanapplication.Models.receiptItem
import com.example.handymanapplication.R
import com.example.handymanapplication.adapters.ReceiptAdapter
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.payment_toolbar.*
import android.widget.EditText
import android.widget.Toast
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.activities.HomePageActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode


class PaymentActivity : AppCompatActivity() {
    var round_counter: Int? = 0
    var request_id: String? = null
    private var recyclerAdapter: ReceiptAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        supportActionBar!!.hide()
        var obje = JSONObject(intent!!.extras!!.getString("object"))
        var object2 = JSONObject(obje.getString("nameValuePairs"))
        request_id = (object2).optString("request_id")
        receipt_recycler!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        add_receipt_item.setOnClickListener {
            withEditText(it)

        }
        reset_btn.setOnClickListener {
            total_receipt.text = 00.00.toString()
            recyclerAdapter!!.removeItems()
            round_counter = 0

            color_round_1.setBackgroundResource(R.drawable.ic_rounds)

            color_round_2.setBackgroundResource(R.drawable.ic_rounds)

            color_round_3.setBackgroundResource(R.drawable.ic_rounds)

            color_round_4.setBackgroundResource(R.drawable.ic_rounds)

            color_round_5.setBackgroundResource(R.drawable.ic_rounds)

            color_round_6.setBackgroundResource(R.drawable.ic_rounds)

        }
        submit_receipt.setOnClickListener {
            sendReceipt()

        }


        recyclerAdapter = ReceiptAdapter(this)

        receipt_recycler.adapter = recyclerAdapter

    }


    fun withEditText(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("item description")
        val dialogLayout = inflater.inflate(R.layout.custom_dialog, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.item_name)
        val editText2 = dialogLayout.findViewById<EditText>(R.id.item_quantity)
        val editText3 = dialogLayout.findViewById<EditText>(R.id.item_price)


        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            round_counter = round_counter!! + 1
            if (round_counter == 1)
                color_round_1.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 2)
                color_round_2.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 3)
                color_round_3.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 4)
                color_round_4.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 5)
                color_round_5.setBackgroundResource(R.drawable.ic_round)

            if (round_counter == 6)
                color_round_6.setBackgroundResource(R.drawable.ic_round)

            total_receipt.text =
                (
                        total_receipt.text.toString().toDouble()
                                + ((editText2.text.toString().toInt()
                                * editText3.text.toString().toDouble()))
                        ).toString()
            val ob: JSONObject = JSONObject()
            ob.put(
                "name", editText.text.toString()
            )
            ob.put(
                "price",
                editText3.text.toString().toDouble()
            )
            ob.put(
                "qty",
                editText2.text.toString().toInt()
            )
            recyclerAdapter!!.setItem(
                ob
            )


        }
        builder.show()
    }

    fun sendReceipt() {
        Fuel.post(
            Utils.API_RECEIPT.plus(request_id),
            listOf(
                "receipt" to recyclerAdapter!!.getList(),
                "total" to total_receipt.text.toString()
            )
        )
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {
                    runOnUiThread {
                        Toast.makeText(this!!, "Receipt Sent", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this, HomePageActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                    }
                }
                result.failure {

                    Toast.makeText(this!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}
