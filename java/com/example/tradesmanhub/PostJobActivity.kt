package com.example.tradesmanhub

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tradesmanhub.Utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class PostJobActivity : AppCompatActivity() {

    // Initialize field variables
    private lateinit var toolbar: Toolbar
    private lateinit var tvService: Spinner
    private lateinit var tvDate: AutoCompleteTextView
    private lateinit var etBudget: EditText
    private lateinit var etDescription: EditText
    private lateinit var etStreet: EditText
    private lateinit var etCity: EditText
    private lateinit var etPostal: EditText
    private lateinit var tvProvince: Spinner
    private lateinit var postBtn: Button
    private lateinit var progressBar: ProgressBar

    // Declare firebase objectt
    private lateinit var firebase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUtil: FirebaseUtil

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)
        toolbar = findViewById(R.id.jobpostToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable the back button

        // Assign fields to variables
        tvService = findViewById(R.id.spinnerServices)
        tvDate = findViewById(R.id.autoCompleteDate)
        etBudget = findViewById(R.id.editTextTextBudget)
        etStreet = findViewById(R.id.editTextTextStreet)
        etCity = findViewById(R.id.editTextTextCity)
        tvProvince = findViewById(R.id.spinnerProvince)
        etPostal = findViewById(R.id.editTextZip)
        etDescription = findViewById(R.id.editTextDescription)
        postBtn = findViewById(R.id.btnPost)
        progressBar = findViewById(R.id.progressBar)

        // Initialize firebase objects
        firebase = Firebase.database
        auth = FirebaseAuth.getInstance()
        firebaseUtil = FirebaseUtil(firebase)

        // Call initialization functions
        initializeCategoriesDropdown()
        initializeDatePickerListener()
        initializeProvincesDropdown()

        postBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            postJob()
        }

    }

    @Suppress("DEPRECATION")
    private fun postJob() {
        val service = tvService.selectedItem.toString()
        val date = tvDate.text.toString()
        val budget = etBudget.text.toString()
        val street = etStreet.text.toString()
        val city = etCity.text.toString()
        val province = tvProvince.selectedItem.toString()
        val postal = etPostal.text.toString()
        val description = etDescription.text.toString()

        if(service.isEmpty() || date.isEmpty() || budget.isEmpty() || street.isEmpty() ||
                city.isEmpty() || province.isEmpty() || postal.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Fields must not be empty.", LENGTH_SHORT).show()
            return
        }

        val address = "$street, $city, $province, $postal"

        val firebase = firebase.reference
        val key = firebase.child("posts").push().key
        val user = auth.currentUser?.uid
        val postMap = mapOf(
            "address" to address,
            "budget" to "R $budget",
            "isAssigned" to false,
            "service" to service,
            "description" to description,
            "client" to user,
            "start_date" to date
        )
        val statusMap = mapOf(key to "pending")

        firebase.child("posts").child(key!!)
            .setValue(postMap)
            .addOnSuccessListener {
                val usersPostsRef = firebase.child("users/$user/posts")
                usersPostsRef.updateChildren(statusMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Post added successfully", LENGTH_LONG).show()
                        Handler().postDelayed({
                            progressBar.visibility = View.GONE
                            finish()
                        }, 3000)
                    }
                    .addOnFailureListener { updateError ->
                        Log.d("Database Error", "${updateError.message}")
                        Toast.makeText(this, "Failed to update user's posts, try again later", LENGTH_SHORT).show()
                        firebase.child("posts").child(key)
                            .removeValue()
                    }
            }
            .addOnFailureListener { postError ->
                Log.d("Database Error", "${postError.message}")
                Toast.makeText(this, "Failed to save post, try again later", LENGTH_SHORT).show()
            }
    }

    private fun initializeDatePickerListener() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        tvDate.setOnClickListener {
            DatePickerDialog(this,
                getSelectedDate(tvDate),
                year,
                month,
                day).show()
        }
    }

    private fun getSelectedDate(autocompleteField: AutoCompleteTextView): DatePickerDialog.OnDateSetListener {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Update the text in the AutoCompleteTextView with the selected date
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                autocompleteField.setText(selectedDate)
            }
        return dateSetListener
    }

    @SuppressLint("PrivateResource")
    private fun initializeCategoriesDropdown() {
        // Initialize custom firebase util
        firebaseUtil.getDataList("services/") { services ->
            val servicesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, services)
            servicesAdapter.insert("Select a service", 0)
            tvService.setAdapter(servicesAdapter)
        }

    }

    private fun initializeProvincesDropdown(){
        val provinceSuggestions = listOf("Select a province", "Eastern Cape", "Free State", "Gauteng", "KwaZulu-Natal", "Limpopo", "Mpumalanga",
        "Northern Cape", "North West", "Western Cape")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, provinceSuggestions)
        tvProvince.setAdapter(adapter)
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