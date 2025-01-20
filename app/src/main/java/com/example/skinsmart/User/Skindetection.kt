package com.example.skinsmart.User

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skinsmart.R
import com.example.skinsmart.databinding.ActivitySkindetectionBinding
import com.example.skinsmart.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.text.DecimalFormat

class Skindetection : AppCompatActivity() {
    private val b by lazy {
        ActivitySkindetectionBinding.inflate(layoutInflater)
    }
    private val model by lazy {
        Model.newInstance(this)

    }

    private val activity =   registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
        it.data?.data?.let { uri ->
            contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }?.let {
                b.imageview.setImageBitmap(it)
                functions(it)
            }
        }
    }

    private val camera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            activityResult.data?.extras?.let { it1 ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it1.getParcelable("data", Bitmap::class.java)
                } else {
                    it1.getParcelable("data")
                }?.let {
                    b.imageview.setImageBitmap(it)
                    functions(it)

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        val image = BitmapFactory.decodeResource(
            resources, R.drawable.sk
        )

        functions(image)

        b.listdoctors.setOnClickListener {
                   startActivity(Intent(this,Viewdoctors::class.java))
        }

        b.imgcamera.setOnClickListener {
            camera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
        b.imggallery.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                activity.launch(this)
            }
        }



    }

    @SuppressLint("SetTextI18n")
    private fun functions(bitmap: Bitmap) {
        val real = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val buffer = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        val image = TensorImage(DataType.UINT8)
        image.load(real)
        buffer.loadBuffer(image.buffer)
        val out = model.process(buffer)
        val array = arrayOf(
            "Melanoma",
            "Atopic Dermatitis",
            "Basal Cell Carcinoma",
            "Benign Keratosis",
            "Melanocytic Nevi",
            "Not Defined"
        )
        var num = 0.0f
        var string = ""
        var number = 0f
        val k = out.outputFeature0AsTensorBuffer.floatArray

        k.forEachIndexed { index, fl ->

            if (num < fl) {
                num = fl
                string = array[index]
            }
            number += fl

        }
        val decimal = DecimalFormat("##.###")
        b.tvname.text = "$string : ${decimal.format((num / number) * 100)}%"
        readinfo(string)


    }

    private fun readinfo(name: String) {
        when(name){
            "Melanoma"->{
                b.tvinfo.text="Melanoma is a serious form of skin cancer that begins in melanocytes. The primary treatment is surgical excision to remove the tumor and some surrounding tissue. Depending on the stage, additional treatments may include lymph node dissection, immunotherapy, targeted therapy, chemotherapy, and radiation therapy. Early detection and treatment are crucial for improving survival rates."
            }
            "Atopic Dermatitis"->{
                b.tvinfo.text="Atopic dermatitis, also known as eczema, is a chronic inflammatory skin condition. Treatment focuses on managing symptoms and may include moisturizing the skin regularly, using topical corticosteroids or calcineurin inhibitors to reduce inflammation, and antihistamines to relieve itching. Severe cases might require systemic treatments like oral corticosteroids, immunosuppressants, or biologic therapies."
            }
            "Basal Cell Carcinoma"->{
                b.tvinfo.text="Basal cell carcinoma (BCC) is the most common type of skin cancer. Treatment typically involves surgical excision, Mohs micrographic surgery, or curettage and electrodessication to remove the cancerous cells. In some cases, topical treatments like imiquimod or 5-fluorouracil may be used. Advanced BCC might require targeted therapy or radiation therapy."
            }
            "Benign Keratosis"->{
                b.tvinfo.text="Benign keratosis includes non-cancerous skin growths such as seborrheic keratosis. Treatment is often not necessary unless the lesions cause discomfort or cosmetic concerns. Options include cryotherapy (freezing), curettage, laser therapy, or electrosurgery to remove the growths. Regular monitoring is recommended to distinguish benign lesions from potential malignancies."
            }
            "Melanocytic Nevi"->{
                b.tvinfo.text="Melanocytic nevi, commonly known as moles, are usually harmless. Regular monitoring for changes in size, shape, or color is essential to detect potential malignant transformation. If a nevus appears suspicious or is cosmetically undesirable, it can be removed via surgical excision, shave removal, or laser therapy. Routine skin checks are vital for early detection of melanoma."
            }
            "Not Defined"->{
                b.tvinfo.text="Disease can't able to find, meet the skin doctors"
            }
        }

    }
}