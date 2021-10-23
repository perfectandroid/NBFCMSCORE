package com.perfect.nbfcmscore.Api

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

    @POST("AccountSummary/CustomerLoanAndDepositDetails")
    fun getCustomerLoanAndDepositDetails(@Body body: RequestBody):Call<String>

    @POST("AccountSummary/AccountModuleWiseDetailsList")
    fun getAccountModuleWiseDetailsList(@Body body: RequestBody):Call<String>

    @POST("AccountSummary/AccountMiniStatement")
    fun getAccountMiniStatement(@Body body: RequestBody):Call<String>

    @POST("AccountSummary/BankBranchDetails")
    fun getBankBranchDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/StandingInstructionDetails")
    fun getStandingInstructionDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/NoticePostingDetails")
    fun getNoticePostingDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/DistrictDetails")
    fun getDistrictDetails(@Body body: RequestBody): Call<String>


}

