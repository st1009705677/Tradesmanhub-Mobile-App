package com.example.tradesmanhub.serviceproviders

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.tradesmanhub.R
import com.example.tradesmanhub.RequestServiceActivity
import com.example.tradesmanhub.data.Provider

class ProfileActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var service: TextView
    private lateinit var name: TextView
    private lateinit var description: TextView
    private lateinit var requestBtn: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        toolbar = findViewById(R.id.profileToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable the back button

        // Initialise variables
        name = findViewById(R.id.apvNameTV)
        service = findViewById(R.id.apvServiceTV)
        description = findViewById(R.id.apvDescriptionTV)
        requestBtn = findViewById(R.id.requestBtn)

        // Retrieve the ServiceProvider from the intent
        val provider = getServiceProviderFromIntent(intent)

        if(provider != null){
            name.text = provider.name
            service.text = provider.service_type
            description.text = provider.description
        }

        requestBtn.setOnClickListener {
            val intent = Intent(this, RequestServiceActivity::class.java)
            intent.putExtra("provider", provider)
            startActivity(intent)
        }

    }
    @Suppress("DEPRECATION")
    private fun getServiceProviderFromIntent(intent: Intent): Provider? {
        return intent.getParcelableExtra("provider")
    }

    // Handle back button click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to the parent activity
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}