package com.example.tradesmanhub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tradesmanhub.R
import com.example.tradesmanhub.Utils.FirebaseUtil
import com.example.tradesmanhub.adapters.SearchAdapter
import com.example.tradesmanhub.data.Provider
import com.example.tradesmanhub.serviceproviders.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class SearchFragment : Fragment() {

    // Declare view components
    private lateinit var searchBar: Toolbar
    private lateinit var searchView: SearchView

    // Declare adapter components
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    // Declare provider object lists
    private lateinit var spList: ArrayList<Provider>
    private lateinit var originalServiceProviderList: List<Provider>
    private lateinit var filteredServiceProviderList: List<Provider>

    // Declare firebase objects
    private lateinit var firebase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUtil: FirebaseUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        // Initalize search bar
        searchBar = view.findViewById(R.id.searchToolbar)
        (activity as AppCompatActivity).setSupportActionBar(searchBar)
        setHasOptionsMenu(true) // Enable the options menu for this fragment

        initializeDeclaredVariables(view)
        getServiceProviders()

        // On item click listener
        adapter.providerClicked = { provider ->
            // Pass the service provider clicked through intent
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra("provider", provider)
            startActivity(intent)
        }


        return view
    }

    private fun initializeDeclaredVariables(view: View) {
        // Initialize declared variables
        firebase = Firebase.database
        auth = FirebaseAuth.getInstance()
        firebaseUtil = FirebaseUtil(firebase)
        recyclerView = view.findViewById(R.id.rvSearchResults)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        spList = ArrayList()
        originalServiceProviderList = ArrayList()
        filteredServiceProviderList = ArrayList()
        adapter = SearchAdapter(filteredServiceProviderList as ArrayList<Provider>)
        recyclerView.adapter = adapter
    }

    private fun getServiceProviders() {
        firebaseUtil.addValueEventListener("services"){snapshot ->
            (originalServiceProviderList as ArrayList).clear()
            snapshot.children.forEach {service ->
                service.children.forEach { data ->
                    val serviceProvider = Provider(
                        data.key ?: "",
                        data.child("name").getValue(String::class.java) ?: "",
                        data.child("service_type").getValue(String::class.java) ?: "",
                        data.child("description").getValue(String::class.java) ?: "",
                        data.child("averageRating").getValue(Long::class.java) ?: 0,
                        data.child("address").getValue(String::class.java) ?: ""
                    )
                    spList.add(serviceProvider)
                    originalServiceProviderList = originalServiceProviderList + serviceProvider
                }
            }
            // Initially, set the filtered list to be the same as the original list
            filteredServiceProviderList = originalServiceProviderList

            // Update the RecyclerView with the original list
            adapter.setServiceProviders(originalServiceProviderList)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        // Initialize and configure the SearchView
        val searchItem = menu.findItem(R.id.search_item)
        searchView = searchItem.actionView as SearchView
        searchView.clearFocus()

        // Set up the query text listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the query submission (if needed)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // Handle query text changes
                if (!query.isNullOrBlank()) {
                    // Call a search function with the query
                    filterServiceProviders(query)
                } else {
                    // If the query is empty, show all service providers
//                    spAdapter.setServiceProviders(originalServiceProviderList)
                }
                return true
            }
        })
        // Customize the SearchView as needed

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun filterServiceProviders(query: String) {
        val filteredList = originalServiceProviderList.filter { serviceProvider ->
            val nameContainsQuery = serviceProvider.name.contains(query, ignoreCase = true)
            val serviceTypeContainsQuery = serviceProvider.service_type.contains(query, ignoreCase = true)

            // Filter based on whether 'query' is contained in the name or service type
            nameContainsQuery || serviceTypeContainsQuery
        }

        // Update the RecyclerView with the filtered list
        adapter.setServiceProviders(filteredList)
    }

}