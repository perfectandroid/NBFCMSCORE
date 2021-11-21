package com.perfect.nbfcmscore.Adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Activity.WelcomeActivity
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class LanguageLsitAdaptor(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var jresult1: JSONArray? = null
    internal var jsonObject: JSONObject? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.rv_language_listing, parent, false
        )
        vh = MainViewHolder(v)
        return vh
    }

    override fun getItemCount(): Int {
        return jsInfo.length()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {


            jsonObject = jsInfo.getJSONObject(position)
            var id = jsonObject!!.getString("ID_Languages")
            if (holder is MainViewHolder) {
                holder.lang_name!!.setText(jsonObject!!.getString("LanguagesName"))
                holder.lang_shortname!!.setText(jsonObject!!.getString("ShortName"))
                holder.lang_shortname!!.background.setColorFilter(
                        Color.parseColor("#E5E8E8"),
                        PorterDuff.Mode.SRC_ATOP
                )
                //holder.lang_shortname!!.setTextColor(Color.parseColor(jsonObject!!.getString("ColorCode")));

                holder.llmain!!.setTag(position)
                holder.llmain!!.setOnClickListener { v ->
                    jsonObject = jsInfo.getJSONObject(position)
                    val ID_LanguagesSP = mContext.getSharedPreferences(Config.SHARED_PREF9, 0)
                    val ID_LanguagesEditer = ID_LanguagesSP.edit()
                    ID_LanguagesEditer.putString(
                            "ID_Languages",
                            jsonObject!!.getString("ID_Languages")
                    )
                    ID_LanguagesEditer.commit()
                  //  Toast.makeText(mContext,""+jsonObject!!.getString("ID_Languages"),Toast.LENGTH_LONG).show()
                    /*val intent = Intent(v.context, WelcomeActivity::class.java)
                    intent.putExtra("id", jsonObject!!.getString("ID_Languages"))
                    v.context.startActivity(intent)*/



               /*     if(id.equals("1"))
                    {
                        id="1"

                    *//*    getlabels(id)
                        val intent = Intent(v.context, WelcomeActivity::class.java)
                        intent.putExtra("id", id)
                        v.context.startActivity(intent)*//*

                    }
                    else if(id.equals("2"))
                    {

                        id="2"
                      *//*  getlabels(id)
                        val intent = Intent(v.context, WelcomeActivity::class.java)
                        intent.putExtra("id", id)
                        v.context.startActivity(intent)*//*


                    }
                    getlabels(id)*/

                    val myIntent = Intent(v.context, WelcomeActivity::class.java)
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    v.context.startActivity(myIntent)
                   // (v.context as Activity).finish()
                  //  val intent = Intent(v.context, WelcomeActivity::class.java)
                //    intent.putExtra("id", id)
                   // v.context.startActivity(intent)
                 //   (v.context as Activity).finish()
                  /*  else
                    {

                    }*/


                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getlabels(id: String) {
        val baseurlSP = mContext.applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(mContext)) {
            true -> {
                /*   progressDialog = ProgressDialog(this@LanguageSelectionActivity, R.style.Progress)
                   progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                   progressDialog!!.setCancelable(false)
                   progressDialog!!.setIndeterminate(true)
                   progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                   progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(mContext))
                            .hostnameVerifier(Config.getHostnameVerifier())
                            .build()
                    val gson = GsonBuilder()
                            .setLenient()
                            .create()
                    val retrofit = Retrofit.Builder()
                            .baseUrl(baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(client)
                            .build()
                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {

                        val FK_CustomerSP = mContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = mContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("16"))
                        // requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        //  requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put(
                                "FK_Languages",
                                MscoreApplication.encryptStart(id)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                mContext.getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  labels   " + requestObject1)
                    } catch (e: Exception) {
                        // progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mSnackbar = Snackbar.make((mContext as Activity).findViewById(android.R.id.content), "Some technical issues.", Snackbar.LENGTH_INDEFINITE)
                    }
                    val body = RequestBody.create(
                            okhttp3.MediaType.parse("application/json; charset=utf-8"),
                            requestObject1.toString()
                    )
                    val call = apiService.getLabels(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                // progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-labels", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("LabelDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    val jresult3 = jsonobj2.getJSONObject("Labels")
                                    //var welcome = jresult3.get("fasterwaytohelpyou") as String

                                    // Log.i("Resultsjson", welcome)

                                    val ID_WelcomeSP = mContext.getSharedPreferences(Config.SHARED_PREF34, 0)
                                    val ID_WelcomeSPEditer = ID_WelcomeSP.edit()
                                    ID_WelcomeSPEditer.putString("welcome", jresult3.get("welcome") as String)
                                    ID_WelcomeSPEditer.commit()


                                    val ID_FasterSP = mContext.getSharedPreferences(Config.SHARED_PREF35, 0)
                                    val ID_FasterSPEditer = ID_FasterSP.edit()
                                    ID_FasterSPEditer.putString("fasterwaytohelpyou", jresult3.get("fasterwaytohelpyou") as String)
                                    ID_FasterSPEditer.commit()

                                    val ID_SigninSP = mContext.getSharedPreferences(Config.SHARED_PREF36, 0)
                                    val ID_SigninSPEditer = ID_SigninSP.edit()
                                    ID_SigninSPEditer.putString("sigin", jresult3.get("sigin") as String)
                                    ID_SigninSPEditer.commit()

                                    val ID_RegisterSP = mContext.getSharedPreferences(Config.SHARED_PREF37, 0)
                                    val ID_RegisterSPEditer = ID_RegisterSP.edit()
                                    ID_RegisterSPEditer.putString("registernow", jresult3.get("registernow") as String)
                                    ID_RegisterSPEditer.commit()

                                    val ID_SelctlanSP = mContext.getSharedPreferences(Config.SHARED_PREF38, 0)
                                    val ID_SelctlanSPEditer = ID_SelctlanSP.edit()
                                    ID_SelctlanSPEditer.putString("SelectLanguage", jresult3.get("SelectLanguage") as String)
                                    ID_SelctlanSPEditer.commit()

                                    val ID_SkipSP = mContext.getSharedPreferences(Config.SHARED_PREF39, 0)
                                    val ID_SkipSPEditer = ID_SkipSP.edit()
                                    ID_SkipSPEditer.putString("Skip", jresult3.get("Skip") as String)
                                    ID_SkipSPEditer.commit()


                                    val ID_LetsSP = mContext.getSharedPreferences(Config.SHARED_PREF40, 0)
                                    val ID_LetsSPEditer = ID_LetsSP.edit()
                                    ID_LetsSPEditer.putString("Let'sgetstarted", jresult3.get("Let'sgetstarted") as String)
                                    ID_LetsSPEditer.commit()

                                    val ID_PersnlinfSP = mContext.getSharedPreferences(Config.SHARED_PREF41, 0)
                                    val ID_PersnlinfEditer = ID_PersnlinfSP.edit()
                                    ID_PersnlinfEditer.putString("pleaseenteryourpersonalinformation", jresult3.get("pleaseenteryourpersonalinformation") as String)
                                    ID_PersnlinfEditer.commit()

                                    val ID_EntermobSP = mContext.getSharedPreferences(Config.SHARED_PREF42, 0)
                                    val ID_EntermobEditer = ID_EntermobSP.edit()
                                    ID_EntermobEditer.putString("entermobilenumber", jresult3.get("entermobilenumber") as String)
                                    ID_EntermobEditer.commit()

                                    /*    val ID_last4SP = mContext.getSharedPreferences(Config.SHARED_PREF43, 0)
                                    val ID_last4SPEditer = ID_last4SP.edit()
                                    ID_last4SPEditer.putString("enter last4digitofa/cno", jresult3.get("enter last4digitofa/cno") as String)
                                    ID_last4SPEditer.commit()*/

                                    val ID_ContinueSP = mContext.getSharedPreferences(Config.SHARED_PREF44, 0)
                                    val ID_ContinueSPEditer = ID_ContinueSP.edit()
                                    ID_ContinueSPEditer.putString("continue", jresult3.get("continue") as String)
                                    ID_ContinueSPEditer.commit()

                                    val ID_LoginmobSP = mContext.getSharedPreferences(Config.SHARED_PREF45, 0)
                                    val ID_LoginMobSPEditer = ID_LoginmobSP.edit()
                                    ID_LoginMobSPEditer.putString("loginwithmobilenumber", jresult3.get("loginwithmobilenumber") as String)
                                    ID_LoginMobSPEditer.commit()

                                    /*  val ID_MobotpeSP = mContext.getSharedPreferences(Config.SHARED_PREF46, 0)
                                    val ID_MobotpSPEditer = ID_MobotpeSP.edit()
                                    ID_MobotpSPEditer.putString("enteryourmobilenumberwewillsentyouOTPtoverify", jresult3.get("enteryourmobilenumberwewillsentyouOTPtoverify") as String)
                                    ID_MobotpSPEditer.commit()
*/
                                    val ID_LoginverifySP = mContext.getSharedPreferences(Config.SHARED_PREF47, 0)
                                    val ID_LoginVerifySPEditer = ID_LoginverifySP.edit()
                                    ID_LoginVerifySPEditer.putString("userloginverified", jresult3.get("userloginverified") as String)
                                    ID_LoginVerifySPEditer.commit()

                                    val ID_OtpverifySP = mContext.getSharedPreferences(Config.SHARED_PREF48, 0)
                                    val ID_OtpVerifySPEditer = ID_OtpverifySP.edit()
                                    ID_OtpVerifySPEditer.putString("Otpverification", jresult3.get("Otpverification") as String)
                                    ID_OtpVerifySPEditer.commit()

                                    val ID_MyaccSP = mContext.getSharedPreferences(Config.SHARED_PREF50, 0)
                                    val ID_MyaccSPEditer = ID_MyaccSP.edit()
                                    ID_MyaccSPEditer.putString("Myaccounts", jresult3.get("Myaccounts") as String)
                                    ID_MyaccSPEditer.commit()

                                    val ID_PassbkSP = mContext.getSharedPreferences(Config.SHARED_PREF51, 0)
                                    val ID_PassbkSPEditer = ID_PassbkSP.edit()
                                    ID_PassbkSPEditer.putString("passbook", jresult3.get("passbook") as String)
                                    ID_PassbkSPEditer.commit()

                                    val ID_QuickbalSP = mContext.getSharedPreferences(Config.SHARED_PREF52, 0)
                                    val ID_QuickbalSPEditer = ID_QuickbalSP.edit()
                                    ID_QuickbalSPEditer.putString("quickbalance", jresult3.get("quickbalance") as String)
                                    ID_QuickbalSPEditer.commit()

                                    val ID_DueremindSP = mContext.getSharedPreferences(Config.SHARED_PREF53, 0)
                                    val ID_DueremindEditer = ID_DueremindSP.edit()
                                    ID_DueremindEditer.putString("duereminder", jresult3.get("duereminder") as String)
                                    ID_DueremindEditer.commit()

                                    val ID_AbtusSP = mContext.getSharedPreferences(Config.SHARED_PREF54, 0)
                                    val ID_AbtusEditer = ID_AbtusSP.edit()
                                    ID_AbtusEditer.putString("aboutus", jresult3.get("aboutus") as String)
                                    ID_AbtusEditer.commit()

                                    val ID_ContactSP = mContext.getSharedPreferences(Config.SHARED_PREF55, 0)
                                    val ID_ContactEditer = ID_ContactSP.edit()
                                    ID_ContactEditer.putString("contactus", jresult3.get("contactus") as String)
                                    ID_ContactEditer.commit()

                                    val ID_FeebkSP = mContext.getSharedPreferences(Config.SHARED_PREF56, 0)
                                    val ID_FeedbkEditer = ID_FeebkSP.edit()
                                    ID_FeedbkEditer.putString("feedback", jresult3.get("feedback") as String)
                                    ID_FeedbkEditer.commit()

                                    val ID_PrivacySP = mContext.getSharedPreferences(Config.SHARED_PREF57, 0)
                                    val ID_PrivacyEditer = ID_PrivacySP.edit()
                                    ID_PrivacyEditer.putString("privacypolicy", jresult3.get("privacypolicy") as String)
                                    ID_PrivacyEditer.commit()

                                    val ID_TermsSP = mContext.getSharedPreferences(Config.SHARED_PREF58, 0)
                                    val ID_TermsEditer = ID_TermsSP.edit()
                                    ID_TermsEditer.putString("termsandconditions", jresult3.get("termsandconditions") as String)
                                    ID_TermsEditer.commit()

                                    val ID_StatmntSP = mContext.getSharedPreferences(Config.SHARED_PREF59, 0)
                                    val ID_StatmntEditer = ID_StatmntSP.edit()
                                    ID_StatmntEditer.putString("statement", jresult3.get("statement") as String)
                                    ID_StatmntEditer.commit()

                                    val ID_SetngsSP = mContext.getSharedPreferences(Config.SHARED_PREF60, 0)
                                    val ID_SetngsSpEditer = ID_SetngsSP.edit()
                                    ID_SetngsSpEditer.putString("settings", jresult3.get("settings") as String)
                                    ID_SetngsSpEditer.commit()

                                    val ID_LogoutSP = mContext.getSharedPreferences(Config.SHARED_PREF61, 0)
                                    val ID_LogoutEditer = ID_LogoutSP.edit()
                                    ID_LogoutEditer.putString("logout", jresult3.get("logout") as String)
                                    ID_LogoutEditer.commit()

                                    val ID_NotifSP = mContext.getSharedPreferences(Config.SHARED_PREF62, 0)
                                    val ID_NotifSpEditer = ID_NotifSP.edit()
                                    ID_NotifSpEditer.putString("NotificationandMessages", jresult3.get("NotificationandMessages") as String)
                                    ID_NotifSpEditer.commit()

                                    val ID_OwnBank = mContext.getSharedPreferences(Config.SHARED_PREF63, 0)
                                    val ID_OwnbnkEditer = ID_OwnBank.edit()
                                    ID_OwnbnkEditer.putString("OwnBank", jresult3.get("OwnBank") as String)
                                    ID_OwnbnkEditer.commit()

                                    val ID_OtherBank = mContext.getSharedPreferences(Config.SHARED_PREF64, 0)
                                    val ID_OtherBankEditer = ID_OtherBank.edit()
                                    ID_OtherBankEditer.putString("OtherBank", jresult3.get("OtherBank") as String)
                                    ID_OtherBankEditer.commit()

                                    val ID_Quickpay = mContext.getSharedPreferences(Config.SHARED_PREF65, 0)
                                    val ID_QuickpayEditer = ID_Quickpay.edit()
                                    ID_QuickpayEditer.putString("QuickPay", jresult3.get("QuickPay") as String)
                                    ID_QuickpayEditer.commit()

                                    val ID_Prepaid = mContext.getSharedPreferences(Config.SHARED_PREF66, 0)
                                    val ID_PrepaidEditer = ID_Prepaid.edit()
                                    ID_PrepaidEditer.putString("PrepaidMobile", jresult3.get("PrepaidMobile") as String)
                                    ID_PrepaidEditer.commit()

                                    val ID_Postpaid = mContext.getSharedPreferences(Config.SHARED_PREF67, 0)
                                    val ID_PostpaidEditer = ID_Postpaid.edit()
                                    ID_PostpaidEditer.putString("PostpaidMobile", jresult3.get("PostpaidMobile") as String)
                                    ID_PostpaidEditer.commit()

                                    val ID_Landline = mContext.getSharedPreferences(Config.SHARED_PREF68, 0)
                                    val ID_LandlineEditer = ID_Landline.edit()
                                    ID_LandlineEditer.putString("Landline", jresult3.get("Landline") as String)
                                    ID_LandlineEditer.commit()

                                    val ID_DTH = mContext.getSharedPreferences(Config.SHARED_PREF69, 0)
                                    val ID_DTHEditer = ID_DTH.edit()
                                    ID_DTHEditer.putString("DTH", jresult3.get("DTH") as String)
                                    ID_DTHEditer.commit()

                                    val ID_Datacrdpay = mContext.getSharedPreferences(Config.SHARED_PREF70, 0)
                                    val ID_DatacrdEditer = ID_Datacrdpay.edit()
                                    ID_DatacrdEditer.putString("DataCard", jresult3.get("DataCard") as String)
                                    ID_DatacrdEditer.commit()

                                    val ID_KSEB = mContext.getSharedPreferences(Config.SHARED_PREF71, 0)
                                    val ID_KSEBEditer = ID_KSEB.edit()
                                    ID_KSEBEditer.putString("KSEB", jresult3.get("KSEB") as String)
                                    ID_KSEBEditer.commit()

                                    val ID_Histry = mContext.getSharedPreferences(Config.SHARED_PREF72, 0)
                                    val ID_HistryEditer = ID_Histry.edit()
                                    ID_HistryEditer.putString("History", jresult3.get("History") as String)
                                    ID_HistryEditer.commit()

                                    val ID_Dashbrd = mContext.getSharedPreferences(Config.SHARED_PREF73, 0)
                                    val ID_DashbrdEditer = ID_Dashbrd.edit()
                                    ID_DashbrdEditer.putString("Dashboard", jresult3.get("Dashboard") as String)
                                    ID_DashbrdEditer.commit()

                                    val ID_Virtualcrd = mContext.getSharedPreferences(Config.SHARED_PREF74, 0)
                                    val ID_VirtualcrdEditer = ID_Virtualcrd.edit()
                                    ID_VirtualcrdEditer.putString("VirtualCard", jresult3.get("VirtualCard") as String)
                                    ID_VirtualcrdEditer.commit()

                                    val ID_Branch = mContext.getSharedPreferences(Config.SHARED_PREF75, 0)
                                    val ID_BranchEditer = ID_Branch.edit()
                                    ID_BranchEditer.putString("BranchDetails", jresult3.get("BranchDetails") as String)
                                    ID_BranchEditer.commit()

                                    val ID_Loanapplictn = mContext.getSharedPreferences(Config.SHARED_PREF76, 0)
                                    val ID_LoanapplictnEditer = ID_Loanapplictn.edit()
                                    ID_LoanapplictnEditer.putString("LoanApplication", jresult3.get("LoanApplication") as String)
                                    ID_LoanapplictnEditer.commit()

                                    val ID_Loanstatus = mContext.getSharedPreferences(Config.SHARED_PREF77, 0)
                                    val ID_LoanstatusEditer = ID_Loanstatus.edit()
                                    ID_LoanstatusEditer.putString("LoanStatus", jresult3.get("LoanStatus") as String)
                                    ID_LoanstatusEditer.commit()

                                    val ID_PrdctDetail = mContext.getSharedPreferences(Config.SHARED_PREF78, 0)
                                    val ID_PrdctDetailEditer = ID_PrdctDetail.edit()
                                    ID_PrdctDetailEditer.putString("ProductDetails", jresult3.get("ProductDetails") as String)
                                    ID_PrdctDetailEditer.commit()

                                    val ID_Emi = mContext.getSharedPreferences(Config.SHARED_PREF79, 0)
                                    val ID_EmiEditer = ID_Emi.edit()
                                    ID_EmiEditer.putString("EMICalculator", jresult3.get("EMICalculator") as String)
                                    ID_EmiEditer.commit()

                                    val ID_Deposit = mContext.getSharedPreferences(Config.SHARED_PREF80, 0)
                                    val ID_DepositEditer = ID_Deposit.edit()
                                    ID_DepositEditer.putString("DepositCalculator", jresult3.get("DepositCalculator") as String)
                                    ID_DepositEditer.commit()

                                    val ID_Goldloan = mContext.getSharedPreferences(Config.SHARED_PREF81, 0)
                                    val ID_GoldloanEditer = ID_Goldloan.edit()
                                    ID_GoldloanEditer.putString("GoldLoanEligibileCalculator", jresult3.get("GoldLoanEligibileCalculator") as String)
                                    ID_GoldloanEditer.commit()

                                    val ID_Enqry = mContext.getSharedPreferences(Config.SHARED_PREF82, 0)
                                    val ID_EnqryEditer = ID_Enqry.edit()
                                    ID_EnqryEditer.putString("Enquires", jresult3.get("Enquires") as String)
                                    ID_EnqryEditer.commit()

                                    val ID_Holidy = mContext.getSharedPreferences(Config.SHARED_PREF83, 0)
                                    val ID_HolidyEditer = ID_Holidy.edit()
                                    ID_HolidyEditer.putString("HolidayList", jresult3.get("HolidayList") as String)
                                    ID_HolidyEditer.commit()

                                    val ID_Executve = mContext.getSharedPreferences(Config.SHARED_PREF84, 0)
                                    val ID_ExecutveEditer = ID_Executve.edit()
                                    ID_ExecutveEditer.putString("ExecutiveCallBack", jresult3.get("ExecutiveCallBack") as String)
                                    ID_ExecutveEditer.commit()

                                    val ID_DEPOSIT = mContext.getSharedPreferences(Config.SHARED_PREF85, 0)
                                    val ID_DEPOSITEditer = ID_DEPOSIT.edit()
                                    ID_DEPOSITEditer.putString("DEPOSIT", jresult3.get("DEPOSIT") as String)
                                    ID_DEPOSITEditer.commit()

                                    val ID_LOAN = mContext.getSharedPreferences(Config.SHARED_PREF86, 0)
                                    val ID_LOANEditer = ID_LOAN.edit()
                                    ID_LOANEditer.putString("LOAN", jresult3.get("LOAN") as String)
                                    ID_LOANEditer.commit()

                                    val ID_Active = mContext.getSharedPreferences(Config.SHARED_PREF87, 0)
                                    val ID_ActiveEditer = ID_Active.edit()
                                    ID_ActiveEditer.putString("Active", jresult3.get("Active") as String)
                                    ID_ActiveEditer.commit()

                                    val ID_Deposit1 = mContext.getSharedPreferences(Config.SHARED_PREF88, 0)
                                    val ID_Deposit1Editer = ID_Deposit1.edit()
                                    ID_Deposit1Editer.putString("Deposit", jresult3.get("Deposit") as String)
                                    ID_Deposit1Editer.commit()

                                    val ID_Loan1 = mContext.getSharedPreferences(Config.SHARED_PREF89, 0)
                                    val ID_Loan1Editer = ID_Loan1.edit()
                                    ID_Loan1Editer.putString("Loan", jresult3.get("Loan") as String)
                                    ID_Loan1Editer.commit()

                                    val ID_Ownacc = mContext.getSharedPreferences(Config.SHARED_PREF90, 0)
                                    val ID_OwnaccEditer = ID_Ownacc.edit()
                                    ID_OwnaccEditer.putString("OWNACCOUNT", jresult3.get("OWNACCOUNT") as String)
                                    ID_OwnaccEditer.commit()

                                    val ID_Otheracc = mContext.getSharedPreferences(Config.SHARED_PREF91, 0)
                                    val ID_OtheraccEditer = ID_Otheracc.edit()
                                    ID_OtheraccEditer.putString("OTHERACCOUNT", jresult3.get("OTHERACCOUNT") as String)
                                    ID_OtheraccEditer.commit()

                                    val ID_Selectacc = mContext.getSharedPreferences(Config.SHARED_PREF92, 0)
                                    val ID_SelectaccEditer = ID_Selectacc.edit()
                                    ID_SelectaccEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                    ID_SelectaccEditer.commit()

                                    val ID_Payingfrm = mContext.getSharedPreferences(Config.SHARED_PREF93, 0)
                                    val ID_PayingfrmEditer = ID_Payingfrm.edit()
                                    ID_PayingfrmEditer.putString("PayingFrom", jresult3.get("PayingFrom") as String)
                                    ID_PayingfrmEditer.commit()

                                    val ID_Payingto = mContext.getSharedPreferences(Config.SHARED_PREF94, 0)
                                    val ID_PayingtoEditer = ID_Payingto.edit()
                                    ID_PayingtoEditer.putString("PayingTo", jresult3.get("PayingTo") as String)
                                    ID_PayingtoEditer.commit()

                                    val ID_Amtpayble = mContext.getSharedPreferences(Config.SHARED_PREF95, 0)
                                    val ID_AmtpaybleEditer = ID_Amtpayble.edit()
                                    ID_AmtpaybleEditer.putString("AmountPayable", jresult3.get("AmountPayable") as String)
                                    ID_AmtpaybleEditer.commit()

                                    val ID_Remark = mContext.getSharedPreferences(Config.SHARED_PREF96, 0)
                                    val ID_RemarkEditer = ID_Remark.edit()
                                    ID_RemarkEditer.putString("Remark", jresult3.get("Remark") as String)
                                    ID_RemarkEditer.commit()


                                    val ID_Pay = mContext.getSharedPreferences(Config.SHARED_PREF97, 0)
                                    val ID_PayEditer = ID_Pay.edit()
                                    ID_PayEditer.putString("PAY", jresult3.get("PAY") as String)
                                    ID_PayEditer.commit()

                                    val ID_Receiveracc = mContext.getSharedPreferences(Config.SHARED_PREF98, 0)
                                    val ID_ReceiveraccEditer = ID_Receiveracc.edit()
                                    ID_ReceiveraccEditer.putString("ReceiverAccountType", jresult3.get("ReceiverAccountType") as String)
                                    ID_ReceiveraccEditer.commit()

                                    val ID_Confirmacc = mContext.getSharedPreferences(Config.SHARED_PREF99, 0)
                                    val ID_ConfirmaccEditer = ID_Confirmacc.edit()
                                    ID_ConfirmaccEditer.putString("ConfirmAccountNo", jresult3.get("ConfirmAccountNo") as String)
                                    ID_ConfirmaccEditer.commit()

                                    val ID_Scan = mContext.getSharedPreferences(Config.SHARED_PREF100, 0)
                                    val ID_ScanEditer = ID_Scan.edit()
                                    ID_ScanEditer.putString("Scan", jresult3.get("Scan") as String)
                                    ID_ScanEditer.commit()

                                    val ID_Slctaccnt = mContext.getSharedPreferences(Config.SHARED_PREF101, 0)
                                    val ID_SlctaccntEditer = ID_Slctaccnt.edit()
                                    ID_SlctaccntEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                    ID_SlctaccntEditer.commit()

                                    val ID_Rechrgehist = mContext.getSharedPreferences(Config.SHARED_PREF102, 0)
                                    val ID_RechrgehistEditer = ID_Rechrgehist.edit()
                                    ID_RechrgehistEditer.putString("RechargeHistory", jresult3.get("RechargeHistory") as String)
                                    ID_RechrgehistEditer.commit()

                                    val ID_Frontview = mContext.getSharedPreferences(Config.SHARED_PREF103, 0)
                                    val ID_FrontviewEditer = ID_Frontview.edit()
                                    ID_FrontviewEditer.putString("FRONTVIEW", jresult3.get("FRONTVIEW") as String)
                                    ID_FrontviewEditer.commit()

                                    val ID_Backview = mContext.getSharedPreferences(Config.SHARED_PREF104, 0)
                                    val ID_BackviewEditer = ID_Backview.edit()
                                    ID_BackviewEditer.putString("BACKVIEW", jresult3.get("BACKVIEW") as String)
                                    ID_BackviewEditer.commit()

                                    val ID_Purpose = mContext.getSharedPreferences(Config.SHARED_PREF105, 0)
                                    val ID_PurposeEditer = ID_Purpose.edit()
                                    ID_PurposeEditer.putString("PurposeofVirtualCard", jresult3.get("PurposeofVirtualCard") as String)
                                    ID_PurposeEditer.commit()


                                    val ID_Quit = mContext.getSharedPreferences(Config.SHARED_PREF106, 0)
                                    val ID_QuitEditer = ID_Quit.edit()
                                    ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                    ID_QuitEditer.commit()

                                    val ID_Accno = mContext.getSharedPreferences(Config.SHARED_PREF107, 0)
                                    val ID_AccnoEditer = ID_Accno.edit()
                                    ID_AccnoEditer.putString("AccountNo", jresult3.get("AccountNo") as String)
                                    ID_AccnoEditer.commit()

                                    val ID_Enterdist = mContext.getSharedPreferences(Config.SHARED_PREF108, 0)
                                    val ID_EnterdistEditer = ID_Enterdist.edit()
                                    ID_EnterdistEditer.putString("EnterDistrict", jresult3.get("EnterDistrict") as String)
                                    ID_EnterdistEditer.commit()


                                    val ID_Mobilenum = mContext.getSharedPreferences(Config.SHARED_PREF110, 0)
                                    val ID_MobilenumEditer = ID_Mobilenum.edit()
                                    ID_MobilenumEditer.putString("MobileNumber", jresult3.get("MobileNumber") as String)
                                    ID_MobilenumEditer.commit()

                                    val ID_Operator = mContext.getSharedPreferences(Config.SHARED_PREF111, 0)
                                    val ID_OperatorEditer = ID_Operator.edit()
                                    ID_OperatorEditer.putString("Operator", jresult3.get("Operator") as String)
                                    ID_OperatorEditer.commit()

                                    val ID_Circle = mContext.getSharedPreferences(Config.SHARED_PREF112, 0)
                                    val ID_CircleEditer = ID_Circle.edit()
                                    ID_CircleEditer.putString("Circle", jresult3.get("Circle") as String)
                                    ID_CircleEditer.commit()


                                    val ID_Amt = mContext.getSharedPreferences(Config.SHARED_PREF113, 0)
                                    val ID_AmtEditer = ID_Amt.edit()
                                    ID_AmtEditer.putString("Amount", jresult3.get("Amount") as String)
                                    ID_AmtEditer.commit()

                                    val ID_Rechrg = mContext.getSharedPreferences(Config.SHARED_PREF114, 0)
                                    val ID_RechrgEditer = ID_Rechrg.edit()
                                    ID_RechrgEditer.putString("RECHARGE", jresult3.get("RECHARGE") as String)
                                    ID_RechrgEditer.commit()

                                    val ID_Selctop = mContext.getSharedPreferences(Config.SHARED_PREF115, 0)
                                    val ID_SelctopEditer = ID_Selctop.edit()
                                    ID_SelctopEditer.putString("SelectOperator", jresult3.get("SelectOperator") as String)
                                    ID_SelctopEditer.commit()


                                    val ID_Subscriber = mContext.getSharedPreferences(Config.SHARED_PREF116, 0)
                                    val ID_SubscriberEditer = ID_Subscriber.edit()
                                    ID_SubscriberEditer.putString("SubscriberID", jresult3.get("SubscriberID") as String)
                                    ID_SubscriberEditer.commit()

                                    val ID_Accnt = mContext.getSharedPreferences(Config.SHARED_PREF117, 0)
                                    val ID_AccntEditer = ID_Accnt.edit()
                                    ID_AccntEditer.putString("Account", jresult3.get("Account") as String)
                                    ID_AccntEditer.commit()

                                    val ID_viewall = mContext.getSharedPreferences(Config.SHARED_PREF118, 0)
                                    val ID_viewallEditer = ID_viewall.edit()
                                    ID_viewallEditer.putString("ViewAllAccounts", jresult3.get("ViewAllAccounts") as String)
                                    ID_viewallEditer.commit()

                                    val ID_availbal = mContext.getSharedPreferences(Config.SHARED_PREF119, 0)
                                    val ID_availbalEditer = ID_availbal.edit()
                                    ID_availbalEditer.putString("AvailableBalance", jresult3.get("AvailableBalance") as String)
                                    ID_availbalEditer.commit()

                                    val ID_lastlog = mContext.getSharedPreferences(Config.SHARED_PREF120, 0)
                                    val ID_lastlogEditer = ID_lastlog.edit()
                                    ID_lastlogEditer.putString("LastLogin", jresult3.get("LastLogin") as String)
                                    ID_lastlogEditer.commit()

                                    val ID_acntdetl = mContext.getSharedPreferences(Config.SHARED_PREF121, 0)
                                    val ID_acntdetlEditer = ID_acntdetl.edit()
                                    ID_acntdetlEditer.putString("AccountDetails", jresult3.get("AccountDetails") as String)
                                    ID_acntdetlEditer.commit()

                                    val ID_fundtrns = mContext.getSharedPreferences(Config.SHARED_PREF122, 0)
                                    val ID_fundtrnsEditer = ID_fundtrns.edit()
                                    ID_fundtrnsEditer.putString("FundTransfer", jresult3.get("FundTransfer") as String)
                                    ID_fundtrnsEditer.commit()

                                    val ID_rchrgbill = mContext.getSharedPreferences(Config.SHARED_PREF123, 0)
                                    val ID_rchrgbillEditer = ID_rchrgbill.edit()
                                    ID_rchrgbillEditer.putString("RechargeBills", jresult3.get("RechargeBills") as String)
                                    ID_rchrgbillEditer.commit()

                                    val ID_reprts = mContext.getSharedPreferences(Config.SHARED_PREF124, 0)
                                    val ID_reportsEditer = ID_reprts.edit()
                                    ID_reportsEditer.putString("ReportsOtherServices", jresult3.get("ReportsOtherServices") as String)
                                    ID_reportsEditer.commit()

                                    val ID_tools = mContext.getSharedPreferences(Config.SHARED_PREF125, 0)
                                    val ID_toolsEditer = ID_tools.edit()
                                    ID_toolsEditer.putString("ToolsSettings", jresult3.get("ToolsSettings") as String)
                                    ID_toolsEditer.commit()

                                    val ID_Slctprd = mContext.getSharedPreferences(Config.SHARED_PREF126, 0)
                                    val ID_SlctprdEditer = ID_Slctprd.edit()
                                    ID_SlctprdEditer.putString("Selectaperiodofyourchoice", jresult3.get("Selectaperiodofyourchoice") as String)
                                    ID_SlctprdEditer.commit()

                                    val ID_Or = mContext.getSharedPreferences(Config.SHARED_PREF127, 0)
                                    val ID_OrEditer = ID_Or.edit()
                                    ID_OrEditer.putString("OR", jresult3.get("OR") as String)
                                    ID_OrEditer.commit()

                                    val ID_customdate = mContext.getSharedPreferences(Config.SHARED_PREF128, 0)
                                    val ID_customdateEditer = ID_customdate.edit()
                                    ID_customdateEditer.putString("Selectacustomdateofyourchoice.", jresult3.get("Selectacustomdateofyourchoice.") as String)
                                    ID_customdateEditer.commit()

                                    val ID_View = mContext.getSharedPreferences(Config.SHARED_PREF129, 0)
                                    val ID_ViewEditer = ID_View.edit()
                                    ID_ViewEditer.putString("View", jresult3.get("View") as String)
                                    ID_ViewEditer.commit()

                                    val ID_downld = mContext.getSharedPreferences(Config.SHARED_PREF130, 0)
                                    val ID_downldEditer = ID_downld.edit()
                                    ID_downldEditer.putString("Download", jresult3.get("Download") as String)
                                    ID_downldEditer.commit()

                                    val ID_lastmnth = mContext.getSharedPreferences(Config.SHARED_PREF131, 0)
                                    val ID_lastmnthEditer = ID_lastmnth.edit()
                                    ID_lastmnthEditer.putString("LastMonth", jresult3.get("LastMonth") as String)
                                    ID_lastmnthEditer.commit()

                                    val ID_lastthree = mContext.getSharedPreferences(Config.SHARED_PREF132, 0)
                                    val ID_lastthreeEditer = ID_lastthree.edit()
                                    ID_lastthreeEditer.putString("Last3Months", jresult3.get("Last3Months") as String)
                                    ID_lastthreeEditer.commit()

                                    val ID_lastsix = mContext.getSharedPreferences(Config.SHARED_PREF133, 0)
                                    val ID_lastsixEditer = ID_lastsix.edit()
                                    ID_lastsixEditer.putString("Last6Months", jresult3.get("Last6Months") as String)
                                    ID_lastsixEditer.commit()

                                    val ID_lastone = mContext.getSharedPreferences(Config.SHARED_PREF134, 0)
                                    val ID_lastoneEditer = ID_lastone.edit()
                                    ID_lastoneEditer.putString("Last1Year", jresult3.get("Last1Year") as String)
                                    ID_lastoneEditer.commit()

                                    val ID_selctacc = mContext.getSharedPreferences(Config.SHARED_PREF135, 0)
                                    val ID_selctaccEditer = ID_selctacc.edit()
                                    ID_selctaccEditer.putString("SelectAccount", jresult3.get("SelectAccount") as String)
                                    ID_selctaccEditer.commit()

                                    val ID_selctsndr = mContext.getSharedPreferences(Config.SHARED_PREF136, 0)
                                    val ID_selctsndrEditer = ID_selctsndr.edit()
                                    ID_selctsndrEditer.putString("SelectSender", jresult3.get("SelectSender") as String)
                                    ID_selctsndrEditer.commit()

                                    val ID_selctrecvr = mContext.getSharedPreferences(Config.SHARED_PREF137, 0)
                                    val ID_selctrecvrEditer = ID_selctrecvr.edit()
                                    ID_selctrecvrEditer.putString("SelectReceiver", jresult3.get("SelectReceiver") as String)
                                    ID_selctrecvrEditer.commit()

                                    val ID_addnewsndr = mContext.getSharedPreferences(Config.SHARED_PREF138, 0)
                                    val ID_addnewsndrEditer = ID_addnewsndr.edit()
                                    ID_addnewsndrEditer.putString("AddNewSender", jresult3.get("AddNewSender") as String)
                                    ID_addnewsndrEditer.commit()

                                    val ID_addnewrecvr = mContext.getSharedPreferences(Config.SHARED_PREF139, 0)
                                    val ID_addnewrecvrEditer = ID_addnewrecvr.edit()
                                    ID_addnewrecvrEditer.putString("AddNewReceiver", jresult3.get("AddNewReceiver") as String)
                                    ID_addnewrecvrEditer.commit()

                                    val ID_mpin = mContext.getSharedPreferences(Config.SHARED_PREF140, 0)
                                    val ID_mpinEditer = ID_mpin.edit()
                                    ID_mpinEditer.putString("MPIN", jresult3.get("MPIN") as String)
                                    ID_mpinEditer.commit()


                                    val ID_frgtmpin = mContext.getSharedPreferences(Config.SHARED_PREF141, 0)
                                    val ID_frgtmpinEditer = ID_frgtmpin.edit()
                                    ID_frgtmpinEditer.putString("ForgotMPIN", jresult3.get("ForgotMPIN") as String)
                                    ID_frgtmpinEditer.commit()

                                    val ID_Makepaymnt = mContext.getSharedPreferences(Config.SHARED_PREF142, 0)
                                    val ID_MakepaymntEditer = ID_Makepaymnt.edit()
                                    ID_MakepaymntEditer.putString("MAKEPAYMENT", jresult3.get("MAKEPAYMENT") as String)
                                    ID_MakepaymntEditer.commit()

                                    val ID_FirstName = mContext.getSharedPreferences(Config.SHARED_PREF143, 0)
                                    val ID_FirstNameEditer = ID_FirstName.edit()
                                    ID_FirstNameEditer.putString("FirstName", jresult3.get("FirstName") as String)
                                    ID_FirstNameEditer.commit()

                                    val ID_LastName = mContext.getSharedPreferences(Config.SHARED_PREF144, 0)
                                    val ID_LastNameEditer = ID_LastName.edit()
                                    ID_LastNameEditer.putString("LastName", jresult3.get("LastName") as String)
                                    ID_LastNameEditer.commit()

                                    val ID_Dob = mContext.getSharedPreferences(Config.SHARED_PREF145, 0)
                                    val ID_DobEditer = ID_Dob.edit()
                                    ID_DobEditer.putString("DOB", jresult3.get("DOB") as String)
                                    ID_DobEditer.commit()

                                    val ID_Registr = mContext.getSharedPreferences(Config.SHARED_PREF146, 0)
                                    val ID_RegistrEditer = ID_Registr.edit()
                                    ID_RegistrEditer.putString("REGISTER", jresult3.get("REGISTER") as String)
                                    ID_RegistrEditer.commit()

                                    val ID_SendrName = mContext.getSharedPreferences(Config.SHARED_PREF147, 0)
                                    val ID_SendrNameEditer = ID_SendrName.edit()
                                    ID_SendrNameEditer.putString("SenderName", jresult3.get("SenderName") as String)
                                    ID_SendrNameEditer.commit()

                                    val ID_ReceivrName = mContext.getSharedPreferences(Config.SHARED_PREF148, 0)
                                    val ID_ReceivrNameEditer = ID_ReceivrName.edit()
                                    ID_ReceivrNameEditer.putString("ReceiverName", jresult3.get("ReceiverName") as String)
                                    ID_ReceivrNameEditer.commit()

                                    val ID_confrmacc = mContext.getSharedPreferences(Config.SHARED_PREF149, 0)
                                    val ID_confrmaccEditer = ID_confrmacc.edit()
                                    ID_confrmaccEditer.putString("ConfirmAccountNumber", jresult3.get("ConfirmAccountNumber") as String)
                                    ID_confrmaccEditer.commit()

                                    val ID_ifsc = mContext.getSharedPreferences(Config.SHARED_PREF150, 0)
                                    val ID_ifscEditer = ID_ifsc.edit()
                                    ID_ifscEditer.putString("IFSCCode", jresult3.get("IFSCCode") as String)
                                    ID_ifscEditer.commit()

                                    val ID_imps = mContext.getSharedPreferences(Config.SHARED_PREF151, 0)
                                    val ID_impsEditer = ID_imps.edit()
                                    ID_impsEditer.putString("IMPS", jresult3.get("IMPS") as String)
                                    ID_impsEditer.commit()

                                    val ID_neft = mContext.getSharedPreferences(Config.SHARED_PREF152, 0)
                                    val ID_neftEditer = ID_neft.edit()
                                    ID_neftEditer.putString("NEFT", jresult3.get("NEFT") as String)
                                    ID_neftEditer.commit()

                                    val ID_rtgs = mContext.getSharedPreferences(Config.SHARED_PREF153, 0)
                                    val ID_rtgsEditer = ID_rtgs.edit()
                                    ID_rtgsEditer.putString("RTGS", jresult3.get("RTGS") as String)
                                    ID_rtgsEditer.commit()

                                    val ID_fundstat = mContext.getSharedPreferences(Config.SHARED_PREF154, 0)
                                    val ID_fundstatEditer = ID_fundstat.edit()
                                    ID_fundstatEditer.putString("FUNDTRANSFERSTATUS", jresult3.get("FUNDTRANSFERSTATUS") as String)
                                    ID_fundstatEditer.commit()

                                    val ID_Benflist = mContext.getSharedPreferences(Config.SHARED_PREF157, 0)
                                    val ID_BenflistEditer = ID_Benflist.edit()
                                    ID_BenflistEditer.putString("BeneficiaryList", jresult3.get("BeneficiaryList") as String)
                                    ID_BenflistEditer.commit()

                                    val ID_acc = mContext.getSharedPreferences(Config.SHARED_PREF158, 0)
                                    val ID_accEditer = ID_acc.edit()
                                    ID_accEditer.putString("AccountNumber", jresult3.get("AccountNumber") as String)
                                    ID_accEditer.commit()

                                    val ID_Benfname = mContext.getSharedPreferences(Config.SHARED_PREF159, 0)
                                    val ID_BenfnameEditer = ID_Benfname.edit()
                                    ID_BenfnameEditer.putString("BeneficiaryName", jresult3.get("BeneficiaryName") as String)
                                    ID_BenfnameEditer.commit()

                                    val ID_Benfaccno = mContext.getSharedPreferences(Config.SHARED_PREF160, 0)
                                    val ID_BenfaccnoEditer = ID_Benfaccno.edit()
                                    ID_BenfaccnoEditer.putString("BeneficiaryCNo", jresult3.get("BeneficiaryCNo") as String)
                                    ID_BenfaccnoEditer.commit()

                                    val ID_Benfconfrmacc = mContext.getSharedPreferences(Config.SHARED_PREF161, 0)
                                    val ID_BenfconfrmaccEditer = ID_Benfconfrmacc.edit()
                                    ID_BenfconfrmaccEditer.putString("ConfirmBeneficiaryACNo", jresult3.get("ConfirmBeneficiaryACNo") as String)
                                    ID_BenfconfrmaccEditer.commit()

                                    val ID_Savedbenf = mContext.getSharedPreferences(Config.SHARED_PREF162, 0)
                                    val ID_SavedbenfEditer = ID_Savedbenf.edit()
                                    ID_SavedbenfEditer.putString("SaveBeneficiaryForFuture", jresult3.get("SaveBeneficiaryForFuture") as String)
                                    ID_SavedbenfEditer.commit()


                                    // mContext.startActivity(Intent(mContext, WelcomeActivity::class.java))


                                } else {
                                    val builder = AlertDialog.Builder(
                                            mContext,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                //  progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        mContext,
                                        R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            // progressDialog!!.dismiss()

                            val builder = AlertDialog.Builder(
                                    mContext,
                                    R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    //  progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(mContext, R.style.MyDialogTheme)
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {
                val builder = AlertDialog.Builder(mContext, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



        internal var llmain: LinearLayout? = null
        var lang_name: TextView? = null
        var lang_shortname: TextView? = null
        var imlogo: ImageView? = null



        init {

            llmain = v.findViewById<View>(R.id.llmain) as LinearLayout
            lang_name = v.findViewById<View>(R.id.lang_name) as TextView
            lang_shortname = v.findViewById<View>(R.id.lang_shortname) as TextView
            imlogo = v.findViewById<View>(R.id.imlogo) as ImageView
        //    tvskip = v.findViewById<View>(R.id.tvskip) as TextView


        }
    }
}