package com.example.handymanapplication.ui.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.handymanapplication.R
import com.example.handymanapplication.adapters.DashboardPagerAdapter
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    var bussinessInformation =
        BussinessinformationFragment()
    var personalInformation =
        PersonalinformationFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, null, false)
    }

//    override fun onBackPressed(): Boolean {
//
//        activity?.runOnUiThread {
//            AlertDialog.Builder(activity)
//                .setTitle(" Discard Changes?")
//                .setMessage("Are you sure?")
//                .setPositiveButton("Yes", { dialog, _ ->
//                    // close profile page
//                    dialog.dismiss()
//                })
//                .setNegativeButton("No", { dialog, _ ->
//                    dialog.dismiss()
//                }).create().show()
//        }
//
//
//        return true
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var adapter = DashboardPagerAdapter(activity!!.supportFragmentManager)


        adapter.addFragment(bussinessInformation, "Business Information")
        adapter.addFragment(personalInformation, "Personal Information")
//        adapter.addFragment(clientAddressesF, getString(R.string.client_address))
        viewPager_profiler!!.offscreenPageLimit = 0
        viewPager_profiler!!.adapter = adapter
        tabs_profiler!!.setupWithViewPager(viewPager_profiler)
        //try
    }

}