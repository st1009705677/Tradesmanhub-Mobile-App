package com.example.tradesmanhub.serviceproviders

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tradesmanhub.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var tvRedirectRegister: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    lateinit var btnLogin: Button

    // Creating firebaseAuth object
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // View Binding
        tvRedirectRegister = findViewById(R.id.tvRedirectRegister)
        btnLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.etEmailAddress)
        etPassword = findViewById(R.id.etPassword)

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            login()
        }

        tvRedirectRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            // using finish() to end the activity
            finish()
        }
    }

    private fun login() {
        val email = etEmail.text.toString()
        val pass = etPassword.text.toString()
        // calling signInWithEmailAndPassword(email, pass)
        // function using Firebase auth object
        // On successful response Display a Toast
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }

}