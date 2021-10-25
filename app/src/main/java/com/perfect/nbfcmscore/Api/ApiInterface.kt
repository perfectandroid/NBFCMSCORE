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

    @POST("AccountSummary/ProvidersList")
    fun getProvidersList(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/PassBookAccountDetails")
    fun getPassbookAccount(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/PassBookAccountStatement")
    fun getPassbookAccountstatement(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/PassBookAccountTransactionList")
    fun getPassbookAccounttranslist(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/AccountDueDateDetails")
    fun getAccountduedetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/HolidayDetails")
    fun getHolidayList(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/OwnAccounDetails")
    fun getOwnbankownaccountdetail(@Body body: RequestBody): Call<String>

}

