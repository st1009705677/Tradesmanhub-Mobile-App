package com.example.tradesmanhub

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class TradesmanHub: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Initialize Firebase App Check
//        val firebaseAppCheck = FirebaseAppCheck.getInstance()
//        firebaseAppCheck.installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
    }
}