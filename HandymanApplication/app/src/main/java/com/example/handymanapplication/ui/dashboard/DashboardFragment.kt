package com.example.handymanapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.handymanapplication.R
import com.example.handymanapplication.adapters.DashboardPagerAdapter
import com.example.handymanapplication.adapters.FragmentAdapter
import kotlinx.android.synthetic.main.activity_bottom_navigator.*
import kotlinx.android.synthetic.main.cell_content_layout.*
import kotlinx.android.synthetic.main.fragment_request.*

class DashboardFragment : Fragment(), ViewPager.OnPageChangeListener {
    var adapter: FragmentAdapter? = null
    var onGoingFragment = OngoingRequestsFragment()
    var outGoingFragment = OutgoingRequestsFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar!!.hide()


        return inflater.inflate(R.layout.fragment_request, null, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//      val adapter = DashboardPagerAdapter(activity!!.supportFragmentManager) 7
        adapter = FragmentAdapter(context!!, childFragmentManager)


//        adapter.addFragment(clientAddressesF, getString(R.string.client_address))
        viewPager!!.offscreenPageLimit = 0
        viewPager!!.adapter = adapter
        viewPager.addOnPageChangeListener(this)
        tabs!!.setupWithViewPager(viewPager)

    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {

        if (adapter!!.getItem(position) is OutgoingRequestsFragment) {
            OutgoingRequestsFragment().update(context!!)
        }

    }


}