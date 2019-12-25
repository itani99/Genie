package com.example.handymanapplication.Utils

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this@App)
    }
}