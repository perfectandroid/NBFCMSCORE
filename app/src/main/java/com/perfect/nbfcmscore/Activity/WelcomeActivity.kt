package com.perfect.nbfcmscore.Activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {
    var Languageid: String? = null
    var tvWelcome:TextView?=null
    var tvfasterway:TextView?=null
    var btlogin:Button?=null
    var btregistration:Button?=null
    private var progressDialog: ProgressDialog? = null
    var skip:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)

        skip = intent.getStringExtra("skip")
        if(skip.equals("1"))
        {
            getlabels(skip)
        }
        else
        {
            val ID_Mylan = applicationContext.getSharedPreferences(Config.SHARED_PREF9,0)
            Languageid =  ID_Mylan.getString("ID_Languages", null)

            getlabels(Languageid)
        }

        //Languageid = intent.getStringExtra("id")



      //  Languageid = intent.getStringExtra("id")
       /* val intent = Intent(this@WelcomeActivity, WelcomeActivity::class.java)
        startActivity(intent)
        finish()*/


        setRegViews()
       /* if(Languageid.equals(""))
        {
            Languageid="1"
            getlabels(Languageid)
        }
        else {

        }*/

        val imwelcome: ImageView = findViewById(R.id.imwelcome)
        val tv_product_name: TextView = findViewById(R.id.tv_product_name)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12, 0)
        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        var IMAGRURL = IMAGE_URL+AppIconImageCodeSP.getString("AppIconImageCode", null)
        val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)

        //  Glide.with(this).load(IMAGRURL).placeholder(null).into(imwelcome!!);
        PicassoTrustAll.getInstance(this@WelcomeActivity)!!.load(imagepath).error(android.R.color.transparent).into(imwelcome!!)
        tv_product_name!!.setText("" + ProductNameSP.getString("ProductName", null))









    }

    private fun setRegViews() {
         btlogin = findViewById<Button>(R.id.btlogin) as Button
        tvWelcome = findViewById<TextView>(R.id.tvWelcome) as TextView
        tvfasterway = findViewById<TextView>(R.id.tvfasterway) as TextView
        btregistration = findViewById<Button>(R.id.btregistration) as Button
        btlogin!!.setOnClickListener(this)
        btregistration!!.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btlogin -> {
                intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.btregistration -> {
                intent = Intent(applicationContext, RegistrationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getlabels(id: String?) {
        val baseurlSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                   progressDialog = ProgressDialog(this@WelcomeActivity, R.style.Progress)
                   progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                   progressDialog!!.setCancelable(false)
                   progressDialog!!.setIndeterminate(true)
                   progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                   progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this))
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

                        val FK_CustomerSP = this.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this.getSharedPreferences(
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
                                this.getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  labels   " + requestObject1)
                    } catch (e: Exception) {
                         progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mSnackbar = Snackbar.make((this as Activity).findViewById(android.R.id.content), "Some technical issues.", Snackbar.LENGTH_INDEFINITE)
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
                                 progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-labels", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("LabelDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    val jresult3 = jsonobj2.getJSONObject("Labels")
                                    //var welcome = jresult3.get("fasterwaytohelpyou") as String

                                    // Log.i("Resultsjson", welcome)
                                    Log.e("TAG","jresult3   210   "+jresult3)

                                    try {
                                        val ID_WelcomeSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF34, 0)
                                        val ID_WelcomeSPEditer = ID_WelcomeSP.edit()
                                        ID_WelcomeSPEditer.putString("welcome", jresult3.get("welcome") as String)
                                        ID_WelcomeSPEditer.commit()

                                        val ID_cnmbr= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF310, 0)
                                        val ID_cnmbrEditer = ID_cnmbr.edit()
                                        ID_cnmbrEditer.putString("CustomerNumber1", jresult3.get("CustomerNumber") as String)
                                        ID_cnmbrEditer.commit()

                                        val ID_FasterSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF35, 0)
                                        val ID_FasterSPEditer = ID_FasterSP.edit()
                                        ID_FasterSPEditer.putString("fasterwaytohelpyou", jresult3.get("fasterwaytohelpyou") as String)
                                        ID_FasterSPEditer.commit()

                                        val ID_SigninSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF36, 0)
                                        val ID_SigninSPEditer = ID_SigninSP.edit()
                                        ID_SigninSPEditer.putString("sigin", jresult3.get("sigin") as String)
                                        ID_SigninSPEditer.commit()

                                        val ID_RegisterSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF37, 0)
                                        val ID_RegisterSPEditer = ID_RegisterSP.edit()
                                        ID_RegisterSPEditer.putString("registernow", jresult3.get("registernow") as String)
                                        ID_RegisterSPEditer.commit()

                                        val ID_SelctlanSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF38, 0)
                                        val ID_SelctlanSPEditer = ID_SelctlanSP.edit()
                                        ID_SelctlanSPEditer.putString("SelectLanguage", jresult3.get("SelectLanguage") as String)
                                        ID_SelctlanSPEditer.commit()

                                        val ID_SkipSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF39, 0)
                                        val ID_SkipSPEditer = ID_SkipSP.edit()
                                        ID_SkipSPEditer.putString("Skip", jresult3.get("Skip") as String)
                                        ID_SkipSPEditer.commit()


                                        val ID_add= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF299, 0)
                                        val ID_addEditer = ID_add.edit()
                                        ID_addEditer.putString("Address1", jresult3.get("Address") as String)
                                        ID_addEditer.commit()

                                        val ID_plsslctacc= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF284, 0)
                                        val ID_plsslctaccEditer = ID_plsslctacc.edit()
                                        ID_plsslctaccEditer.putString("PleaseSelectAccount", jresult3.get("PleaseSelectAccount") as String)
                                        ID_plsslctaccEditer.commit()



                                        val ID_termstxt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF307, 0)
                                        val ID_termstxtEditer = ID_termstxt.edit()
                                        ID_termstxtEditer.putString("TermsandConditionstext", jresult3.get("TermsandConditionstext") as String)
                                        ID_termstxtEditer.commit()

                                        val ID_licnse= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF305, 0)
                                        val ID_licnseEditer = ID_licnse.edit()
                                        ID_licnseEditer.putString("LICENSEDTO", jresult3.get("LICENSEDTO") as String)
                                        ID_licnseEditer.commit()

                                        val ID_tchpart= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF306, 0)
                                        val ID_tchpartEditer = ID_tchpart.edit()
                                        ID_tchpartEditer.putString("TECHNOLOGYPARTNER", jresult3.get("TECHNOLOGYPARTNER") as String)
                                        ID_tchpartEditer.commit()

                                        val ID_mail= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF300, 0)
                                        val ID_mailEditer = ID_mail.edit()
                                        ID_mailEditer.putString("Email1", jresult3.get("Email1") as String)
                                        ID_mailEditer.commit()


                                        val ID_gndr= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF308, 0)
                                        val ID_gndrEditer = ID_gndr.edit()
                                        ID_gndrEditer.putString("Gender1", jresult3.get("Gender1") as String)
                                        ID_gndrEditer.commit()


                                        val ID_dob1= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF309, 0)
                                        val ID_dob1Editer = ID_dob1.edit()
                                        ID_dob1Editer.putString("DateofBirth1", jresult3.get("DateofBirth1") as String)
                                        ID_dob1Editer.commit()


                                        val ID_entramt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF259, 0)
                                        val ID_entramtEditer = ID_entramt.edit()
                                        ID_entramtEditer.putString("Pleaseenteramount", jresult3.get("Pleaseenteramount") as String)
                                        ID_entramtEditer.commit()

                                        val ID_plsentrambw= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF322, 0)
                                        val ID_ovrdueloansEditer = ID_plsentrambw.edit()
                                        ID_ovrdueloansEditer.putString("PleaseEnterAmountbetween1and25000.0.", jresult3.get("PleaseEnterAmountbetween1and25000.0.") as String)
                                        ID_ovrdueloansEditer.commit()


                                        val ID_plsentrmpin= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF323, 0)
                                        val ID_plsentrmpinEditer = ID_plsentrmpin.edit()
                                        ID_plsentrmpinEditer.putString("pleaseentermpin", jresult3.get("pleaseentermpin") as String)
                                        ID_plsentrmpinEditer.commit()


                                        val ID_plsentrfnme= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF303, 0)
                                        val ID_plsentrfnmeEditer = ID_plsentrfnme.edit()
                                        ID_plsentrfnmeEditer.putString("PleaseEnterFirstName", jresult3.get("PleaseEnterFirstName") as String)
                                        ID_plsentrfnmeEditer.commit()

                                        val ID_plsentrlnme= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF304, 0)
                                        val ID_plsentrlnmeEditer = ID_plsentrlnme.edit()
                                        ID_plsentrlnmeEditer.putString("PleaseEnterLastName", jresult3.get("PleaseEnterLastName") as String)
                                        ID_plsentrlnmeEditer.commit()

                                        val ID_plsentmob= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF281, 0)
                                        val ID_plsentmobEditer = ID_plsentmob.edit()
                                        ID_plsentmobEditer.putString("PleaseEnterMobileNumber", jresult3.get("PleaseEnterMobileNumber") as String)
                                        ID_plsentmobEditer.commit()


                                        val ID_plssvalidmob= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF287, 0)
                                        val ID_plssvalidmobEditer = ID_plssvalidmob.edit()
                                        ID_plssvalidmobEditer.putString("PleaseEnterValidMobileNumber", jresult3.get("PleaseEnterValidMobileNumber") as String)
                                        ID_plssvalidmobEditer.commit()



                                        val ID_selctsndr = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF136, 0)
                                        val ID_selctsndrEditer = ID_selctsndr.edit()
                                        ID_selctsndrEditer.putString("SelectSender", jresult3.get("SelectSender") as String)
                                        ID_selctsndrEditer.commit()

                                        val ID_entrcvrnme = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF324, 0)
                                        val ID_entrcvrnmeEditer = ID_entrcvrnme.edit()
                                        ID_entrcvrnmeEditer.putString("PleaseEnterReceiverName", jresult3.get("PleaseEnterReceiverName") as String)
                                        ID_entrcvrnmeEditer.commit()


                                        val ID_entrifsc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF325, 0)
                                        val ID_entrifscEditer = ID_entrifsc.edit()
                                        ID_entrifscEditer.putString("PleaseenterIFSCcode", jresult3.get("PleaseenterIFSCcode") as String)
                                        ID_entrifscEditer.commit()


                                        val ID_invaldifsc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF326, 0)
                                        val ID_invaldifscEditer = ID_invaldifsc.edit()
                                        ID_invaldifscEditer.putString("Invalidifsc", jresult3.get("Invalidifsc") as String)
                                        ID_invaldifscEditer.commit()


                                        val ID_plsentaccno = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF327, 0)
                                        val ID_plsentaccnoEditer = ID_plsentaccno.edit()
                                        ID_plsentaccnoEditer.putString("PleaseEnterAccountNumber", jresult3.get("PleaseEnterAccountNumber") as String)
                                        ID_plsentaccnoEditer.commit()

                                        val ID_plscnfrmacc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF328, 0)
                                        val ID_plscnfrmaccEditer = ID_plscnfrmacc.edit()
                                        ID_plscnfrmaccEditer.putString("PleaseEnterConfirmAccountNumber", jresult3.get("PleaseEnterConfirmAccountNumber") as String)
                                        ID_plscnfrmaccEditer.commit()

                                        val ID_accndcnfrm = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF329, 0)
                                        val ID_accndcnfrmEditer = ID_accndcnfrm.edit()
                                        ID_accndcnfrmEditer.putString("AccountnumberandConfirmAccountnumbernotmatching", jresult3.get("AccountnumberandConfirmAccountnumbernotmatching") as String)
                                        ID_accndcnfrmEditer.commit()

                                        val ID_atleast3 = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF330, 0)
                                        val ID_atleast3Editer = ID_atleast3.edit()
                                        ID_atleast3Editer.putString("Atleast3digitsarerequired.", jresult3.get("Atleast3digitsarerequired.") as String)
                                        ID_atleast3Editer.commit()

                                        val ID_atleast6 = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF331, 0)
                                        val ID_atleast6Editer = ID_atleast6.edit()
                                        ID_atleast6Editer.putString("Atleast6digitsarerequired.", jresult3.get("Atleast6digitsarerequired.") as String)
                                        ID_atleast6Editer.commit()

                                        val ID_plsentconsnme= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF285, 0)
                                        val ID_plsentconsnmeEditer = ID_plsentconsnme.edit()
                                        ID_plsentconsnmeEditer.putString("PleaseEnterConsumerName", jresult3.get("PleaseEnterConsumerName") as String)
                                        ID_plsentconsnmeEditer.commit()

                                        val ID_plsentrconsno= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF286, 0)
                                        val ID_plsentrconsnoEditer = ID_plsentrconsno.edit()
                                        ID_plsentrconsnoEditer.putString("PleaseEnterConsumerNumber", jresult3.get("PleaseEnterConsumerNumber") as String)
                                        ID_plsentrconsnoEditer.commit()

                                        val ID_plsslctoprtr= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF301, 0)
                                        val ID_plsslctoprtrEditer = ID_plsslctoprtr.edit()
                                        ID_plsslctoprtrEditer.putString("PleaseSelectOperator", jresult3.get("PleaseSelectOperator") as String)
                                        ID_plsslctoprtrEditer.commit()

                                        val ID_plsslctcircle= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF302, 0)
                                        val ID_plsslctcircleEditer = ID_plsslctcircle.edit()
                                        ID_plsslctcircleEditer.putString("PleaseSelectCircle", jresult3.get("PleaseSelectCircle") as String)
                                        ID_plsslctcircleEditer.commit()

                                        val ID_plentrvalidsubscrbrid= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF332, 0)
                                        val ID_plentrvalidsubscrbridEditer = ID_plentrvalidsubscrbrid.edit()
                                        ID_plentrvalidsubscrbridEditer.putString("Pleaseentervalidsubscriberid", jresult3.get("Pleaseentervalidsubscriberid") as String)
                                        ID_plentrvalidsubscrbridEditer.commit()

                                        val ID_bank= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF333, 0)
                                        val ID_bankEditer = ID_bank.edit()
                                        ID_bankEditer.putString("Bank", jresult3.get("Bank") as String)
                                        ID_bankEditer.commit()

                                        val ID_plce= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF334, 0)
                                        val ID_plceEditer = ID_plce.edit()
                                        ID_plceEditer.putString("Place", jresult3.get("Place") as String)
                                        ID_plceEditer.commit()

                                        val ID_post= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF335, 0)
                                        val ID_postEditer = ID_post.edit()
                                        ID_postEditer.putString("Post", jresult3.get("Post") as String)
                                        ID_postEditer.commit()

                                        val ID_district= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF336, 0)
                                        val ID_districtEditer = ID_district.edit()
                                        ID_districtEditer.putString("District", jresult3.get("District") as String)
                                        ID_districtEditer.commit()


                                        val ID_bnkdetl= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF109, 0)
                                        val ID_bnkdetlEditer = ID_bnkdetl.edit()
                                        ID_bnkdetlEditer.putString("BankDetails", jresult3.get("BankDetails") as String)
                                        ID_bnkdetlEditer.commit()


                                        val ID_brnch= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF337, 0)
                                        val ID_brnchEditer = ID_brnch.edit()
                                        ID_brnchEditer.putString("Branch", jresult3.get("Branch") as String)
                                        ID_brnchEditer.commit()





                                        val ID_LetsSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF40, 0)
                                        val ID_LetsSPEditer = ID_LetsSP.edit()
                                        ID_LetsSPEditer.putString("Let'sgetstarted", jresult3.get("Let'sgetstarted") as String)
                                        ID_LetsSPEditer.commit()

                                        val ID_PersnlinfSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF41, 0)
                                        val ID_PersnlinfEditer = ID_PersnlinfSP.edit()
                                        ID_PersnlinfEditer.putString("pleaseenteryourpersonalinformation", jresult3.get("pleaseenteryourpersonalinformation") as String)
                                        ID_PersnlinfEditer.commit()

                                        val ID_EntermobSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF42, 0)
                                        val ID_EntermobEditer = ID_EntermobSP.edit()
                                        ID_EntermobEditer.putString("entermobilenumber", jresult3.get("entermobilenumber") as String)
                                        ID_EntermobEditer.commit()

                                        /*    val ID_last4SP = mContext.getSharedPreferences(Config.SHARED_PREF43, 0)
                                        val ID_last4SPEditer = ID_last4SP.edit()
                                        ID_last4SPEditer.putString("enter last4digitofa/cno", jresult3.get("enter last4digitofa/cno") as String)
                                        ID_last4SPEditer.commit()*/

                                        val ID_ContinueSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF44, 0)
                                        val ID_ContinueSPEditer = ID_ContinueSP.edit()
                                        ID_ContinueSPEditer.putString("continue", jresult3.get("continue") as String)
                                        ID_ContinueSPEditer.commit()

                                        val ID_LoginmobSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF45, 0)
                                        val ID_LoginMobSPEditer = ID_LoginmobSP.edit()
                                        ID_LoginMobSPEditer.putString("loginwithmobilenumber", jresult3.get("loginwithmobilenumber") as String)
                                        ID_LoginMobSPEditer.commit()

                                        /*  val ID_MobotpeSP = mContext.getSharedPreferences(Config.SHARED_PREF46, 0)
                                        val ID_MobotpSPEditer = ID_MobotpeSP.edit()
                                        ID_MobotpSPEditer.putString("enteryourmobilenumberwewillsentyouOTPtoverify", jresult3.get("enteryourmobilenumberwewillsentyouOTPtoverify") as String)
                                        ID_MobotpSPEditer.commit()
    */
                                        val ID_LoginverifySP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF47, 0)
                                        val ID_LoginVerifySPEditer = ID_LoginverifySP.edit()
                                        ID_LoginVerifySPEditer.putString("userloginverified", jresult3.get("userloginverified") as String)
                                        ID_LoginVerifySPEditer.commit()

                                        val ID_OtpverifySP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF48, 0)
                                        val ID_OtpVerifySPEditer = ID_OtpverifySP.edit()
                                        ID_OtpVerifySPEditer.putString("Otpverification", jresult3.get("Otpverification") as String)
                                        ID_OtpVerifySPEditer.commit()

                                        val ID_MyaccSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF50, 0)
                                        val ID_MyaccSPEditer = ID_MyaccSP.edit()
                                        ID_MyaccSPEditer.putString("Myaccounts", jresult3.get("Myaccounts") as String)
                                        ID_MyaccSPEditer.commit()

                                        val ID_PassbkSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF51, 0)
                                        val ID_PassbkSPEditer = ID_PassbkSP.edit()
                                        ID_PassbkSPEditer.putString("passbook", jresult3.get("passbook") as String)
                                        ID_PassbkSPEditer.commit()

                                        val ID_QuickbalSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF52, 0)
                                        val ID_QuickbalSPEditer = ID_QuickbalSP.edit()
                                        ID_QuickbalSPEditer.putString("quickbalance", jresult3.get("quickbalance") as String)
                                        ID_QuickbalSPEditer.commit()

                                        val ID_DueremindSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF53, 0)
                                        val ID_DueremindEditer = ID_DueremindSP.edit()
                                        ID_DueremindEditer.putString("duereminder", jresult3.get("duereminder") as String)
                                        ID_DueremindEditer.commit()

                                        val ID_AbtusSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF54, 0)
                                        val ID_AbtusEditer = ID_AbtusSP.edit()
                                        ID_AbtusEditer.putString("aboutus", jresult3.get("aboutus") as String)
                                        ID_AbtusEditer.commit()

                                        val ID_ContactSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF55, 0)
                                        val ID_ContactEditer = ID_ContactSP.edit()
                                        ID_ContactEditer.putString("contactus", jresult3.get("contactus") as String)
                                        ID_ContactEditer.commit()

                                        val ID_FeebkSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF56, 0)
                                        val ID_FeedbkEditer = ID_FeebkSP.edit()
                                        ID_FeedbkEditer.putString("feedback", jresult3.get("feedback") as String)
                                        ID_FeedbkEditer.commit()

                                        val WelcomeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF34, 0)
                                        val FasterSP = applicationContext.getSharedPreferences(Config.SHARED_PREF35, 0)
                                        val SigninSP = applicationContext.getSharedPreferences(Config.SHARED_PREF36, 0)
                                        val RegisterSP = applicationContext.getSharedPreferences(Config.SHARED_PREF37, 0)


                                        tvWelcome!!.setText(WelcomeSP.getString("welcome", null))
                                        tvfasterway!!.setText(FasterSP.getString("fasterwaytohelpyou", null))
                                        btlogin!!.setText(SigninSP.getString("sigin", null))
                                        btregistration!!.setText(RegisterSP.getString("registernow", null))

                                        val ID_PrivacySP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF57, 0)
                                        val ID_PrivacyEditer = ID_PrivacySP.edit()
                                        ID_PrivacyEditer.putString("privacypolicy", jresult3.get("privacypolicy") as String)
                                        ID_PrivacyEditer.commit()

                                        val ID_TermsSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF58, 0)
                                        val ID_TermsEditer = ID_TermsSP.edit()
                                        ID_TermsEditer.putString("termsandconditions", jresult3.get("termsandconditions") as String)
                                        ID_TermsEditer.commit()

                                        val ID_StatmntSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF59, 0)
                                        val ID_StatmntEditer = ID_StatmntSP.edit()
                                        ID_StatmntEditer.putString("statement", jresult3.get("statement") as String)
                                        ID_StatmntEditer.commit()

                                        val ID_SetngsSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF60, 0)
                                        val ID_SetngsSpEditer = ID_SetngsSP.edit()
                                        ID_SetngsSpEditer.putString("settings", jresult3.get("settings") as String)
                                        ID_SetngsSpEditer.commit()

                                        val ID_LogoutSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF61, 0)
                                        val ID_LogoutEditer = ID_LogoutSP.edit()
                                        ID_LogoutEditer.putString("logout", jresult3.get("logout") as String)
                                        ID_LogoutEditer.commit()

                                        val ID_NotifSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF62, 0)
                                        val ID_NotifSpEditer = ID_NotifSP.edit()
                                        ID_NotifSpEditer.putString("NotificationandMessages", jresult3.get("NotificationandMessages") as String)
                                        ID_NotifSpEditer.commit()

                                        val ID_OwnBank = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF63, 0)
                                        val ID_OwnbnkEditer = ID_OwnBank.edit()
                                        ID_OwnbnkEditer.putString("OwnBank", jresult3.get("OwnBank") as String)
                                        ID_OwnbnkEditer.commit()

                                        val ID_OtherBank = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF64, 0)
                                        val ID_OtherBankEditer = ID_OtherBank.edit()
                                        ID_OtherBankEditer.putString("OtherBank", jresult3.get("OtherBank") as String)
                                        ID_OtherBankEditer.commit()

                                    val ID_Quickpay = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF65, 0)
                                    val ID_QuickpayEditer = ID_Quickpay.edit()
                                    ID_QuickpayEditer.putString("QuickPay", jresult3.get("QuickPay") as String)
                                    ID_QuickpayEditer.commit()

                                        val ID_Prepaid = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF66, 0)
                                        val ID_PrepaidEditer = ID_Prepaid.edit()
                                        ID_PrepaidEditer.putString("PrepaidMobile", jresult3.get("PrepaidMobile") as String)
                                        ID_PrepaidEditer.commit()

                                        val ID_Postpaid = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF67, 0)
                                        val ID_PostpaidEditer = ID_Postpaid.edit()
                                        ID_PostpaidEditer.putString("PostpaidMobile", jresult3.get("PostpaidMobile") as String)
                                        ID_PostpaidEditer.commit()

                                        val ID_Landline = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF68, 0)
                                        val ID_LandlineEditer = ID_Landline.edit()
                                        ID_LandlineEditer.putString("Landline", jresult3.get("Landline") as String)
                                        ID_LandlineEditer.commit()

                                        val ID_DTH = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF69, 0)
                                        val ID_DTHEditer = ID_DTH.edit()
                                        ID_DTHEditer.putString("DTH", jresult3.get("DTH") as String)
                                        ID_DTHEditer.commit()

                                        val ID_Datacrdpay = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF70, 0)
                                        val ID_DatacrdEditer = ID_Datacrdpay.edit()
                                        ID_DatacrdEditer.putString("DataCard", jresult3.get("DataCard") as String)
                                        ID_DatacrdEditer.commit()

                                        val ID_KSEB = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF71, 0)
                                        val ID_KSEBEditer = ID_KSEB.edit()
                                        ID_KSEBEditer.putString("KSEB", jresult3.get("KSEB") as String)
                                        ID_KSEBEditer.commit()

                                        val ID_Histry = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF72, 0)
                                        val ID_HistryEditer = ID_Histry.edit()
                                        ID_HistryEditer.putString("History", jresult3.get("History") as String)
                                        ID_HistryEditer.commit()

                                        val ID_Dashbrd = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF73, 0)
                                        val ID_DashbrdEditer = ID_Dashbrd.edit()
                                        ID_DashbrdEditer.putString("Dashboard", jresult3.get("Dashboard") as String)
                                        ID_DashbrdEditer.commit()

                                        val ID_Virtualcrd = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF74, 0)
                                        val ID_VirtualcrdEditer = ID_Virtualcrd.edit()
                                        ID_VirtualcrdEditer.putString("VirtualCard", jresult3.get("VirtualCard") as String)
                                        ID_VirtualcrdEditer.commit()

                                        val ID_Branch = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF75, 0)
                                        val ID_BranchEditer = ID_Branch.edit()
                                        ID_BranchEditer.putString("BranchDetails", jresult3.get("BranchDetails") as String)
                                        ID_BranchEditer.commit()

                                        val ID_Loanapplictn = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF76, 0)
                                        val ID_LoanapplictnEditer = ID_Loanapplictn.edit()
                                        ID_LoanapplictnEditer.putString("LoanApplication", jresult3.get("LoanApplication") as String)
                                        ID_LoanapplictnEditer.commit()

                                        val ID_Loanstatus = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF77, 0)
                                        val ID_LoanstatusEditer = ID_Loanstatus.edit()
                                        ID_LoanstatusEditer.putString("LoanStatus", jresult3.get("LoanStatus") as String)
                                        ID_LoanstatusEditer.commit()

                                        val ID_PrdctDetail = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF78, 0)
                                        val ID_PrdctDetailEditer = ID_PrdctDetail.edit()
                                        ID_PrdctDetailEditer.putString("ProductDetails", jresult3.get("ProductDetails") as String)
                                        ID_PrdctDetailEditer.commit()

                                        val ID_Emi = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF79, 0)
                                        val ID_EmiEditer = ID_Emi.edit()
                                        ID_EmiEditer.putString("EMICalculator", jresult3.get("EMICalculator") as String)
                                        ID_EmiEditer.commit()

                                        val ID_Deposit = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF80, 0)
                                        val ID_DepositEditer = ID_Deposit.edit()
                                        ID_DepositEditer.putString("DepositCalculator", jresult3.get("DepositCalculator") as String)
                                        ID_DepositEditer.commit()

                                        val ID_Goldloan = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF81, 0)
                                        val ID_GoldloanEditer = ID_Goldloan.edit()
                                        ID_GoldloanEditer.putString("GoldLoanEligibileCalculator", jresult3.get("GoldLoanEligibileCalculator") as String)
                                        ID_GoldloanEditer.commit()

                                        val ID_Enqry = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF82, 0)
                                        val ID_EnqryEditer = ID_Enqry.edit()
                                        ID_EnqryEditer.putString("Enquires", jresult3.get("Enquires") as String)
                                        ID_EnqryEditer.commit()

                                        val ID_Holidy = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF83, 0)
                                        val ID_HolidyEditer = ID_Holidy.edit()
                                        ID_HolidyEditer.putString("HolidayList", jresult3.get("HolidayList") as String)
                                        ID_HolidyEditer.commit()

                                        val ID_Executve = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF84, 0)
                                        val ID_ExecutveEditer = ID_Executve.edit()
                                        ID_ExecutveEditer.putString("ExecutiveCallBack", jresult3.get("ExecutiveCallBack") as String)
                                        ID_ExecutveEditer.commit()

                                        val ID_DEPOSIT = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF85, 0)
                                        val ID_DEPOSITEditer = ID_DEPOSIT.edit()
                                        ID_DEPOSITEditer.putString("DEPOSIT", jresult3.get("DEPOSIT") as String)
                                        ID_DEPOSITEditer.commit()

                                        val ID_LOAN = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF86, 0)
                                        val ID_LOANEditer = ID_LOAN.edit()
                                        ID_LOANEditer.putString("LOAN", jresult3.get("LOAN") as String)
                                        ID_LOANEditer.commit()

                                        val ID_Active = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF87, 0)
                                        val ID_ActiveEditer = ID_Active.edit()
                                        ID_ActiveEditer.putString("Active", jresult3.get("Active") as String)
                                        ID_ActiveEditer.commit()

                                        val ID_Deposit1 = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF88, 0)
                                        val ID_Deposit1Editer = ID_Deposit1.edit()
                                        ID_Deposit1Editer.putString("Deposit", jresult3.get("Deposit") as String)
                                        ID_Deposit1Editer.commit()

                                        val ID_Loan1 = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF89, 0)
                                        val ID_Loan1Editer = ID_Loan1.edit()
                                        ID_Loan1Editer.putString("Loan", jresult3.get("Loan") as String)
                                        ID_Loan1Editer.commit()

                                        val ID_Ownacc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF90, 0)
                                        val ID_OwnaccEditer = ID_Ownacc.edit()
                                        ID_OwnaccEditer.putString("OWNACCOUNT", jresult3.get("OWNACCOUNT") as String)
                                        ID_OwnaccEditer.commit()

                                        val ID_Otheracc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF91, 0)
                                        val ID_OtheraccEditer = ID_Otheracc.edit()
                                        ID_OtheraccEditer.putString("OTHERACCOUNT", jresult3.get("OTHERACCOUNT") as String)
                                        ID_OtheraccEditer.commit()

                                        val ID_Selectacc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF92, 0)
                                        val ID_SelectaccEditer = ID_Selectacc.edit()
                                        ID_SelectaccEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                        ID_SelectaccEditer.commit()

                                        val ID_Payingfrm = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF93, 0)
                                        val ID_PayingfrmEditer = ID_Payingfrm.edit()
                                        ID_PayingfrmEditer.putString("PayingFrom", jresult3.get("PayingFrom") as String)
                                        ID_PayingfrmEditer.commit()

                                        val ID_Payingto = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF94, 0)
                                        val ID_PayingtoEditer = ID_Payingto.edit()
                                        ID_PayingtoEditer.putString("PayingTo", jresult3.get("PayingTo") as String)
                                        ID_PayingtoEditer.commit()

                                        val ID_Amtpayble = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF95, 0)
                                        val ID_AmtpaybleEditer = ID_Amtpayble.edit()
                                        ID_AmtpaybleEditer.putString("AmountPayable", jresult3.get("AmountPayable") as String)
                                        ID_AmtpaybleEditer.commit()

                                        val ID_Remark = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF96, 0)
                                        val ID_RemarkEditer = ID_Remark.edit()
                                        ID_RemarkEditer.putString("Remark", jresult3.get("Remark") as String)
                                        ID_RemarkEditer.commit()


                                        val ID_Pay = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF97, 0)
                                        val ID_PayEditer = ID_Pay.edit()
                                        ID_PayEditer.putString("PAY", jresult3.get("PAY") as String)
                                        ID_PayEditer.commit()

                                        val ID_Receiveracc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF98, 0)
                                        val ID_ReceiveraccEditer = ID_Receiveracc.edit()
                                        ID_ReceiveraccEditer.putString("ReceiverAccountType", jresult3.get("ReceiverAccountType") as String)
                                        ID_ReceiveraccEditer.commit()

                                        val ID_Confirmacc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF99, 0)
                                        val ID_ConfirmaccEditer = ID_Confirmacc.edit()
                                        ID_ConfirmaccEditer.putString("ConfirmAccountNo", jresult3.get("ConfirmAccountNo") as String)
                                        ID_ConfirmaccEditer.commit()

                                        val ID_Scan = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF100, 0)
                                        val ID_ScanEditer = ID_Scan.edit()
                                        ID_ScanEditer.putString("Scan", jresult3.get("Scan") as String)
                                        ID_ScanEditer.commit()

                                        val ID_Slctaccnt = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF101, 0)
                                        val ID_SlctaccntEditer = ID_Slctaccnt.edit()
                                        ID_SlctaccntEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                        ID_SlctaccntEditer.commit()

                                        val ID_Rechrgehist = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF102, 0)
                                        val ID_RechrgehistEditer = ID_Rechrgehist.edit()
                                        ID_RechrgehistEditer.putString("RechargeHistory", jresult3.get("RechargeHistory") as String)
                                        ID_RechrgehistEditer.commit()

                                        val ID_Frontview = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF103, 0)
                                        val ID_FrontviewEditer = ID_Frontview.edit()
                                        ID_FrontviewEditer.putString("FRONTVIEW", jresult3.get("FRONTVIEW") as String)
                                        ID_FrontviewEditer.commit()

                                        val ID_Backview = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF104, 0)
                                        val ID_BackviewEditer = ID_Backview.edit()
                                        ID_BackviewEditer.putString("BACKVIEW", jresult3.get("BACKVIEW") as String)
                                        ID_BackviewEditer.commit()

                                        val ID_Purpose = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF105, 0)
                                        val ID_PurposeEditer = ID_Purpose.edit()
                                        ID_PurposeEditer.putString("PurposeofVirtualCard", jresult3.get("PurposeofVirtualCard") as String)
                                        ID_PurposeEditer.commit()


                                        val ID_Quit = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF106, 0)
                                        val ID_QuitEditer = ID_Quit.edit()
                                        ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                        ID_QuitEditer.commit()

                                        val ID_Accno = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF107, 0)
                                        val ID_AccnoEditer = ID_Accno.edit()
                                        ID_AccnoEditer.putString("AccountNo", jresult3.get("AccountNo") as String)
                                        ID_AccnoEditer.commit()

                                        val ID_Enterdist = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF108, 0)
                                        val ID_EnterdistEditer = ID_Enterdist.edit()
                                        ID_EnterdistEditer.putString("EnterDistrict", jresult3.get("EnterDistrict") as String)
                                        ID_EnterdistEditer.commit()


                                        val ID_Mobilenum = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF110, 0)
                                        val ID_MobilenumEditer = ID_Mobilenum.edit()
                                        ID_MobilenumEditer.putString("MobileNumber", jresult3.get("MobileNumber") as String)
                                        ID_MobilenumEditer.commit()

                                        val ID_Operator = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF111, 0)
                                        val ID_OperatorEditer = ID_Operator.edit()
                                        ID_OperatorEditer.putString("Operator", jresult3.get("Operator") as String)
                                        ID_OperatorEditer.commit()

                                        val ID_Circle = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF112, 0)
                                        val ID_CircleEditer = ID_Circle.edit()
                                        ID_CircleEditer.putString("Circle", jresult3.get("Circle") as String)
                                        ID_CircleEditer.commit()


                                        val ID_Amt = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF113, 0)
                                        val ID_AmtEditer = ID_Amt.edit()
                                        ID_AmtEditer.putString("Amount", jresult3.get("Amount") as String)
                                        ID_AmtEditer.commit()

                                        val ID_Rechrg = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF114, 0)
                                        val ID_RechrgEditer = ID_Rechrg.edit()
                                        ID_RechrgEditer.putString("RECHARGE", jresult3.get("RECHARGE") as String)
                                        ID_RechrgEditer.commit()

                                        val ID_Selctop = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF115, 0)
                                        val ID_SelctopEditer = ID_Selctop.edit()
                                        ID_SelctopEditer.putString("SelectOperator", jresult3.get("SelectOperator") as String)
                                        ID_SelctopEditer.commit()


                                        val ID_Subscriber = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF116, 0)
                                        val ID_SubscriberEditer = ID_Subscriber.edit()
                                        ID_SubscriberEditer.putString("SubscriberID", jresult3.get("SubscriberID") as String)
                                        ID_SubscriberEditer.commit()

                                        val ID_Accnt = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF117, 0)
                                        val ID_AccntEditer = ID_Accnt.edit()
                                        ID_AccntEditer.putString("Account", jresult3.get("Account") as String)
                                        ID_AccntEditer.commit()

                                        val ID_viewall = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF118, 0)
                                        val ID_viewallEditer = ID_viewall.edit()
                                        ID_viewallEditer.putString("ViewAllAccounts", jresult3.get("ViewAllAccounts") as String)
                                        ID_viewallEditer.commit()

                                        val ID_availbal = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF119, 0)
                                        val ID_availbalEditer = ID_availbal.edit()
                                        ID_availbalEditer.putString("AvailableBalance", jresult3.get("AvailableBalance") as String)
                                        ID_availbalEditer.commit()

                                        /* val LastloginSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF29, 0)
                                         val LastloginEditer = LastloginSP.edit()
                                         LastloginEditer.putString("LastLoginTime", "")
                                         LastloginEditer.commit()*/

                                        val ID_lastlog = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF120, 0)
                                        val ID_lastlogEditer = ID_lastlog.edit()
                                        ID_lastlogEditer.putString("LastLogin", jresult3.get("LastLogin") as String)
                                        ID_lastlogEditer.commit()

                                        val ID_acntdetl = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF121, 0)
                                        val ID_acntdetlEditer = ID_acntdetl.edit()
                                        ID_acntdetlEditer.putString("AccountDetails", jresult3.get("AccountDetails") as String)
                                        ID_acntdetlEditer.commit()

                                        val ID_fundtrns = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF122, 0)
                                        val ID_fundtrnsEditer = ID_fundtrns.edit()
                                        ID_fundtrnsEditer.putString("FundTransfer", jresult3.get("FundTransfer") as String)
                                        ID_fundtrnsEditer.commit()

                                        val ID_rchrgbill = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF123, 0)
                                        val ID_rchrgbillEditer = ID_rchrgbill.edit()
                                        ID_rchrgbillEditer.putString("RechargeBills", jresult3.get("RechargeBills") as String)
                                        ID_rchrgbillEditer.commit()

                                        val ID_reprts = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF124, 0)
                                        val ID_reportsEditer = ID_reprts.edit()
                                        ID_reportsEditer.putString("ReportsOtherServices", jresult3.get("ReportsOtherServices") as String)
                                        ID_reportsEditer.commit()



                                        val ID_tools = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF125, 0)
                                        val ID_toolsEditer = ID_tools.edit()
                                        ID_toolsEditer.putString("ToolsSettings", jresult3.get("ToolsSettings") as String)
                                        ID_toolsEditer.commit()

                                        val ID_Slctprd = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF126, 0)
                                        val ID_SlctprdEditer = ID_Slctprd.edit()
                                        ID_SlctprdEditer.putString("Selectaperiodofyourchoice", jresult3.get("Selectaperiodofyourchoice") as String)
                                        ID_SlctprdEditer.commit()

                                        val ID_Or = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF127, 0)
                                        val ID_OrEditer = ID_Or.edit()
                                        ID_OrEditer.putString("OR", jresult3.get("OR") as String)
                                        ID_OrEditer.commit()

                                        val ID_customdate = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF128, 0)
                                        val ID_customdateEditer = ID_customdate.edit()
                                        ID_customdateEditer.putString("Selectacustomdateofyourchoice.", jresult3.get("Selectacustomdateofyourchoice.") as String)
                                        ID_customdateEditer.commit()

                                        val ID_View = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF129, 0)
                                        val ID_ViewEditer = ID_View.edit()
                                        ID_ViewEditer.putString("View", jresult3.get("View") as String)
                                        ID_ViewEditer.commit()

                                        val ID_downld = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF130, 0)
                                        val ID_downldEditer = ID_downld.edit()
                                        ID_downldEditer.putString("Download", jresult3.get("Download") as String)
                                        ID_downldEditer.commit()

                                        val ID_lastmnth = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF131, 0)
                                        val ID_lastmnthEditer = ID_lastmnth.edit()
                                        ID_lastmnthEditer.putString("LastMonth", jresult3.get("LastMonth") as String)
                                        ID_lastmnthEditer.commit()

                                        val ID_lastthree = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF132, 0)
                                        val ID_lastthreeEditer = ID_lastthree.edit()
                                        ID_lastthreeEditer.putString("Last3Months", jresult3.get("Last3Months") as String)
                                        ID_lastthreeEditer.commit()

                                        val ID_lastsix = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF133, 0)
                                        val ID_lastsixEditer = ID_lastsix.edit()
                                        ID_lastsixEditer.putString("Last6Months", jresult3.get("Last6Months") as String)
                                        ID_lastsixEditer.commit()

                                        val ID_lastone = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF134, 0)
                                        val ID_lastoneEditer = ID_lastone.edit()
                                        ID_lastoneEditer.putString("Last1Year", jresult3.get("Last1Year") as String)
                                        ID_lastoneEditer.commit()

                                        val ID_selctacc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF135, 0)
                                        val ID_selctaccEditer = ID_selctacc.edit()
                                        ID_selctaccEditer.putString("SelectAccount", jresult3.get("SelectAccount") as String)
                                        ID_selctaccEditer.commit()



                                        val ID_selctrecvr = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF137, 0)
                                        val ID_selctrecvrEditer = ID_selctrecvr.edit()
                                        ID_selctrecvrEditer.putString("SelectReceiver", jresult3.get("SelectReceiver") as String)
                                        ID_selctrecvrEditer.commit()

                                        val ID_addnewsndr = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF138, 0)
                                        val ID_addnewsndrEditer = ID_addnewsndr.edit()
                                        ID_addnewsndrEditer.putString("AddNewSender", jresult3.get("AddNewSender") as String)
                                        ID_addnewsndrEditer.commit()

                                        val ID_addnewrecvr = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF139, 0)
                                        val ID_addnewrecvrEditer = ID_addnewrecvr.edit()
                                        ID_addnewrecvrEditer.putString("AddNewReceiver", jresult3.get("AddNewReceiver") as String)
                                        ID_addnewrecvrEditer.commit()

                                        val ID_mpin = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF140, 0)
                                        val ID_mpinEditer = ID_mpin.edit()
                                        ID_mpinEditer.putString("MPIN", jresult3.get("MPIN") as String)
                                        ID_mpinEditer.commit()


                                        val ID_frgtmpin = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF141, 0)
                                        val ID_frgtmpinEditer = ID_frgtmpin.edit()
                                        ID_frgtmpinEditer.putString("ForgotMPIN", jresult3.get("ForgotMPIN") as String)
                                        ID_frgtmpinEditer.commit()

                                        val ID_Makepaymnt = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF142, 0)
                                        val ID_MakepaymntEditer = ID_Makepaymnt.edit()
                                        ID_MakepaymntEditer.putString("MAKEPAYMENT", jresult3.get("MAKEPAYMENT") as String)
                                        ID_MakepaymntEditer.commit()

                                        val ID_FirstName = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF143, 0)
                                        val ID_FirstNameEditer = ID_FirstName.edit()
                                        ID_FirstNameEditer.putString("FirstName", jresult3.get("FirstName") as String)
                                        ID_FirstNameEditer.commit()

                                        val ID_LastName = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF144, 0)
                                        val ID_LastNameEditer = ID_LastName.edit()
                                        ID_LastNameEditer.putString("LastName", jresult3.get("LastName") as String)
                                        ID_LastNameEditer.commit()

                                        val ID_Dob = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF145, 0)
                                        val ID_DobEditer = ID_Dob.edit()
                                        ID_DobEditer.putString("DOB", jresult3.get("DOB") as String)
                                        ID_DobEditer.commit()

                                        val ID_Registr = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF146, 0)
                                        val ID_RegistrEditer = ID_Registr.edit()
                                        ID_RegistrEditer.putString("REGISTER", jresult3.get("REGISTER") as String)
                                        ID_RegistrEditer.commit()

                                        val ID_SendrName = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF147, 0)
                                        val ID_SendrNameEditer = ID_SendrName.edit()
                                        ID_SendrNameEditer.putString("SenderName", jresult3.get("SenderName") as String)
                                        ID_SendrNameEditer.commit()

                                        val ID_ReceivrName = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF148, 0)
                                        val ID_ReceivrNameEditer = ID_ReceivrName.edit()
                                        ID_ReceivrNameEditer.putString("ReceiverName", jresult3.get("ReceiverName") as String)
                                        ID_ReceivrNameEditer.commit()

                                        val ID_confrmacc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF149, 0)
                                        val ID_confrmaccEditer = ID_confrmacc.edit()
                                        ID_confrmaccEditer.putString("ConfirmAccountNumber", jresult3.get("ConfirmAccountNumber") as String)
                                        ID_confrmaccEditer.commit()

                                        val ID_ifsc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF150, 0)
                                        val ID_ifscEditer = ID_ifsc.edit()
                                        ID_ifscEditer.putString("IFSCCode", jresult3.get("IFSCCode") as String)
                                        ID_ifscEditer.commit()

                                        val ID_imps = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF151, 0)
                                        val ID_impsEditer = ID_imps.edit()
                                        ID_impsEditer.putString("IMPS", jresult3.get("IMPS") as String)
                                        ID_impsEditer.commit()

                                        val ID_neft = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF152, 0)
                                        val ID_neftEditer = ID_neft.edit()
                                        ID_neftEditer.putString("NEFT", jresult3.get("NEFT") as String)
                                        ID_neftEditer.commit()

                                        val ID_rtgs = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF153, 0)
                                        val ID_rtgsEditer = ID_rtgs.edit()
                                        ID_rtgsEditer.putString("RTGS", jresult3.get("RTGS") as String)
                                        ID_rtgsEditer.commit()

                                        val ID_fundstat = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF154, 0)
                                        val ID_fundstatEditer = ID_fundstat.edit()
                                        ID_fundstatEditer.putString("FUNDTRANSFERSTATUS", jresult3.get("FUNDTRANSFERSTATUS") as String)
                                        ID_fundstatEditer.commit()

                                        val ID_Benflist = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF157, 0)
                                        val ID_BenflistEditer = ID_Benflist.edit()
                                        ID_BenflistEditer.putString("BeneficiaryList", jresult3.get("BeneficiaryList") as String)
                                        ID_BenflistEditer.commit()

                                        val ID_acc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF158, 0)
                                        val ID_accEditer = ID_acc.edit()
                                        ID_accEditer.putString("AccountNumber", jresult3.get("AccountNumber") as String)
                                        ID_accEditer.commit()

                                        val ID_Benfname = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF159, 0)
                                        val ID_BenfnameEditer = ID_Benfname.edit()
                                        ID_BenfnameEditer.putString("BeneficiaryName", jresult3.get("BeneficiaryName") as String)
                                        ID_BenfnameEditer.commit()

                                        val ID_Benfaccno = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF160, 0)
                                        val ID_BenfaccnoEditer = ID_Benfaccno.edit()
                                        ID_BenfaccnoEditer.putString("BeneficiaryCNo", jresult3.get("BeneficiaryCNo") as String)
                                        ID_BenfaccnoEditer.commit()

                                        val ID_Benfconfrmacc = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF161, 0)
                                        val ID_BenfconfrmaccEditer = ID_Benfconfrmacc.edit()
                                        ID_BenfconfrmaccEditer.putString("ConfirmBeneficiaryACNo", jresult3.get("ConfirmBeneficiaryACNo") as String)
                                        ID_BenfconfrmaccEditer.commit()

                                        val ID_Savedbenf = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF162, 0)
                                        val ID_SavedbenfEditer = ID_Savedbenf.edit()
                                        ID_SavedbenfEditer.putString("SaveBeneficiaryForFuture", jresult3.get("SaveBeneficiaryForFuture") as String)
                                        ID_SavedbenfEditer.commit()

                                        val ID_Otpmsg = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF166, 0)
                                        val ID_OtpmsgEditer = ID_Otpmsg.edit()
                                        ID_OtpmsgEditer.putString("please enter validation code senttoyourregisteredmobilenumber", jresult3.get("please enter validation code senttoyourregisteredmobilenumber") as String)
                                        ID_OtpmsgEditer.commit()

                                        val ID_Languagsp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF167, 0)
                                        val ID_LanguagspEditer = ID_Languagsp.edit()
                                        ID_LanguagspEditer.putString("Language", jresult3.get("Language") as String)
                                        ID_LanguagspEditer.commit()

                                        val ID_Mpinverifysp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF168, 0)
                                        val ID_MpinverifyspEditer = ID_Mpinverifysp.edit()
                                        ID_MpinverifyspEditer.putString("MPINVerification", jresult3.get("MPINVerification") as String)
                                        ID_MpinverifyspEditer.commit()


                                        val ID_Plsunlocksp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF169,0)
                                        val ID_PlsunlockspEditer = ID_Plsunlocksp.edit()
                                        ID_PlsunlockspEditer.putString("PleaseunlockwithyourMPIN", jresult3.get("PleaseunlockwithyourMPIN") as String)
                                        ID_PlsunlockspEditer.commit()


                                        val ID_ChangeMpinsp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF170, 0)
                                        val ID_ChangeMpinEditer = ID_ChangeMpinsp.edit()
                                        ID_ChangeMpinEditer.putString("ChangeMPIN", jresult3.get("ChangeMPIN") as String)
                                        ID_ChangeMpinEditer.commit()

                                        val ID_Namesp= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF171, 0)
                                        val ID_NamespEditer = ID_Namesp.edit()
                                        ID_NamespEditer.putString("Name", jresult3.get("Name") as String)
                                        ID_NamespEditer.commit()

                                        val ID_Mobilesp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF172, 0)
                                        val ID_MobilespEditer = ID_Mobilesp.edit()
                                        ID_MobilespEditer.putString("Mobile", jresult3.get("Mobile") as String)
                                        ID_MobilespEditer.commit()

                                        val ID_Datesp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF173, 0)
                                        val ID_DatespEditer = ID_Datesp.edit()
                                        ID_DatespEditer.putString("Date", jresult3.get("Date") as String)
                                        ID_DatespEditer.commit()

                                        val ID_Timesp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF174, 0)
                                        val ID_TimespEditer = ID_Timesp.edit()
                                        ID_TimespEditer.putString("Time", jresult3.get("Time") as String)
                                        ID_TimespEditer.commit()

                                        val ID_Reprterror = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF175, 0)
                                        val ID_ReprterrorEditer = ID_Reprterror.edit()
                                        ID_ReprterrorEditer.putString("Reportanerror", jresult3.get("Reportanerror") as String)
                                        ID_ReprterrorEditer.commit()

                                        val ID_sugstnsp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF176, 0)
                                        val ID_sugstnspEditer = ID_sugstnsp.edit()
                                        ID_sugstnspEditer.putString("Giveasuggestion", jresult3.get("Giveasuggestion") as String)
                                        ID_sugstnspEditer.commit()

                                        val ID_suggstnsp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF177, 0)
                                        val ID_suggstnspEditer = ID_suggstnsp.edit()
                                        ID_suggstnspEditer.putString("Anythingelse", jresult3.get("Anythingelse") as String)
                                        ID_suggstnspEditer.commit()

                                        val ID_Typedep = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF178, 0)
                                        val ID_TypedepEditer = ID_Typedep.edit()
                                        ID_TypedepEditer.putString("TypeofDeposit", jresult3.get("TypeofDeposit") as String)
                                        ID_TypedepEditer.commit()

                                        val ID_benfsp = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF179, 0)
                                        val ID_benfspEditer = ID_benfsp.edit()
                                        ID_benfspEditer.putString("Beneficiary", jresult3.get("Beneficiary") as String)
                                        ID_benfspEditer.commit()


                                        val ID_tenure = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF180, 0)
                                        val ID_tenureEditer = ID_tenure.edit()
                                        ID_tenureEditer.putString("Tenure", jresult3.get("Tenure") as String)
                                        ID_tenureEditer.commit()


                                        val ID_entrmnth = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF181, 0)
                                        val ID_entrmnthEditer = ID_entrmnth.edit()
                                        ID_entrmnthEditer.putString("PleaseEnterMonth", jresult3.get("PleaseEnterMonth") as String)
                                        ID_entrmnthEditer.commit()


                                        val ID_entrdy= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF182, 0)
                                        val ID_entrdyEditer = ID_entrdy.edit()
                                        ID_entrdyEditer.putString("PleaseEnterDay", jresult3.get("PleaseEnterDay") as String)
                                        ID_entrdyEditer.commit()

                                        val ID_ASSETS= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF183, 0)
                                        val ID_ASSETSEditer = ID_ASSETS.edit()
                                        ID_ASSETSEditer.putString("Assets", jresult3.get("Assets") as String)
                                        ID_ASSETSEditer.commit()

                                        val ID_LIABLTY= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF184, 0)
                                        val ID_LIABLTYEditer = ID_LIABLTY.edit()
                                        ID_LIABLTYEditer.putString("Liability", jresult3.get("Liability") as String)
                                        ID_LIABLTYEditer.commit()

                                        val ID_paymnt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF185, 0)
                                        val ID_paymntEditer = ID_paymnt.edit()
                                        ID_paymntEditer.putString("PaymentReceipt", jresult3.get("PaymentReceipt") as String)
                                        ID_paymntEditer.commit()

                                        val ID_calc= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF190, 0)
                                        val ID_calcEditer = ID_calc.edit()
                                        ID_calcEditer.putString("CALCULATE", jresult3.get("CALCULATE") as String)
                                        ID_calcEditer.commit()


                                        val ID_reset= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF189, 0)
                                        val ID_resetEditer = ID_reset.edit()
                                        ID_resetEditer.putString("RESET", jresult3.get("RESET") as String)
                                        ID_resetEditer.commit()

                                        val ID_princamt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF191, 0)
                                        val ID_princamtEditer = ID_princamt.edit()
                                        ID_princamtEditer.putString("PRINCIPALAMOUNT", jresult3.get("PRINCIPALAMOUNT") as String)
                                        ID_princamtEditer.commit()

                                        val ID_intrstrate= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF192, 0)
                                        val ID_intrstrateEditer = ID_intrstrate.edit()
                                        ID_intrstrateEditer.putString("INTERESTRATE", jresult3.get("INTERESTRATE") as String)
                                        ID_intrstrateEditer.commit()

                                        val ID_month= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF193, 0)
                                        val ID_monthEditer = ID_month.edit()
                                        ID_monthEditer.putString("MONTH", jresult3.get("MONTH") as String)
                                        ID_monthEditer.commit()

                                        val ID_emi= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF194, 0)
                                        val ID_emiEditer = ID_emi.edit()
                                        ID_emiEditer.putString("Selectemitype", jresult3.get("Selectemitype") as String)
                                        ID_emiEditer.commit()

                                        val ID_prodctdetl= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF195, 0)
                                        val ID_prodctdetlEditer = ID_prodctdetl.edit()
                                        ID_prodctdetlEditer.putString("ProductListDetails", jresult3.get("ProductListDetails") as String)
                                        ID_prodctdetlEditer.commit()

                                        val ID_loanappstatus= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF196, 0)
                                        val ID_loanappstatusEditer = ID_loanappstatus.edit()
                                        ID_loanappstatusEditer.putString("LoanApplicationStatus", jresult3.get("LoanApplicationStatus") as String)
                                        ID_loanappstatusEditer.commit()

                                        val ID_loantype= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF197, 0)
                                        val ID_loantypeEditer = ID_loantype.edit()
                                        ID_loantypeEditer.putString("SelectLoantype", jresult3.get("SelectLoantype") as String)
                                        ID_loantypeEditer.commit()

                                        val ID_loanpurpse= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF198, 0)
                                        val ID_loanpurpseEditer = ID_loanpurpse.edit()
                                        ID_loanpurpseEditer.putString("Selectloanpurpose", jresult3.get("Selectloanpurpose") as String)
                                        ID_loanpurpseEditer.commit()

                                        val ID_closd= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF199, 0)
                                        val ID_closdEditer = ID_closd.edit()
                                        ID_closdEditer.putString("Closed", jresult3.get("Closed") as String)
                                        ID_closdEditer.commit()

                                        val ID_frmdte= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF200, 0)
                                        val ID_frmdteEditer = ID_frmdte.edit()
                                        ID_frmdteEditer.putString("FromDate", jresult3.get("FromDate") as String)
                                        ID_frmdteEditer.commit()

                                        val ID_enddte= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF201, 0)
                                        val ID_enddteEditer = ID_enddte.edit()
                                        ID_enddteEditer.putString("EndDate", jresult3.get("EndDate") as String)
                                        ID_enddteEditer.commit()

                                        val ID_duedtecal= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF202, 0)
                                        val ID_duedtecalEditer = ID_duedtecal.edit()
                                        ID_duedtecalEditer.putString("DueDatesCalender", jresult3.get("DueDatesCalender") as String)
                                        ID_duedtecalEditer.commit()

                                        val ID_duedtelistforupcom= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF203, 0)
                                        val ID_duedtelistforupcomEditer = ID_duedtelistforupcom.edit()
                                        ID_duedtelistforupcomEditer.putString("Duedatelistforupcomingtwoweeks.", jresult3.get("Duedatelistforupcomingtwoweeks.") as String)
                                        ID_duedtelistforupcomEditer.commit()

                                        val ID_duedate= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF204, 0)
                                        val ID_duedateEditer = ID_duedate.edit()
                                        ID_duedateEditer.putString("Duedate", jresult3.get("Duedate") as String)
                                        ID_duedateEditer.commit()

                                        val ID_doyouwntdlte= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF205, 0)
                                        val ID_doyouwntdlteEditer = ID_doyouwntdlte.edit()
                                        ID_doyouwntdlteEditer.putString("DoYouWanttoDeleteThisAccountAndRegisterWithAnotherAccount?", jresult3.get("DoYouWanttoDeleteThisAccountAndRegisterWithAnotherAccount?") as String)
                                        ID_doyouwntdlteEditer.commit()

                                        val ID_ys= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF206, 0)
                                        val ID_ysEditer = ID_ys.edit()
                                        ID_ysEditer.putString("Yes", jresult3.get("Yes") as String)
                                        ID_ysEditer.commit()

                                        val ID_no= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF207, 0)
                                        val ID_noEditer = ID_no.edit()
                                        ID_noEditer.putString("No", jresult3.get("No") as String)
                                        ID_noEditer.commit()

                                        val ID_doyouwquit= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF208, 0)
                                        val ID_doyouwquitEditer = ID_doyouwquit.edit()
                                        ID_doyouwquitEditer.putString("DoYouWantToQuit?", jresult3.get("DoYouWantToQuit?") as String)
                                        ID_doyouwquitEditer.commit()

                                        val ID_listasondate= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF209, 0)
                                        val ID_listasondateEditer = ID_listasondate.edit()
                                        ID_listasondateEditer.putString("ListasonDate", jresult3.get("ListasonDate-Listason(now)") as String)
                                        ID_listasondateEditer.commit()

                                        val ID_bal= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF210, 0)
                                        val ID_balEditer = ID_bal.edit()
                                        ID_balEditer.putString("Balance", jresult3.get("Balance") as String)
                                        ID_balEditer.commit()

                                        val ID_min= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF211, 0)
                                        val ID_minEditer = ID_min.edit()
                                        ID_minEditer.putString("MINISTATEMENT", jresult3.get("MINISTATEMENT") as String)
                                        ID_minEditer.commit()

                                        val ID_acntstat= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF212, 0)
                                        val ID_acntstatEditer = ID_acntstat.edit()
                                        ID_acntstatEditer.putString("ACCOUNTSTATEMENT", jresult3.get("ACCOUNTSTATEMENT") as String)
                                        ID_acntstatEditer.commit()

                                        val ID_moreopt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF213, 0)
                                        val ID_moreoptEditer = ID_moreopt.edit()
                                        ID_moreoptEditer.putString("MOREOPTIONS", jresult3.get("MOREOPTIONS") as String)
                                        ID_moreoptEditer.commit()

                                        val ID_acntsummary= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF214, 0)
                                        val ID_acntsummaryEditer = ID_acntsummary.edit()
                                        ID_acntsummaryEditer.putString("AccountSummary", jresult3.get("AccountSummary") as String)
                                        ID_acntsummaryEditer.commit()

                                        val ID_standins= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF215, 0)
                                        val ID_standinsEditer = ID_standins.edit()
                                        ID_standinsEditer.putString("StandingInstruction", jresult3.get("StandingInstruction") as String)
                                        ID_standinsEditer.commit()

                                        val ID_notce= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF216, 0)
                                        val ID_notceEditer = ID_notce.edit()
                                        ID_notceEditer.putString("Notice", jresult3.get("Notice") as String)
                                        ID_notceEditer.commit()

                                        val ID_ListingDataforpast= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF217, 0)
                                        val ListingDataforpastEditer = ID_ListingDataforpast.edit()
                                        ListingDataforpastEditer.putString("ListingDataforpast", jresult3.get("ListingDataforpast .") as String)
                                        ListingDataforpastEditer.commit()


                                        val ID_days= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF218, 0)
                                        val daysEditer = ID_days.edit()
                                        daysEditer.putString("days", jresult3.get("days") as String)
                                        daysEditer.commit()



                                        val ID_youcanchangeitfromsettings= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF219, 0)
                                        val youcanchangeitfromsettingsEditer = ID_youcanchangeitfromsettings.edit()
                                        youcanchangeitfromsettingsEditer.putString("youcanchangeitfromsettings", jresult3.get("youcanchangeitfromsettings") as String)
                                        youcanchangeitfromsettingsEditer.commit()

                                        val ID_Clear= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF220, 0)
                                        val ClearEditer = ID_Clear.edit()
                                        ClearEditer.putString("Clear", jresult3.get("Clear") as String)
                                        ClearEditer.commit()



                                        val ID_LoanPeriod= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF221, 0)
                                        val LoanPeriodEditer = ID_LoanPeriod.edit()
                                        LoanPeriodEditer.putString("LoanPeriod", jresult3.get("LoanPeriod") as String)
                                        LoanPeriodEditer.commit()

                                        val ID_Weight= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF222, 0)
                                        val WeightEditer = ID_Weight.edit()
                                        WeightEditer.putString("Weight", jresult3.get("Weight") as String)
                                        WeightEditer.commit()

                                        val ID_EnterAmount= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF223, 0)
                                        val EnterAmountEditer = ID_EnterAmount.edit()
                                        EnterAmountEditer.putString("EnterAmount", jresult3.get("EnterAmount") as String)
                                        EnterAmountEditer.commit()

                                        val ID_SelectBranch= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF224, 0)
                                        val SelectBranchEditer = ID_SelectBranch.edit()
                                        SelectBranchEditer.putString("SelectBranch", jresult3.get("SelectBranch") as String)
                                        SelectBranchEditer.commit()

                                        val ID_Pleaseselectpayingtoaccountnumber= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF225, 0)
                                        val PleaseselectpayingtoaccountnumberEditer = ID_Pleaseselectpayingtoaccountnumber.edit()
                                        PleaseselectpayingtoaccountnumberEditer.putString("Pleaseselectpayingtoaccountnumber", jresult3.get("Pleaseselectpayingtoaccountnumber") as String)
                                        PleaseselectpayingtoaccountnumberEditer.commit()

                                        val ID_PayableAmount= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF226, 0)
                                        val PayableAmountEditer = ID_PayableAmount.edit()
                                        PayableAmountEditer.putString("PayableAmount", jresult3.get("PayableAmount") as String)
                                        PayableAmountEditer.commit()


                                        val ID_Atleast3digitsarerequired= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF227, 0)
                                        val Atleast3digitsarerequiredEditer = ID_Atleast3digitsarerequired.edit()
                                        Atleast3digitsarerequiredEditer.putString("Atleast3digitsarerequired", jresult3.get("Atleast3digitsarerequired.") as String)
                                        Atleast3digitsarerequiredEditer.commit()

                                        val ID_Atleast6digitsarerequired= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF228, 0)
                                        val Atleast6digitsarerequiredEditer = ID_Atleast6digitsarerequired.edit()
                                        Atleast6digitsarerequiredEditer.putString("Atleast6digitsarerequired", jresult3.get("Atleast6digitsarerequired.") as String)
                                        Atleast6digitsarerequiredEditer.commit()

                                        val ID_PleaseEnterBeneficiaryName= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF229, 0)
                                        val PleaseEnterBeneficiaryNameEditer = ID_PleaseEnterBeneficiaryName.edit()
                                        PleaseEnterBeneficiaryNameEditer.putString("PleaseEnterBeneficiaryName", jresult3.get("PleaseEnterBeneficiaryName") as String)
                                        PleaseEnterBeneficiaryNameEditer.commit()

                                        val ID_PleaseEnterValidBeneficiaryAccountNumber= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF230, 0)
                                        val PleaseEnterValidBeneficiaryAccountNumberEditer = ID_PleaseEnterValidBeneficiaryAccountNumber.edit()
                                        PleaseEnterValidBeneficiaryAccountNumberEditer.putString("PleaseEnterValidBeneficiaryAccountNumber", jresult3.get("PleaseEnterValidBeneficiaryAccountNumber") as String)
                                        PleaseEnterValidBeneficiaryAccountNumberEditer.commit()




                                        val ID_PleaseentervalidConfirmBeneficiaryaccountnumber= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF231, 0)
                                        val PleaseentervalidConfirmBeneficiaryaccountnumberEditer = ID_PleaseentervalidConfirmBeneficiaryaccountnumber.edit()
                                        PleaseentervalidConfirmBeneficiaryaccountnumberEditer.putString("PleaseentervalidConfirmBeneficiaryaccountnumber", jresult3.get("PleaseentervalidConfirmBeneficiaryaccountnumber") as String)
                                        PleaseentervalidConfirmBeneficiaryaccountnumberEditer.commit()

                                        val ID_SelectavalidAccountNumber= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF232, 0)
                                        val SelectavalidAccountNumberEditer = ID_SelectavalidAccountNumber.edit()
                                        SelectavalidAccountNumberEditer.putString("SelectavalidAccountNumber", jresult3.get("SelectavalidAccountNumber") as String)
                                        SelectavalidAccountNumberEditer.commit()

                                        val ID_PleaseEnterYourFeedback= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF233, 0)
                                        val PleaseEnterYourFeedbackEditer = ID_PleaseEnterYourFeedback.edit()
                                        PleaseEnterYourFeedbackEditer.putString("PleaseEnterYourFeedback", jresult3.get("PleaseEnterYourFeedback") as String)
                                        PleaseEnterYourFeedbackEditer.commit()

                                        val ID_Giveusacall= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF234, 0)
                                        val GiveusacallEditer = ID_Giveusacall.edit()
                                        GiveusacallEditer.putString("Giveusacall", jresult3.get("Giveusacall") as String)
                                        GiveusacallEditer.commit()

                                        val ID_Sendusamessage= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF235, 0)
                                        val SendusamessageEditer = ID_Sendusamessage.edit()
                                        SendusamessageEditer.putString("Sendusamessage", jresult3.get("Sendusamessage") as String)
                                        SendusamessageEditer.commit()

                                        val ID_Visitourlocation= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF236, 0)
                                        val VisitourlocationEditer = ID_Visitourlocation.edit()
                                        VisitourlocationEditer.putString("Visitourlocation", jresult3.get("Visitourlocation") as String)
                                        VisitourlocationEditer.commit()

                                        val ID_Aboutustext= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF237, 0)
                                        val AboutustextEditer = ID_Aboutustext.edit()
                                        AboutustextEditer.putString("Aboutustext", jresult3.get("Aboutustext") as String)
                                        AboutustextEditer.commit()

                                        val ID_Privacypolicytext= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF238, 0)
                                        val PrivacypolicytextEditer = ID_Privacypolicytext.edit()
                                        PrivacypolicytextEditer.putString("Privacypolicytext", jresult3.get("Privacypolicytext") as String)
                                        PrivacypolicytextEditer.commit()

                                        val ID_TermsandConditionstext= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF239, 0)
                                        val TermsandConditionstextEditer = ID_TermsandConditionstext.edit()
                                        TermsandConditionstextEditer.putString("TermsandConditionstext", jresult3.get("TermsandConditionstext") as String)
                                        TermsandConditionstextEditer.commit()

                                        val ID_Apply= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF240, 0)
                                        val ApplyEditer = ID_Apply.edit()
                                        ApplyEditer.putString("Apply", jresult3.get("Apply") as String)
                                        ApplyEditer.commit()

                                    val ID_acnttyp= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF241, 0)
                                    val ID_acnttypEditer = ID_acnttyp.edit()
                                    ID_acnttypEditer.putString("AccountType", jresult3.get("AccountType") as String)
                                    ID_acnttypEditer.commit()


                                    val ID_unclramt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF242, 0)
                                    val ID_unclramtEditer = ID_unclramt.edit()
                                    ID_unclramtEditer.putString("UnclearAmount", jresult3.get("UnclearAmount") as String)
                                    ID_unclramtEditer.commit()


                                    val ID_transupdte= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF244, 0)
                                    val ID_transupdteEditer = ID_transupdte.edit()
                                    ID_transupdteEditer.putString("TransactionUpdate(Days)", jresult3.get("TransactionUpdate(Days)") as String)
                                    ID_transupdteEditer.commit()

                                    val ID_updteintrvl= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF245, 0)
                                    val ID_updteintrvlEditer = ID_updteintrvl.edit()
                                    ID_updteintrvlEditer.putString("UpdateInterval", jresult3.get("UpdateInterval") as String)
                                    ID_updteintrvlEditer.commit()

                                    val ID_defltacc= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF246, 0)
                                    val ID_defltaccEditer = ID_defltacc.edit()
                                    ID_defltaccEditer.putString("DefaultAccount", jresult3.get("DefaultAccount") as String)
                                    ID_defltaccEditer.commit()

                                        val ID_giveuscall= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF247, 0)
                                        val ID_giveuscallEditer = ID_giveuscall.edit()
                                        ID_giveuscallEditer.putString("Giveusacall", jresult3.get("Giveusacall") as String)
                                        ID_giveuscallEditer.commit()

                                        val ID_sendus= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF248, 0)
                                        val ID_sendusEditer = ID_sendus.edit()
                                        ID_sendusEditer.putString("Sendusamessage", jresult3.get("Sendusamessage") as String)
                                        ID_sendusEditer.commit()

                                        val ID_visitloc= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF249, 0)
                                        val ID_visitlocEditer = ID_visitloc.edit()
                                        ID_visitlocEditer.putString("Visitourlocation", jresult3.get("Visitourlocation") as String)
                                        ID_visitlocEditer.commit()

                                        val ID_submt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF250, 0)
                                        val ID_submtEditer = ID_submt.edit()
                                        ID_submtEditer.putString("Submit", jresult3.get("Submit") as String)
                                        ID_submtEditer.commit()

                                        val ID_ownaccfndtransfr= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF251, 0)
                                        val ID_ownaccfndtransfrEditer = ID_ownaccfndtransfr.edit()
                                        ID_ownaccfndtransfrEditer.putString("OwnAccountFundTransfer", jresult3.get("OwnAccountFundTransfer") as String)
                                        ID_ownaccfndtransfrEditer.commit()

                                        val ID_othraccfndtransfr= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF252, 0)
                                        val ID_othraccfndtransfrEditer = ID_othraccfndtransfr.edit()
                                        ID_othraccfndtransfrEditer.putString("OtherAccountFundTransfer", jresult3.get("OtherAccountFundTransfer") as String)
                                        ID_othraccfndtransfrEditer.commit()

                                        val ID_transferupto= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF253, 0)
                                        val ID_transferuptoEditer = ID_transferupto.edit()
                                        ID_transferuptoEditer.putString("Transfer upto", jresult3.get("Transfer upto") as String)
                                        ID_transferuptoEditer.commit()

                                        val ID_instantly= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF254, 0)
                                        val ID_instantlyEditer = ID_instantly.edit()
                                        ID_instantlyEditer.putString("Instantly", jresult3.get("Instantly") as String)
                                        ID_instantlyEditer.commit()

                                        val ID_weight= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF255, 0)
                                        val ID_weightEditer = ID_weight.edit()
                                        ID_weightEditer.putString("Weight", jresult3.get("Weight") as String)
                                        ID_weightEditer.commit()

                                        val ID_emitype= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF256, 0)
                                        val ID_emitypeEditer = ID_emitype.edit()
                                        ID_emitypeEditer.putString("EMITYPE", jresult3.get("EMITYPE") as String)
                                        ID_emitypeEditer.commit()

                                        val ID_ENTRWEIGHT= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF257, 0)
                                        val ID_ENTRWEIGHTEditer = ID_ENTRWEIGHT.edit()
                                        ID_ENTRWEIGHTEditer.putString("Enter Weight", jresult3.get("EnterWeight") as String)
                                        ID_ENTRWEIGHTEditer.commit()

                                        val ID_Plsentrweght= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF258, 0)
                                        val ID_PlsentrweghtEditer = ID_Plsentrweght.edit()
                                        ID_PlsentrweghtEditer.putString("PleaseEnterWeight", jresult3.get("PleaseEnterWeight") as String)
                                        ID_PlsentrweghtEditer.commit()



                                        val ID_entrprincamt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF260, 0)
                                        val ID_entrprincamtEditer = ID_entrprincamt.edit()
                                        ID_entrprincamtEditer.putString("EnterPrincipalAmount", jresult3.get("EnterPrincipalAmount") as String)
                                        ID_entrprincamtEditer.commit()

                                        val ID_entrmnth1= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF261, 0)
                                        val ID_entrmnth1Editer = ID_entrmnth1.edit()
                                        ID_entrmnth1Editer.putString("EnterMonth", jresult3.get("EnterMonth") as String)
                                        ID_entrmnth1Editer.commit()

                                        val ID_entrintrstrate= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF262, 0)
                                        val ID_entrintrstrateEditer = ID_entrintrstrate.edit()
                                        ID_entrintrstrateEditer.putString("EnterInterestRate", jresult3.get("EnterInterestRate") as String)
                                        ID_entrintrstrateEditer.commit()

                                        val ID_loanpurpse1= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF263, 0)
                                        val ID_loanpurpse1Editer = ID_loanpurpse1.edit()
                                        ID_loanpurpse1Editer.putString("LoanPurpose", jresult3.get("LoanPurpose") as String)
                                        ID_loanpurpse1Editer.commit()

                                        val ID_loantyp= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF264, 0)
                                        val ID_loantypEditer = ID_loantyp.edit()
                                        ID_loantypEditer.putString("LoanType", jresult3.get("LoanType") as String)
                                        ID_loantypEditer.commit()


                                        val ID_applctnamt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF266, 0)
                                        val ID_applctnamtEditer = ID_applctnamt.edit()
                                        ID_applctnamtEditer.putString("ApplicationAmount", jresult3.get("ApplicationAmount") as String)
                                        ID_applctnamtEditer.commit()

                                        val ID_applctnno= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF267, 0)
                                        val ID_applctnnoEditer = ID_applctnno.edit()
                                        ID_applctnnoEditer.putString("ApplicationNumber", jresult3.get("ApplicationNumber") as String)
                                        ID_applctnnoEditer.commit()

                                        val ID_cusname= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF268, 0)
                                        val ID_cusnameEditer = ID_cusname.edit()
                                        ID_cusnameEditer.putString("Customer Name", jresult3.get("Customer Name") as String)
                                        ID_cusnameEditer.commit()

                                        val ID_cusid= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF269, 0)
                                        val ID_cusidEditer = ID_cusid.edit()
                                        ID_cusidEditer.putString("Customer Id", jresult3.get("Customer Id") as String)
                                        ID_cusidEditer.commit()

                                        val ID_elctrnc= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF270, 0)
                                        val ID_elctrncEditer = ID_elctrnc.edit()
                                        ID_elctrncEditer.putString("Electronicuseonly", jresult3.get("Electronicuseonly") as String)
                                        ID_elctrncEditer.commit()


                                        val ID_point1= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF271, 0)
                                        val ID_point1Editer = ID_point1.edit()
                                        ID_point1Editer.putString("Streamlinetransactions", jresult3.get("Streamlinetransactions") as String)
                                        ID_point1Editer.commit()


                                        val ID_point2= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF272, 0)
                                        val ID_point2Editer = ID_point2.edit()
                                        ID_point2Editer.putString("Enableasinglepointofcontactforcreditanddebit", jresult3.get("Enableasinglepointofcontactforcreditanddebit") as String)
                                        ID_point2Editer.commit()


                                        val ID_pont3= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF273, 0)
                                        val ID_pont3Editer = ID_pont3.edit()
                                        ID_pont3Editer.putString("Strengthenyourloanportfolio", jresult3.get("Strengthenyourloanportfolio") as String)
                                        ID_pont3Editer.commit()


                                        val ID_point4= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF274, 0)
                                        val ID_point4Editer = ID_point4.edit()
                                        ID_point4Editer.putString("Eliminatethelongqueues", jresult3.get("Eliminatethelongqueues") as String)
                                        ID_point4Editer.commit()

                                        val ID_validifsc= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF275, 0)
                                        val ID_validifscEditer = ID_validifsc.edit()
                                        ID_validifscEditer.putString("PleaseEnterValidIFSC", jresult3.get("PleaseEnterValidIFSC") as String)
                                        ID_validifscEditer.commit()

                                        val ID_benfmatch= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF276, 0)
                                        val ID_benfmatchEditer = ID_benfmatch.edit()
                                        ID_benfmatchEditer.putString("BeneficiaryAccountNumberdidntmatch", jresult3.get("BeneficiaryAccountNumberdidntmatch") as String)
                                        ID_benfmatchEditer.commit()

                                        val ID_consname= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF277, 0)
                                        val ID_consnameEditer = ID_consname.edit()
                                        ID_consnameEditer.putString("ConsumerName", jresult3.get("ConsumerName") as String)
                                        ID_consnameEditer.commit()

                                        val ID_consnum= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF278, 0)
                                        val ID_consnumEditer = ID_consnum.edit()
                                        ID_consnumEditer.putString("ConsumerNumber", jresult3.get("ConsumerNumber") as String)
                                        ID_consnumEditer.commit()

                                        val ID_sectnme= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF279, 0)
                                        val ID_sectnmeEditer = ID_sectnme.edit()
                                        ID_sectnmeEditer.putString("SectionName", jresult3.get("SectionName") as String)
                                        ID_sectnmeEditer.commit()

                                        val ID_bill= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF280, 0)
                                        val ID_billEditer = ID_bill.edit()
                                        ID_billEditer.putString("BillNumber", jresult3.get("BillNumber") as String)
                                        ID_billEditer.commit()




                                        val ID_plsentsec= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF282, 0)
                                        val ID_plsentsecEditer = ID_plsentsec.edit()
                                        ID_plsentsecEditer.putString("PleaseEnterSectionName", jresult3.get("PleaseEnterSectionName") as String)
                                        ID_plsentsecEditer.commit()

                                        val ID_plsentbill= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF283, 0)
                                        val ID_plsentbillEditer = ID_plsentbill.edit()
                                        ID_plsentbillEditer.putString("PleaseEnterBillnumber", jresult3.get("PleaseEnterBillnumber") as String)
                                        ID_plsentbillEditer.commit()




                                        val ID_slctn= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF288, 0)
                                        val ID_slctnEditer = ID_slctn.edit()
                                        ID_slctnEditer.putString("Selection", jresult3.get("Selection") as String)
                                        ID_slctnEditer.commit()

                                        val ID_sctn= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF289, 0)
                                        val ID_sctnEditer = ID_sctn.edit()
                                        ID_sctnEditer.putString("Section", jresult3.get("Section") as String)
                                        ID_sctnEditer.commit()

                                        val ID_nottransf= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF290, 0)
                                        val ID_nottransfEditer = ID_nottransf.edit()
                                        ID_nottransfEditer.putString("NotTransferable", jresult3.get("NotTransferable") as String)
                                        ID_nottransfEditer.commit()

                                        val ID_keepcrd= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF291, 0)
                                        val ID_keepcrdEditer = ID_keepcrd.edit()
                                        ID_keepcrdEditer.putString("PleaseKeepYourCardConfidential", jresult3.get("PleaseKeepYourCardConfidential") as String)
                                        ID_keepcrdEditer.commit()

                                        val ID_ph= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF292, 0)
                                        val ID_phEditer = ID_ph.edit()
                                        ID_phEditer.putString("Phone", jresult3.get("Phone") as String)
                                        ID_phEditer.commit()











                                        val ID_applctndt= this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF265, 0)
                                        val ID_applctndtEditer = ID_applctndt.edit()
                                        ID_applctndtEditer.putString("ApplicationDate", jresult3.get("ApplicationDate") as String)
                                        ID_applctndtEditer.commit()







                                        /*  if(WelcomeSP.equals("")||FasterSP.equals("")||SigninSP.equals("")||RegisterSP.equals(""))
                                          {

                                          }
                                          else
                                          {*/


                                    }catch (e: Exception){
                                        Log.e("TAG","Exception   2101   "+e.toString())
                                    }



                                    // }



//        Glide.with(this).load(R.drawable.welcomegif).into(imwelcome)




                                    // mContext.startActivity(Intent(mContext, WelcomeActivity::class.java))


                                } else {
                                    val builder = AlertDialog.Builder(
                                            applicationContext,
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
                                        applicationContext,
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
                                    applicationContext,
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
                    val builder = AlertDialog.Builder(applicationContext, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(applicationContext, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }
}

