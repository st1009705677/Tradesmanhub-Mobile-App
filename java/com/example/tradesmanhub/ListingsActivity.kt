package com.example.tradesmanhub

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tradesmanhub.Utils.ContactUtil
import com.example.tradesmanhub.Utils.FirebaseUtil
import com.example.tradesmanhub.adapters.ListingAdapter
import com.example.tradesmanhub.data.JobPost
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class ListingsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    // Declare firebase objects
    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: FirebaseDatabase
    private lateinit var firebaseUtil: FirebaseUtil
    private lateinit var contactUtil: ContactUtil

    // Declare view components
    private lateinit var jobsList: ArrayList<JobPost>

    // Declare adapter components
    private lateinit var recyclerView: RecyclerView
    private lateinit var listingsAdapter: ListingAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listings)
        toolbar = findViewById(R.id.listingToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable the back button

        // Initialize objects
        auth = FirebaseAuth.getInstance()
        firebase = Firebase.database
        firebaseUtil = FirebaseUtil(firebase)
        jobsList = ArrayList()
        recyclerView = findViewById(R.id.rvListingResults)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        listingsAdapter = ListingAdapter(jobsList)
        recyclerView.adapter = listingsAdapter

        getUserJobListings()        // Get firebase data and populate the recyclerview

        // Handle recyclerview item click
        listingsAdapter.viewJob = {post ->
            val intent = Intent(this, PostDetailsActivity::class.java)
            intent.putExtra("post", post)
            startActivity(intent)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getUserJobListings() {
        // Get current users id
        val user = auth.currentUser

        firebaseUtil.addValueEventListener("posts") { Posts ->

//            postsList.clear()       // Clear list on event change

            Posts.children.forEach { post ->
                val isAssigned = post.child("isAssigned").getValue(Boolean::class.java)

                if(isAssigned == false){
                    jobsList.add(
                        JobPost(
                            post.key ?: "",
                            post.child("client").getValue(String::class.java) ?: "",
                            post.child("service").getValue(String::class.java) ?: "",
                            post.child("description").getValue(String::class.java) ?: "",
                            post.child("address").getValue(String::class.java) ?: "",
                            post.child("budget").getValue(String::class.java) ?: "",
                            isAssigned ?: false,
                            "",
                            post.child("start_date").getValue(String::class.java) ?: ""
                    ))
                    listingsAdapter.notifyDataSetChanged()
                }
            }

//                postsList.add(JobPost(client, service, description, address, budget, isAssigned, "", date, provider))
//            Log.d("Posts Activity", "${Posts}")
            listingsAdapter.notifyDataSetChanged()

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