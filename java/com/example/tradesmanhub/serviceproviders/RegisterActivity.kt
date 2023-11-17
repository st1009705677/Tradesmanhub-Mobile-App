package com.example.tradesmanhub.serviceproviders

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tradesmanhub.LoginActivity
import com.example.tradesmanhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
            private lateinit var etEmail: EditText
            private lateinit var etConfirmPassword: EditText
            private lateinit var etPassword: EditText
            private lateinit var btnRegister: Button
            private lateinit var tvRedirectLogin: TextView

            // create Firebase authentication object
            private lateinit var auth: FirebaseAuth


            @SuppressLint("MissingInflatedId", "MissingSuperCall")
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_register)

                // View Bindings
                etEmail = findViewById(R.id.etSEmailAddress)
                etConfirmPassword = findViewById(R.id.etSConfPassword)
                etPassword = findViewById(R.id.etSPassword)
                btnRegister = findViewById(R.id.btnLogin)
                tvRedirectLogin = findViewById(R.id.tvRedirectLogin)

                // Initialising auth object
                auth = Firebase.auth

                btnRegister.setOnClickListener {
                    registerUser()
                }

                // switching from signUp Activity to Login Activity
                tvRedirectLogin.setOnClickListener {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

            }

            private fun registerUser() {
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                // check pass
                if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
                    Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
                    return
                }

                if (pass != confirmPassword) {
                    Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                // If all credential are correct
                // We call createUserWithEmailAndPassword
                // using auth object and pass the
                // email and pass in it.
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

