package com.example.skinsmart.User

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grihagully.model.RetrofitClient
import com.example.skinsmart.Admin.AdminUser.userAdminAdapter
import com.example.skinsmart.Login
import com.example.skinsmart.R
import com.example.skinsmart.databinding.ActivityViewdoctorsBinding
import com.example.skinsmart.databinding.CardaddslotBinding
import com.example.skinsmart.databinding.CarduseradminBinding
import com.example.skinsmart.databinding.CarduserhospitalBinding
import com.example.skinsmart.model.Userresponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class Viewdoctors : AppCompatActivity() {
    private val b by lazy {
        ActivityViewdoctorsBinding.inflate(layoutInflater)
    }
    var uemail=""
    private lateinit var progressDialog: AlertDialog
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

        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            b.etcity.setText(getString("city","").toString())
            uemail=getString("email","").toString()}


        readcity(b.etcity.text.toString(),uemail)

    }




    private fun readcity(city: String,uemail:String) {
        if(city.isEmpty()){
            b.etcity.error="Enter City to Search"
        }else{
            val builder = AlertDialog.Builder(this,R.style.TransparentDialog)
            val inflater = this.layoutInflater
            builder.setView(inflater.inflate(R.layout.progressdialog, null))
            builder.setCancelable(false)

            progressDialog = builder.create()
            progressDialog.show()

            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.viewhospital(city,"viewhospital")
                    .enqueue(object : Callback<Userresponse> {
                        override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {


                            b.listuser.adapter=hospitaluserAdapter(this@Viewdoctors,response.body()!!.user,uemail)
                            b.listuser.layoutManager=LinearLayoutManager(this@Viewdoctors)
                            progressDialog.dismiss()
                        }

                        override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                            Toast.makeText(this@Viewdoctors, "${t.message}", Toast.LENGTH_SHORT).show()

                        }

                    })
            }

        }
    }

    class hospitaluserAdapter(var context: Context, var listdata: ArrayList<User>,var uemail:String):
        RecyclerView.Adapter<hospitaluserAdapter.DataViewHolder>(){
          private val bind by lazy {
                     CardaddslotBinding.inflate(LayoutInflater.from(context))
          }
        inner class DataViewHolder(val view: CarduserhospitalBinding) : RecyclerView.ViewHolder(view.root)



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CarduserhospitalBinding.inflate(
                    LayoutInflater.from(context),parent,
                    false))
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            with(holder.view){

                listdata[position].apply {
                    tvfname.text=name
                    tvfemail.text=email
                    tvfnum.text=num
                    tvfcity.text=city
                    tvtimimg.text=timing
                    tvdescri.text=descrip

                    bind.etdate.setOnClickListener {
                        val c: Calendar = Calendar.getInstance()
                        DatePickerDialog(context, { view, year, month, dayOfMonth ->
                            val dt = "$dayOfMonth-${month + 1}-$year"
                            bind.etdate.setText(dt)
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).apply {
                            this.datePicker.minDate=System.currentTimeMillis()-1000
                            show()
                        }

                    }

                    val k=arrayOf("choose your choice","Morning Slot","Evening Slot")

                    ArrayAdapter(context,
                        android.R.layout.simple_list_item_checked, k).apply {
                        bind.spin.adapter=this
                    }

                    btnslots.setOnClickListener {
                        BottomSheetDialog(context).apply {
                            setContentView(bind.root)
                            bind.btnaddslot.setOnClickListener {
                              val date=bind.etdate.text.toString().trim()
                              val udescrip=bind.etdescription.text.toString().trim()

                                if (date.isEmpty()){
                                    bind.etdate.error="Choose the proper date"
                                }else if(udescrip.isEmpty()){
                                    bind.etdescription.error="Enter your Description"
                                }else{
                                    CoroutineScope(Dispatchers.IO).launch {
                                        RetrofitClient.instance.addrequest(uemail,email,name,num,udescrip,bind.spin.selectedItem.toString(),"","",date,"Pending","pending","","",address,"addrequest")
                                            .enqueue(object: Callback<DefaultResponse> {
                                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                                    Toast.makeText(context, ""+t.message, Toast.LENGTH_SHORT).show()
                                                }
                                                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                                    Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                                    dismiss()
                                                    (context as Activity).finish()
                                                    context.startActivity(Intent(context,Userhistory::class.java))
                                                }
                                            })
                                    }
                                }

                            }

                            show()
                        }

                    }
                    btnfeedback.setOnClickListener {
                        context.startActivity(Intent(context,Viewdocfeedback::class.java).apply {
                            putExtra("email",email)
                        })

                    }
                    btncall.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$num")
                        }
                        context.startActivity(intent)
                    }




                }

            }


        }

        override fun getItemCount() = listdata.size
    }
}