package com.example.handymanapplication.ui.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.adapters.DashboardPagerAdapter
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment()
    , IOnBackPressed {
    private var businessInformation =
        BusinessInformationFragment()
    private var personalInformation =
        PersonalinformationFragment()
    private var creditCardFragment = CreditCardFragment()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, null, false)
    }


    var adapter: DashboardPagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DashboardPagerAdapter(activity!!.supportFragmentManager)


        adapter!!.addFragment(personalInformation, "Personal Information")
        adapter!!.addFragment(businessInformation, "Business Information")
        adapter!!.addFragment(creditCardFragment," Credit Card")

//        adapter.addFragment(clientAddressesF, getString(R.string.client_address))
        viewPager_profiler!!.offscreenPageLimit = 0
        viewPager_profiler!!.adapter = adapter
        tabs_profiler!!.setupWithViewPager(viewPager_profiler)
        //try

    }

    override fun onBackPressed(): Boolean {
        val childFragments = adapter!!.getItems()
        childFragments.forEach { fragment ->
            if (fragment is IOnBackPressed)
                return (fragment as IOnBackPressed).onBackPressed()
        }
        return false
    }
}