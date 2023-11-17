package com.example.tradesmanhub.adapters

import android.annotation.SuppressLint
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradesmanhub.R
import com.example.tradesmanhub.data.JobPost
import com.example.tradesmanhub.data.Provider


class PostAdapter (private var postList: ArrayList<JobPost>)
    : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var emailProvider: ((JobPost) -> Unit)? = null
    var callProvider: ((JobPost) -> Unit)? = null
    var viewProvider: ((JobPost) -> Unit)? = null

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val service = itemView.findViewById<TextView>(R.id.post_transaction_id)
        val status = itemView.findViewById<TextView>(R.id.post_transaction_status)
        val date = itemView.findViewById<TextView>(R.id.post_transaction_date)
        val description = itemView.findViewById<TextView>(R.id.post_transaction_description)
        val address = itemView.findViewById<TextView>(R.id.post_transaction_address)
        val layout = itemView.findViewById<RelativeLayout>(R.id.post_isAssigned_rl)
        val provider = itemView.findViewById<TextView>(R.id.post_provider_name)
        val email = itemView.findViewById<ImageView>(R.id.post_provider_email)
        val cell = itemView.findViewById<ImageView>(R.id.post_provider_call)
        val profile = itemView.findViewById<ImageView>(R.id.post_provider_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item_result, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        holder.service.text = post.service
        holder.status.text = post.status
        holder.date.text = post.start_date
        holder.description.text = post.description
        holder.address.text = post.address

        if(post.provider != "" && post.isAssigned){
            holder.layout.visibility = View.VISIBLE
        }

        holder.email.setOnClickListener { emailProvider?.invoke(post) }
        holder.cell.setOnClickListener { callProvider?.invoke(post) }
        holder.profile.setOnClickListener { viewProvider?.invoke(post) }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setJobPosts(jobPosts: ArrayList<JobPost>) {
        postList.clear()
        postList.addAll(jobPosts)
        notifyDataSetChanged()
    }
}

