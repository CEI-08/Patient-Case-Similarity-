package com.example.skinsmart

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


fun Activity.logout() {
    val alertdialog= AlertDialog.Builder(this)
    alertdialog.setTitle("LOGOUT")
    alertdialog.setIcon(R.drawable.logo)
    alertdialog.setCancelable(false)
    alertdialog.setMessage("Do you Want to Logout?")
    alertdialog.setPositiveButton("Yes"){ alertdialog, which->
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        val  shared=getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        shared.edit().clear().apply()
        alertdialog.dismiss()
    }
    alertdialog.setNegativeButton("No"){alertdialog,which->
        Toast.makeText(this,"thank you", Toast.LENGTH_SHORT).show()
        alertdialog.dismiss()
    }
    alertdialog.show()
}