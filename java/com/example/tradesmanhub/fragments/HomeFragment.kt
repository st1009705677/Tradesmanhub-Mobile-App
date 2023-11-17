package com.example.tradesmanhub.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.tradesmanhub.ListingsActivity
import com.example.tradesmanhub.PostJobActivity
import com.example.tradesmanhub.R
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    private lateinit var username: TextView
    private lateinit var addPost: RelativeLayout
    private lateinit var jobListings: CardView

    // Firebase objects
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()
        addPost = view.findViewById(R.id.postJob)
        jobListings = view.findViewById(R.id.cv_clickable_listings)
        username = view.findViewById(R.id.uname_home)

        // Set username text
        val user = auth.currentUser
        username.text = user?.displayName ?: user?.email

        addPost.setOnClickListener {
            // Redirect to add post screen
            val intent = Intent(requireContext(), PostJobActivity::class.java)
            startActivity(intent)
        }
        jobListings.setOnClickListener {
            val intent = Intent(requireContext(), ListingsActivity::class.java)
            startActivity(intent)
        }


        return view
    }

}