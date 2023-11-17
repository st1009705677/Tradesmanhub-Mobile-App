package com.example.tradesmanhub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradesmanhub.R
import com.example.tradesmanhub.data.Provider

class SearchAdapter (private var providerList: ArrayList<Provider>)
    : RecyclerView.Adapter<SearchAdapter.ProviderViewHolder>() {

    var providerClicked: ((Provider) -> Unit)? = null

    class ProviderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val picture: ImageView = itemView.findViewById(R.id.ivProfileImg)
        val name: TextView = itemView.findViewById(R.id.tvSPName)
        val rating: TextView = itemView.findViewById(R.id.tvAveRating)
        val service: TextView = itemView.findViewById(R.id.tvSPService)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.ProviderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item_result, parent, false)
        return ProviderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ProviderViewHolder, position: Int) {
        val sp = providerList[position]
        holder.name.text = sp.name
        holder.rating.text = sp.averageRating.toString()
        holder.service.text = sp.service_type

        // Add clickable item listener
        holder.itemView.setOnClickListener {
            providerClicked?.invoke(sp)
        }
    }

    override fun getItemCount(): Int {
        return providerList.size
    }

    fun setServiceProviders(serviceProviders: List<Provider>) {
        providerList.clear()
        providerList.addAll(serviceProviders)
        notifyDataSetChanged()
    }
}