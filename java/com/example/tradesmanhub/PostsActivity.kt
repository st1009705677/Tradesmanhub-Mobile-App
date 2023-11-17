package com.example.tradesmanhub

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tradesmanhub.Utils.ContactUtil
import com.example.tradesmanhub.Utils.FirebaseUtil
import com.example.tradesmanhub.adapters.PostAdapter
import com.example.tradesmanhub.adapters.SearchAdapter
import com.example.tradesmanhub.data.JobPost
import com.example.tradesmanhub.data.Provider
import com.example.tradesmanhub.serviceproviders.ProfileActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class PostsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    // Declare firebase objects
    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: FirebaseDatabase
    private lateinit var firebaseUtil: FirebaseUtil
    private lateinit var contactUtil: ContactUtil

    // Declare view components
    private lateinit var postsList: ArrayList<JobPost>

    // Declare adapter components
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        toolbar = findViewById(R.id.postsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable the back button

        // Initialize firebase objects
        auth = FirebaseAuth.getInstance()
        firebase = Firebase.database
        firebaseUtil = FirebaseUtil(firebase)
        contactUtil = ContactUtil(this)

        //
        postsList = ArrayList()
        recyclerView = findViewById(R.id.rvPostResults)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postsList)
        recyclerView.adapter = postAdapter

        getUserJobPosts()

        postAdapter.emailProvider = {post ->
            val pid = post.provider
            if(pid != ""){
                firebaseUtil.addSingleValueEventListener("users/$pid/email"){
                    val email = it.value as? String ?: ""
                    if(email != ""){
                        contactUtil.openEmailIntent(email)
                    }
                }
            }
        }

        postAdapter.callProvider = {post ->
            val pid = post.provider
            if(pid != ""){
                firebaseUtil.addSingleValueEventListener("users/$pid/phoneNumber"){
                    val cell = it.value as? String ?: ""
                    if(cell != ""){
                        contactUtil.openPhoneDialer(cell)
                    }
                }
            }
        }

        postAdapter.viewProvider = {post ->
            val pid = post.provider
            val service = post.service
            if(pid != "") {
                firebaseUtil.addSingleValueEventListener("services/$service/$pid") {
                    val provider = it.value as Map<String, Any>
                    val serviceProvider = Provider(
                        it.key ?: "",
                        provider["name"] as? String ?: "",
                        provider["service_type"] as? String ?: "",
                        provider["description"] as? String ?: "",
                        provider["averageRating"] as? Long ?: 0,
                        provider["address"] as? String ?: ""
                    )

                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("provider", serviceProvider)
                    startActivity(intent)
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getUserJobPosts() {
        // Get current users id
        val user = auth.currentUser

        firebaseUtil.addValueEventListener("users/${user?.uid}/posts") { userPosts ->

            postsList.clear()       // Clear list on event change
            val posts = userPosts.value as? Map<String, String>

            if (posts != null) {
                for ((postId, status) in posts) {
                    firebaseUtil.addSingleValueEventListener("posts/$postId") { snapshot ->
                        val post = snapshot.value as Map<String, Any>
                        val client = post["client"] as String
                        val service = post["service"] as String
                        val description = post["description"] as String
                        val address = post["address"] as String
                        val budget = post["budget"] as String
                        val isAssigned = post["isAssigned"] as Boolean
                        val date = post["start_date"] as String
                        val provider = post.get("provider") as? String ?: ""

                        // add to the list and otify adapter
                        postsList.add(JobPost(snapshot.key!!, client, service, description, address, budget, isAssigned, status, date, provider))
                        postAdapter.notifyDataSetChanged()
                    }
                }
            }
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