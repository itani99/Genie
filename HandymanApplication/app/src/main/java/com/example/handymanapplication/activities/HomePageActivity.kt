package com.example.handymanapplication.activities

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.IOnBackPressed



class HomePageActivity : AppCompatActivity() {

    var navController :NavController?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigator)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

         navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.navigation_home, R.id.navigation_dashboard,
//            R.id.navigation_notifications,R.id.navigation_profile))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController!!)
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
        if(navHostFragment != null) {
            val childFragments = navHostFragment.childFragmentManager.fragments
            childFragments.forEach { fragment ->
                if ( fragment is IOnBackPressed
                    && !(fragment as IOnBackPressed).onBackPressed())
                    return
            }
        }
        super.onBackPressed()

    }
}
