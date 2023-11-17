package com.example.tradesmanhub.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.tradesmanhub.ListingsActivity
import com.example.tradesmanhub.PostsActivity
import com.example.tradesmanhub.R
import com.example.tradesmanhub.RequestsActivity

class ProfileFragment : Fragment() {

    // Declare component variables
    private lateinit var posts: CardView
    private lateinit var requests: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize component variables
        posts = view.findViewById(R.id.cv_job_posts)
        requests = view.findViewById(R.id.cv_job_requests)

        // Set onclick listeners
        posts.setOnClickListener {
            val intent = Intent(requireContext(), PostsActivity::class.java)
            startActivity(intent)
        }
        requests.setOnClickListener {
            val intent = Intent(requireContext(), RequestsActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}