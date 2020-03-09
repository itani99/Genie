package com.example.handymanapplication.ui.Profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.handymanapplication.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_credit_card.*


class CreditCardFragment : Fragment() {

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.show()
//init()
         return inflater.inflate(
            com.example.handymanapplication.R.layout.fragment_credit_card,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
       getView()!!.findViewById<TextView>(R.id.payment_amount).setText("11")
    }

    private fun init() { //eh wan kina kena bel billing :p

    }

}
