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

            getlabels("1")
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



                    if(id.equals("1"))
                    {
                        id="1"

                    /*    getlabels(id)
                        val intent = Intent(v.context, WelcomeActivity::class.java)
                        intent.putExtra("id", id)
                        v.context.startActivity(intent)*/

                    }
                    else if(id.equals("2"))
                    {

                        id="2"
                      /*  getlabels(id)
                        val intent = Intent(v.context, WelcomeActivity::class.java)
                        intent.putExtra("id", id)
                        v.context.startActivity(intent)*/


                    }
                    getlabels(id)


                    val intent = Intent(v.context, WelcomeActivity::class.java)
                    intent.putExtra("id", id)
                    v.context.startActivity(intent)
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
                            .baseUrl(Config.BASE_URL)
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

                                    val ID_Emi= mContext.getSharedPreferences(Config.SHARED_PREF79, 0)
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


                                    /* val ID_Quit = mContext.getSharedPreferences(Config.SHARED_PREF106, 0)
                                     val ID_QuitEditer = ID_Quit.edit()
                                     ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                     ID_QuitEditer.commit()

                                     val ID_Quit = mContext.getSharedPreferences(Config.SHARED_PREF106, 0)
                                     val ID_QuitEditer = ID_Quit.edit()
                                     ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                     ID_QuitEditer.commit()

                                     val ID_Quit = mContext.getSharedPreferences(Config.SHARED_PREF106, 0)
                                     val ID_QuitEditer = ID_Quit.edit()
                                     ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                     ID_QuitEditer.commit()*/


                                   /* val ID_Quit = mContext.getSharedPreferences(Config.SHARED_PREF106, 0)
                                    val ID_QuitEditer = ID_Quit.edit()
                                    ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                    ID_QuitEditer.commit()

                                    val ID_Quit = mContext.getSharedPreferences(Config.SHARED_PREF106, 0)
                                    val ID_QuitEditer = ID_Quit.edit()
                                    ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                    ID_QuitEditer.commit()

                                    val ID_Quit = mContext.getSharedPreferences(Config.SHARED_PREF106, 0)
                                    val ID_QuitEditer = ID_Quit.edit()
                                    ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                    ID_QuitEditer.commit()*/


                                  /*  val ID_Quit = mContext.getSharedPreferences(Config.SHARED_PREF106, 0)
                                    val ID_QuitEditer = ID_Quit.edit()
                                    ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                    ID_QuitEditer.commit()*/



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


        }
    }
}