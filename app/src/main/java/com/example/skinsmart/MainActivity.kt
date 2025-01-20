package com.example.skinsmart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.skinsmart.Hospital.HosptialDashbroad

import com.example.skinsmart.User.Userdashboard
import com.example.skinsmart.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private val b by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)



        val type=getSharedPreferences("user", MODE_PRIVATE).getString("type", "")!!

        when(type){
            "admin"-> {
                startActivity(Intent(this, Admindashboard::class.java))
                finish()
            }
            "User"->{
                startActivity(Intent(this,Userdashboard::class.java))
                finish()
            }
            "Hospital"->{
                startActivity(Intent(this, HosptialDashbroad::class.java))
                finish()
            }


        }


        b.btnmain.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()

        }



    }
}