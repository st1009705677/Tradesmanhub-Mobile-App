package com.example.tradesmanhub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {

    // Decalare a firebase auth object
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Initialize the firebase onject
        auth = FirebaseAuth.getInstance()

        // Declare a user object
        val user = auth.currentUser

        Handler().postDelayed({
            if(user != null){       // Check if user is already signed in
                val intent = Intent(this, NavMenu::class.java)
                startActivity(intent)
                finish()
            }else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }, 3000)
    }
}
