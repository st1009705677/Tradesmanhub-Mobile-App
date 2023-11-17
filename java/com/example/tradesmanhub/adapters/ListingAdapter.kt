package com.example.tradesmanhub.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradesmanhub.R
import com.example.tradesmanhub.Utils.FirebaseUtil
import com.example.tradesmanhub.data.JobPost
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import org.w3c.dom.Text

class ListingAdapter (private var jobsList: ArrayList<JobPost>)
    : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>()  {

    private lateinit var firebase: FirebaseDatabase
    private lateinit var firebaseUtil: FirebaseUtil
    var viewJob: ((JobPost) -> Unit)? = null

    class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val client = itemView.findViewById<TextView>(R.id.listing_client)
        val service = itemView.findViewById<TextView>(R.id.listing_service)
        val start_date = itemView.findViewById<TextView>(R.id.listing_commence)
        val address = itemView.findViewById<TextView>(R.id.listing_address)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listing_item_result, parent, false)
        return ListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListingAdapter.ListingViewHolder, position: Int) {
        val post = jobsList[position]
        getUserName(post.client) {name ->
            holder.client.text = name
            holder.service.text = post.service
            holder.start_date.text = post.start_date
            holder.address.text = post.address
        }

        // Invoke on item click
        holder.itemView.setOnClickListener {
            viewJob?.invoke(post)
        }
    }

    private fun getUserName(uid: String, callback: (String) -> Unit) {
        firebase = Firebase.database
        firebaseUtil = FirebaseUtil(firebase)
        firebaseUtil.addSingleValueEventListener("users/$uid"){snapshot ->
            val name = snapshot.child("displayName").getValue(String::class.java) ?: "Unknown"
            callback(name)
        }
    }

    override fun getItemCount(): Int {
        return jobsList.size
    }
}