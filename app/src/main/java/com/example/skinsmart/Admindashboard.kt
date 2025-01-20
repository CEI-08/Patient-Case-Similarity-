package com.example.skinsmart

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skinsmart.Admin.AdminHospital
import com.example.skinsmart.Admin.AdminUser
import com.example.skinsmart.databinding.ActivityAdmindashboardBinding

class Admindashboard : AppCompatActivity() {
    private val b by lazy {
        ActivityAdmindashboardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          setContentView(b.root)


        b.imageView2.setOnClickListener { logout() }
        b.btnadminuser.setOnClickListener {startActivity(Intent(this,AdminUser::class.java))  }
        b.btnadminhospital.setOnClickListener { startActivity(Intent(this,AdminHospital::class.java)) }

    }
}