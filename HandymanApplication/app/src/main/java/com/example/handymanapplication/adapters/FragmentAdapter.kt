package com.example.handymanapplication.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.handymanapplication.ui.dashboard.OngoingRequestsFragment
import com.example.handymanapplication.ui.dashboard.OutgoingRequestsFragment
import android.provider.SyncStateContract.Helpers.update




//wan 3am test3mil l fragment

class FragmentAdapter
    (
    private val context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount() = 2

    override fun getItem(position: Int) = when (position) {
        0 -> OngoingRequestsFragment().newInstance()
        1 ->
            OutgoingRequestsFragment().newInstance()
        else -> throw IllegalStateException("Unexpected position $position")
    }



    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Requests"
        1 -> "Jobs"
        else -> throw IllegalStateException("Unexpected position $position")
    }



}