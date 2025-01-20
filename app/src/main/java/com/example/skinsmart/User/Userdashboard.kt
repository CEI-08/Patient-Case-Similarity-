package com.example.skinsmart.User

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grihagully.model.RetrofitClient
import com.example.skinsmart.R
import com.example.skinsmart.databinding.ActivityUserdashboardBinding
import com.example.skinsmart.databinding.CardprofileBinding
import com.example.skinsmart.databinding.CarduserprofileBinding
import com.example.skinsmart.logout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Userdashboard : AppCompatActivity() {
    private val b by lazy {
        ActivityUserdashboardBinding.inflate(layoutInflater)
    }
    private val bind by lazy {
        CarduserprofileBinding.inflate(layoutInflater)
    }
   var id=0
   var name=""
   var num=""
   var email=""
   var address=""
   var city=""
   var pass=""
   var type=""
   var status=""
   var descrip=""
   var timing=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)


        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            id=getInt("id",0)
            name=getString("name","").toString()
            num=getString("num","").toString()
            email=getString("email","").toString()
            address=getString("address","").toString()
            city=getString("city","").toString()
            pass=getString("pass","").toString()
            type=getString("type","").toString()
            status=getString("status","").toString()
            descrip=getString("descrip","").toString()
            timing=getString("timing","").toString()



        }

        bind.etcity.setFilters(arrayOf<InputFilter>(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.length > 0 && dstart == 0) {
                val v = CharArray(source.length)
                TextUtils.getChars(source, 0, source.length, v, 0)
                v[0] = v[0].uppercaseChar()
                return@InputFilter String(v)
            }
            null
        }
        ))

        bind.etname.setText(name)
        bind.etnum.setText(num)
        bind.etemail.setText(email)
        bind.etaddress.setText(address)
        bind.etcity.setText(city)
        bind.etpassword.setText(pass)


        b.tvname.text="WELCOME $name"
        b.carduserlogout.setOnClickListener { logout() }
        b.linearskincare.setOnClickListener { startActivity(Intent(this,Skindetection::class.java)) }
        b.cardhistory.setOnClickListener { startActivity(Intent(this,Userhistory::class.java)) }



        b.carduserprofile.setOnClickListener {
            BottomSheetDialog(this).apply {
                setContentView(bind.root)

                bind.btnupdateprofile.setOnClickListener {
                    val name1=bind.etname.text.toString().trim()
                    val num1=bind.etnum.text.toString().trim()
                    val email1=bind.etemail.text.toString().trim()
                    val address1=bind.etaddress.text.toString().trim()
                    val city1=bind.etcity.text.toString().trim()
                    val password1=bind.etpassword.text.toString().trim()


                    if(name1.isEmpty()){
                        bind.etname.error="Enter your Name"
                    }else if(num1.isEmpty()){
                        bind.etnum.error="Enter your Number"
                    }else if(email1.isEmpty()){
                        bind.etemail.error="Enter your Email"
                    }else if(address1.isEmpty()){
                        bind.etaddress.error="Enter your Address"
                    }else if(city1.isEmpty()){
                        bind.etcity.error="Enter your city"
                    }else if(password1.isEmpty()){
                        bind.etpassword.error="Enter your Password"
                    } else if(num1.count()!=10){
                        bind.etnum.error="Enter your Number properly"
                    }else{
                        CoroutineScope(Dispatchers.IO).launch {
                            RetrofitClient.instance.updateprofile(id,name1,num1,email1,address1,city1,password1,type,status,descrip,timing,"update")
                                .enqueue(object: Callback<DefaultResponse> {
                                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                        Toast.makeText(this@Userdashboard, ""+t.message, Toast.LENGTH_SHORT).show()
                                    }
                                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                        Toast.makeText(this@Userdashboard, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).edit().apply {
                                            putInt("id",id)
                                            putString("name",name1)
                                            putString("num",num1)
                                            putString("email",email1)
                                            putString("address",address1)
                                            putString("city",city1)
                                            putString("pass",password1)
                                            putString("type",type)
                                            putString("status",status)
                                            putString("descrip",descrip)
                                            putString("timing",timing)
                                            apply()
                                        }
                                        dismiss()
                                    }
                                })
                        }
                    }
                }

                show()
            }
        }

    }
}