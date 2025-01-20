package com.example.skinsmart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grihagully.model.RetrofitClient
import com.example.skinsmart.Hospital.HosptialDashbroad
import com.example.skinsmart.User.Userdashboard
import com.example.skinsmart.databinding.ActivityLoginBinding

import com.ymts0579.model.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private val b by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.linearregister.setOnClickListener {
            startActivity(Intent(this,Register::class.java))
        }

        b.btnsignin.setOnClickListener {
            val email=b.etemail.text.toString().trim()
            val password=b.etpassword.text.toString().trim()


            if(email.isEmpty()){
                b.etemail.error="Enter Your Email"
            }else if(password.isEmpty()){
                b.etpassword.error="Enter Your Password"
            }else if(email.contains("admin")&&password.contains("admin")){
                getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).edit().putString("type","admin").apply()
                startActivity(Intent(this,Admindashboard::class.java))
                finish()
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.login(email,password,"login")
                        .enqueue(object: Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>, response: Response<LoginResponse>
                            ) {
                                if(!response.body()?.error!!){
                                    val typ=response.body()?.user
                                    if (typ!=null) {
                                        typ.apply {
                                            getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).edit().apply {
                                                putInt("id",id)
                                                putString("name",name)
                                                putString("num",num)
                                                putString("email",email)
                                                putString("address",address)
                                                putString("city",city)
                                                putString("pass",pass)
                                                putString("type",type)
                                                putString("status",status)
                                                putString("descrip",descrip)
                                                putString("timing",timing)
                                                apply()
                                            }
                                            when (type) {
                                                "User"->{
                                                    startActivity(Intent(this@Login,Userdashboard::class.java))
                                                    finish()
                                                }
                                                "Hospital"->{
                                                    startActivity(Intent(this@Login, HosptialDashbroad::class.java))
                                                    finish()
                                                }

                                            }
                                        }
                                        Toast.makeText(this@Login, response.body()?.message, Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    Toast.makeText(this@Login, response.body()?.message, Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(this@Login, t.message, Toast.LENGTH_LONG).show()


                            }

                        })
                }

            }
        }

    }
}