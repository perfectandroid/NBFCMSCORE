package com.perfect.bizcorelite.Api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("StudentParentLogin/StudentLoginCheck")
    fun getLogin(@Body body: RequestBody):Call<String>

    @POST("LoginVarification")
    fun getloginverification(@Body body: RequestBody):Call<String>

    @POST("Customer/CustomerRegistration")
    fun getregistration(@Body body: RequestBody):Call<String>

    @POST("Customer/VarificationMaintenance")
    fun getOTP(@Body body: RequestBody):Call<String>



}

