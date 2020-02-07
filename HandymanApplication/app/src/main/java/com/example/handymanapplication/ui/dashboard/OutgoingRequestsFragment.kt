package com.example.handymanapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.handymanapplication.R

class OutgoingRequestsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.outgoing_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        edt_text.text = "Fragment two"
    }


}