package com.example.tradesmanhub

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.tradesmanhub.data.Item

class SendQuoteActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_quote)
        toolbar = findViewById(R.id.quoteToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable the back button


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