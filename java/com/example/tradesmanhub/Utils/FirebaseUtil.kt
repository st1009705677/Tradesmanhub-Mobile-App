package com.example.tradesmanhub.Utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FirebaseUtil(private val firebase: FirebaseDatabase) {

    fun addValueEventListener(dataPath: String, onDataChange: (DataSnapshot) -> Unit) {
        val databaseReference = firebase.reference.child(dataPath)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Pass the DataSnapshot to the callback function
                onDataChange(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.d("Database Error", error.message)
            }
        }

        // Add the ValueEventListener to the database reference
        databaseReference.addValueEventListener(valueEventListener)
        databaseReference.keepSynced(true)      // Add persistence
    }

    fun addSingleValueEventListener(dataPath: String, onDataLoaded: (DataSnapshot) -> Unit) {
        val databaseReference = firebase.reference.child(dataPath)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onDataLoaded(snapshot)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.d("Database Error", error.message)
            }
        })

        databaseReference.keepSynced(true)
    }

    fun getDataList(dataPath: String, callback: (List<String>) -> Unit) {
        val databaseReference = firebase.reference.child(dataPath)

        databaseReference.get().addOnSuccessListener { snapshot ->
            val dataList = snapshot.children.map { it.key ?: "" }
            callback(dataList)
        }.addOnFailureListener { exception ->
            Log.e("firebase", "Error getting data", exception)
            callback(emptyList()) // Handle the error case
        }

        databaseReference.keepSynced(true)
    }
}