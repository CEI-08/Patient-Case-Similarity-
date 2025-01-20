package com.example.skinsmart.model

import android.net.http.SslCertificate.DName
import com.google.gson.annotations.SerializedName
import com.ymts0579.model.model.User

class requestresponse (val error: Boolean, val message:String, var user:ArrayList<requestda>){
}


data class requestda(
    @SerializedName("requesId"   ) var requesId    : String? = null,
    @SerializedName("statusPoint") var statusPoint : String? = null,
    @SerializedName("usstatus"   ) var usstatus    : String? = null,
    @SerializedName("uemail"     ) var uemail      : String? = null,
    @SerializedName("demail"     ) var demail      : String? = null,
    @SerializedName("dname"      ) var dname       : String? = null,
    @SerializedName("dnum"       ) var dnum        : String? = null,
    @SerializedName("udescrip"   ) var udescrip    : String? = null,
    @SerializedName("slot"       ) var slot        : String? = null,
    @SerializedName("cost"       ) var cost        : String? = null,
    @SerializedName("ddescrip"   ) var ddescrip    : String? = null,
    @SerializedName("date"       ) var date        : String? = null,
    @SerializedName("rating"     ) var rating      : String? = null,
    @SerializedName("feedback"   ) var feedback    : String? = null,
    @SerializedName("daddress"   ) var daddress    : String? = null,
    @SerializedName("name"       ) var name        : String? = null,
    @SerializedName("num"        ) var num         : String? = null,
    )