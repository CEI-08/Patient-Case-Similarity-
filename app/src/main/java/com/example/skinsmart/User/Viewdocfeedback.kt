package com.example.skinsmart.User

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grihagully.model.RetrofitClient
import com.example.skinsmart.R
import com.example.skinsmart.databinding.ActivityViewdocfeedbackBinding
import com.example.skinsmart.model.requestda
import com.example.skinsmart.model.requestresponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Viewdocfeedback : AppCompatActivity() {
    private val b by lazy {
        ActivityViewdocfeedbackBinding.inflate(layoutInflater)
    }
    var uemail=""
    private lateinit var progressDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        uemail=intent.getStringExtra("email").toString()

        val builder = AlertDialog.Builder(this,R.style.TransparentDialog)
        val inflater = this.layoutInflater
        builder.setView(inflater.inflate(R.layout.progressdialog, null))
        builder.setCancelable(false)

        progressDialog = builder.create()
        progressDialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.getdoctororders(uemail,"vieworders")
                .enqueue(object : Callback<requestresponse> {
                    override fun onResponse(call: Call<requestresponse>, response: Response<requestresponse>) {

                        b.main.layoutManager=LinearLayoutManager(this@Viewdocfeedback)
                        b.main.adapter=feedbackadapter(this@Viewdocfeedback,response.body()!!.user)
                        progressDialog.dismiss()
                        Toast.makeText(this@Viewdocfeedback, "$uemail", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<requestresponse>, t: Throwable) {
                        Toast.makeText(this@Viewdocfeedback, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }


    class feedbackadapter(var context: Context, var listdata: ArrayList<requestda>):
        RecyclerView.Adapter<feedbackadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {


            val  ratingbar: RatingBar =view.findViewById(R.id.rating)
            val  tvfeedback: TextView =view.findViewById(R.id.tvfeedback)
            val linearfeed: LinearLayout =view.findViewById(R.id.linearfeed)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardfeed, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            holder.apply {
                listdata.get(position).apply {

                    tvfeedback.text = feedback

                    var floaft = 0.0f
                    rating!!.forEach {
                        if (it != ' ' && it.isDigit() || it == '.') {
                            floaft = it.toFloat()
                        }
                    }
                    ratingbar.isIndeterminate = true
                    ratingbar.rating = floaft

                    if (feedback!!.isEmpty()) {
                        linearfeed.visibility = View.GONE
                    } else {
                        linearfeed.visibility = View.VISIBLE
                    }

                }

            }

        }

        override fun getItemCount() = listdata.size
    }
}