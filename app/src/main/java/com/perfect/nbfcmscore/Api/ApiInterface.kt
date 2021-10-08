package com.perfect.bizcorelite.Api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("Customer/CustomerRegistration")
    fun getregistration(@Body body: RequestBody):Call<String>

    @POST("Customer/VarificationMaintenance")
    fun getOTP(@Body body: RequestBody):Call<String>

    @POST("Customer/Languages")
    fun getLanguages(@Body body: RequestBody):Call<String>

    @POST("Customer/CustomerLoginVerification")
    fun getCustomerLoginVerification(@Body body: RequestBody):Call<String>

    @POST("Customer/ResellerDetails")
    fun getResellerDetails(@Body body: RequestBody):Call<String>




}

