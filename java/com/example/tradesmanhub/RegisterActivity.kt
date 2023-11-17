package com.example.tradesmanhub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
            private lateinit var etEmail: EditText
            private lateinit var etConfirmPassword: EditText
            private lateinit var etPassword: EditText
            private lateinit var etName: EditText
            private lateinit var btnRegister: Button
            private lateinit var tvRedirectLogin: TextView

            // create Firebase authentication object
            private lateinit var auth: FirebaseAuth
            private lateinit var firebase: FirebaseDatabase


            @SuppressLint("MissingInflatedId", "MissingSuperCall")
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_register)

                // View Bindings
                etEmail = findViewById(R.id.editTextTextEmailAddress)
                etName = findViewById(R.id.editTextTextName)
                etConfirmPassword = findViewById(R.id.editTextTextPassword4)
                etPassword = findViewById(R.id.editTextTextPassword2)
                btnRegister = findViewById(R.id.btnRegister)
                tvRedirectLogin = findViewById(R.id.tvRedirectLogin)

                // Initialising auth object
                auth = Firebase.auth
                firebase = Firebase.database

                btnRegister.setOnClickListener {
                    registerUser()
                }

                // switching from RegisterActivity to LoginActivity
                this.tvRedirectLogin.setOnClickListener {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

            }

            private fun registerUser() {
                val name = etName.text.toString()
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
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Update the user's name
                            val user = auth.currentUser
                            @Suppress("DEPRECATED")
                            val uname = userProfileChangeRequest{
                                displayName = name
                            }
                            user!!.updateProfile(uname)

                            // Store the record in firebase
                            val properties = mapOf(
                                "displayName" to name,
                                "email" to email
                            )
                            firebase.reference.child("users/${user.uid}")
                                    .setValue(properties)
                            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()

                            //Redirect to the Home screen
                            val intent = Intent(this, NavMenu::class.java)
                            startActivity(intent)
                            finish()    // Close the register activity
                    } else {
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

