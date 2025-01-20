package com.example.skinsmart

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grihagully.model.RetrofitClient
import com.example.skinsmart.databinding.ActivityRegisterBinding
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Register : AppCompatActivity() {
    private val b by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.etcity.setFilters(arrayOf<InputFilter>(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0 && dstart == 0) {
                val v = CharArray(source.length)
                TextUtils.getChars(source, 0, source.length, v, 0)
                v[0] = v[0].uppercaseChar()
                return@InputFilter String(v)
            }
            null
        }
        ))


        b.btnsignup.setOnClickListener {
            val name=b.etname.text.toString().trim()
            val num=b.etnum.text.toString().trim()
            val email=b.etemail.text.toString().trim()
            val address=b.etaddress.text.toString().trim()
            val city=b.etcity.text.toString().trim()
            val password=b.etpassword.text.toString().trim()


            if(name.isEmpty()){
                b.etname.error="Enter Your Name"
            }else if(num.isEmpty()){
                b.etnum.error="Enter Your Number"
            }else if(email.isEmpty()){
                b.etemail.error="Enter Your Email"
            }else if(address.isEmpty()){
                b.etaddress.error="Enter Your Address"
            }else if(city.isEmpty()){
                b.etcity.error="Enter Your city"
            }else if(password.isEmpty()){
                b.etpassword.error="Enter Your Password"
            }else if(num.count()!=10){
                b.etnum.error="Enter Your Number properly"
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.register(name,num,email,address,city,password,"User","","","","register")
                        .enqueue(object: Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                Toast.makeText(this@Register, ""+t.message, Toast.LENGTH_SHORT).show()
                            }
                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                Toast.makeText(this@Register, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                b.etname.text.clear()
                                b.etnum.text.clear()
                                b.etemail.text.clear()
                                b.etaddress.text.clear()
                                b.etcity.text.clear()
                                b.etpassword.text.clear()
                                startActivity(Intent(this@Register,Login::class.java))
                                finish()
                            }
                        })
                }


            }
        }

    }
}