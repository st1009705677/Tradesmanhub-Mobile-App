package com.example.tradesmanhub
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {
    // on below line we are
    // creating variables for listview
    private lateinit var jobsLV: ListView

    // creating array adapter for listview
    lateinit var listAdapter: ArrayAdapter<String>

    // creating array list for listview
    lateinit var jobsList: ArrayList<String>

    // creating variable for search-view
    private lateinit var searchView: SearchView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_search
        )

        // initializing variables of list view with their ids.
       jobsLV = findViewById(R.id.idLVjobs)
        searchView = findViewById(R.id.idSV)

        // initializing list and adding data to list
        jobsList = ArrayList()
        jobsList .add("Cleaning, Cleaners")
        jobsList .add("Car Wash, Car Repair,")
        jobsList .add("Landscape")
        jobsList .add("Local Electrician, Electrician, ")
        jobsList .add("Plumber")
        jobsList .add("Storage Mover, Storage")
        jobsList .add("Rubbish Collector")
        jobsList .add("Waste Collector, Waste Management, Rubbish Collector")

        // initializing list adapter and setting layout
        // for each list view item and adding array list to it.
        listAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            jobsList
        )

        // on below line setting list
        // adapter to our list view.
        jobsLV.adapter = listAdapter

        // on below line we are adding on query
        // listener for our search view.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // on below line we are checking
                // if query exist or not.
                // if query is not present we are displaying
                // a toast message as no  data found..
                if (jobsList.contains(query)) {
                    // if query exist within list we
                    // are filtering our list adapter.
                    listAdapter.filter.filter(query)
                } else Toast.makeText(this@SearchActivity, "service provider found..", Toast.LENGTH_LONG)
                    .show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                listAdapter.filter.filter(newText)
                return false
            }
        })
    }
}