package com.example.tradesmanhub

import android.annotation.SuppressLint
import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.tradesmanhub.Utils.ContactUtil
import com.example.tradesmanhub.Utils.FirebaseUtil
import com.example.tradesmanhub.data.JobPost
import com.example.tradesmanhub.data.Provider
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class PostDetailsActivity : AppCompatActivity() {

    private lateinit var firebase: FirebaseDatabase
    private lateinit var firebaseUtil: FirebaseUtil

    private lateinit var toolbar: Toolbar
    private lateinit var client: TextView
    private lateinit var location: TextView
    private lateinit var description: TextView
    private lateinit var sendQuote: Button
    private lateinit var quotesRecyclerView: RecyclerView
    private lateinit var callBtn: ImageButton
    private lateinit var emailBtn: ImageButton
    private lateinit var contactLayout: LinearLayout
    private lateinit var contactUtil: ContactUtil

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        toolbar = findViewById(R.id.jobDetailsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable the back button


        // Initialize variables
        firebase = Firebase.database
        firebaseUtil = FirebaseUtil(firebase)
        contactUtil = ContactUtil(this)
        client = findViewById(R.id.details_client)
        location = findViewById(R.id.details_address)
        description = findViewById(R.id.details_description)
        sendQuote = findViewById(R.id.details_quote_btn)
        quotesRecyclerView = findViewById(R.id.details_quote_rv)
        callBtn = findViewById(R.id.details_call)
        emailBtn = findViewById(R.id.details_email)
        contactLayout = findViewById(R.id.details_contact_layout)

        val job = getJobFromIntent(intent)
        if(job != null){
            getUserName(job.client){user->
                val name = user.child("displayName").getValue(String::class.java) ?: "Unknown"
                val imageUrl = user.child("imageUrl").getValue(String::class.java) ?: ""
                val cell = user.child("phoneNumber").getValue(String::class.java) ?: ""
                val email = user.child("email").getValue(String::class.java) ?: ""
                client.text = name
                location.text = job.address
                description.text = job.description

                if(email != "" || cell != ""){
                    contactLayout.visibility =  View.VISIBLE
                    if(email != "") {
                        emailBtn.visibility = View.VISIBLE
                        emailBtn.setOnClickListener {
                            contactUtil.openEmailIntent(email)
                        }
                    }
                    if(cell != "") {
                        callBtn.visibility = View.VISIBLE
                        contactUtil.openPhoneDialer(cell)
                    }
                }

            }
        }

        // Handle send quote button
        sendQuote.setOnClickListener {

        }

    }
    private fun getJobFromIntent(intent: Intent): JobPost? {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return intent.getParcelableExtra("post")
        } else {
            @Suppress("DEPRECATION")
            return intent.getParcelableExtra("post") as? JobPost
        }
    }

    private fun getUserName(uid: String, callback: (DataSnapshot) -> Unit) {
        firebaseUtil.addSingleValueEventListener("users/$uid"){snapshot ->
            callback(snapshot)
        }
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