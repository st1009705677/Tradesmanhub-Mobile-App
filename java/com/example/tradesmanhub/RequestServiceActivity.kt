package com.example.tradesmanhub

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.tradesmanhub.R
import com.example.tradesmanhub.Utils.FirebaseUtil
import com.example.tradesmanhub.data.Provider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RequestServiceActivity : AppCompatActivity() {


    private lateinit var toolbar: Toolbar
    private lateinit var tvService: EditText
    private lateinit var tvProvider: EditText
    private lateinit var tvDate: AutoCompleteTextView
    private lateinit var etBudget: EditText
    private lateinit var etDescription: EditText
    private lateinit var etStreet: EditText
    private lateinit var etCity: EditText
    private lateinit var etPostal: EditText
    private lateinit var tvProvince: Spinner
    private lateinit var requestBtn: Button
    private lateinit var progressBar: ProgressBar

    // Declare firebase objectt
    private lateinit var firebase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUtil: FirebaseUtil
    private lateinit var  provider: Provider

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_service)
        toolbar = findViewById(R.id.requestJobToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable the back button

        // Assign fields to variables
        firebase = Firebase.database
        auth = FirebaseAuth.getInstance()
        firebaseUtil = FirebaseUtil(firebase)
        provider = getServiceProviderFromIntent(intent)!!
        //
        tvProvider = findViewById(R.id.editTextTextProvider)
        tvService = findViewById(R.id.editTextServices)
        tvDate = findViewById(R.id.autoCompleteDate)
        etBudget = findViewById(R.id.editTextTextBudget)
        etStreet = findViewById(R.id.editTextTextStreet)
        etCity = findViewById(R.id.editTextTextCity)
        tvProvince = findViewById(R.id.spinnerProvince)
        etPostal = findViewById(R.id.editTextZip)
        etDescription = findViewById(R.id.editTextDescription)
        requestBtn = findViewById(R.id.btnRequest)
        progressBar = findViewById(R.id.progressBar)

        initializeDatePickerListener()
        initializeProvincesDropdown()

        // Initialize service provider fields
        tvProvider.text = Editable.Factory.getInstance().newEditable(provider.name)
        tvService.text = Editable.Factory.getInstance().newEditable(provider.service_type)

        requestBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            processRequest(provider)
        }

    }

    @Suppress("DEPRECATION")
    private fun processRequest(provider: Provider) {

        val name = tvProvider.text.toString()
        val service = tvService.text.toString()
        val date = tvDate.text.toString()
        val budget = etBudget.text.toString()
        val street = etStreet.text.toString()
        val city = etCity.text.toString()
        val province = tvProvince.selectedItem.toString()
        val postal = etPostal.text.toString()
        val description = etDescription.text.toString()

        if (name.isEmpty() || service.isEmpty() || date.isEmpty() || budget.isEmpty() || street.isEmpty() ||
            city.isEmpty() || province.isEmpty() || postal.isEmpty() || description.isEmpty()
        ) {
            Toast.makeText(this, "Fields must not be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if initalized fields were changed
        if(name != provider.name || service != provider.service_type){
            Toast.makeText(this, "Unknown service provider or service", Toast.LENGTH_SHORT).show()
            return
        }


        val address = "$street, $city, $province, $postal"

        val firebase = firebase.reference
        val key = firebase.child("requests").push().key
        val user = auth.currentUser?.uid
        val postMap = mapOf(
            "provder" to provider.spID,
            "address" to "R $address",
            "budget" to budget,
            "isAssigned" to false,
            "service" to service,
            "description" to description,
            "client" to user,
            "start_date" to date
        )
        val statusMap = mapOf(key to "pending")

        firebase.child("requests").child(key!!)
            .setValue(postMap)
            .addOnSuccessListener {
                val usersPostsRef = firebase.child("users/$user/requests")
                usersPostsRef.updateChildren(statusMap)
                    .addOnSuccessListener {
                        val providersPostsRef = firebase.child("users/${provider.spID}/jobs")
                        providersPostsRef.updateChildren(statusMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Your request was successfully", Toast.LENGTH_LONG).show()
                                Handler().postDelayed({
                                    progressBar.visibility = View.GONE
                                    finish()
                                }, 3000)
                            }.addOnFailureListener {
                                progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
                                firebase.child("posts").child(key)
                                    .removeValue()
                            }
                    }.addOnFailureListener {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
                        firebase.child("posts").child(key)
                            .removeValue()
                    }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Log.d("Database Error", "${it.message}")
                Toast.makeText(this, "Failed to save request, try again later", Toast.LENGTH_SHORT).show()
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


    @Suppress("DEPRECATION")
    private fun getServiceProviderFromIntent(intent: Intent): Provider? {
        return intent.getParcelableExtra("provider")
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