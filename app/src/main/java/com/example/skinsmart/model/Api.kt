package com.example.skinsmart.model
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse
import com.ymts0579.model.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("users.php")
    fun register(
        @Field("name")name:String,
        @Field("num")num:String,
        @Field("email")email:String,
        @Field("address")address:String,
        @Field("city")city:String,
        @Field("pass")pass:String,
        @Field("type")type:String,
        @Field("status")status:String,
        @Field("descrip")descrip:String,
        @Field("timing")timing:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun login(@Field("email") email:String, @Field("pass") pass:String,
              @Field("condition") condition:String): Call<LoginResponse>


    @FormUrlEncoded
    @POST("users.php")
    fun updateprofile(
        @Field("id") id:Int,
        @Field("name")name:String,
        @Field("num")num:String,
        @Field("email")email:String,
        @Field("address")address:String,
        @Field("city")city:String,
        @Field("pass")pass:String,
        @Field("type")type:String,
        @Field("status")status:String,
        @Field("descrip")descrip:String,
        @Field("timing")timing:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>



    @GET("adminuser.php")
    fun adminuser():Call<Userresponse>


    @GET("adminhospital.php")
    fun adminhospital():Call<Userresponse>


    @FormUrlEncoded
    @POST("users.php")
    fun Deleteperson(
        @Field("id")id:Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun viewhospital(
        @Field("city")city:String,
        @Field("condition") condition:String,
    ):Call<Userresponse>

    @FormUrlEncoded
    @POST("request.php")
    fun addrequest(
        @Field("uemail")uemail:String,
        @Field("demail")demail:String,
        @Field("dname")dname:String,
        @Field("dnum")dnum:String,
        @Field("udescrip")udescrip:String,
        @Field("slot")slot:String,
        @Field("cost")cost:String,
        @Field("ddescrip")ddescrip:String,
        @Field("date")date:String,
        @Field("status")status:String,
        @Field("ustatus")ustatus:String,
        @Field("rating")rating:String,
        @Field("feedback")feedback:String,
        @Field("daddress")daddress:String,
        @Field("condition") condition:String,
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("request.php")
    fun getuserhistory(
        @Field("uemail")uemail:String,
        @Field("condition") condition:String,
    ):Call<requestresponse>


    @FormUrlEncoded
    @POST("request.php")
    fun getdoctororders(
        @Field("demail")demail:String,
        @Field("condition") condition:String,
    ):Call<requestresponse>


    @FormUrlEncoded
    @POST("request.php")
    fun updatestatusdoc(
        @Field("status")status:String,
        @Field("cost")cost:String,
        @Field("ddescrip")ddescrip:String,
        @Field("id") id:Int,
        @Field("condition") condition:String,
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("request.php")
    fun updatestatusdocreject(
        @Field("status")status:String,
        @Field("ustatus")ustatus:String,
        @Field("id") id:Int,
        @Field("condition") condition:String,
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("request.php")
    fun updatestatuser(
        @Field("ustatus")ustatus:String,
        @Field("id") id:Int,
        @Field("condition") condition:String,
    ):Call<DefaultResponse>


    @FormUrlEncoded
    @POST("request.php")
    fun Feedbackadd(
        @Field("rating")rating:String,
        @Field("feedback")feedback:String,
        @Field("id") id:Int,
        @Field("condition") condition:String,
    ):Call<DefaultResponse>

}