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

    @POST("Customer/MPINVarificationMaintenance")
    fun getMPINVarificationMaintenance(@Body body: RequestBody):Call<String>

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

    @POST("AccountSummary/LoanSlabDetails")
    fun getLoanSlabDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/PassBookAccountTransactionList")
    fun getPassbookAccounttranslist(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/AccountDueDateDetails")
    fun getAccountduedetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/HolidayDetails")
    fun getHolidayList(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/BarcodeFormatDet")
    fun getBardCodeData(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/OwnAccounDetails")
    fun getOwnbankownaccountdetail(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/RechargeCircleDetails")
    fun getRechargeCircleDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/OwnAccounDetails")
    fun getOwnAccounDetails(@Body body: RequestBody): Call<String>

    @POST("Recharge/MobileRecharge")
    fun getMobileRecharge(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/BalanceSplitUpDetails")
    fun getbalancesplitupdetail(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/FundTransferToOwnBank")
    fun getfundtransferownBank(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/EMICalculatorDateils")
    fun getEMICalculatorDateils(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/EMIMethodDateils")
    fun getEMIMethodDateils(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/GoldSlabEstimator")
    fun getGoldSlabEstimator(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/BeneficiaryDeatils")
    fun getBeneficiaryDeatils(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/FundTransferLimit")
    fun getfundtransferlimit(@Body body: RequestBody): Call<String>


    @POST("AccountSummary/GetInstalmmentRemittanceAmount")
    fun getinstanceremittanceamt(@Body body: RequestBody): Call<String>


    @POST("AccountSummary/DashBoardAssetsDataDetails")
    fun getDashBoardAssetsDataDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/DashBoardLaibilityDataDetails")
    fun getDashBoardLiabilityDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/DashBoardDataPaymentAndReceiptDetails")
    fun getDashBoardpaymentandreceipt(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/ProductDetailsList")
    fun getProductDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/ProductDetailsSummary")
    fun getProductsummary(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/FundTransferToOtherBank")
    fun getFundTransferToOtherBank(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/OwnFundTransferHistory")
    fun getFundTransferStatus(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/LoanApplicationRequset")
    fun getLoanApplicationRequset(@Body body: RequestBody): Call<String>

    @POST("Recharge/RechargeHistory")
    fun getRechargeHistory(@Body body: RequestBody): Call<String>

    @POST("Recharge/RechargeOffers")
    fun getRechargeOffers(@Body body: RequestBody): Call<String>

    @POST("Recharge/DTHRecharge")
    fun getDTHRecharge(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/QuickPayMoneyTransferPayment")
    fun getQuickPay(@Body body: RequestBody): Call<String>

    @POST("Customer/BannerDetails")
    fun getBannerdetails(@Body body: RequestBody): Call<String>

    @POST("Customer/VersionCheck")
    fun getVersioncheck(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/MaintenanceMessage")
    fun getMaintenanceMsg(@Body body: RequestBody): Call<String>

    @POST("Customer/LabelDetails")
    fun getLabels(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/QuickPaySenderReciver")
    fun getSenderReceiver(@Body body: RequestBody): Call<String>

    @POST("Statement/StatementOfAccount")
    fun getStatementOfAccount(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/QuickPayResendMPIN")
    fun getResendMpin(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/LoanTypeDetails")
    fun getLoanTypeDetails(@Body body: RequestBody): Call<String>

    @POST("AccountSummary/LoanPurposeDetails")
    fun getLoanPurposeDetails(@Body body: RequestBody): Call<String>


}

