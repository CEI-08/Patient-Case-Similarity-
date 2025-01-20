package com.example.skinsmart.Admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.skinsmart.User.Viewdocfeedback
import com.example.skinsmart.databinding.ActivityAdminHospitalBinding
import com.example.skinsmart.databinding.CardaddhospitalBinding
import com.example.skinsmart.databinding.CarduseradminBinding
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

class AdminHospital : AppCompatActivity() {
    private val b by lazy {
        ActivityAdminHospitalBinding.inflate(layoutInflater)
    }
    private val bind by lazy{
        CardaddhospitalBinding.inflate(layoutInflater)
    }

    private lateinit var progressDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)
        readhospital()



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

        b.imageView3.setOnClickListener {
            BottomSheetDialog(this@AdminHospital,R.style.TransparentDialog).apply {
                setContentView(bind.root)

                bind.btnaddhospital.setOnClickListener {
                    val name=bind.etname.text.toString().trim()
                    val num=bind.etnum.text.toString().trim()
                    val email=bind.etemail.text.toString().trim()
                    val address=bind.etaddress.text.toString().trim()
                    val city=bind.etcity.text.toString().trim()
                    val password=bind.etpassword.text.toString().trim()
                    val timing=bind.ettiming.text.toString().trim()
                    val descrip=bind.etdescri.text.toString().trim()

                    if(name.isEmpty()){
                        bind.etname.error="Enter Hospital Name"
                    }else if(num.isEmpty()){
                        bind.etnum.error="Enter Hospital Number"
                    }else if(email.isEmpty()){
                        bind.etemail.error="Enter Hospital Email"
                    }else if(address.isEmpty()){
                        bind.etaddress.error="Enter Hospital Address"
                    }else if(city.isEmpty()){
                        bind.etcity.error="Enter Hospital city"
                    }else if(password.isEmpty()){
                        bind.etpassword.error="Enter Hospital Password"
                    }else if(timing.isEmpty()){
                        bind.ettiming.error="Enter Hospital timings"
                    }else if(descrip.isEmpty()){
                        bind.etnum.error="Enter Hospital Description"
                    } else if(num.count()!=10){
                        bind.etnum.error="Enter Hospital Number properly"
                    }else{
                        CoroutineScope(Dispatchers.IO).launch {
                            RetrofitClient.instance.register(name,num,email,address,city,password,"Hospital","Available",descrip,timing,"register")
                                .enqueue(object: Callback<DefaultResponse> {
                                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                        Toast.makeText(this@AdminHospital, ""+t.message, Toast.LENGTH_SHORT).show()
                                    }
                                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                        Toast.makeText(this@AdminHospital, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                        bind.etname.text.clear()
                                        bind.etnum.text.clear()
                                        bind.etemail.text.clear()
                                        bind.etaddress.text.clear()
                                        bind.etcity.text.clear()
                                        bind.etpassword.text.clear()
                                        bind.ettiming.text.clear()
                                        bind.etdescri.text.clear()
                                        dismiss()
                                        readhospital()


                                    }
                                })
                        }
                    }
                }

                show()
            }

        }

    }

    private fun readhospital() {
        val builder = AlertDialog.Builder(this,R.style.TransparentDialog)
        val inflater = this.layoutInflater
        builder.setView(inflater.inflate(R.layout.progressdialog, null))
        builder.setCancelable(false)
        progressDialog = builder.create()
        progressDialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.adminhospital()
                .enqueue(object : Callback<Userresponse> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {

                        b.listhospital.let {
                            response.body()?.user?.let {
                                    it1 ->
                                it.adapter=hospitalAdminAdapter(this@AdminHospital,response.body()!!.user)
                                it.layoutManager= LinearLayoutManager(this@AdminHospital)
                                Toast.makeText(this@AdminHospital, "success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        progressDialog.dismiss()
                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@AdminHospital, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }


    class hospitalAdminAdapter(var context: Context, var listdata: ArrayList<User>):
        RecyclerView.Adapter<hospitalAdminAdapter.DataViewHolder>(){

        inner class DataViewHolder(val view: CarduseradminBinding) : RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            return DataViewHolder(
                CarduseradminBinding.inflate(
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
                    tvstatus.text=status
                    tvtimimg.text=timing
                    tvdescri.text=descrip


                    btnfeedback.setOnClickListener {
                        context.startActivity(Intent(context, Viewdocfeedback::class.java).apply {
                            putExtra("email",email)
                        })
                    }

                    btndelete.setOnClickListener {
                        val alertdialog= AlertDialog.Builder(context)
                        alertdialog.setTitle("Delete")
                        alertdialog.setIcon(R.drawable.logo)
                        alertdialog.setCancelable(false)
                        alertdialog.setMessage("Do you Deleted the Profile?")
                        alertdialog.setPositiveButton("Yes"){ alertdialog, which->

                            deletehospital(id)
                            alertdialog.dismiss()
                        }

                        alertdialog.show()

                    }

                }

            }




        }

        private fun deletehospital(id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.Deleteperson(id,"deletetable")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, ""+t.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(context, "${response.body()!!.message }", Toast.LENGTH_SHORT).show()


                        }
                    })
            }

        }


        override fun getItemCount() = listdata.size
    }
}