package com.example.handymanapplication.adapters

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.handymanapplication.ui.dashboard.OngoingRequestsFragment
import com.example.handymanapplication.ui.dashboard.OutgoingRequestsFragment
import android.view.ViewGroup
import java.util.HashMap
import java.util.HashSet


class CustomAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    val NUM_ITEMS: Int = 2

    var mFragmentTags: HashMap<Int, String>? = HashMap<Int, String>()
    var mFragmentManager: FragmentManager = manager


    override fun getCount(): Int {
        return NUM_ITEMS

    }


    override fun getItem(position: Int): Fragment {

        when (position) {
            0 ->
                return OngoingRequestsFragment().newInstance()
            1 ->
                return OutgoingRequestsFragment().newInstance()
            else
            -> throw IllegalStateException("Unexpected position $position")

        }
    }


    override fun getPageTitle(position: Int): CharSequence? {
        if (position==0)
            return  "Ongoing Requests"
        else
            return "Outgoing Requests"
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val `object` = super.instantiateItem(container, position)
        if (`object` is Fragment) {
            val tag = `object`.tag
            mFragmentTags!!.put(position, tag!!)
        }
        return `object`
    }

    fun getFragment(position: Int): Fragment? {
        var fragment: Fragment? = null
        val tag = mFragmentTags!!.get(position)
        if (tag != null) {
            fragment = mFragmentManager.findFragmentByTag(tag)
        }
        return fragment
    }

}