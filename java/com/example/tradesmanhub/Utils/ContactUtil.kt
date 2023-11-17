package com.example.tradesmanhub.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class ContactUtil(val context: Context) {
    @SuppressLint("QueryPermissionsNeeded")
    fun openEmailIntent(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")

        // Specify the recipient email address
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

        // Specify the subject (optional)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "A job you accepted")

        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(emailIntent)
        } else {
            Toast.makeText(context, "No email app installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun openPhoneDialer(phoneNumber: String) {

        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")

        if (dialIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(dialIntent)
        } else {
            Toast.makeText(context, "No dialer app installed", Toast.LENGTH_SHORT).show()
        }
    }

}