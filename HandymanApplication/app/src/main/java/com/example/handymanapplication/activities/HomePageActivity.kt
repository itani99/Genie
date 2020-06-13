package com.example.handymanapplication.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.IOnBackPressed
import com.example.handymanapplication.Utils.Utils
import com.example.handymanapplication.ui.dashboard.DashboardFragment
import com.google.firebase.auth.FirebaseAuth
import okhttp3.internal.Util


class HomePageActivity : AppCompatActivity() {

    var navController :NavController?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigator)
       // actionBar!!.hide()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

         navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController!!)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {
                Utils.logout(this)

            }
            R.id.action_settings -> {
               //todo
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
        if(navHostFragment != null) {
            val childFragments = navHostFragment.childFragmentManager.fragments
            childFragments.forEach { fragment ->
                if ( fragment is IOnBackPressed && !(fragment as IOnBackPressed).onBackPressed())
                    return
            }
        }
        super.onBackPressed()

    }
}
