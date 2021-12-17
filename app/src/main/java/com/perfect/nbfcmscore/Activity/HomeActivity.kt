
package com.perfect.nbfcmscore.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.*
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.R
import me.relex.circleindicator.CircleIndicator
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import android.R.id
import androidx.recyclerview.widget.GridLayoutManager


class HomeActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        ItemClickListener {

    val TAG: String? = "HomeActivity"
    private var progressDialog: ProgressDialog? = null
    var llloanstatus: LinearLayout? = null
    var llloanapplication: LinearLayout? = null
    var lldashboard: LinearLayout? = null
    var llprdctdetail: LinearLayout? = null
    var llgoldslab: LinearLayout? = null
    var llmyaccounts: LinearLayout? = null
    var lvNavMenu: ListView? = null
    var drawer: DrawerLayout? = null
    var imgMenu: ImageView? = null
    var ll_branschDetails : LinearLayout?=null
    var ll_prepaid : LinearLayout?=null
    var ll_postpaid : LinearLayout?=null
    var ll_landline : LinearLayout?=null
    var ll_kseb : LinearLayout?=null
    var ll_rechargehistory : LinearLayout?=null
    var ll_holidaylist : LinearLayout?=null
    var llownbank : LinearLayout?=null
    var llDeposit:LinearLayout?=null
    var ll_dth : LinearLayout?=null
    var ll_datacard : LinearLayout?=null
    var llEmi : LinearLayout?=null
    var llnotif : LinearLayout?=null
    var llpassbook: LinearLayout? = null
    var llduereminder: LinearLayout? = null
    var ll_virtualcard: LinearLayout? = null
    var ll_otherbank: LinearLayout? = null
    var llquickbalance: LinearLayout? = null
    var llstatement: LinearLayout? = null
    var llquickpay: LinearLayout? = null
    var llprofile: LinearLayout? = null
    var llexecutive: LinearLayout? = null
    var llenquiry: LinearLayout? = null
    var ll_fundtransfer: LinearLayout? = null
    var ll_recharge: LinearLayout? = null

    var tv_def_account: TextView? = null
    var tv_def_availablebal: TextView? = null
    var tv_lastlogin: TextView? = null
    var txtv_availbal: TextView? = null

    var txtv_acntdetail: TextView? = null
    var txtv_fundtrans: TextView? = null
    var txtv_rechbill: TextView? = null
    var txtv_reprts: TextView? = null
    var txtv_tools: TextView? = null

    var improfile: ImageView? = null
    var imlanguage: ImageView? = null
    var imquit: ImageView? = null
    var imlogout: ImageView? = null
    var im_applogo: ImageView? = null
    var tv_header: TextView? = null
    var tvuser: TextView? = null
    var tv_mobile: TextView? = null
    var txtv_myacc: TextView? = null
    var txtv_pasbk: TextView? = null
    var txtv_quickbal: TextView? = null
    var txtvstatmnt: TextView? = null
    var txtv_dueremndr: TextView? = null
    var txtvnotif: TextView? = null
    var txtv_ownbnk: TextView? = null
    var txtv_othrbnk: TextView? = null
    var txtv_quickpay: TextView? = null
    var txtv_prepaid: TextView? = null
    var txtv_pospaid: TextView? = null
    var txtv_landline: TextView? = null
    var txtv_dth: TextView? = null
    var txtv_datacrd: TextView? = null
    var txtv_Kseb: TextView? = null
    var txtv_history: TextView? = null
    var txtv_dashbrd: TextView? = null
    var txtv_virtual: TextView? = null
    var txtv_branch: TextView? = null
    var txtv_loanaplctn: TextView? = null
    var txtv_loanstats: TextView? = null
    var txtv_prdctdetail: TextView? = null
    var tv_viewall: TextView? = null
    var txtv_emi: TextView? = null
    var txtv_deposit: TextView? = null
    var txtv_goldloan: TextView? = null
    var txtv_enqry: TextView? = null
    var txtv_holidy: TextView? = null
    var txtv_exectve: TextView? = null
    var txtv_accnt: TextView? = null


    private var mPager: ViewPager? = null
    private var indicator: CircleIndicator? = null
    private var currentPage = 0
    // private val XMEN = arrayOf<String>
    // private val XMEN = arrayOf<String>(R.drawable.ban1, R.drawable.ban2, R.drawable.ban3, R.drawable.ban4)
    public val XMENArray = ArrayList<String>()
    var XMEN = intArrayOf(0)
    var jArrayAccount: JSONArray? = null
    var jArrayLang: JSONArray? = null

    var rv_Languagelist: RecyclerView? = null
    var alertDialogLang: AlertDialog? = null

    var rvfundTransfer: RecyclerView? = null
    var jArrayMenuFund: JSONArray? = null

    var rvRecharge: RecyclerView? = null
    var jArrayMenuRech: JSONArray? = null

    private var jresult: JSONArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_main)

        setInitialise()
        setRegister()
        setHomeNavMenu()
        init()
        versioncheck()

        setdefaultAccountDetails()
        createHomeMenuFund()
        createHomeMenuRecharge()

        val ID_MyaccSP = applicationContext.getSharedPreferences(Config.SHARED_PREF50, 0)
        val ID_PassbkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF51, 0)
        val ID_QuickbalSP = applicationContext.getSharedPreferences(Config.SHARED_PREF52, 0)
        val ID_DueremindSP = applicationContext.getSharedPreferences(Config.SHARED_PREF53, 0)
        val ID_StatmntSP = applicationContext.getSharedPreferences(Config.SHARED_PREF59, 0)
        val ID_NotifSP = applicationContext.getSharedPreferences(Config.SHARED_PREF62, 0)
        val ID_OwnBankSP = applicationContext.getSharedPreferences(Config.SHARED_PREF63, 0)
        val ID_OtherBankSP = applicationContext.getSharedPreferences(Config.SHARED_PREF64, 0)
        val ID_QuickpaySP = applicationContext.getSharedPreferences(Config.SHARED_PREF65, 0)

        val ID_Prepaid = applicationContext.getSharedPreferences(Config.SHARED_PREF66, 0)
        val ID_Postpaid = applicationContext.getSharedPreferences(Config.SHARED_PREF67, 0)
        val ID_Landline = applicationContext.getSharedPreferences(Config.SHARED_PREF68, 0)
        val ID_DTH = applicationContext.getSharedPreferences(Config.SHARED_PREF69, 0)
        val ID_Datacrdpay = applicationContext.getSharedPreferences(Config.SHARED_PREF70, 0)
        val ID_KSEB = applicationContext.getSharedPreferences(Config.SHARED_PREF71, 0)
        val ID_Histry = applicationContext.getSharedPreferences(Config.SHARED_PREF72, 0)

        val ID_Dashbrd = applicationContext.getSharedPreferences(Config.SHARED_PREF73, 0)
        val ID_Virtualcrd = applicationContext.getSharedPreferences(Config.SHARED_PREF74, 0)
        val ID_Branch = applicationContext.getSharedPreferences(Config.SHARED_PREF75, 0)
        val ID_Loanapplictn = applicationContext.getSharedPreferences(Config.SHARED_PREF76, 0)
        val ID_Loanstatus = applicationContext.getSharedPreferences(Config.SHARED_PREF77, 0)
        val ID_PrdctDetail = applicationContext.getSharedPreferences(Config.SHARED_PREF78, 0)

        val ID_Emi = applicationContext.getSharedPreferences(Config.SHARED_PREF79, 0)
        val ID_Deposit = applicationContext.getSharedPreferences(Config.SHARED_PREF80, 0)
        val ID_Goldloan = applicationContext.getSharedPreferences(Config.SHARED_PREF81, 0)
        val ID_Enqry = applicationContext.getSharedPreferences(Config.SHARED_PREF82, 0)
        val ID_Holidy = applicationContext.getSharedPreferences(Config.SHARED_PREF83, 0)
        val ID_Executve = applicationContext.getSharedPreferences(Config.SHARED_PREF84, 0)

        val ID_Accnt = applicationContext.getSharedPreferences(Config.SHARED_PREF117, 0)
        val ID_Viewall = applicationContext.getSharedPreferences(Config.SHARED_PREF118, 0)
        val ID_Availbal = applicationContext.getSharedPreferences(Config.SHARED_PREF119, 0)
        //   val ID_Lastlog = applicationContext.getSharedPreferences(Config.SHARED_PREF120, 0)

        txtv_myacc!!.setText(ID_MyaccSP.getString("Myaccounts", null))
        txtv_pasbk!!.setText(ID_PassbkSP.getString("passbook", null))
        txtv_quickbal!!.setText(ID_QuickbalSP.getString("quickbalance", null))
        txtv_dueremndr!!.setText(ID_DueremindSP.getString("duereminder", null))
        txtvstatmnt!!.setText(ID_StatmntSP.getString("statement", null))
        txtvnotif!!.setText(ID_NotifSP.getString("NotificationandMessages", null))
        txtv_ownbnk!!.setText(ID_OwnBankSP.getString("OwnBank", null))
        txtv_othrbnk!!.setText(ID_OtherBankSP.getString("OtherBank", null))
        txtv_quickpay!!.setText(ID_QuickpaySP.getString("QuickPay", null))

        txtv_prepaid!!.setText(ID_Prepaid.getString("PrepaidMobile", null))
        txtv_pospaid!!.setText(ID_Postpaid.getString("PostpaidMobile", null))
        txtv_landline!!.setText(ID_Landline.getString("Landline", null))
        txtv_dth!!.setText(ID_DTH.getString("DTH", null))
        txtv_datacrd!!.setText(ID_Datacrdpay.getString("DataCard", null))
        txtv_Kseb!!.setText(ID_KSEB.getString("KSEB", null))
        txtv_history!!.setText(ID_Histry.getString("History", null))

        txtv_dashbrd!!.setText(ID_Dashbrd.getString("Dashboard", null))
        txtv_virtual!!.setText(ID_Virtualcrd.getString("VirtualCard", null))
        txtv_branch!!.setText(ID_Branch.getString("BranchDetails", null))
        txtv_loanaplctn!!.setText(ID_Loanapplictn.getString("LoanApplication", null))
        txtv_loanstats!!.setText(ID_Loanstatus.getString("LoanStatus", null))
        txtv_prdctdetail!!.setText(ID_PrdctDetail.getString("ProductDetails", null))

        txtv_emi!!.setText(ID_Emi.getString("EMICalculator", null))
        txtv_deposit!!.setText(ID_Deposit.getString("DepositCalculator", null))
        txtv_goldloan!!.setText(ID_Goldloan.getString("GoldLoanEligibileCalculator", null))
        txtv_enqry!!.setText(ID_Enqry.getString("Enquires", null))
        txtv_holidy!!.setText(ID_Holidy.getString("HolidayList", null))
        txtv_exectve!!.setText(ID_Executve.getString("ExecutiveCallBack", null))

        txtv_accnt!!.setText(ID_Accnt.getString("Account", null))
        tv_viewall!!.setText(ID_Viewall.getString("ViewAllAccounts", null))
        txtv_availbal!!.setText(ID_Availbal.getString("AvailableBalance", null))
        val LastLoginTimeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF29, 0)
        val LastLogin = applicationContext.getSharedPreferences(Config.SHARED_PREF120, 0)
        val s =LastLogin.getString("LastLogin", null)+"\n"+LastLoginTimeSP.getString("LastLoginTime", null)
        Log.i("Values",s)
        tv_lastlogin!!.setText(LastLogin.getString("LastLogin", null) + " : " + LastLoginTimeSP.getString("LastLoginTime", ""))

        val ID_Accntdetail = applicationContext.getSharedPreferences(Config.SHARED_PREF121, 0)
        val ID_Fundtrans = applicationContext.getSharedPreferences(Config.SHARED_PREF122, 0)
        val ID_Rebill = applicationContext.getSharedPreferences(Config.SHARED_PREF123, 0)
        val ID_Reprts = applicationContext.getSharedPreferences(Config.SHARED_PREF124, 0)
        val ID_Tools = applicationContext.getSharedPreferences(Config.SHARED_PREF125, 0)

        txtv_acntdetail!!.setText(ID_Accntdetail.getString("AccountDetails", null))
        txtv_fundtrans!!.setText(ID_Fundtrans.getString("FundTransfer", null))
        txtv_rechbill!!.setText(ID_Rebill.getString("RechargeBills", null))
        txtv_reprts!!.setText(ID_Reprts.getString("ReportsOtherServices", null))
        txtv_tools!!.setText(ID_Tools.getString("ToolsSettings", null))



        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12, 0)
        try {
            val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString(
                    "AppIconImageCode",
                    null
            )
            Log.e("TAG", "imagepath  116   " + imagepath)
            //PicassoTrustAll.getInstance(this)!!.load(imagepath).error(null).into(im_applogo)
            PicassoTrustAll.getInstance(this@HomeActivity)!!.load(imagepath).error(android.R.color.transparent).into(im_applogo!!)

        }catch (e: Exception) {
            e.printStackTrace()}
        tv_header!!.setText(ProductNameSP.getString("ProductName", null))

        val CustomerNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF3, 0)
        tvuser!!.setText(CustomerNameSP.getString("CustomerName", null))
        val CusMobileSP = applicationContext.getSharedPreferences(Config.SHARED_PREF2, 0)
        tv_mobile!!.setText(CusMobileSP.getString("CusMobile", null))




    }



    private fun createHomeMenuFund() {

        val ID_OwnBankSP = applicationContext.getSharedPreferences(Config.SHARED_PREF63, 0)
        val ID_OtherBankSP = applicationContext.getSharedPreferences(Config.SHARED_PREF64, 0)
        val ID_QuickpaySP = applicationContext.getSharedPreferences(Config.SHARED_PREF65, 0)

        val LicenceImpsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF293,0)
        val LicenceNeftSP = applicationContext.getSharedPreferences(Config.SHARED_PREF295,0)
        val LicenceRtgsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF298,0)
        val LicenceQuickPaySP = applicationContext.getSharedPreferences(Config.SHARED_PREF296,0)

        txtv_ownbnk!!.setText(ID_OwnBankSP.getString("OwnBank", null))
        txtv_othrbnk!!.setText(ID_OtherBankSP.getString("OtherBank", null))
        txtv_quickpay!!.setText(ID_QuickpaySP.getString("QuickPay", null))

        jArrayMenuFund = JSONArray()
        var dlObjectFund = JSONObject()
        dlObjectFund.put("FundId",0)
        dlObjectFund.put("Fundlabel",ID_OwnBankSP.getString("OwnBank", null))
        dlObjectFund.put("FundImage",R.drawable.myonwbank)
        jArrayMenuFund!!.put(dlObjectFund)
        if (LicenceImpsSP.getString("LicenceImps",null).equals("true") || LicenceNeftSP.getString("LicenceNeft",null).equals("true") || LicenceRtgsSP.getString("LicenceRtgs",null).equals("true")){
            dlObjectFund = JSONObject()
            dlObjectFund.put("FundId",1)
            dlObjectFund.put("Fundlabel",ID_OtherBankSP.getString("OtherBank", null))
            dlObjectFund.put("FundImage",R.drawable.myotherbank)
            jArrayMenuFund!!.put(dlObjectFund)
        }
        if (LicenceQuickPaySP.getString("LicenceQuickPay",null).equals("true")){
            dlObjectFund = JSONObject()
            dlObjectFund.put("FundId",2)
            dlObjectFund.put("Fundlabel",ID_QuickpaySP.getString("QuickPay", null))
            dlObjectFund.put("FundImage",R.drawable.myquickpay)
            jArrayMenuFund!!.put(dlObjectFund)

        }

//        if (!LicenceImpsSP.getString("LicenceImps",null).equals("true") && !LicenceNeftSP.getString("LicenceNeft",null).equals("true") &&
//            !LicenceRtgsSP.getString("LicenceRtgs",null).equals("true") && !LicenceQuickPaySP.getString("LicenceQuickPay",null).equals("true")){
//            ll_fundtransfer!!.visibility = View.GONE
//        }



//        val menuFund = arrayOf<String>(""+ID_OwnBankSP.getString("OwnBank", null),
//            ""+ID_OtherBankSP.getString("OtherBank", null),""+ID_QuickpaySP.getString("QuickPay", null))
//        val imageIdFund = arrayOf<Int>(R.drawable.myonwbank, R.drawable.myotherbank, R.drawable.myquickpay)
//
//
//
//        jArrayMenuFund = JSONArray()
//        try {
//            for (x in 0 until menuFund!!.size){
//                val dlObjectFund = JSONObject()
//                if(x == 0){
//                    dlObjectFund.put("FundId",x)
//                    dlObjectFund.put("Fundlabel",menuFund[x])
//                    dlObjectFund.put("FundImage",imageIdFund[x])
//                    jArrayMenuFund!!.put(dlObjectFund)
//                }
//
////                if(x == 1){
////                    dlObjectFund.put("FundId",x)
////                    dlObjectFund.put("Fundlabel",menuFund[x])
////                    dlObjectFund.put("FundImage",imageIdFund[x])
////                    jArrayMenuFund!!.put(dlObjectFund)
////                }
////
////                if(x == 2){
////                    dlObjectFund.put("FundId",x)
////                    dlObjectFund.put("Fundlabel",menuFund[x])
////                    dlObjectFund.put("FundImage",imageIdFund[x])
////                    jArrayMenuFund!!.put(dlObjectFund)
////                }
//
//            }
//
//        } catch (e: Exception) {
//            Log.e(TAG, "Exception  186   "+e.toString())
//            e.printStackTrace()
//        }

        Log.e(TAG,"jArrayMenuFund  186   "+jArrayMenuFund)


        rvfundTransfer = findViewById<View>(R.id.rvfundTransfer) as RecyclerView
        val lLayoutFund = GridLayoutManager(this@HomeActivity, 3)
        rvfundTransfer!!.setLayoutManager(lLayoutFund)
        val fund_adapter = HomeFundTransferAdapter(applicationContext!!,jArrayMenuFund!!)
        rvfundTransfer!!.adapter = fund_adapter
        fund_adapter!!.setClickListener(this@HomeActivity)


    }

    private fun createHomeMenuRecharge() {

        val ID_Prepaid = applicationContext.getSharedPreferences(Config.SHARED_PREF66, 0)
        val ID_Postpaid = applicationContext.getSharedPreferences(Config.SHARED_PREF67, 0)
        val ID_Landline = applicationContext.getSharedPreferences(Config.SHARED_PREF68, 0)
        val ID_DTH = applicationContext.getSharedPreferences(Config.SHARED_PREF69, 0)
        val ID_Datacrdpay = applicationContext.getSharedPreferences(Config.SHARED_PREF70, 0)
        val ID_KSEB = applicationContext.getSharedPreferences(Config.SHARED_PREF71, 0)
        val ID_Histry = applicationContext.getSharedPreferences(Config.SHARED_PREF72, 0)

        val LicenceKsebSP = applicationContext.getSharedPreferences(Config.SHARED_PREF294,0)
        val LicenceRechargeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF297,0)


        jArrayMenuRech = JSONArray()
        var dlObjectRech = JSONObject()
        if (LicenceRechargeSP.getString("LicenceRecharge",null).equals("true")){

            dlObjectRech.put("RechId",0)
            dlObjectRech.put("Rechlabel",ID_Prepaid.getString("PrepaidMobile", null))
            dlObjectRech.put("RechImage",R.drawable.myprepaidmobile)
            jArrayMenuRech!!.put(dlObjectRech)

            dlObjectRech = JSONObject()
            dlObjectRech.put("RechId",1)
            dlObjectRech.put("Rechlabel",ID_Postpaid.getString("PostpaidMobile", null))
            dlObjectRech.put("RechImage",R.drawable.mypostpaidmobile)
            jArrayMenuRech!!.put(dlObjectRech)

            dlObjectRech = JSONObject()
            dlObjectRech.put("RechId",2)
            dlObjectRech.put("Rechlabel",ID_Landline.getString("Landline", null))
            dlObjectRech.put("RechImage",R.drawable.mylandline)
            jArrayMenuRech!!.put(dlObjectRech)

            dlObjectRech = JSONObject()
            dlObjectRech.put("RechId",3)
            dlObjectRech.put("Rechlabel",ID_DTH.getString("DTH", null))
            dlObjectRech.put("RechImage",R.drawable.mydth)
            jArrayMenuRech!!.put(dlObjectRech)



            dlObjectRech = JSONObject()
            dlObjectRech.put("RechId",4)
            dlObjectRech.put("Rechlabel",ID_Datacrdpay.getString("DataCard", null))
            dlObjectRech.put("RechImage",R.drawable.mydatacard)
            jArrayMenuRech!!.put(dlObjectRech)


        }
        if (LicenceKsebSP.getString("LicenceKseb",null).equals("true")){
            dlObjectRech = JSONObject()
            dlObjectRech.put("RechId",5)
            dlObjectRech.put("Rechlabel",ID_KSEB.getString("KSEB", null))
            dlObjectRech.put("RechImage",R.drawable.mykseb)
            jArrayMenuRech!!.put(dlObjectRech)
        }

        if (LicenceRechargeSP.getString("LicenceRecharge",null).equals("true") || LicenceKsebSP.getString("LicenceKseb",null).equals("true")){

            dlObjectRech = JSONObject()
            dlObjectRech.put("RechId",6)
            dlObjectRech.put("Rechlabel",ID_Histry.getString("History", null))
            dlObjectRech.put("RechImage",R.drawable.myhistory)
            jArrayMenuRech!!.put(dlObjectRech)
        }

        if (!LicenceRechargeSP.getString("LicenceRecharge",null).equals("true") && !LicenceKsebSP.getString("LicenceKseb",null).equals("true")){

            ll_recharge!!.visibility = View.GONE

        }


//        val menuRech = arrayOf<String>(""+ID_Prepaid.getString("PrepaidMobile", null),
//            ""+ID_Postpaid.getString("PostpaidMobile", null),""+ID_Landline.getString("Landline", null),
//            ""+ID_DTH.getString("DTH", null),""+ID_Datacrdpay.getString("DataCard", null),
//            ""+ID_KSEB.getString("KSEB", null),""+ID_Histry.getString("History", null))
//        val imageIdRech = arrayOf<Int>(R.drawable.myprepaidmobile, R.drawable.mypostpaidmobile, R.drawable.mylandline,
//            R.drawable.mydth, R.drawable.mydatacard, R.drawable.mykseb, R.drawable.myhistory)
//
//
//        jArrayMenuRech = JSONArray()
//        try {
//            for (x in 0 until menuRech!!.size){
//                val dlObjectRech = JSONObject()
//                if(x == 0){
//                    dlObjectRech.put("RechId",x)
//                    dlObjectRech.put("Rechlabel",menuRech[x])
//                    dlObjectRech.put("RechImage",imageIdRech[x])
//                    jArrayMenuRech!!.put(dlObjectRech)
//                }
//                if(x == 1){
//                    dlObjectRech.put("RechId",x)
//                    dlObjectRech.put("Rechlabel",menuRech[x])
//                    dlObjectRech.put("RechImage",imageIdRech[x])
//                    jArrayMenuRech!!.put(dlObjectRech)
//                }
//                if(x == 2){
//                    dlObjectRech.put("RechId",x)
//                    dlObjectRech.put("Rechlabel",menuRech[x])
//                    dlObjectRech.put("RechImage",imageIdRech[x])
//                    jArrayMenuRech!!.put(dlObjectRech)
//                }
//                if(x == 3){
//                    dlObjectRech.put("RechId",x)
//                    dlObjectRech.put("Rechlabel",menuRech[x])
//                    dlObjectRech.put("RechImage",imageIdRech[x])
//                    jArrayMenuRech!!.put(dlObjectRech)
//                }
//                if(x == 4){
//                    dlObjectRech.put("RechId",x)
//                    dlObjectRech.put("Rechlabel",menuRech[x])
//                    dlObjectRech.put("RechImage",imageIdRech[x])
//                    jArrayMenuRech!!.put(dlObjectRech)
//                }
//                if(x == 5){
//                    dlObjectRech.put("RechId",x)
//                    dlObjectRech.put("Rechlabel",menuRech[x])
//                    dlObjectRech.put("RechImage",imageIdRech[x])
//                    jArrayMenuRech!!.put(dlObjectRech)
//                }
//                if(x == 6){
//                    dlObjectRech.put("RechId",x)
//                    dlObjectRech.put("Rechlabel",menuRech[x])
//                    dlObjectRech.put("RechImage",imageIdRech[x])
//                    jArrayMenuRech!!.put(dlObjectRech)
//                }
//
//
//            }
//
//        } catch (e: Exception) {
//            Log.e(TAG, "Exception  1861   "+e.toString())
//            e.printStackTrace()
//        }

        Log.e(TAG,"jArrayMenuRech  1861   "+jArrayMenuRech)


        rvRecharge = findViewById<View>(R.id.rvRecharge) as RecyclerView
        val lLayoutRech = GridLayoutManager(this@HomeActivity, 3)
        rvRecharge!!.setLayoutManager(lLayoutRech)
        val rech_adapter = HomeRechargeAdapter(applicationContext!!,jArrayMenuRech!!)
        rvRecharge!!.adapter = rech_adapter
        rech_adapter!!.setClickListener(this@HomeActivity)

    }

    private fun versioncheck() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /* progressDialog = ProgressDialog(this@PassbookActivity, R.style.Progress)
                 progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                 progressDialog!!.setCancelable(false)
                 progressDialog!!.setIndeterminate(true)
                 progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                 progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@HomeActivity))
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

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this!!.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)
                        val versionNumber = getCurrentVersionNumber(this@HomeActivity)
                        // requestObject1.put("Reqmode", MscoreApplication.encryptStart("42"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "VersionNo",
                                MscoreApplication.encryptStart(versionNumber.toString())
                        )
                        requestObject1.put(
                                "OsType",
                                MscoreApplication.encryptStart("0")
                        )

                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1 versionchk   " + requestObject1)
                    } catch (e: Exception) {
                        // progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mySnackbar = Snackbar.make(
                                findViewById(R.id.rl_main),
                                " Some technical issues.", Snackbar.LENGTH_SHORT
                        )
                        mySnackbar.show()
                    }
                    val body = RequestBody.create(
                            okhttp3.MediaType.parse("application/json; charset=utf-8"),
                            requestObject1.toString()
                    )
                    val call = apiService.getVersioncheck(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-Versioncheck", response.body())

                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("VersionCheck")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    var status = jsonobj2.getString("Status")
                                    if (status.equals("10")) {
                                        goToPlayStore()
                                    } else {

                                    }


                                } else {
                                    val builder = android.app.AlertDialog.Builder(
                                            this@HomeActivity,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: android.app.AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                //   progressDialog!!.dismiss()

                                val builder = android.app.AlertDialog.Builder(
                                        this@HomeActivity,
                                        R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: android.app.AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            //  progressDialog!!.dismiss()

                            val builder = android.app.AlertDialog.Builder(
                                    this@HomeActivity,
                                    R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: android.app.AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    // progressDialog!!.dismiss()
                    val builder = android.app.AlertDialog.Builder(
                            this@HomeActivity,
                            R.style.MyDialogTheme
                    )
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: android.app.AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {
                val builder = android.app.AlertDialog.Builder(
                        this@HomeActivity,
                        R.style.MyDialogTheme
                )
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: android.app.AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun goToPlayStore() {
        try {
//            String url = getResources().getString(R.string.app_link );
            val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF11, 0)
            val url = pref.getString("PlayStoreLink", null)
            URL(url)
        } catch (e: MalformedURLException) {
            val alertDialogBuilder = AlertDialog.Builder(this@HomeActivity)
            alertDialogBuilder.setMessage("The app is under maintenance. Sorry for the inconvenience.")
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton("Ok") { dialog: DialogInterface?, which: Int -> finish() }
            alertDialogBuilder.show()
            return
        }
        val dialogBuilder = android.app.AlertDialog.Builder(this@HomeActivity)
        val inflater: LayoutInflater = this@HomeActivity.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.alert_layout, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val tv_share = dialogView.findViewById<TextView>(R.id.tv_share)
        val tv_msg = dialogView.findViewById<TextView>(R.id.txt1)
        val tv_msg2 = dialogView.findViewById<TextView>(R.id.txt2)
        tv_msg.text = "New Version Available"
        tv_msg2.text = "New version of this application is available.\n" +
                "Click OK to upgrade now"
        val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)
        tv_cancel.setOnClickListener { alertDialog.dismiss() }
        tv_share.setOnClickListener { //  finishAffinity();

            val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF11, 0)
            val url = pref.getString("PlayStoreLink", null)
            Log.i("URL", url.toString())
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (anfe: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            alertDialog.dismiss()
        }

        alertDialog.show()


    }


    private fun setdefaultAccountDetails() {


        val DefaultAccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF24, 0)
        val DefaultBalanceSP = applicationContext.getSharedPreferences(Config.SHARED_PREF27, 0)
        val LastLoginTimeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF29, 0)
        val LastLogin = applicationContext.getSharedPreferences(Config.SHARED_PREF120, 0)

        tv_lastlogin!!.setText(LastLogin.getString("LastLogin", null)+" : " + LastLoginTimeSP.getString("LastLoginTime", null))
        val default = DefaultAccountSP.getString("DefaultAccount1", null)

        if (DefaultAccountSP.getString("DefaultAccount1", null) == null){
            tv_def_account!!.setText("")
            tv_def_availablebal!!.setText("")
            getOwnAccount()

        }else{
            tv_def_account!!.setText(DefaultAccountSP.getString("DefaultAccount1", null))
            //  val balance = DefaultBalanceSP.getString("DefaultBalance", null)!!.toDouble()
            //   tv_def_availablebal!!.setText("Rs. " + Config.getDecimelFormate(balance))
            getOwnAccount()
        }

    }

    private fun init() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /* progressDialog = ProgressDialog(this@PassbookActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@HomeActivity))
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

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this!!.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("42"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
                    } catch (e: Exception) {
                        // progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mySnackbar = Snackbar.make(
                                findViewById(R.id.rl_main),
                                " Some technical issues.", Snackbar.LENGTH_SHORT
                        )
                        mySnackbar.show()
                    }
                    val body = RequestBody.create(
                            okhttp3.MediaType.parse("application/json; charset=utf-8"),
                            requestObject1.toString()
                    )
                    val call = apiService.getBannerdetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-Banner", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("BannerDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("BannerDetailsList")

                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)

                                            val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
                                            // var s = "https://202.164.150.65:14262/NbfcAndroidAPI" + json.getString("ImagePath")
                                            var s = ImageURLSP.getString("ImageURL", null) + json.getString("ImagePath")
                                            Log.e(TAG, "s    618    " + s)


                                            XMENArray!!.add(s)


                                            mPager!!.adapter = BannerAdapter1(
                                                    this@HomeActivity,
                                                    XMENArray
                                            )
                                            indicator!!.setViewPager(mPager)
                                            val handler = Handler()
                                            val Update = Runnable {
                                                if (currentPage === jresult!!.length()) {
                                                    currentPage = 0
                                                }
                                                mPager!!.setCurrentItem(currentPage++, true)
                                            }
                                            val swipeTimer = Timer()
                                            swipeTimer.schedule(object : TimerTask() {
                                                override fun run() {
                                                    handler.post(Update)
                                                }
                                            }, 3000, 3000)
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }


                                } else {
                                    val builder = android.app.AlertDialog.Builder(
                                            this@HomeActivity,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: android.app.AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                //   progressDialog!!.dismiss()

                                val builder = android.app.AlertDialog.Builder(
                                        this@HomeActivity,
                                        R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: android.app.AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            //  progressDialog!!.dismiss()

                            val builder = android.app.AlertDialog.Builder(
                                    this@HomeActivity,
                                    R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: android.app.AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    // progressDialog!!.dismiss()
                    val builder = android.app.AlertDialog.Builder(
                            this@HomeActivity,
                            R.style.MyDialogTheme
                    )
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: android.app.AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {
                val builder = android.app.AlertDialog.Builder(
                        this@HomeActivity,
                        R.style.MyDialogTheme
                )
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: android.app.AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
        /* for (i in 0 until 4)
             XMENArray.add(XMEN[i])
         mPager!!.adapter = BannerAdapter(this, XMENArray)
         indicator!!.setViewPager(mPager)
         val handler = Handler()
         val Update = Runnable {
             if (currentPage === 4) {
                 currentPage = 0
             }
             mPager!!.setCurrentItem(currentPage++, true)
         }
         val swipeTimer = Timer()
         swipeTimer.schedule(object : TimerTask() {
             override fun run() {
                 handler.post(Update)
             }
         }, 3000, 3000)*/
    }

    open fun setInitialise() {


        llloanstatus = findViewById(R.id.llloanstatus)
        im_applogo = findViewById(R.id.im_applogo)
        txtv_availbal = findViewById(R.id.txtv_availbal)

        txtv_acntdetail = findViewById(R.id.txtv_acntdetail)
        txtv_fundtrans = findViewById(R.id.txtv_fundtrans)
        txtv_rechbill = findViewById(R.id.txtv_rechbill)
        txtv_reprts = findViewById(R.id.txtv_reprts)
        txtv_tools = findViewById(R.id.txtv_tools)


        tv_mobile = findViewById(R.id.tv_mobile)
        tvuser = findViewById(R.id.tvuser)
        llprofile = findViewById(R.id.llprofile)
        improfile = findViewById(R.id.improfile)
        imlanguage = findViewById(R.id.imlanguage)
        imquit = findViewById(R.id.imquit)
        imlogout = findViewById(R.id.imlogout)
        tv_header = findViewById(R.id.tv_header)
        llloanapplication = findViewById(R.id.llloanapplication)
        lldashboard = findViewById(R.id.lldashboard)
        llDeposit = findViewById(R.id.llDeposit)
        llprdctdetail = findViewById(R.id.llprdctdetail)
        llmyaccounts = findViewById(R.id.llmyaccounts)
        ll_fundtransfer = findViewById(R.id.ll_fundtransfer)
        ll_recharge = findViewById(R.id.ll_recharge)

        imgMenu = findViewById(R.id.imgMenu)
        drawer = findViewById(R.id.drawer_layout)
        lvNavMenu = findViewById(R.id.lvNavMenu)
        ll_branschDetails = findViewById(R.id.ll_branschDetails)
        llownbank = findViewById(R.id.llownbank)
        ll_holidaylist= findViewById(R.id.ll_holidaylist)
        ll_prepaid = findViewById(R.id.ll_prepaid)
        ll_postpaid = findViewById(R.id.ll_postpaid)
        ll_landline = findViewById(R.id.ll_landline)
        ll_kseb = findViewById(R.id.ll_kseb)
        ll_rechargehistory = findViewById(R.id.ll_rechargehistory)
        llexecutive = findViewById(R.id.llexecutive)
        txtv_accnt= findViewById(R.id.txtv_accnt)

        txtv_myacc= findViewById(R.id.txtv_myacc)
        txtv_pasbk= findViewById(R.id.txtv_pasbk)
        txtv_quickbal= findViewById(R.id.txtv_quickbal)
        txtvstatmnt= findViewById(R.id.txtvstatmnt)
        txtv_dueremndr= findViewById(R.id.txtv_dueremndr)
        txtvnotif= findViewById(R.id.txtvnotif)

        llnotif= findViewById(R.id.llnotif)

        ll_dth = findViewById(R.id.ll_dth)
        ll_datacard = findViewById(R.id.ll_datacard)
        llEmi = findViewById(R.id.llEmi)
        llpassbook = findViewById<LinearLayout>(R.id.llpassbook)
        llduereminder = findViewById<LinearLayout>(R.id.lldueremindrer)
        llgoldslab = findViewById<LinearLayout>(R.id.llgoldslab)
        ll_virtualcard = findViewById<LinearLayout>(R.id.ll_virtualcard)
        ll_otherbank = findViewById<LinearLayout>(R.id.ll_otherbank)
        llquickbalance = findViewById<LinearLayout>(R.id.llquickbalance)
        llstatement = findViewById<LinearLayout>(R.id.llstatement)
        llquickpay = findViewById<LinearLayout>(R.id.llquickpay)
        llenquiry = findViewById<LinearLayout>(R.id.llenquiry)

        tv_def_account = findViewById<TextView>(R.id.tv_def_account)
        tv_def_availablebal = findViewById<TextView>(R.id.tv_def_availablebal)
        tv_lastlogin = findViewById<TextView>(R.id.tv_lastlogin)
        tv_viewall = findViewById<TextView>(R.id.tv_viewall)

        txtv_ownbnk = findViewById<TextView>(R.id.txtv_ownbnk)
        txtv_othrbnk = findViewById<TextView>(R.id.txtv_othrbnk)
        txtv_quickpay = findViewById<TextView>(R.id.txtv_quickpay)

        txtv_prepaid = findViewById<TextView>(R.id.txtv_prepaid)
        txtv_pospaid = findViewById<TextView>(R.id.txtv_pospaid)
        txtv_landline = findViewById<TextView>(R.id.txtv_landline)
        txtv_dth = findViewById<TextView>(R.id.txtv_dth)
        txtv_datacrd = findViewById<TextView>(R.id.txtv_datacrd)
        txtv_Kseb = findViewById<TextView>(R.id.txtv_Kseb)
        txtv_history = findViewById<TextView>(R.id.txtv_history)

        txtv_dashbrd = findViewById<TextView>(R.id.txtv_dashbrd)
        txtv_virtual = findViewById<TextView>(R.id.txtv_virtual)
        txtv_branch = findViewById<TextView>(R.id.txtv_branch)
        txtv_loanaplctn = findViewById<TextView>(R.id.txtv_loanaplctn)
        txtv_loanstats = findViewById<TextView>(R.id.txtv_loanstats)
        txtv_prdctdetail = findViewById<TextView>(R.id.txtv_prdctdetail)

        txtv_emi = findViewById<TextView>(R.id.txtv_emi)
        txtv_deposit = findViewById<TextView>(R.id.txtv_deposit)
        txtv_goldloan = findViewById<TextView>(R.id.txtv_goldloan)
        txtv_enqry = findViewById<TextView>(R.id.txtv_enqry)
        txtv_holidy = findViewById<TextView>(R.id.txtv_holidy)
        txtv_exectve = findViewById<TextView>(R.id.txtv_exectve)



    }

    open fun setRegister() {
        llloanstatus!!.setOnClickListener(this)
        improfile!!.setOnClickListener(this)
        imlanguage!!.setOnClickListener(this)
        imquit!!.setOnClickListener(this)
        imlogout!!.setOnClickListener(this)
        lldashboard!!.setOnClickListener(this)
        llprdctdetail!!.setOnClickListener(this)
        llgoldslab!!.setOnClickListener(this)
        llquickbalance!!.setOnClickListener(this)
        llmyaccounts!!.setOnClickListener(this)
        imgMenu!!.setOnClickListener(this)
        mPager = findViewById(R.id.pager)
        indicator =findViewById(R.id.indicator)
        llownbank!!.setOnClickListener(this)
        llpassbook!!.setOnClickListener(this)
        llduereminder!!.setOnClickListener(this)
        ll_holidaylist!!.setOnClickListener(this)
        ll_branschDetails!!.setOnClickListener(this)
        ll_prepaid!!.setOnClickListener(this)
        ll_postpaid!!.setOnClickListener(this)
        ll_landline!!.setOnClickListener(this)
        ll_kseb!!.setOnClickListener(this)
        ll_rechargehistory!!.setOnClickListener(this)
        ll_dth!!.setOnClickListener(this)
        ll_datacard!!.setOnClickListener(this)
        llEmi!!.setOnClickListener(this)
        ll_virtualcard!!.setOnClickListener(this)
        ll_otherbank!!.setOnClickListener(this)
        llloanapplication!!.setOnClickListener(this)
        llenquiry!!.setOnClickListener(this)
        llprofile!!.setOnClickListener(this)
        llquickpay!!.setOnClickListener(this)
        llnotif!!.setOnClickListener(this)
        llstatement!!.setOnClickListener(this)
        tv_viewall!!.setOnClickListener(this)
        llDeposit!!.setOnClickListener(this)
        llexecutive!!.setOnClickListener(this)
    }

    open fun setHomeNavMenu() {
        val ID_AbtusSP = applicationContext.getSharedPreferences(Config.SHARED_PREF54, 0)
        val ID_ContactSP = applicationContext.getSharedPreferences(Config.SHARED_PREF55, 0)
        val ID_FeebkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF56, 0)
        val ID_PrivacySP = applicationContext.getSharedPreferences(Config.SHARED_PREF57, 0)
        val ID_TermsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF58, 0)
        val ID_SetngsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF60, 0)
        val ID_LogoutSP = applicationContext.getSharedPreferences(Config.SHARED_PREF61, 0)
        val ID_Quit = applicationContext.getSharedPreferences(Config.SHARED_PREF106, 0)
        val ID_Lang = applicationContext.getSharedPreferences(Config.SHARED_PREF167, 0)


        var abt =ID_AbtusSP.getString("aboutus", null)
        var cntct =ID_ContactSP.getString("contactus", null)
        var feebk =ID_FeebkSP.getString("feedback", null)
        var privacy =ID_PrivacySP.getString("privacypolicy", null)
        var terms =ID_TermsSP.getString("termsandconditions", null)
        var setngs =ID_SetngsSP.getString("settings", null)
        var logout =ID_LogoutSP.getString("logout", null)
        var quit =ID_Quit.getString("quit", null)
        var lang =ID_Lang.getString("Language", null)

        val menulist= arrayOf(abt, cntct, feebk, privacy, terms, setngs, lang, logout, quit)
        /*  val menulist = arrayOf(
                  "About Us",
                  "Contact Us",
                  "Feedback",
                  "Privacy Policies",
                  "Terms & Conditions",
                  "Settings",
                  "Logout",
                  "Quit"
          )*/
        val imageId = arrayOf<Int>(
                R.drawable.aboutnav, R.drawable.contnav,
                R.drawable.feedbacknav, R.drawable.ppnav, R.drawable.tncnav, R.drawable.ic_settings, R.drawable.langicon,
                R.drawable.logoutnav, R.drawable.exitnav
        )
        val adapter = NavMenuAdapter(this@HomeActivity, menulist, imageId)
        lvNavMenu!!.adapter = adapter
        lvNavMenu!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                startActivity(Intent(this@HomeActivity, AboutActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            } else if (position == 1) {
                startActivity(Intent(this@HomeActivity, ContactUsActivity::class.java))
            } else if (position == 2) {
                startActivity(Intent(this@HomeActivity, FeedbackActivity::class.java))
            } else if (position == 3) {
                startActivity(Intent(this@HomeActivity, PrivacyPolicyActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }else if (position == 4) {
                startActivity(Intent(this@HomeActivity, TermsnconditionsActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }
            else if (position == 5) {
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
                /* val ID_lan = applicationContext.getSharedPreferences(Config.SHARED_PREF9,0)
                 var lanid =ID_lan.getString("ID_Languages", null)
                 getLabels(lanid)*/
            }
            else if (position == 6) {
                //  startActivity(Intent(this@HomeActivity, LanguageSelectionActivity::class.java))
                getLanguagelist()
                drawer!!.closeDrawer(GravityCompat.START)
            }
            else if (position == 7) {
                try {
                    val ID_dlt = applicationContext.getSharedPreferences(Config.SHARED_PREF205, 0)
                    val ID_ys = applicationContext.getSharedPreferences(Config.SHARED_PREF206, 0)
                    val ID_no = applicationContext.getSharedPreferences(Config.SHARED_PREF207, 0)

                    val dialogBuilder = android.app.AlertDialog.Builder(this@HomeActivity)
                    val inflater: LayoutInflater = this@HomeActivity.getLayoutInflater()
                    val dialogView: View = inflater.inflate(R.layout.logout_popup, null)
                    dialogBuilder.setView(dialogView)
                    val alertDialog = dialogBuilder.create()
                    val tv_share = dialogView.findViewById<TextView>(R.id.tv_share)
                    val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)
                    val tv_dlte = dialogView.findViewById<TextView>(R.id.ttxv_dlte)

                    tv_dlte!!.setText(ID_dlt.getString("DoYouWanttoDeleteThisAccountAndRegisterWithAnotherAccount?", null))
                    tv_share!!.setText(ID_ys.getString("Yes", null))
                    tv_cancel!!.setText(ID_no.getString("No", null))

                    tv_cancel.setOnClickListener { alertDialog.dismiss() }
                    tv_share.setOnClickListener {
                        alertDialog.dismiss()
                        logout()
                        val intent = Intent(this, WelcomeActivity::class.java)
                        intent.putExtra("from", "true")
                        this.startActivity(intent)
                        this.finish()
                    }
                    alertDialog.show()





                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            else if (position == 8) {
                quit()
            }

        }
    }



    private fun quit() {
        try {
            val dialog1 = Dialog(this)
            dialog1 .requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog1 .setCancelable(false)
            dialog1 .setContentView(R.layout.quit_popup)
            val btn_Yes = dialog1.findViewById(R.id.tv_share) as TextView
            val btn_cancel = dialog1.findViewById(R.id.tv_cancel) as TextView
            val txtv_quit = dialog1.findViewById(R.id.txtv_quit) as TextView

            val ID_quit = applicationContext.getSharedPreferences(Config.SHARED_PREF208, 0)
            val ID_ys = applicationContext.getSharedPreferences(Config.SHARED_PREF206, 0)
            val ID_no = applicationContext.getSharedPreferences(Config.SHARED_PREF207, 0)

            txtv_quit!!.setText(ID_quit.getString("DoYouWantToQuit?", null))
            btn_Yes!!.setText(ID_ys.getString("Yes", null))
            btn_cancel!!.setText(ID_no.getString("No", null))

            val imglogo = dialog1.findViewById(R.id.imglogo) as ImageView

            val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
            val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
            val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
            try {
                val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
                PicassoTrustAll.getInstance(this@HomeActivity)!!.load(imagepath).error(android.R.color.transparent).into(imglogo!!)
            }catch (e: Exception) {
                e.printStackTrace()
            }

            btn_cancel.setOnClickListener {
                dialog1 .dismiss()
            }
            btn_Yes.setOnClickListener {
                dialog1.dismiss()
                finish()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity()
                }
            }
            dialog1.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun logout() {
        val loginSP = this!!.getSharedPreferences(Config.SHARED_PREF, 0)
        val loginEditer = loginSP.edit()
        loginEditer.putString("loginsession", "No")
        loginEditer.commit()


        val FK_CustomerSP = this!!.getSharedPreferences(Config.SHARED_PREF1, 0)
        val FK_CustomerEditer = FK_CustomerSP.edit()
        FK_CustomerEditer.putString("FK_Customer", "")
        FK_CustomerEditer.commit()

        val FK_CustomerMobSp = this!!.getSharedPreferences(Config.SHARED_PREF2, 0)
        val FK_CustomerMobEditer = FK_CustomerMobSp.edit()
        FK_CustomerMobEditer.putString("CusMobile", "")
        FK_CustomerMobEditer.commit()

        val CustomerNameSP = this!!.getSharedPreferences(Config.SHARED_PREF3, 0)
        val CustomerNameEditer = CustomerNameSP.edit()
        CustomerNameEditer.putString("CustomerName", "")
        CustomerNameEditer.commit()

        val CustomerAddressSP = this!!.getSharedPreferences(Config.SHARED_PREF4, 0)
        val CustomerAddressEditer = CustomerAddressSP.edit()
        CustomerAddressEditer.putString("Address", "")
        CustomerAddressEditer.commit()

        val CustomerEmailSP = this!!.getSharedPreferences(Config.SHARED_PREF5, 0)
        val CustomerEmailEditer = CustomerEmailSP.edit()
        CustomerEmailEditer.putString("Email", "")
        CustomerEmailEditer.commit()

        val CustomerGenderSP = this!!.getSharedPreferences(Config.SHARED_PREF6, 0)
        val CustomerGenderEditer = CustomerGenderSP.edit()
        CustomerGenderEditer.putString("Gender", "")
        CustomerGenderEditer.commit()

        val CustomerDobSP = this!!.getSharedPreferences(Config.SHARED_PREF7, 0)
        val CustomerDobEditer = CustomerDobSP.edit()
        CustomerDobEditer.putString("DateOfBirth", "")
        CustomerDobEditer.commit()

        val TokenSP = this!!.getSharedPreferences(Config.SHARED_PREF8, 0)
        val TokenEditer = TokenSP.edit()
        TokenEditer.putString("Token", "")
        TokenEditer.commit()

        val AppstoreSP = this!!.getSharedPreferences(Config.SHARED_PREF10, 0)
        val AppstoreEditer = AppstoreSP.edit()
        AppstoreEditer.putString("AppStoreLink", "")
        AppstoreEditer.commit()


        val ID_PlaystoreSP = this!!.getSharedPreferences(Config.SHARED_PREF11, 0)
        val ID_PlaystoreEditer = ID_PlaystoreSP.edit()
        ID_PlaystoreEditer.putString("PlayStoreLink", "")
        ID_PlaystoreEditer.commit()

        val FKAccountSP = this!!.getSharedPreferences(Config.SHARED_PREF16, 0)
        val FKAccountEditer = FKAccountSP.edit()
        FKAccountEditer.putString("FK_Account", "")
        FKAccountEditer.commit()

        val SubmoduleeSP = this!!.getSharedPreferences(Config.SHARED_PREF17, 0)
        val SubmoduleEditer = SubmoduleeSP.edit()
        SubmoduleEditer.putString("SubModule", "")
        SubmoduleEditer.commit()

        val StatusSP = this!!.getSharedPreferences(Config.SHARED_PREF18, 0)
        val StatusEditer = StatusSP.edit()
        StatusEditer.putString("Status", "")
        StatusEditer.commit()

        val CustnoSP = this!!.getSharedPreferences(Config.SHARED_PREF19, 0)
        val CustnoEditer = CustnoSP.edit()
        CustnoEditer.putString("CustomerNumber", "")
        CustnoEditer.commit()

        val Custno1SP = this!!.getSharedPreferences(Config.SHARED_PREF20, 0)
        val Custno1Editer = Custno1SP.edit()
        Custno1Editer.putString("CustomerNumber", "")
        Custno1Editer.commit()

        val LastloginSP = this!!.getSharedPreferences(Config.SHARED_PREF29, 0)
        val LastloginEditer = LastloginSP.edit()
        LastloginEditer.putString("LastLoginTime", "")
        LastloginEditer.commit()

        val LastloginSP1 = this!!.getSharedPreferences(Config.SHARED_PREF120, 0)
        val LastloginEditer1 = LastloginSP1.edit()
        LastloginEditer1.putString("LastLogin", "")
        LastloginEditer1.commit()

    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("WrongConstant")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgMenu ->
                drawer!!.openDrawer(Gravity.START)
            R.id.lldashboard -> {
                startActivity(Intent(this@HomeActivity, DashboardActivity::class.java))
            }
            R.id.llprdctdetail -> {

                startActivity(Intent(this@HomeActivity, ProductListActivity::class.java))
            }
            R.id.llmyaccounts -> {
                startActivity(Intent(this@HomeActivity, AccountlistActivity::class.java))
            }
            R.id.ll_branschDetails -> {
                startActivity(Intent(this@HomeActivity, BranchDetailActivity::class.java))
            }

            R.id.ll_holidaylist -> {
                startActivity(Intent(this@HomeActivity, HolidayListActivity::class.java))
            }

            R.id.llEmi -> {
                startActivity(Intent(this@HomeActivity, EMIActivity::class.java))
            }
            R.id.lldueremindrer -> {
                //  startActivity(Intent(this@HomeActivity, DueReminderActivity::class.java))
                var intent = Intent(this@HomeActivity, DuedateActivity::class.java)
                startActivity(intent)
            }
            R.id.improfile -> {
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            }
            R.id.imlanguage -> {

                getLanguagelist()
            }
            R.id.imquit -> {
                quit()
            }
            R.id.imlogout -> {
                try {

                    val dialogBuilder = android.app.AlertDialog.Builder(this@HomeActivity)
                    val inflater: LayoutInflater = this@HomeActivity.getLayoutInflater()
                    val dialogView: View = inflater.inflate(R.layout.logout_popup, null)
                    dialogBuilder.setView(dialogView)
                    val alertDialog = dialogBuilder.create()
                    val tv_share = dialogView.findViewById<TextView>(R.id.tv_share)
                    val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)


                    tv_cancel.setOnClickListener { alertDialog.dismiss() }
                    tv_share.setOnClickListener {
                        alertDialog.dismiss()
                        logout()
                        val intent = Intent(this, WelcomeActivity::class.java)
                        intent.putExtra("from", "true")
                        this.startActivity(intent)
                        this.finish()
                    }
                    alertDialog.show()


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            R.id.llprofile -> {
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            }
            R.id.llpassbook -> {
                startActivity(Intent(this@HomeActivity, PassbookActivity::class.java))
            }
            R.id.llquickbalance -> {
                startActivity(Intent(this@HomeActivity, QuickBalanceActivity::class.java))
            }
            R.id.llownbank -> {
                startActivity(
                        Intent(
                                this@HomeActivity,
                                OwnBankFundTransferServiceActivity::class.java
                        )
                )
            }
            R.id.llgoldslab -> {
//                startActivity(Intent(this@HomeActivity, GoldSlabEstimatorActivity::class.java))
                startActivity(Intent(this@HomeActivity, GoldLoanActivity::class.java))
            }
            R.id.llloanapplication -> {
                startActivity(Intent(this@HomeActivity, LoanApplicationActivity::class.java))
            }
            R.id.llloanstatus -> {
                startActivity(Intent(this@HomeActivity, LoanStatusActivity::class.java))
            }
            R.id.ll_prepaid -> {

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "prepaid")
                startActivity(intent)
            }
            R.id.ll_postpaid -> {

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "postpaid")
                startActivity(intent)
            }
            R.id.llnotif -> {

                var intent = Intent(this@HomeActivity, MessagesActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_landline -> {

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "landline")
                startActivity(intent)

//                var intent = Intent(this@HomeActivity, KSEBActivity::class.java)
//                startActivity(intent)
            }
            R.id.ll_kseb -> {

                var intent = Intent(this@HomeActivity, KSEBActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_dth -> {

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "dth")
                startActivity(intent)
            }
            R.id.ll_datacard -> {

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "datacard")
                startActivity(intent)
            }

            R.id.ll_virtualcard -> {

                var intent = Intent(this@HomeActivity, VirtualActivity::class.java)
                startActivity(intent)
            }
            R.id.llDeposit -> {
                var intent = Intent(this@HomeActivity, DepositCalculatorActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_otherbank -> {

                var intent = Intent(this@HomeActivity, OtherBankActivity::class.java)
                startActivity(intent)
            }
            R.id.llexecutive -> {

                var intent = Intent(this@HomeActivity, ExecutiveActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_rechargehistory -> {

                var intent = Intent(this@HomeActivity, RechargeHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.llstatement -> {

                var intent = Intent(this@HomeActivity, StatementActivity::class.java)
                startActivity(intent)
            }
            R.id.llquickpay -> {

                var intent = Intent(this@HomeActivity, QuickPayActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_viewall -> {

                startActivity(Intent(this@HomeActivity, AccountlistActivity::class.java))
            }
            R.id.llenquiry -> {

                startActivity(Intent(this@HomeActivity, EnquiryActivity::class.java))
            }
        }
    }


    private fun getOwnAccount() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {

                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@HomeActivity))
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
                        val TokenSP = applicationContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put("SubMode", MscoreApplication.encryptStart("1"))
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )
                        requestObject1.put(
                                "BankHeader", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankHeader
                                )
                        )
                        )


                    } catch (e: Exception) {
                        e.printStackTrace()
                        val mySnackbar = Snackbar.make(
                                findViewById(R.id.rl_main),
                                " Some technical issues.", Snackbar.LENGTH_SHORT
                        )
                        mySnackbar.show()
                    }
                    val body = RequestBody.create(
                            okhttp3.MediaType.parse("application/json; charset=utf-8"),
                            requestObject1.toString()
                    )
                    val call = apiService.getOwnAccounDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {

                                val jObject = JSONObject(response.body())

                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("OwnAccountdetails")
                                    jArrayAccount = jobjt.getJSONArray("OwnAccountdetailsList")
                                    val accountItems: ArrayList<String> = ArrayList()
                                    for (i in 0 until jArrayAccount!!.length()) {
                                        val obj: JSONObject = jArrayAccount!!.getJSONObject(i)
                                        accountItems.add(obj.getString("AccountNumber"));
                                        val DefaultAccountSP =
                                                applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF24,
                                                        0
                                                )
                                        if (DefaultAccountSP.getString("DefaultAccount1", null) == null) {
                                            if (i == 0) {

                                                val balance = obj.getString("Balance").toDouble()
                                                tv_def_availablebal!!.setText(
                                                        "Rs. " + Config.getDecimelFormate(
                                                                balance
                                                        )
                                                )
                                                tv_def_account!!.setText(obj.getString("AccountNumber"))

                                            }

                                        }
                                        else if(DefaultAccountSP.getString("DefaultAccount1", null).equals(obj.getString("AccountNumber")) )
                                        {


                                            val balance = obj.getString("Balance").toDouble()
                                            tv_def_availablebal!!.setText(
                                                    "Rs. " + Config.getDecimelFormate(
                                                            balance
                                                    )
                                            )
                                            tv_def_account!!.setText(obj.getString("AccountNumber"))


                                        }
                                    }

                                } else {

                                }
                            } catch (e: Exception) {

                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {

                        }
                    })
                } catch (e: Exception) {

                }
            }
            false -> {

                val builder = AlertDialog.Builder(this@HomeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }
    private fun getCurrentVersionNumber(context: Context): Int {
        try {
            return context.packageManager
                    .getPackageInfo(context.packageName, 0)!!.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            // Do nothing
        }
        return 1
    }

    override fun onBackPressed() {

        quit()

    }


    private fun getLanguagelist() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@HomeActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@HomeActivity))
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
                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("7"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey))
                        )

                        Log.e("TAG", "requestObject1  language   " + requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mySnackbar = Snackbar.make(
                                findViewById(R.id.rl_main),
                                " Some technical issues.", Snackbar.LENGTH_SHORT
                        )
                        mySnackbar.show()
                    }
                    val body = RequestBody.create(
                            okhttp3.MediaType.parse("application/json; charset=utf-8"),
                            requestObject1.toString()
                    )
                    val call = apiService.getLanguages(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    //   val jobjt = jObject.getJSONObject("VarificationMaintenance")

                                    val jobjt = jObject.getJSONObject("Languages")
                                    jArrayLang = jobjt.getJSONArray("LanguagesList")

                                    Log.e(TAG, "jobjt  2056 " + jobjt)
                                    Log.e(TAG, "jarray  2056 " + jArrayLang)
                                    LanguagePopup(jArrayLang!!)

                                } else {
                                    val builder = android.app.AlertDialog.Builder(
                                            this@HomeActivity,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: android.app.AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                val builder = android.app.AlertDialog.Builder(
                                        this@HomeActivity,
                                        R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: android.app.AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            val builder = android.app.AlertDialog.Builder(
                                    this@HomeActivity,
                                    R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: android.app.AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    val builder = android.app.AlertDialog.Builder(this@HomeActivity, R.style.MyDialogTheme)
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: android.app.AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {
                val builder = android.app.AlertDialog.Builder(this@HomeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: android.app.AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun LanguagePopup(jArrayLang: JSONArray) {

        Log.e(TAG, "jarray  2130 " + jArrayLang!!)
        try {
            val builder = AlertDialog.Builder(this@HomeActivity)
            val inflater1 = this@HomeActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater1.inflate(R.layout.popup_language, null)


            builder.setView(layout)
//            val alertDialog = builder.create()
            alertDialogLang = builder.create()
            alertDialogLang!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            alertDialogLang!!.setView(layout, 0, 0, 0, 0);

            rv_Languagelist = layout.findViewById<View>(R.id.rv_Languagelist) as RecyclerView
            val obj_adapter = LanguageAdapter(applicationContext!!, jArrayLang!!)
            rv_Languagelist!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            rv_Languagelist!!.adapter = obj_adapter
            obj_adapter.setClickListener(this@HomeActivity)


            alertDialogLang!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Exception  2149   " + e.toString())
        }

    }

    override fun onClick(position: Int, data: String) {

       if (data.equals("lang")){
           Log.e(TAG, "onClick  2155   " + position)
           var jsonObject1 = jArrayLang!!.getJSONObject(position)
           alertDialogLang!!.dismiss()
           Log.e(TAG, "LanguagesName  2155   " + jsonObject1.getString("LanguagesName"))
           getlabels(jsonObject1.getString("ID_Languages"))
       }
        if (data.equals("fund")){
            Log.e(TAG, "onClick  2155   " + position)
            var jsonObject1 = jArrayMenuFund!!.getJSONObject(position)
            Log.e(TAG,"IDS 2155 "+jsonObject1.getInt("FundId"))

            if (jsonObject1.getInt("FundId") == 0){
                startActivity(
                    Intent(
                        this@HomeActivity,
                        OwnBankFundTransferServiceActivity::class.java
                    )
                )
            }
            if (jsonObject1.getInt("FundId") == 1){
                var intent = Intent(this@HomeActivity, OtherBankActivity::class.java)
                startActivity(intent)
            }
            if (jsonObject1.getInt("FundId") == 2){
                var intent = Intent(this@HomeActivity, QuickPayActivity::class.java)
                startActivity(intent)
            }
        }

        if (data.equals("rech")){

            Log.e(TAG, "onClick RECH  2155   " + position)
            var jsonObject1 = jArrayMenuRech!!.getJSONObject(position)

            if (jsonObject1.getInt("RechId") == 0){

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "prepaid")
                startActivity(intent)


            }

            if (jsonObject1.getInt("RechId") == 1){
                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "postpaid")
                startActivity(intent)

            }
            if (jsonObject1.getInt("RechId") == 2){
                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "landline")
                startActivity(intent)

            }

            if (jsonObject1.getInt("RechId") == 3){
                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "dth")
                startActivity(intent)
            }
            if (jsonObject1.getInt("RechId") == 4){
                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "datacard")
                startActivity(intent)
            }

            if (jsonObject1.getInt("RechId") == 5){

                var intent = Intent(this@HomeActivity, KSEBActivity::class.java)
                startActivity(intent)
            }
            if (jsonObject1.getInt("RechId") == 6){
                var intent = Intent(this@HomeActivity, RechargeHistoryActivity::class.java)
                startActivity(intent)

            }



        }


    }



    private fun getlabels(id: String) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this@HomeActivity)) {
            true -> {
                progressDialog = ProgressDialog(this@HomeActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                // progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setMessage("Loading..")
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@HomeActivity))
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

                        val FK_CustomerSP = this@HomeActivity.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this@HomeActivity.getSharedPreferences(
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
                                this@HomeActivity.getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  labels   " + requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mSnackbar = Snackbar.make(this@HomeActivity.findViewById(android.R.id.content), "Some technical issues.", Snackbar.LENGTH_INDEFINITE)
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

                                 /*   val ID_WelcomeSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF34, 0)
                                    val ID_WelcomeSPEditer = ID_WelcomeSP.edit()
                                    ID_WelcomeSPEditer.putString("welcome", jresult3.get("welcome") as String)
                                    ID_WelcomeSPEditer.commit()


                                    val ID_FasterSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF35, 0)
                                    val ID_FasterSPEditer = ID_FasterSP.edit()
                                    ID_FasterSPEditer.putString("fasterwaytohelpyou", jresult3.get("fasterwaytohelpyou") as String)
                                    ID_FasterSPEditer.commit()

                                    val ID_SigninSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF36, 0)
                                    val ID_SigninSPEditer = ID_SigninSP.edit()
                                    ID_SigninSPEditer.putString("sigin", jresult3.get("sigin") as String)
                                    ID_SigninSPEditer.commit()

                                    val ID_RegisterSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF37, 0)
                                    val ID_RegisterSPEditer = ID_RegisterSP.edit()
                                    ID_RegisterSPEditer.putString("registernow", jresult3.get("registernow") as String)
                                    ID_RegisterSPEditer.commit()

                                    val ID_SelctlanSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF38, 0)
                                    val ID_SelctlanSPEditer = ID_SelctlanSP.edit()
                                    ID_SelctlanSPEditer.putString("SelectLanguage", jresult3.get("SelectLanguage") as String)
                                    ID_SelctlanSPEditer.commit()

                                    val ID_SkipSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF39, 0)
                                    val ID_SkipSPEditer = ID_SkipSP.edit()
                                    ID_SkipSPEditer.putString("Skip", jresult3.get("Skip") as String)
                                    ID_SkipSPEditer.commit()


                                    val ID_LetsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF40, 0)
                                    val ID_LetsSPEditer = ID_LetsSP.edit()
                                    ID_LetsSPEditer.putString("Let'sgetstarted", jresult3.get("Let'sgetstarted") as String)
                                    ID_LetsSPEditer.commit()

                                    val ID_PersnlinfSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF41, 0)
                                    val ID_PersnlinfEditer = ID_PersnlinfSP.edit()
                                    ID_PersnlinfEditer.putString("pleaseenteryourpersonalinformation", jresult3.get("pleaseenteryourpersonalinformation") as String)
                                    ID_PersnlinfEditer.commit()

                                    val ID_EntermobSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF42, 0)
                                    val ID_EntermobEditer = ID_EntermobSP.edit()
                                    ID_EntermobEditer.putString("entermobilenumber", jresult3.get("entermobilenumber") as String)
                                    ID_EntermobEditer.commit()

                                    *//*    val ID_last4SP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF43, 0)
                                    val ID_last4SPEditer = ID_last4SP.edit()
                                    ID_last4SPEditer.putString("enter last4digitofa/cno", jresult3.get("enter last4digitofa/cno") as String)
                                    ID_last4SPEditer.commit()*//*

                                    val ID_ContinueSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF44, 0)
                                    val ID_ContinueSPEditer = ID_ContinueSP.edit()
                                    ID_ContinueSPEditer.putString("continue", jresult3.get("continue") as String)
                                    ID_ContinueSPEditer.commit()

                                    val ID_LoginmobSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF45, 0)
                                    val ID_LoginMobSPEditer = ID_LoginmobSP.edit()
                                    ID_LoginMobSPEditer.putString("loginwithmobilenumber", jresult3.get("loginwithmobilenumber") as String)
                                    ID_LoginMobSPEditer.commit()

                                    *//*  val ID_MobotpeSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF46, 0)
                                    val ID_MobotpSPEditer = ID_MobotpeSP.edit()
                                    ID_MobotpSPEditer.putString("enteryourmobilenumberwewillsentyouOTPtoverify", jresult3.get("enteryourmobilenumberwewillsentyouOTPtoverify") as String)
                                    ID_MobotpSPEditer.commit()
*//*
                                    val ID_LoginverifySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF47, 0)
                                    val ID_LoginVerifySPEditer = ID_LoginverifySP.edit()
                                    ID_LoginVerifySPEditer.putString("userloginverified", jresult3.get("userloginverified") as String)
                                    ID_LoginVerifySPEditer.commit()

                                    val ID_OtpverifySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF48, 0)
                                    val ID_OtpVerifySPEditer = ID_OtpverifySP.edit()
                                    ID_OtpVerifySPEditer.putString("Otpverification", jresult3.get("Otpverification") as String)
                                    ID_OtpVerifySPEditer.commit()

                                    val ID_MyaccSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF50, 0)
                                    val ID_MyaccSPEditer = ID_MyaccSP.edit()
                                    ID_MyaccSPEditer.putString("Myaccounts", jresult3.get("Myaccounts") as String)
                                    ID_MyaccSPEditer.commit()

                                    val ID_PassbkSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF51, 0)
                                    val ID_PassbkSPEditer = ID_PassbkSP.edit()
                                    ID_PassbkSPEditer.putString("passbook", jresult3.get("passbook") as String)
                                    ID_PassbkSPEditer.commit()

                                    val ID_QuickbalSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF52, 0)
                                    val ID_QuickbalSPEditer = ID_QuickbalSP.edit()
                                    ID_QuickbalSPEditer.putString("quickbalance", jresult3.get("quickbalance") as String)
                                    ID_QuickbalSPEditer.commit()

                                    val ID_DueremindSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF53, 0)
                                    val ID_DueremindEditer = ID_DueremindSP.edit()
                                    ID_DueremindEditer.putString("duereminder", jresult3.get("duereminder") as String)
                                    ID_DueremindEditer.commit()

                                    val ID_AbtusSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF54, 0)
                                    val ID_AbtusEditer = ID_AbtusSP.edit()
                                    ID_AbtusEditer.putString("aboutus", jresult3.get("aboutus") as String)
                                    ID_AbtusEditer.commit()

                                    val ID_ContactSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF55, 0)
                                    val ID_ContactEditer = ID_ContactSP.edit()
                                    ID_ContactEditer.putString("contactus", jresult3.get("contactus") as String)
                                    ID_ContactEditer.commit()

                                    val ID_FeebkSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF56, 0)
                                    val ID_FeedbkEditer = ID_FeebkSP.edit()
                                    ID_FeedbkEditer.putString("feedback", jresult3.get("feedback") as String)
                                    ID_FeedbkEditer.commit()

                                    val ID_PrivacySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF57, 0)
                                    val ID_PrivacyEditer = ID_PrivacySP.edit()
                                    ID_PrivacyEditer.putString("privacypolicy", jresult3.get("privacypolicy") as String)
                                    ID_PrivacyEditer.commit()

                                    val ID_TermsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF58, 0)
                                    val ID_TermsEditer = ID_TermsSP.edit()
                                    ID_TermsEditer.putString("termsandconditions", jresult3.get("termsandconditions") as String)
                                    ID_TermsEditer.commit()

                                    val ID_StatmntSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF59, 0)
                                    val ID_StatmntEditer = ID_StatmntSP.edit()
                                    ID_StatmntEditer.putString("statement", jresult3.get("statement") as String)
                                    ID_StatmntEditer.commit()

                                    val ID_SetngsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF60, 0)
                                    val ID_SetngsSpEditer = ID_SetngsSP.edit()
                                    ID_SetngsSpEditer.putString("settings", jresult3.get("settings") as String)
                                    ID_SetngsSpEditer.commit()

                                    val ID_LogoutSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF61, 0)
                                    val ID_LogoutEditer = ID_LogoutSP.edit()
                                    ID_LogoutEditer.putString("logout", jresult3.get("logout") as String)
                                    ID_LogoutEditer.commit()

                                    val ID_NotifSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF62, 0)
                                    val ID_NotifSpEditer = ID_NotifSP.edit()
                                    ID_NotifSpEditer.putString("NotificationandMessages", jresult3.get("NotificationandMessages") as String)
                                    ID_NotifSpEditer.commit()

                                    val ID_OwnBank = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF63, 0)
                                    val ID_OwnbnkEditer = ID_OwnBank.edit()
                                    ID_OwnbnkEditer.putString("OwnBank", jresult3.get("OwnBank") as String)
                                    ID_OwnbnkEditer.commit()

                                    val ID_OtherBank = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF64, 0)
                                    val ID_OtherBankEditer = ID_OtherBank.edit()
                                    ID_OtherBankEditer.putString("OtherBank", jresult3.get("OtherBank") as String)
                                    ID_OtherBankEditer.commit()

                                    val ID_Quickpay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF65, 0)
                                    val ID_QuickpayEditer = ID_Quickpay.edit()
                                    ID_QuickpayEditer.putString("QuickPay", jresult3.get("QuickPay") as String)
                                    ID_QuickpayEditer.commit()

                                    val ID_Prepaid = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF66, 0)
                                    val ID_PrepaidEditer = ID_Prepaid.edit()
                                    ID_PrepaidEditer.putString("PrepaidMobile", jresult3.get("PrepaidMobile") as String)
                                    ID_PrepaidEditer.commit()

                                    val ID_Postpaid = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF67, 0)
                                    val ID_PostpaidEditer = ID_Postpaid.edit()
                                    ID_PostpaidEditer.putString("PostpaidMobile", jresult3.get("PostpaidMobile") as String)
                                    ID_PostpaidEditer.commit()

                                    val ID_Landline = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF68, 0)
                                    val ID_LandlineEditer = ID_Landline.edit()
                                    ID_LandlineEditer.putString("Landline", jresult3.get("Landline") as String)
                                    ID_LandlineEditer.commit()

                                    val ID_DTH = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF69, 0)
                                    val ID_DTHEditer = ID_DTH.edit()
                                    ID_DTHEditer.putString("DTH", jresult3.get("DTH") as String)
                                    ID_DTHEditer.commit()

                                    val ID_Datacrdpay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF70, 0)
                                    val ID_DatacrdEditer = ID_Datacrdpay.edit()
                                    ID_DatacrdEditer.putString("DataCard", jresult3.get("DataCard") as String)
                                    ID_DatacrdEditer.commit()

                                    val ID_KSEB = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF71, 0)
                                    val ID_KSEBEditer = ID_KSEB.edit()
                                    ID_KSEBEditer.putString("KSEB", jresult3.get("KSEB") as String)
                                    ID_KSEBEditer.commit()

                                    val ID_Histry = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF72, 0)
                                    val ID_HistryEditer = ID_Histry.edit()
                                    ID_HistryEditer.putString("History", jresult3.get("History") as String)
                                    ID_HistryEditer.commit()

                                    val ID_Dashbrd = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF73, 0)
                                    val ID_DashbrdEditer = ID_Dashbrd.edit()
                                    ID_DashbrdEditer.putString("Dashboard", jresult3.get("Dashboard") as String)
                                    ID_DashbrdEditer.commit()

                                    val ID_Virtualcrd = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF74, 0)
                                    val ID_VirtualcrdEditer = ID_Virtualcrd.edit()
                                    ID_VirtualcrdEditer.putString("VirtualCard", jresult3.get("VirtualCard") as String)
                                    ID_VirtualcrdEditer.commit()

                                    val ID_Branch = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF75, 0)
                                    val ID_BranchEditer = ID_Branch.edit()
                                    ID_BranchEditer.putString("BranchDetails", jresult3.get("BranchDetails") as String)
                                    ID_BranchEditer.commit()

                                    val ID_Loanapplictn = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF76, 0)
                                    val ID_LoanapplictnEditer = ID_Loanapplictn.edit()
                                    ID_LoanapplictnEditer.putString("LoanApplication", jresult3.get("LoanApplication") as String)
                                    ID_LoanapplictnEditer.commit()

                                    val ID_Loanstatus = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF77, 0)
                                    val ID_LoanstatusEditer = ID_Loanstatus.edit()
                                    ID_LoanstatusEditer.putString("LoanStatus", jresult3.get("LoanStatus") as String)
                                    ID_LoanstatusEditer.commit()

                                    val ID_PrdctDetail = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF78, 0)
                                    val ID_PrdctDetailEditer = ID_PrdctDetail.edit()
                                    ID_PrdctDetailEditer.putString("ProductDetails", jresult3.get("ProductDetails") as String)
                                    ID_PrdctDetailEditer.commit()

                                    val ID_Emi = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF79, 0)
                                    val ID_EmiEditer = ID_Emi.edit()
                                    ID_EmiEditer.putString("EMICalculator", jresult3.get("EMICalculator") as String)
                                    ID_EmiEditer.commit()

                                    val ID_Deposit = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF80, 0)
                                    val ID_DepositEditer = ID_Deposit.edit()
                                    ID_DepositEditer.putString("DepositCalculator", jresult3.get("DepositCalculator") as String)
                                    ID_DepositEditer.commit()

                                    val ID_Goldloan = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF81, 0)
                                    val ID_GoldloanEditer = ID_Goldloan.edit()
                                    ID_GoldloanEditer.putString("GoldLoanEligibileCalculator", jresult3.get("GoldLoanEligibileCalculator") as String)
                                    ID_GoldloanEditer.commit()

                                    val ID_Enqry = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF82, 0)
                                    val ID_EnqryEditer = ID_Enqry.edit()
                                    ID_EnqryEditer.putString("Enquires", jresult3.get("Enquires") as String)
                                    ID_EnqryEditer.commit()

                                    val ID_Holidy = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF83, 0)
                                    val ID_HolidyEditer = ID_Holidy.edit()
                                    ID_HolidyEditer.putString("HolidayList", jresult3.get("HolidayList") as String)
                                    ID_HolidyEditer.commit()

                                    val ID_Executve = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF84, 0)
                                    val ID_ExecutveEditer = ID_Executve.edit()
                                    ID_ExecutveEditer.putString("ExecutiveCallBack", jresult3.get("ExecutiveCallBack") as String)
                                    ID_ExecutveEditer.commit()

                                    val ID_DEPOSIT = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF85, 0)
                                    val ID_DEPOSITEditer = ID_DEPOSIT.edit()
                                    ID_DEPOSITEditer.putString("DEPOSIT", jresult3.get("DEPOSIT") as String)
                                    ID_DEPOSITEditer.commit()

                                    val ID_LOAN = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF86, 0)
                                    val ID_LOANEditer = ID_LOAN.edit()
                                    ID_LOANEditer.putString("LOAN", jresult3.get("LOAN") as String)
                                    ID_LOANEditer.commit()

                                    val ID_Active = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF87, 0)
                                    val ID_ActiveEditer = ID_Active.edit()
                                    ID_ActiveEditer.putString("Active", jresult3.get("Active") as String)
                                    ID_ActiveEditer.commit()

                                    val ID_Deposit1 = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF88, 0)
                                    val ID_Deposit1Editer = ID_Deposit1.edit()
                                    ID_Deposit1Editer.putString("Deposit", jresult3.get("Deposit") as String)
                                    ID_Deposit1Editer.commit()

                                    val ID_Loan1 = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF89, 0)
                                    val ID_Loan1Editer = ID_Loan1.edit()
                                    ID_Loan1Editer.putString("Loan", jresult3.get("Loan") as String)
                                    ID_Loan1Editer.commit()

                                    val ID_Ownacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF90, 0)
                                    val ID_OwnaccEditer = ID_Ownacc.edit()
                                    ID_OwnaccEditer.putString("OWNACCOUNT", jresult3.get("OWNACCOUNT") as String)
                                    ID_OwnaccEditer.commit()

                                    val ID_Otheracc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF91, 0)
                                    val ID_OtheraccEditer = ID_Otheracc.edit()
                                    ID_OtheraccEditer.putString("OTHERACCOUNT", jresult3.get("OTHERACCOUNT") as String)
                                    ID_OtheraccEditer.commit()

                                    val ID_Selectacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF92, 0)
                                    val ID_SelectaccEditer = ID_Selectacc.edit()
                                    ID_SelectaccEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                    ID_SelectaccEditer.commit()

                                    val ID_Payingfrm = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF93, 0)
                                    val ID_PayingfrmEditer = ID_Payingfrm.edit()
                                    ID_PayingfrmEditer.putString("PayingFrom", jresult3.get("PayingFrom") as String)
                                    ID_PayingfrmEditer.commit()

                                    val ID_Payingto = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF94, 0)
                                    val ID_PayingtoEditer = ID_Payingto.edit()
                                    ID_PayingtoEditer.putString("PayingTo", jresult3.get("PayingTo") as String)
                                    ID_PayingtoEditer.commit()

                                    val ID_Amtpayble = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF95, 0)
                                    val ID_AmtpaybleEditer = ID_Amtpayble.edit()
                                    ID_AmtpaybleEditer.putString("AmountPayable", jresult3.get("AmountPayable") as String)
                                    ID_AmtpaybleEditer.commit()

                                    val ID_Remark = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF96, 0)
                                    val ID_RemarkEditer = ID_Remark.edit()
                                    ID_RemarkEditer.putString("Remark", jresult3.get("Remark") as String)
                                    ID_RemarkEditer.commit()


                                    val ID_Pay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF97, 0)
                                    val ID_PayEditer = ID_Pay.edit()
                                    ID_PayEditer.putString("PAY", jresult3.get("PAY") as String)
                                    ID_PayEditer.commit()

                                    val ID_Receiveracc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF98, 0)
                                    val ID_ReceiveraccEditer = ID_Receiveracc.edit()
                                    ID_ReceiveraccEditer.putString("ReceiverAccountType", jresult3.get("ReceiverAccountType") as String)
                                    ID_ReceiveraccEditer.commit()

                                    val ID_Confirmacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF99, 0)
                                    val ID_ConfirmaccEditer = ID_Confirmacc.edit()
                                    ID_ConfirmaccEditer.putString("ConfirmAccountNo", jresult3.get("ConfirmAccountNo") as String)
                                    ID_ConfirmaccEditer.commit()

                                    val ID_Scan = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF100, 0)
                                    val ID_ScanEditer = ID_Scan.edit()
                                    ID_ScanEditer.putString("Scan", jresult3.get("Scan") as String)
                                    ID_ScanEditer.commit()

                                    val ID_Slctaccnt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF101, 0)
                                    val ID_SlctaccntEditer = ID_Slctaccnt.edit()
                                    ID_SlctaccntEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                    ID_SlctaccntEditer.commit()

                                    val ID_Rechrgehist = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF102, 0)
                                    val ID_RechrgehistEditer = ID_Rechrgehist.edit()
                                    ID_RechrgehistEditer.putString("RechargeHistory", jresult3.get("RechargeHistory") as String)
                                    ID_RechrgehistEditer.commit()

                                    val ID_Frontview = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF103, 0)
                                    val ID_FrontviewEditer = ID_Frontview.edit()
                                    ID_FrontviewEditer.putString("FRONTVIEW", jresult3.get("FRONTVIEW") as String)
                                    ID_FrontviewEditer.commit()

                                    val ID_Backview = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF104, 0)
                                    val ID_BackviewEditer = ID_Backview.edit()
                                    ID_BackviewEditer.putString("BACKVIEW", jresult3.get("BACKVIEW") as String)
                                    ID_BackviewEditer.commit()

                                    val ID_Purpose = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF105, 0)
                                    val ID_PurposeEditer = ID_Purpose.edit()
                                    ID_PurposeEditer.putString("PurposeofVirtualCard", jresult3.get("PurposeofVirtualCard") as String)
                                    ID_PurposeEditer.commit()


                                    val ID_Quit = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF106, 0)
                                    val ID_QuitEditer = ID_Quit.edit()
                                    ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                    ID_QuitEditer.commit()

                                    val ID_Accno = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF107, 0)
                                    val ID_AccnoEditer = ID_Accno.edit()
                                    ID_AccnoEditer.putString("AccountNo", jresult3.get("AccountNo") as String)
                                    ID_AccnoEditer.commit()

                                    val ID_Enterdist = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF108, 0)
                                    val ID_EnterdistEditer = ID_Enterdist.edit()
                                    ID_EnterdistEditer.putString("EnterDistrict", jresult3.get("EnterDistrict") as String)
                                    ID_EnterdistEditer.commit()


                                    val ID_Mobilenum = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF110, 0)
                                    val ID_MobilenumEditer = ID_Mobilenum.edit()
                                    ID_MobilenumEditer.putString("MobileNumber", jresult3.get("MobileNumber") as String)
                                    ID_MobilenumEditer.commit()

                                    val ID_Operator = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF111, 0)
                                    val ID_OperatorEditer = ID_Operator.edit()
                                    ID_OperatorEditer.putString("Operator", jresult3.get("Operator") as String)
                                    ID_OperatorEditer.commit()

                                    val ID_Circle = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF112, 0)
                                    val ID_CircleEditer = ID_Circle.edit()
                                    ID_CircleEditer.putString("Circle", jresult3.get("Circle") as String)
                                    ID_CircleEditer.commit()


                                    val ID_Amt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF113, 0)
                                    val ID_AmtEditer = ID_Amt.edit()
                                    ID_AmtEditer.putString("Amount", jresult3.get("Amount") as String)
                                    ID_AmtEditer.commit()

                                    val ID_Rechrg = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF114, 0)
                                    val ID_RechrgEditer = ID_Rechrg.edit()
                                    ID_RechrgEditer.putString("RECHARGE", jresult3.get("RECHARGE") as String)
                                    ID_RechrgEditer.commit()

                                    val ID_Selctop = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF115, 0)
                                    val ID_SelctopEditer = ID_Selctop.edit()
                                    ID_SelctopEditer.putString("SelectOperator", jresult3.get("SelectOperator") as String)
                                    ID_SelctopEditer.commit()


                                    val ID_Subscriber = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF116, 0)
                                    val ID_SubscriberEditer = ID_Subscriber.edit()
                                    ID_SubscriberEditer.putString("SubscriberID", jresult3.get("SubscriberID") as String)
                                    ID_SubscriberEditer.commit()

                                    val ID_Accnt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF117, 0)
                                    val ID_AccntEditer = ID_Accnt.edit()
                                    ID_AccntEditer.putString("Account", jresult3.get("Account") as String)
                                    ID_AccntEditer.commit()

                                    val ID_viewall = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF118, 0)
                                    val ID_viewallEditer = ID_viewall.edit()
                                    ID_viewallEditer.putString("ViewAllAccounts", jresult3.get("ViewAllAccounts") as String)
                                    ID_viewallEditer.commit()

                                    val ID_availbal = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF119, 0)
                                    val ID_availbalEditer = ID_availbal.edit()
                                    ID_availbalEditer.putString("AvailableBalance", jresult3.get("AvailableBalance") as String)
                                    ID_availbalEditer.commit()

                                    val ID_lastlog = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF120, 0)
                                    val ID_lastlogEditer = ID_lastlog.edit()
                                    ID_lastlogEditer.putString("LastLogin", jresult3.get("LastLogin") as String)
                                    ID_lastlogEditer.commit()

                                    *//* val ID_lastlogtime = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF29, 0)
                                     val ID_lastlogtimeEditer = ID_lastlogtime.edit()
                                     ID_lastlogtimeEditer.putString("LastLoginTime", jresult3.get("LastLoginTime") as String)
                                     ID_lastlogtimeEditer.commit()*//*

                                    val ID_acntdetl = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF121, 0)
                                    val ID_acntdetlEditer = ID_acntdetl.edit()
                                    ID_acntdetlEditer.putString("AccountDetails", jresult3.get("AccountDetails") as String)
                                    ID_acntdetlEditer.commit()

                                    val ID_fundtrns = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF122, 0)
                                    val ID_fundtrnsEditer = ID_fundtrns.edit()
                                    ID_fundtrnsEditer.putString("FundTransfer", jresult3.get("FundTransfer") as String)
                                    ID_fundtrnsEditer.commit()

                                    val ID_rchrgbill = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF123, 0)
                                    val ID_rchrgbillEditer = ID_rchrgbill.edit()
                                    ID_rchrgbillEditer.putString("RechargeBills", jresult3.get("RechargeBills") as String)
                                    ID_rchrgbillEditer.commit()

                                    val ID_reprts = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF124, 0)
                                    val ID_reportsEditer = ID_reprts.edit()
                                    ID_reportsEditer.putString("ReportsOtherServices", jresult3.get("ReportsOtherServices") as String)
                                    ID_reportsEditer.commit()

                                    val ID_tools = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF125, 0)
                                    val ID_toolsEditer = ID_tools.edit()
                                    ID_toolsEditer.putString("ToolsSettings", jresult3.get("ToolsSettings") as String)
                                    ID_toolsEditer.commit()

                                    val ID_Slctprd = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF126, 0)
                                    val ID_SlctprdEditer = ID_Slctprd.edit()
                                    ID_SlctprdEditer.putString("Selectaperiodofyourchoice", jresult3.get("Selectaperiodofyourchoice") as String)
                                    ID_SlctprdEditer.commit()

                                    val ID_Or = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF127, 0)
                                    val ID_OrEditer = ID_Or.edit()
                                    ID_OrEditer.putString("OR", jresult3.get("OR") as String)
                                    ID_OrEditer.commit()

                                    val ID_customdate = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF128, 0)
                                    val ID_customdateEditer = ID_customdate.edit()
                                    ID_customdateEditer.putString("Selectacustomdateofyourchoice.", jresult3.get("Selectacustomdateofyourchoice.") as String)
                                    ID_customdateEditer.commit()

                                    val ID_View = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF129, 0)
                                    val ID_ViewEditer = ID_View.edit()
                                    ID_ViewEditer.putString("View", jresult3.get("View") as String)
                                    ID_ViewEditer.commit()

                                    val ID_downld = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF130, 0)
                                    val ID_downldEditer = ID_downld.edit()
                                    ID_downldEditer.putString("Download", jresult3.get("Download") as String)
                                    ID_downldEditer.commit()

                                    val ID_lastmnth = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF131, 0)
                                    val ID_lastmnthEditer = ID_lastmnth.edit()
                                    ID_lastmnthEditer.putString("LastMonth", jresult3.get("LastMonth") as String)
                                    ID_lastmnthEditer.commit()

                                    val ID_lastthree = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF132, 0)
                                    val ID_lastthreeEditer = ID_lastthree.edit()
                                    ID_lastthreeEditer.putString("Last3Months", jresult3.get("Last3Months") as String)
                                    ID_lastthreeEditer.commit()

                                    val ID_lastsix = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF133, 0)
                                    val ID_lastsixEditer = ID_lastsix.edit()
                                    ID_lastsixEditer.putString("Last6Months", jresult3.get("Last6Months") as String)
                                    ID_lastsixEditer.commit()

                                    val ID_lastone = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF134, 0)
                                    val ID_lastoneEditer = ID_lastone.edit()
                                    ID_lastoneEditer.putString("Last1Year", jresult3.get("Last1Year") as String)
                                    ID_lastoneEditer.commit()

                                    val ID_selctacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF135, 0)
                                    val ID_selctaccEditer = ID_selctacc.edit()
                                    ID_selctaccEditer.putString("SelectAccount", jresult3.get("SelectAccount") as String)
                                    ID_selctaccEditer.commit()

                                    val ID_selctsndr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF136, 0)
                                    val ID_selctsndrEditer = ID_selctsndr.edit()
                                    ID_selctsndrEditer.putString("SelectSender", jresult3.get("SelectSender") as String)
                                    ID_selctsndrEditer.commit()

                                    val ID_selctrecvr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF137, 0)
                                    val ID_selctrecvrEditer = ID_selctrecvr.edit()
                                    ID_selctrecvrEditer.putString("SelectReceiver", jresult3.get("SelectReceiver") as String)
                                    ID_selctrecvrEditer.commit()

                                    val ID_addnewsndr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF138, 0)
                                    val ID_addnewsndrEditer = ID_addnewsndr.edit()
                                    ID_addnewsndrEditer.putString("AddNewSender", jresult3.get("AddNewSender") as String)
                                    ID_addnewsndrEditer.commit()

                                    val ID_addnewrecvr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF139, 0)
                                    val ID_addnewrecvrEditer = ID_addnewrecvr.edit()
                                    ID_addnewrecvrEditer.putString("AddNewReceiver", jresult3.get("AddNewReceiver") as String)
                                    ID_addnewrecvrEditer.commit()

                                    val ID_mpin = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF140, 0)
                                    val ID_mpinEditer = ID_mpin.edit()
                                    ID_mpinEditer.putString("MPIN", jresult3.get("MPIN") as String)
                                    ID_mpinEditer.commit()


                                    val ID_frgtmpin = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF141, 0)
                                    val ID_frgtmpinEditer = ID_frgtmpin.edit()
                                    ID_frgtmpinEditer.putString("ForgotMPIN", jresult3.get("ForgotMPIN") as String)
                                    ID_frgtmpinEditer.commit()

                                    val ID_Makepaymnt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF142, 0)
                                    val ID_MakepaymntEditer = ID_Makepaymnt.edit()
                                    ID_MakepaymntEditer.putString("MAKEPAYMENT", jresult3.get("MAKEPAYMENT") as String)
                                    ID_MakepaymntEditer.commit()

                                    val ID_FirstName = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF143, 0)
                                    val ID_FirstNameEditer = ID_FirstName.edit()
                                    ID_FirstNameEditer.putString("FirstName", jresult3.get("FirstName") as String)
                                    ID_FirstNameEditer.commit()

                                    val ID_LastName = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF144, 0)
                                    val ID_LastNameEditer = ID_LastName.edit()
                                    ID_LastNameEditer.putString("LastName", jresult3.get("LastName") as String)
                                    ID_LastNameEditer.commit()

                                    val ID_Dob = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF145, 0)
                                    val ID_DobEditer = ID_Dob.edit()
                                    ID_DobEditer.putString("DOB", jresult3.get("DOB") as String)
                                    ID_DobEditer.commit()

                                    val ID_Registr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF146, 0)
                                    val ID_RegistrEditer = ID_Registr.edit()
                                    ID_RegistrEditer.putString("REGISTER", jresult3.get("REGISTER") as String)
                                    ID_RegistrEditer.commit()

                                    val ID_SendrName = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF147, 0)
                                    val ID_SendrNameEditer = ID_SendrName.edit()
                                    ID_SendrNameEditer.putString("SenderName", jresult3.get("SenderName") as String)
                                    ID_SendrNameEditer.commit()

                                    val ID_ReceivrName = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF148, 0)
                                    val ID_ReceivrNameEditer = ID_ReceivrName.edit()
                                    ID_ReceivrNameEditer.putString("ReceiverName", jresult3.get("ReceiverName") as String)
                                    ID_ReceivrNameEditer.commit()

                                    val ID_confrmacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF149, 0)
                                    val ID_confrmaccEditer = ID_confrmacc.edit()
                                    ID_confrmaccEditer.putString("ConfirmAccountNumber", jresult3.get("ConfirmAccountNumber") as String)
                                    ID_confrmaccEditer.commit()

                                    val ID_ifsc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF150, 0)
                                    val ID_ifscEditer = ID_ifsc.edit()
                                    ID_ifscEditer.putString("IFSCCode", jresult3.get("IFSCCode") as String)
                                    ID_ifscEditer.commit()

                                    val ID_imps = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF151, 0)
                                    val ID_impsEditer = ID_imps.edit()
                                    ID_impsEditer.putString("IMPS", jresult3.get("IMPS") as String)
                                    ID_impsEditer.commit()

                                    val ID_neft = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF152, 0)
                                    val ID_neftEditer = ID_neft.edit()
                                    ID_neftEditer.putString("NEFT", jresult3.get("NEFT") as String)
                                    ID_neftEditer.commit()

                                    val ID_rtgs = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF153, 0)
                                    val ID_rtgsEditer = ID_rtgs.edit()
                                    ID_rtgsEditer.putString("RTGS", jresult3.get("RTGS") as String)
                                    ID_rtgsEditer.commit()

                                    val ID_fundstat = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF154, 0)
                                    val ID_fundstatEditer = ID_fundstat.edit()
                                    ID_fundstatEditer.putString("FUNDTRANSFERSTATUS", jresult3.get("FUNDTRANSFERSTATUS") as String)
                                    ID_fundstatEditer.commit()

                                    val ID_Benflist = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF157, 0)
                                    val ID_BenflistEditer = ID_Benflist.edit()
                                    ID_BenflistEditer.putString("BeneficiaryList", jresult3.get("BeneficiaryList") as String)
                                    ID_BenflistEditer.commit()

                                    val ID_acc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF158, 0)
                                    val ID_accEditer = ID_acc.edit()
                                    ID_accEditer.putString("AccountNumber", jresult3.get("AccountNumber") as String)
                                    ID_accEditer.commit()

                                    val ID_Benfname = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF159, 0)
                                    val ID_BenfnameEditer = ID_Benfname.edit()
                                    ID_BenfnameEditer.putString("BeneficiaryName", jresult3.get("BeneficiaryName") as String)
                                    ID_BenfnameEditer.commit()

                                    val ID_Benfaccno = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF160, 0)
                                    val ID_BenfaccnoEditer = ID_Benfaccno.edit()
                                    ID_BenfaccnoEditer.putString("BeneficiaryCNo", jresult3.get("BeneficiaryCNo") as String)
                                    ID_BenfaccnoEditer.commit()

                                    val ID_Benfconfrmacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF161, 0)
                                    val ID_BenfconfrmaccEditer = ID_Benfconfrmacc.edit()
                                    ID_BenfconfrmaccEditer.putString("ConfirmBeneficiaryACNo", jresult3.get("ConfirmBeneficiaryACNo") as String)
                                    ID_BenfconfrmaccEditer.commit()

                                    val ID_Savedbenf = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF162, 0)
                                    val ID_SavedbenfEditer = ID_Savedbenf.edit()
                                    ID_SavedbenfEditer.putString("SaveBeneficiaryForFuture", jresult3.get("SaveBeneficiaryForFuture") as String)
                                    ID_SavedbenfEditer.commit()

                                    val ID_lan = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF167, 0)
                                    val ID_lanEditer = ID_lan.edit()
                                    ID_lanEditer.putString("Language", jresult3.get("Language") as String)
                                    ID_lanEditer.commit()

                                    val ID_ASSETS= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF183, 0)
                                    val ID_ASSETSEditer = ID_ASSETS.edit()
                                    ID_ASSETSEditer.putString("Assets", jresult3.get("Assets") as String)
                                    ID_ASSETSEditer.commit()

                                    val ID_LIABLTY= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF184, 0)
                                    val ID_LIABLTYEditer = ID_LIABLTY.edit()
                                    ID_LIABLTYEditer.putString("Liability", jresult3.get("Liability") as String)
                                    ID_LIABLTYEditer.commit()

                                    val ID_paymnt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF185, 0)
                                    val ID_paymntEditer = ID_paymnt.edit()
                                    ID_paymntEditer.putString("PaymentReceipt", jresult3.get("PaymentReceipt") as String)
                                    ID_paymntEditer.commit()

                                    val ID_listasondate= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF209, 0)
                                    val ID_listasondateEditer = ID_listasondate.edit()
                                    ID_listasondateEditer.putString("ListasonDate", jresult3.get("ListasonDate") as String)
                                    ID_listasondateEditer.commit()

                                    val ID_bal= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF210, 0)
                                    val ID_balEditer = ID_bal.edit()
                                    ID_balEditer.putString("Balance", jresult3.get("Balance") as String)
                                    ID_balEditer.commit()
//                                     startActivity(Intent(this@HomeActivity, HomeActivity::class.java))

                                    val ID_min= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF211, 0)
                                    val ID_minEditer = ID_min.edit()
                                    ID_minEditer.putString("MINISTATEMENT", jresult3.get("MINISTATEMENT") as String)
                                    ID_minEditer.commit()

                                    val ID_acntstat= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF212, 0)
                                    val ID_acntstatEditer = ID_acntstat.edit()
                                    ID_acntstatEditer.putString("ACCOUNTSTATEMENT", jresult3.get("ACCOUNTSTATEMENT") as String)
                                    ID_acntstatEditer.commit()

                                    val ID_moreopt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF213, 0)
                                    val ID_moreoptEditer = ID_moreopt.edit()
                                    ID_moreoptEditer.putString("MOREOPTIONS", jresult3.get("MOREOPTIONS") as String)
                                    ID_moreoptEditer.commit()

                                    val ID_acntsummary= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF214, 0)
                                    val ID_acntsummaryEditer = ID_acntsummary.edit()
                                    ID_acntsummaryEditer.putString("AccountSummary", jresult3.get("AccountSummary") as String)
                                    ID_acntsummaryEditer.commit()

                                    val ID_standins= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF215, 0)
                                    val ID_standinsEditer = ID_standins.edit()
                                    ID_standinsEditer.putString("StandingInstruction", jresult3.get("StandingInstruction") as String)
                                    ID_standinsEditer.commit()

                                    val ID_notce= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF216, 0)
                                    val ID_notceEditer = ID_notce.edit()
                                    ID_notceEditer.putString("Notice", jresult3.get("Notice") as String)
                                    ID_notceEditer.commit()


                                    val ID_ListingDataforpast= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF217, 0)
                                    val ListingDataforpastEditer = ID_ListingDataforpast.edit()
                                    ListingDataforpastEditer.putString("ListingDataforpast", jresult3.get("ListingDataforpast.") as String)
                                    ListingDataforpastEditer.commit()

                                    val ID_days= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF218, 0)
                                    val daysEditer = ID_days.edit()
                                    daysEditer.putString("days", jresult3.get("days") as String)
                                    daysEditer.commit()



                                    val ID_youcanchangeitfromsettings= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF219, 0)
                                    val youcanchangeitfromsettingsEditer = ID_youcanchangeitfromsettings.edit()
                                    youcanchangeitfromsettingsEditer.putString("youcanchangeitfromsettings", jresult3.get("youcanchangeitfromsettings") as String)
                                    youcanchangeitfromsettingsEditer.commit()

                                    val ID_Clear= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF220, 0)
                                    val ClearEditer = ID_Clear.edit()
                                    ClearEditer.putString("Clear", jresult3.get("Clear") as String)
                                    ClearEditer.commit()



                                    val ID_LoanPeriod= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF221, 0)
                                    val LoanPeriodEditer = ID_LoanPeriod.edit()
                                    LoanPeriodEditer.putString("LoanPeriod", jresult3.get("LoanPeriod") as String)
                                    LoanPeriodEditer.commit()

                                    val ID_Weight= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF222, 0)
                                    val WeightEditer = ID_Weight.edit()
                                    WeightEditer.putString("Weight", jresult3.get("Weight") as String)
                                    WeightEditer.commit()

                                    val ID_EnterAmount= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF223, 0)
                                    val EnterAmountEditer = ID_EnterAmount.edit()
                                    EnterAmountEditer.putString("EnterAmount", jresult3.get("EnterAmount") as String)
                                    EnterAmountEditer.commit()

                                    val ID_SelectBranch= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF224, 0)
                                    val SelectBranchEditer = ID_SelectBranch.edit()
                                    SelectBranchEditer.putString("SelectBranch", jresult3.get("SelectBranch") as String)
                                    SelectBranchEditer.commit()

                                    val ID_Pleaseselectpayingtoaccountnumber= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF225, 0)
                                    val PleaseselectpayingtoaccountnumberEditer = ID_Pleaseselectpayingtoaccountnumber.edit()
                                    PleaseselectpayingtoaccountnumberEditer.putString("Pleaseselectpayingtoaccountnumber", jresult3.get("Pleaseselectpayingtoaccountnumber") as String)
                                    PleaseselectpayingtoaccountnumberEditer.commit()

                                    val ID_PayableAmount= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF226, 0)
                                    val PayableAmountEditer = ID_PayableAmount.edit()
                                    PayableAmountEditer.putString("PayableAmount", jresult3.get("PayableAmount") as String)
                                    PayableAmountEditer.commit()


                                    val ID_Atleast3digitsarerequired= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF227, 0)
                                    val Atleast3digitsarerequiredEditer = ID_Atleast3digitsarerequired.edit()
                                    Atleast3digitsarerequiredEditer.putString("Atleast3digitsarerequired", jresult3.get("Atleast3digitsarerequired.") as String)
                                    Atleast3digitsarerequiredEditer.commit()

                                    val ID_Atleast6digitsarerequired= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF228, 0)
                                    val Atleast6digitsarerequiredEditer = ID_Atleast6digitsarerequired.edit()
                                    Atleast6digitsarerequiredEditer.putString("Atleast6digitsarerequired", jresult3.get("Atleast6digitsarerequired.") as String)
                                    Atleast6digitsarerequiredEditer.commit()

                                    val ID_PleaseEnterBeneficiaryName= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF229, 0)
                                    val PleaseEnterBeneficiaryNameEditer = ID_PleaseEnterBeneficiaryName.edit()
                                    PleaseEnterBeneficiaryNameEditer.putString("PleaseEnterBeneficiaryName", jresult3.get("PleaseEnterBeneficiaryName") as String)
                                    PleaseEnterBeneficiaryNameEditer.commit()

                                    val ID_PleaseEnterValidBeneficiaryAccountNumber= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF230, 0)
                                    val PleaseEnterValidBeneficiaryAccountNumberEditer = ID_PleaseEnterValidBeneficiaryAccountNumber.edit()
                                    PleaseEnterValidBeneficiaryAccountNumberEditer.putString("PleaseEnterValidBeneficiaryAccountNumber", jresult3.get("PleaseEnterValidBeneficiaryAccountNumber") as String)
                                    PleaseEnterValidBeneficiaryAccountNumberEditer.commit()




                                    val ID_PleaseentervalidConfirmBeneficiaryaccountnumber= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF231, 0)
                                    val PleaseentervalidConfirmBeneficiaryaccountnumberEditer = ID_PleaseentervalidConfirmBeneficiaryaccountnumber.edit()
                                    PleaseentervalidConfirmBeneficiaryaccountnumberEditer.putString("PleaseentervalidConfirmBeneficiaryaccountnumber", jresult3.get("PleaseentervalidConfirmBeneficiaryaccountnumber") as String)
                                    PleaseentervalidConfirmBeneficiaryaccountnumberEditer.commit()

                                    val ID_SelectavalidAccountNumber= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF232, 0)
                                    val SelectavalidAccountNumberEditer = ID_SelectavalidAccountNumber.edit()
                                    SelectavalidAccountNumberEditer.putString("SelectavalidAccountNumber", jresult3.get("SelectavalidAccountNumber") as String)
                                    SelectavalidAccountNumberEditer.commit()

                                    val ID_PleaseEnterYourFeedback= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF233, 0)
                                    val PleaseEnterYourFeedbackEditer = ID_PleaseEnterYourFeedback.edit()
                                    PleaseEnterYourFeedbackEditer.putString("PleaseEnterYourFeedback", jresult3.get("PleaseEnterYourFeedback") as String)
                                    PleaseEnterYourFeedbackEditer.commit()

                                    val ID_Giveusacall= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF234, 0)
                                    val GiveusacallEditer = ID_Giveusacall.edit()
                                    GiveusacallEditer.putString("Giveusacall", jresult3.get("Giveusacall") as String)
                                    GiveusacallEditer.commit()

                                    val ID_Sendusamessage= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF235, 0)
                                    val SendusamessageEditer = ID_Sendusamessage.edit()
                                    SendusamessageEditer.putString("Sendusamessage", jresult3.get("Sendusamessage") as String)
                                    SendusamessageEditer.commit()

                                    val ID_Visitourlocation= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF236, 0)
                                    val VisitourlocationEditer = ID_Visitourlocation.edit()
                                    VisitourlocationEditer.putString("Visitourlocation", jresult3.get("Visitourlocation") as String)
                                    VisitourlocationEditer.commit()

                                    val ID_Aboutustext= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF237, 0)
                                    val AboutustextEditer = ID_Aboutustext.edit()
                                    AboutustextEditer.putString("Aboutustext", jresult3.get("Aboutustext") as String)
                                    AboutustextEditer.commit()

                                    val ID_Privacypolicytext= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF238, 0)
                                    val PrivacypolicytextEditer = ID_Privacypolicytext.edit()
                                    PrivacypolicytextEditer.putString("Privacypolicytext", jresult3.get("Privacypolicytext") as String)
                                    PrivacypolicytextEditer.commit()

                                    val ID_TermsandConditionstext= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF239, 0)
                                    val TermsandConditionstextEditer = ID_TermsandConditionstext.edit()
                                    TermsandConditionstextEditer.putString("TermsandConditionstext", jresult3.get("TermsandConditionstext") as String)
                                    TermsandConditionstextEditer.commit()

                                    val ID_Apply= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF240, 0)
                                    val ApplyEditer = ID_Apply.edit()
                                    ApplyEditer.putString("Apply", jresult3.get("Apply") as String)
                                    ApplyEditer.commit()

                                    val ID_acnttyp= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF241, 0)
                                    val ID_acnttypEditer = ID_acnttyp.edit()
                                    ID_acnttypEditer.putString("AccountType", jresult3.get("AccountType") as String)
                                    ID_acnttypEditer.commit()


                                    val ID_unclramt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF242, 0)
                                    val ID_unclramtEditer = ID_unclramt.edit()
                                    ID_unclramtEditer.putString("UnclearAmount", jresult3.get("UnclearAmount") as String)
                                    ID_unclramtEditer.commit()



                                    val ID_reset= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF189, 0)
                                    val ID_resetEditer = ID_reset.edit()
                                    ID_resetEditer.putString("RESET", jresult3.get("RESET") as String)
                                    ID_resetEditer.commit()

                                    val ID_transupdte= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF244, 0)
                                    val ID_transupdteEditer = ID_transupdte.edit()
                                    ID_transupdteEditer.putString("TransactionUpdate(Days)", jresult3.get("TransactionUpdate(Days)") as String)
                                    ID_transupdteEditer.commit()

                                    val ID_updteintrvl= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF245, 0)
                                    val ID_updteintrvlEditer = ID_updteintrvl.edit()
                                    ID_updteintrvlEditer.putString("UpdateInterval", jresult3.get("UpdateInterval") as String)
                                    ID_updteintrvlEditer.commit()

                                    val ID_defltacc= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF246, 0)
                                    val ID_defltaccEditer = ID_defltacc.edit()
                                    ID_defltaccEditer.putString("DefaultAccount", jresult3.get("DefaultAccount") as String)
                                    ID_defltaccEditer.commit()

                                    val ID_giveuscall= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF247, 0)
                                    val ID_giveuscallEditer = ID_giveuscall.edit()
                                    ID_giveuscallEditer.putString("Giveusacall", jresult3.get("Giveusacall") as String)
                                    ID_giveuscallEditer.commit()

                                    val ID_sendus= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF248, 0)
                                    val ID_sendusEditer = ID_sendus.edit()
                                    ID_sendusEditer.putString("Sendusamessage", jresult3.get("Sendusamessage") as String)
                                    ID_sendusEditer.commit()

                                    val ID_visitloc= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF249, 0)
                                    val ID_visitlocEditer = ID_visitloc.edit()
                                    ID_visitlocEditer.putString("Visitourlocation", jresult3.get("Visitourlocation") as String)
                                    ID_visitlocEditer.commit()

                                    val ID_submt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF250, 0)
                                    val ID_submtEditer = ID_submt.edit()
                                    ID_submtEditer.putString("Submit", jresult3.get("Submit") as String)
                                    ID_submtEditer.commit()

                                    val ID_ownaccfndtransfr= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF251, 0)
                                    val ID_ownaccfndtransfrEditer = ID_ownaccfndtransfr.edit()
                                    ID_ownaccfndtransfrEditer.putString("OwnAccountFundTransfer", jresult3.get("OwnAccountFundTransfer") as String)
                                    ID_ownaccfndtransfrEditer.commit()

                                    val ID_othraccfndtransfr= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF252, 0)
                                    val ID_othraccfndtransfrEditer = ID_othraccfndtransfr.edit()
                                    ID_othraccfndtransfrEditer.putString("OtherAccountFundTransfer", jresult3.get("OtherAccountFundTransfer") as String)
                                    ID_othraccfndtransfrEditer.commit()

                                    val ID_transferupto= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF253, 0)
                                    val ID_transferuptoEditer = ID_transferupto.edit()
                                    ID_transferuptoEditer.putString("Transfer upto", jresult3.get("Transfer upto") as String)
                                    ID_transferuptoEditer.commit()

                                    val ID_instantly= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF254, 0)
                                    val ID_instantlyEditer = ID_instantly.edit()
                                    ID_instantlyEditer.putString("Instantly", jresult3.get("Instantly") as String)
                                    ID_instantlyEditer.commit()

                                    val ID_weight= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF255, 0)
                                    val ID_weightEditer = ID_weight.edit()
                                    ID_weightEditer.putString("Weight", jresult3.get("Weight") as String)
                                    ID_weightEditer.commit()

                                    val ID_emitype= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF256, 0)
                                    val ID_emitypeEditer = ID_emitype.edit()
                                    ID_emitypeEditer.putString("EMITYPE", jresult3.get("EMITYPE") as String)
                                    ID_emitypeEditer.commit()

                                    val ID_ENTRWEIGHT= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF257, 0)
                                    val ID_ENTRWEIGHTEditer = ID_ENTRWEIGHT.edit()
                                    ID_ENTRWEIGHTEditer.putString("Enter Weight", jresult3.get("Enter Weight") as String)
                                    ID_ENTRWEIGHTEditer.commit()

                                    val ID_Plsentrweght= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF258, 0)
                                    val ID_PlsentrweghtEditer = ID_Plsentrweght.edit()
                                    ID_PlsentrweghtEditer.putString("PleaseEnterWeight", jresult3.get("PleaseEnterWeight") as String)
                                    ID_PlsentrweghtEditer.commit()

                                    val ID_entramt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF259, 0)
                                    val ID_entramtEditer = ID_entramt.edit()
                                    ID_entramtEditer.putString("Pleaseenteramount", jresult3.get("Pleaseenteramount") as String)
                                    ID_entramtEditer.commit()

                                    val ID_entrprincamt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF260, 0)
                                    val ID_entrprincamtEditer = ID_entrprincamt.edit()
                                    ID_entrprincamtEditer.putString("EnterPrincipalAmount", jresult3.get("EnterPrincipalAmount") as String)
                                    ID_entrprincamtEditer.commit()

                                    val ID_entrmnth= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF261, 0)
                                    val ID_entrmnthEditer = ID_entrmnth.edit()
                                    ID_entrmnthEditer.putString("EnterMonth", jresult3.get("EnterMonth") as String)
                                    ID_entrmnthEditer.commit()

                                    val ID_entrintrstrate= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF262, 0)
                                    val ID_entrintrstrateEditer = ID_entrintrstrate.edit()
                                    ID_entrintrstrateEditer.putString("EnterInterestRate", jresult3.get("EnterInterestRate") as String)
                                    ID_entrintrstrateEditer.commit()
*/

                                    val ID_WelcomeSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF34, 0)
                                    val ID_WelcomeSPEditer = ID_WelcomeSP.edit()
                                    ID_WelcomeSPEditer.putString("welcome", jresult3.get("welcome") as String)
                                    ID_WelcomeSPEditer.commit()

                                    val ID_cnmbr= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF310, 0)
                                    val ID_cnmbrEditer = ID_cnmbr.edit()
                                    ID_cnmbrEditer.putString("CustomerNumber1", jresult3.get("CustomerNumber") as String)
                                    ID_cnmbrEditer.commit()


                                    val ID_FasterSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF35, 0)
                                    val ID_FasterSPEditer = ID_FasterSP.edit()
                                    ID_FasterSPEditer.putString("fasterwaytohelpyou", jresult3.get("fasterwaytohelpyou") as String)
                                    ID_FasterSPEditer.commit()

                                    val ID_SigninSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF36, 0)
                                    val ID_SigninSPEditer = ID_SigninSP.edit()
                                    ID_SigninSPEditer.putString("sigin", jresult3.get("sigin") as String)
                                    ID_SigninSPEditer.commit()

                                    val ID_RegisterSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF37, 0)
                                    val ID_RegisterSPEditer = ID_RegisterSP.edit()
                                    ID_RegisterSPEditer.putString("registernow", jresult3.get("registernow") as String)
                                    ID_RegisterSPEditer.commit()

                                    val ID_SelctlanSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF38, 0)
                                    val ID_SelctlanSPEditer = ID_SelctlanSP.edit()
                                    ID_SelctlanSPEditer.putString("SelectLanguage", jresult3.get("SelectLanguage") as String)
                                    ID_SelctlanSPEditer.commit()

                                    val ID_SkipSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF39, 0)
                                    val ID_SkipSPEditer = ID_SkipSP.edit()
                                    ID_SkipSPEditer.putString("Skip", jresult3.get("Skip") as String)
                                    ID_SkipSPEditer.commit()


                                    val ID_LetsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF40, 0)
                                    val ID_LetsSPEditer = ID_LetsSP.edit()
                                    ID_LetsSPEditer.putString("Let'sgetstarted", jresult3.get("Let'sgetstarted") as String)
                                    ID_LetsSPEditer.commit()


                                    val ID_add= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF299, 0)
                                    val ID_addEditer = ID_add.edit()
                                    ID_addEditer.putString("Address1", jresult3.get("Address") as String)
                                    ID_addEditer.commit()

                                    val ID_mail= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF300, 0)
                                    val ID_mailEditer = ID_mail.edit()
                                    ID_mailEditer.putString("Email1", jresult3.get("Email1") as String)
                                    ID_mailEditer.commit()


                                    val ID_gndr= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF308, 0)
                                    val ID_gndrEditer = ID_gndr.edit()
                                    ID_gndrEditer.putString("Gender1", jresult3.get("Gender1") as String)
                                    ID_gndrEditer.commit()


                                    val ID_dob1= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF309, 0)
                                    val ID_dob1Editer = ID_dob1.edit()
                                    ID_dob1Editer.putString("DateofBirth1", jresult3.get("DateofBirth1") as String)
                                    ID_dob1Editer.commit()

                                    val ID_entramt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF259, 0)
                                    val ID_entramtEditer = ID_entramt.edit()
                                    ID_entramtEditer.putString("Pleaseenteramount", jresult3.get("Pleaseenteramount") as String)
                                    ID_entramtEditer.commit()

                                    val ID_plsentrfnme= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF303, 0)
                                    val ID_plsentrfnmeEditer = ID_plsentrfnme.edit()
                                    ID_plsentrfnmeEditer.putString("PleaseEnterFirstName", jresult3.get("PleaseEnterFirstName") as String)
                                    ID_plsentrfnmeEditer.commit()

                                    val ID_plsentrlnme= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF304, 0)
                                    val ID_plsentrlnmeEditer = ID_plsentrlnme.edit()
                                    ID_plsentrlnmeEditer.putString("PleaseEnterLastName", jresult3.get("PleaseEnterLastName") as String)
                                    ID_plsentrlnmeEditer.commit()

                                    val ID_plsentmob= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF281, 0)
                                    val ID_plsentmobEditer = ID_plsentmob.edit()
                                    ID_plsentmobEditer.putString("PleaseEnterMobileNumber", jresult3.get("PleaseEnterMobileNumber") as String)
                                    ID_plsentmobEditer.commit()


                                    val ID_plssvalidmob= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF287, 0)
                                    val ID_plssvalidmobEditer = ID_plssvalidmob.edit()
                                    ID_plssvalidmobEditer.putString("PleaseEnterValidMobileNumber", jresult3.get("PleaseEnterValidMobileNumber") as String)
                                    ID_plssvalidmobEditer.commit()


                                    val ID_selctsndr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF136, 0)
                                    val ID_selctsndrEditer = ID_selctsndr.edit()
                                    ID_selctsndrEditer.putString("SelectSender", jresult3.get("SelectSender") as String)
                                    ID_selctsndrEditer.commit()


                                    val ID_atleast3 = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF330, 0)
                                    val ID_atleast3Editer = ID_atleast3.edit()
                                    ID_atleast3Editer.putString("Atleast3digitsarerequired.", jresult3.get("Atleast3digitsarerequired.") as String)
                                    ID_atleast3Editer.commit()

                                    val ID_atleast6 = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF331, 0)
                                    val ID_atleast6Editer = ID_atleast6.edit()
                                    ID_atleast6Editer.putString("Atleast6digitsarerequired.", jresult3.get("Atleast6digitsarerequired.") as String)
                                    ID_atleast6Editer.commit()


                                    val ID_plsslctcircle= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF302, 0)
                                    val ID_plsslctcircleEditer = ID_plsslctcircle.edit()
                                    ID_plsslctcircleEditer.putString("PleaseSelectCircle", jresult3.get("PleaseSelectCircle") as String)
                                    ID_plsslctcircleEditer.commit()

                                    val ID_plsslctoprtr= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF301, 0)
                                    val ID_plsslctoprtrEditer = ID_plsslctoprtr.edit()
                                    ID_plsslctoprtrEditer.putString("PleaseSelectOperator", jresult3.get("PleaseSelectOperator") as String)
                                    ID_plsslctoprtrEditer.commit()

                                    val ID_plsslctacc= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF284, 0)
                                    val ID_plsslctaccEditer = ID_plsslctacc.edit()
                                    ID_plsslctaccEditer.putString("PleaseSelectAccount", jresult3.get("PleaseSelectAccount") as String)
                                    ID_plsslctaccEditer.commit()



                                    val ID_entrcvrnme = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF324, 0)
                                    val ID_entrcvrnmeEditer = ID_entrcvrnme.edit()
                                    ID_entrcvrnmeEditer.putString("PleaseEnterReceiverName", jresult3.get("PleaseEnterReceiverName") as String)
                                    ID_entrcvrnmeEditer.commit()


                                    val ID_entrifsc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF325, 0)
                                    val ID_entrifscEditer = ID_entrifsc.edit()
                                    ID_entrifscEditer.putString("PleaseenterIFSCcode", jresult3.get("PleaseenterIFSCcode") as String)
                                    ID_entrifscEditer.commit()

                                    val ID_invaldifsc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF326, 0)
                                    val ID_invaldifscEditer = ID_invaldifsc.edit()
                                    ID_invaldifscEditer.putString("Invalidifsc", jresult3.get("Invalidifsc") as String)
                                    ID_invaldifscEditer.commit()

                                    val ID_plsentaccno = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF327, 0)
                                    val ID_plsentaccnoEditer = ID_plsentaccno.edit()
                                    ID_plsentaccnoEditer.putString("PleaseEnterAccountNumber", jresult3.get("PleaseEnterAccountNumber") as String)
                                    ID_plsentaccnoEditer.commit()

                                    val ID_plscnfrmacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF328, 0)
                                    val ID_plscnfrmaccEditer = ID_plscnfrmacc.edit()
                                    ID_plscnfrmaccEditer.putString("PleaseEnterConfirmAccountNumber", jresult3.get("PleaseEnterConfirmAccountNumber") as String)
                                    ID_plscnfrmaccEditer.commit()

                                    val ID_accndcnfrm = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF329, 0)
                                    val ID_accndcnfrmEditer = ID_accndcnfrm.edit()
                                    ID_accndcnfrmEditer.putString("AccountnumberandConfirmAccountnumbernotmatching", jresult3.get("AccountnumberandConfirmAccountnumbernotmatching") as String)
                                    ID_accndcnfrmEditer.commit()

                                    val ID_plsentrconsno= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF286, 0)
                                    val ID_plsentrconsnoEditer = ID_plsentrconsno.edit()
                                    ID_plsentrconsnoEditer.putString("PleaseEnterConsumerNumber", jresult3.get("PleaseEnterConsumerNumber") as String)
                                    ID_plsentrconsnoEditer.commit()


                                    val ID_plentrvalidsubscrbrid= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF332, 0)
                                    val ID_plentrvalidsubscrbridEditer = ID_plentrvalidsubscrbrid.edit()
                                    ID_plentrvalidsubscrbridEditer.putString("Pleaseentervalidsubscriberid", jresult3.get("Pleaseentervalidsubscriberid") as String)
                                    ID_plentrvalidsubscrbridEditer.commit()

                                    val ID_PersnlinfSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF41, 0)
                                    val ID_PersnlinfEditer = ID_PersnlinfSP.edit()
                                    ID_PersnlinfEditer.putString("pleaseenteryourpersonalinformation", jresult3.get("pleaseenteryourpersonalinformation") as String)
                                    ID_PersnlinfEditer.commit()

                                    val ID_EntermobSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF42, 0)
                                    val ID_EntermobEditer = ID_EntermobSP.edit()
                                    ID_EntermobEditer.putString("entermobilenumber", jresult3.get("entermobilenumber") as String)
                                    ID_EntermobEditer.commit()



                                    /*    val ID_last4SP = mContext.getSharedPreferences(Config.SHARED_PREF43, 0)
                                    val ID_last4SPEditer = ID_last4SP.edit()
                                    ID_last4SPEditer.putString("enter last4digitofa/cno", jresult3.get("enter last4digitofa/cno") as String)
                                    ID_last4SPEditer.commit()*/

                                    val ID_ContinueSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF44, 0)
                                    val ID_ContinueSPEditer = ID_ContinueSP.edit()
                                    ID_ContinueSPEditer.putString("continue", jresult3.get("continue") as String)
                                    ID_ContinueSPEditer.commit()

                                    val ID_LoginmobSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF45, 0)
                                    val ID_LoginMobSPEditer = ID_LoginmobSP.edit()
                                    ID_LoginMobSPEditer.putString("loginwithmobilenumber", jresult3.get("loginwithmobilenumber") as String)
                                    ID_LoginMobSPEditer.commit()

                                    /*  val ID_MobotpeSP = mContext.getSharedPreferences(Config.SHARED_PREF46, 0)
                                    val ID_MobotpSPEditer = ID_MobotpeSP.edit()
                                    ID_MobotpSPEditer.putString("enteryourmobilenumberwewillsentyouOTPtoverify", jresult3.get("enteryourmobilenumberwewillsentyouOTPtoverify") as String)
                                    ID_MobotpSPEditer.commit()
*/
                                    val ID_LoginverifySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF47, 0)
                                    val ID_LoginVerifySPEditer = ID_LoginverifySP.edit()
                                    ID_LoginVerifySPEditer.putString("userloginverified", jresult3.get("userloginverified") as String)
                                    ID_LoginVerifySPEditer.commit()

                                    val ID_OtpverifySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF48, 0)
                                    val ID_OtpVerifySPEditer = ID_OtpverifySP.edit()
                                    ID_OtpVerifySPEditer.putString("Otpverification", jresult3.get("Otpverification") as String)
                                    ID_OtpVerifySPEditer.commit()


                                    val ID_licnse= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF305, 0)
                                    val ID_licnseEditer = ID_licnse.edit()
                                    ID_licnseEditer.putString("LICENSEDTO", jresult3.get("LICENSEDTO") as String)
                                    ID_licnseEditer.commit()

                                    val ID_tchpart= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF306, 0)
                                    val ID_tchpartEditer = ID_tchpart.edit()
                                    ID_tchpartEditer.putString("TECHNOLOGYPARTNER", jresult3.get("TECHNOLOGYPARTNER") as String)
                                    ID_tchpartEditer.commit()

                                    val ID_MyaccSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF50, 0)
                                    val ID_MyaccSPEditer = ID_MyaccSP.edit()
                                    ID_MyaccSPEditer.putString("Myaccounts", jresult3.get("Myaccounts") as String)
                                    ID_MyaccSPEditer.commit()

                                    val ID_PassbkSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF51, 0)
                                    val ID_PassbkSPEditer = ID_PassbkSP.edit()
                                    ID_PassbkSPEditer.putString("passbook", jresult3.get("passbook") as String)
                                    ID_PassbkSPEditer.commit()

                                    val ID_QuickbalSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF52, 0)
                                    val ID_QuickbalSPEditer = ID_QuickbalSP.edit()
                                    ID_QuickbalSPEditer.putString("quickbalance", jresult3.get("quickbalance") as String)
                                    ID_QuickbalSPEditer.commit()

                                    val ID_DueremindSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF53, 0)
                                    val ID_DueremindEditer = ID_DueremindSP.edit()
                                    ID_DueremindEditer.putString("duereminder", jresult3.get("duereminder") as String)
                                    ID_DueremindEditer.commit()

                                    val ID_AbtusSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF54, 0)
                                    val ID_AbtusEditer = ID_AbtusSP.edit()
                                    ID_AbtusEditer.putString("aboutus", jresult3.get("aboutus") as String)
                                    ID_AbtusEditer.commit()

                                    val ID_ContactSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF55, 0)
                                    val ID_ContactEditer = ID_ContactSP.edit()
                                    ID_ContactEditer.putString("contactus", jresult3.get("contactus") as String)
                                    ID_ContactEditer.commit()

                                    val ID_FeebkSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF56, 0)
                                    val ID_FeedbkEditer = ID_FeebkSP.edit()
                                    ID_FeedbkEditer.putString("feedback", jresult3.get("feedback") as String)
                                    ID_FeedbkEditer.commit()

                                    val ID_PrivacySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF57, 0)
                                    val ID_PrivacyEditer = ID_PrivacySP.edit()
                                    ID_PrivacyEditer.putString("privacypolicy", jresult3.get("privacypolicy") as String)
                                    ID_PrivacyEditer.commit()

                                    val ID_TermsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF58, 0)
                                    val ID_TermsEditer = ID_TermsSP.edit()
                                    ID_TermsEditer.putString("termsandconditions", jresult3.get("termsandconditions") as String)
                                    ID_TermsEditer.commit()

                                    val ID_StatmntSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF59, 0)
                                    val ID_StatmntEditer = ID_StatmntSP.edit()
                                    ID_StatmntEditer.putString("statement", jresult3.get("statement") as String)
                                    ID_StatmntEditer.commit()

                                    val ID_SetngsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF60, 0)
                                    val ID_SetngsSpEditer = ID_SetngsSP.edit()
                                    ID_SetngsSpEditer.putString("settings", jresult3.get("settings") as String)
                                    ID_SetngsSpEditer.commit()

                                    val ID_LogoutSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF61, 0)
                                    val ID_LogoutEditer = ID_LogoutSP.edit()
                                    ID_LogoutEditer.putString("logout", jresult3.get("logout") as String)
                                    ID_LogoutEditer.commit()

                                    val ID_NotifSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF62, 0)
                                    val ID_NotifSpEditer = ID_NotifSP.edit()
                                    ID_NotifSpEditer.putString("NotificationandMessages", jresult3.get("NotificationandMessages") as String)
                                    ID_NotifSpEditer.commit()

                                    val ID_OwnBank = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF63, 0)
                                    val ID_OwnbnkEditer = ID_OwnBank.edit()
                                    ID_OwnbnkEditer.putString("OwnBank", jresult3.get("OwnBank") as String)
                                    ID_OwnbnkEditer.commit()

                                    val ID_OtherBank = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF64, 0)
                                    val ID_OtherBankEditer = ID_OtherBank.edit()
                                    ID_OtherBankEditer.putString("OtherBank", jresult3.get("OtherBank") as String)
                                    ID_OtherBankEditer.commit()

                                    val ID_Quickpay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF65, 0)
                                    val ID_QuickpayEditer = ID_Quickpay.edit()
                                    ID_QuickpayEditer.putString("QuickPay", jresult3.get("QuickPay") as String)
                                    ID_QuickpayEditer.commit()

                                    val ID_Prepaid = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF66, 0)
                                    val ID_PrepaidEditer = ID_Prepaid.edit()
                                    ID_PrepaidEditer.putString("PrepaidMobile", jresult3.get("PrepaidMobile") as String)
                                    ID_PrepaidEditer.commit()

                                    val ID_Postpaid = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF67, 0)
                                    val ID_PostpaidEditer = ID_Postpaid.edit()
                                    ID_PostpaidEditer.putString("PostpaidMobile", jresult3.get("PostpaidMobile") as String)
                                    ID_PostpaidEditer.commit()

                                    val ID_Landline = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF68, 0)
                                    val ID_LandlineEditer = ID_Landline.edit()
                                    ID_LandlineEditer.putString("Landline", jresult3.get("Landline") as String)
                                    ID_LandlineEditer.commit()

                                    val ID_DTH = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF69, 0)
                                    val ID_DTHEditer = ID_DTH.edit()
                                    ID_DTHEditer.putString("DTH", jresult3.get("DTH") as String)
                                    ID_DTHEditer.commit()

                                    val ID_Datacrdpay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF70, 0)
                                    val ID_DatacrdEditer = ID_Datacrdpay.edit()
                                    ID_DatacrdEditer.putString("DataCard", jresult3.get("DataCard") as String)
                                    ID_DatacrdEditer.commit()

                                    val ID_KSEB = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF71, 0)
                                    val ID_KSEBEditer = ID_KSEB.edit()
                                    ID_KSEBEditer.putString("KSEB", jresult3.get("KSEB") as String)
                                    ID_KSEBEditer.commit()

                                    val ID_Histry = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF72, 0)
                                    val ID_HistryEditer = ID_Histry.edit()
                                    ID_HistryEditer.putString("History", jresult3.get("History") as String)
                                    ID_HistryEditer.commit()

                                    val ID_Dashbrd = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF73, 0)
                                    val ID_DashbrdEditer = ID_Dashbrd.edit()
                                    ID_DashbrdEditer.putString("Dashboard", jresult3.get("Dashboard") as String)
                                    ID_DashbrdEditer.commit()

                                    val ID_Virtualcrd = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF74, 0)
                                    val ID_VirtualcrdEditer = ID_Virtualcrd.edit()
                                    ID_VirtualcrdEditer.putString("VirtualCard", jresult3.get("VirtualCard") as String)
                                    ID_VirtualcrdEditer.commit()

                                    val ID_Branch = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF75, 0)
                                    val ID_BranchEditer = ID_Branch.edit()
                                    ID_BranchEditer.putString("BranchDetails", jresult3.get("BranchDetails") as String)
                                    ID_BranchEditer.commit()

                                    val ID_Loanapplictn = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF76, 0)
                                    val ID_LoanapplictnEditer = ID_Loanapplictn.edit()
                                    ID_LoanapplictnEditer.putString("LoanApplication", jresult3.get("LoanApplication") as String)
                                    ID_LoanapplictnEditer.commit()

                                    val ID_Loanstatus = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF77, 0)
                                    val ID_LoanstatusEditer = ID_Loanstatus.edit()
                                    ID_LoanstatusEditer.putString("LoanStatus", jresult3.get("LoanStatus") as String)
                                    ID_LoanstatusEditer.commit()

                                    val ID_PrdctDetail = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF78, 0)
                                    val ID_PrdctDetailEditer = ID_PrdctDetail.edit()
                                    ID_PrdctDetailEditer.putString("ProductDetails", jresult3.get("ProductDetails") as String)
                                    ID_PrdctDetailEditer.commit()

                                    val ID_Emi = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF79, 0)
                                    val ID_EmiEditer = ID_Emi.edit()
                                    ID_EmiEditer.putString("EMICalculator", jresult3.get("EMICalculator") as String)
                                    ID_EmiEditer.commit()

                                    val ID_Deposit = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF80, 0)
                                    val ID_DepositEditer = ID_Deposit.edit()
                                    ID_DepositEditer.putString("DepositCalculator", jresult3.get("DepositCalculator") as String)
                                    ID_DepositEditer.commit()

                                    val ID_Goldloan = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF81, 0)
                                    val ID_GoldloanEditer = ID_Goldloan.edit()
                                    ID_GoldloanEditer.putString("GoldLoanEligibileCalculator", jresult3.get("GoldLoanEligibileCalculator") as String)
                                    ID_GoldloanEditer.commit()

                                    val ID_Enqry = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF82, 0)
                                    val ID_EnqryEditer = ID_Enqry.edit()
                                    ID_EnqryEditer.putString("Enquires", jresult3.get("Enquires") as String)
                                    ID_EnqryEditer.commit()

                                    val ID_Holidy = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF83, 0)
                                    val ID_HolidyEditer = ID_Holidy.edit()
                                    ID_HolidyEditer.putString("HolidayList", jresult3.get("HolidayList") as String)
                                    ID_HolidyEditer.commit()

                                    val ID_termstxt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF307, 0)
                                    val ID_termstxtEditer = ID_termstxt.edit()
                                    ID_termstxtEditer.putString("TermsandConditionstext", jresult3.get("TermsandConditionstext") as String)
                                    ID_termstxtEditer.commit()

                                    val ID_Executve = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF84, 0)
                                    val ID_ExecutveEditer = ID_Executve.edit()
                                    ID_ExecutveEditer.putString("ExecutiveCallBack", jresult3.get("ExecutiveCallBack") as String)
                                    ID_ExecutveEditer.commit()

                                    val ID_DEPOSIT = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF85, 0)
                                    val ID_DEPOSITEditer = ID_DEPOSIT.edit()
                                    ID_DEPOSITEditer.putString("DEPOSIT", jresult3.get("DEPOSIT") as String)
                                    ID_DEPOSITEditer.commit()

                                    val ID_LOAN = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF86, 0)
                                    val ID_LOANEditer = ID_LOAN.edit()
                                    ID_LOANEditer.putString("LOAN", jresult3.get("LOAN") as String)
                                    ID_LOANEditer.commit()

                                    val ID_Active = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF87, 0)
                                    val ID_ActiveEditer = ID_Active.edit()
                                    ID_ActiveEditer.putString("Active", jresult3.get("Active") as String)
                                    ID_ActiveEditer.commit()

                                    val ID_Deposit1 = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF88, 0)
                                    val ID_Deposit1Editer = ID_Deposit1.edit()
                                    ID_Deposit1Editer.putString("Deposit", jresult3.get("Deposit") as String)
                                    ID_Deposit1Editer.commit()

                                    val ID_Loan1 = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF89, 0)
                                    val ID_Loan1Editer = ID_Loan1.edit()
                                    ID_Loan1Editer.putString("Loan", jresult3.get("Loan") as String)
                                    ID_Loan1Editer.commit()

                                    val ID_Ownacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF90, 0)
                                    val ID_OwnaccEditer = ID_Ownacc.edit()
                                    ID_OwnaccEditer.putString("OWNACCOUNT", jresult3.get("OWNACCOUNT") as String)
                                    ID_OwnaccEditer.commit()

                                    val ID_Otheracc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF91, 0)
                                    val ID_OtheraccEditer = ID_Otheracc.edit()
                                    ID_OtheraccEditer.putString("OTHERACCOUNT", jresult3.get("OTHERACCOUNT") as String)
                                    ID_OtheraccEditer.commit()

                                    val ID_Selectacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF92, 0)
                                    val ID_SelectaccEditer = ID_Selectacc.edit()
                                    ID_SelectaccEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                    ID_SelectaccEditer.commit()

                                    val ID_Payingfrm = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF93, 0)
                                    val ID_PayingfrmEditer = ID_Payingfrm.edit()
                                    ID_PayingfrmEditer.putString("PayingFrom", jresult3.get("PayingFrom") as String)
                                    ID_PayingfrmEditer.commit()

                                    val ID_Payingto = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF94, 0)
                                    val ID_PayingtoEditer = ID_Payingto.edit()
                                    ID_PayingtoEditer.putString("PayingTo", jresult3.get("PayingTo") as String)
                                    ID_PayingtoEditer.commit()

                                    val ID_Amtpayble = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF95, 0)
                                    val ID_AmtpaybleEditer = ID_Amtpayble.edit()
                                    ID_AmtpaybleEditer.putString("AmountPayable", jresult3.get("AmountPayable") as String)
                                    ID_AmtpaybleEditer.commit()

                                    val ID_Remark = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF96, 0)
                                    val ID_RemarkEditer = ID_Remark.edit()
                                    ID_RemarkEditer.putString("Remark", jresult3.get("Remark") as String)
                                    ID_RemarkEditer.commit()


                                    val ID_Pay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF97, 0)
                                    val ID_PayEditer = ID_Pay.edit()
                                    ID_PayEditer.putString("PAY", jresult3.get("PAY") as String)
                                    ID_PayEditer.commit()

                                    val ID_Receiveracc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF98, 0)
                                    val ID_ReceiveraccEditer = ID_Receiveracc.edit()
                                    ID_ReceiveraccEditer.putString("ReceiverAccountType", jresult3.get("ReceiverAccountType") as String)
                                    ID_ReceiveraccEditer.commit()

                                    val ID_Confirmacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF99, 0)
                                    val ID_ConfirmaccEditer = ID_Confirmacc.edit()
                                    ID_ConfirmaccEditer.putString("ConfirmAccountNo", jresult3.get("ConfirmAccountNo") as String)
                                    ID_ConfirmaccEditer.commit()

                                    val ID_Scan = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF100, 0)
                                    val ID_ScanEditer = ID_Scan.edit()
                                    ID_ScanEditer.putString("Scan", jresult3.get("Scan") as String)
                                    ID_ScanEditer.commit()

                                    val ID_Slctaccnt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF101, 0)
                                    val ID_SlctaccntEditer = ID_Slctaccnt.edit()
                                    ID_SlctaccntEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                    ID_SlctaccntEditer.commit()

                                    val ID_Rechrgehist = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF102, 0)
                                    val ID_RechrgehistEditer = ID_Rechrgehist.edit()
                                    ID_RechrgehistEditer.putString("RechargeHistory", jresult3.get("RechargeHistory") as String)
                                    ID_RechrgehistEditer.commit()

                                    val ID_Frontview = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF103, 0)
                                    val ID_FrontviewEditer = ID_Frontview.edit()
                                    ID_FrontviewEditer.putString("FRONTVIEW", jresult3.get("FRONTVIEW") as String)
                                    ID_FrontviewEditer.commit()

                                    val ID_Backview = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF104, 0)
                                    val ID_BackviewEditer = ID_Backview.edit()
                                    ID_BackviewEditer.putString("BACKVIEW", jresult3.get("BACKVIEW") as String)
                                    ID_BackviewEditer.commit()

                                    val ID_Purpose = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF105, 0)
                                    val ID_PurposeEditer = ID_Purpose.edit()
                                    ID_PurposeEditer.putString("PurposeofVirtualCard", jresult3.get("PurposeofVirtualCard") as String)
                                    ID_PurposeEditer.commit()


                                    val ID_Quit = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF106, 0)
                                    val ID_QuitEditer = ID_Quit.edit()
                                    ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                    ID_QuitEditer.commit()

                                    val ID_Accno = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF107, 0)
                                    val ID_AccnoEditer = ID_Accno.edit()
                                    ID_AccnoEditer.putString("AccountNo", jresult3.get("AccountNo") as String)
                                    ID_AccnoEditer.commit()

                                    val ID_Enterdist = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF108, 0)
                                    val ID_EnterdistEditer = ID_Enterdist.edit()
                                    ID_EnterdistEditer.putString("EnterDistrict", jresult3.get("EnterDistrict") as String)
                                    ID_EnterdistEditer.commit()


                                    val ID_Mobilenum = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF110, 0)
                                    val ID_MobilenumEditer = ID_Mobilenum.edit()
                                    ID_MobilenumEditer.putString("MobileNumber", jresult3.get("MobileNumber") as String)
                                    ID_MobilenumEditer.commit()

                                    val ID_Operator = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF111, 0)
                                    val ID_OperatorEditer = ID_Operator.edit()
                                    ID_OperatorEditer.putString("Operator", jresult3.get("Operator") as String)
                                    ID_OperatorEditer.commit()

                                    val ID_Circle = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF112, 0)
                                    val ID_CircleEditer = ID_Circle.edit()
                                    ID_CircleEditer.putString("Circle", jresult3.get("Circle") as String)
                                    ID_CircleEditer.commit()


                                    val ID_Amt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF113, 0)
                                    val ID_AmtEditer = ID_Amt.edit()
                                    ID_AmtEditer.putString("Amount", jresult3.get("Amount") as String)
                                    ID_AmtEditer.commit()

                                    val ID_Rechrg = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF114, 0)
                                    val ID_RechrgEditer = ID_Rechrg.edit()
                                    ID_RechrgEditer.putString("RECHARGE", jresult3.get("RECHARGE") as String)
                                    ID_RechrgEditer.commit()

                                    val ID_Selctop = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF115, 0)
                                    val ID_SelctopEditer = ID_Selctop.edit()
                                    ID_SelctopEditer.putString("SelectOperator", jresult3.get("SelectOperator") as String)
                                    ID_SelctopEditer.commit()


                                    val ID_Subscriber = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF116, 0)
                                    val ID_SubscriberEditer = ID_Subscriber.edit()
                                    ID_SubscriberEditer.putString("SubscriberID", jresult3.get("SubscriberID") as String)
                                    ID_SubscriberEditer.commit()

                                    val ID_Accnt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF117, 0)
                                    val ID_AccntEditer = ID_Accnt.edit()
                                    ID_AccntEditer.putString("Account", jresult3.get("Account") as String)
                                    ID_AccntEditer.commit()

                                    val ID_viewall = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF118, 0)
                                    val ID_viewallEditer = ID_viewall.edit()
                                    ID_viewallEditer.putString("ViewAllAccounts", jresult3.get("ViewAllAccounts") as String)
                                    ID_viewallEditer.commit()

                                    val ID_availbal = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF119, 0)
                                    val ID_availbalEditer = ID_availbal.edit()
                                    ID_availbalEditer.putString("AvailableBalance", jresult3.get("AvailableBalance") as String)
                                    ID_availbalEditer.commit()

                                    /* val LastloginSP = this@WelcomeActivity.getSharedPreferences(Config.SHARED_PREF29, 0)
                                     val LastloginEditer = LastloginSP.edit()
                                     LastloginEditer.putString("LastLoginTime", "")
                                     LastloginEditer.commit()*/

                                    val ID_lastlog = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF120, 0)
                                    val ID_lastlogEditer = ID_lastlog.edit()
                                    ID_lastlogEditer.putString("LastLogin", jresult3.get("LastLogin") as String)
                                    ID_lastlogEditer.commit()

                                    val ID_acntdetl = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF121, 0)
                                    val ID_acntdetlEditer = ID_acntdetl.edit()
                                    ID_acntdetlEditer.putString("AccountDetails", jresult3.get("AccountDetails") as String)
                                    ID_acntdetlEditer.commit()

                                    val ID_fundtrns = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF122, 0)
                                    val ID_fundtrnsEditer = ID_fundtrns.edit()
                                    ID_fundtrnsEditer.putString("FundTransfer", jresult3.get("FundTransfer") as String)
                                    ID_fundtrnsEditer.commit()

                                    val ID_rchrgbill = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF123, 0)
                                    val ID_rchrgbillEditer = ID_rchrgbill.edit()
                                    ID_rchrgbillEditer.putString("RechargeBills", jresult3.get("RechargeBills") as String)
                                    ID_rchrgbillEditer.commit()

                                    val ID_reprts = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF124, 0)
                                    val ID_reportsEditer = ID_reprts.edit()
                                    ID_reportsEditer.putString("ReportsOtherServices", jresult3.get("ReportsOtherServices") as String)
                                    ID_reportsEditer.commit()

                                    val ID_tools = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF125, 0)
                                    val ID_toolsEditer = ID_tools.edit()
                                    ID_toolsEditer.putString("ToolsSettings", jresult3.get("ToolsSettings") as String)
                                    ID_toolsEditer.commit()

                                    val ID_Slctprd = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF126, 0)
                                    val ID_SlctprdEditer = ID_Slctprd.edit()
                                    ID_SlctprdEditer.putString("Selectaperiodofyourchoice", jresult3.get("Selectaperiodofyourchoice") as String)
                                    ID_SlctprdEditer.commit()

                                    val ID_Or = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF127, 0)
                                    val ID_OrEditer = ID_Or.edit()
                                    ID_OrEditer.putString("OR", jresult3.get("OR") as String)
                                    ID_OrEditer.commit()

                                    val ID_customdate = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF128, 0)
                                    val ID_customdateEditer = ID_customdate.edit()
                                    ID_customdateEditer.putString("Selectacustomdateofyourchoice.", jresult3.get("Selectacustomdateofyourchoice.") as String)
                                    ID_customdateEditer.commit()

                                    val ID_View = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF129, 0)
                                    val ID_ViewEditer = ID_View.edit()
                                    ID_ViewEditer.putString("View", jresult3.get("View") as String)
                                    ID_ViewEditer.commit()

                                    val ID_downld = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF130, 0)
                                    val ID_downldEditer = ID_downld.edit()
                                    ID_downldEditer.putString("Download", jresult3.get("Download") as String)
                                    ID_downldEditer.commit()

                                    val ID_lastmnth = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF131, 0)
                                    val ID_lastmnthEditer = ID_lastmnth.edit()
                                    ID_lastmnthEditer.putString("LastMonth", jresult3.get("LastMonth") as String)
                                    ID_lastmnthEditer.commit()

                                    val ID_lastthree = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF132, 0)
                                    val ID_lastthreeEditer = ID_lastthree.edit()
                                    ID_lastthreeEditer.putString("Last3Months", jresult3.get("Last3Months") as String)
                                    ID_lastthreeEditer.commit()

                                    val ID_lastsix = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF133, 0)
                                    val ID_lastsixEditer = ID_lastsix.edit()
                                    ID_lastsixEditer.putString("Last6Months", jresult3.get("Last6Months") as String)
                                    ID_lastsixEditer.commit()

                                    val ID_lastone = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF134, 0)
                                    val ID_lastoneEditer = ID_lastone.edit()
                                    ID_lastoneEditer.putString("Last1Year", jresult3.get("Last1Year") as String)
                                    ID_lastoneEditer.commit()

                                    val ID_selctacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF135, 0)
                                    val ID_selctaccEditer = ID_selctacc.edit()
                                    ID_selctaccEditer.putString("SelectAccount", jresult3.get("SelectAccount") as String)
                                    ID_selctaccEditer.commit()



                                    val ID_selctrecvr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF137, 0)
                                    val ID_selctrecvrEditer = ID_selctrecvr.edit()
                                    ID_selctrecvrEditer.putString("SelectReceiver", jresult3.get("SelectReceiver") as String)
                                    ID_selctrecvrEditer.commit()

                                    val ID_addnewsndr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF138, 0)
                                    val ID_addnewsndrEditer = ID_addnewsndr.edit()
                                    ID_addnewsndrEditer.putString("AddNewSender", jresult3.get("AddNewSender") as String)
                                    ID_addnewsndrEditer.commit()

                                    val ID_addnewrecvr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF139, 0)
                                    val ID_addnewrecvrEditer = ID_addnewrecvr.edit()
                                    ID_addnewrecvrEditer.putString("AddNewReceiver", jresult3.get("AddNewReceiver") as String)
                                    ID_addnewrecvrEditer.commit()

                                    val ID_mpin = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF140, 0)
                                    val ID_mpinEditer = ID_mpin.edit()
                                    ID_mpinEditer.putString("MPIN", jresult3.get("MPIN") as String)
                                    ID_mpinEditer.commit()


                                    val ID_frgtmpin = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF141, 0)
                                    val ID_frgtmpinEditer = ID_frgtmpin.edit()
                                    ID_frgtmpinEditer.putString("ForgotMPIN", jresult3.get("ForgotMPIN") as String)
                                    ID_frgtmpinEditer.commit()

                                    val ID_Makepaymnt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF142, 0)
                                    val ID_MakepaymntEditer = ID_Makepaymnt.edit()
                                    ID_MakepaymntEditer.putString("MAKEPAYMENT", jresult3.get("MAKEPAYMENT") as String)
                                    ID_MakepaymntEditer.commit()

                                    val ID_FirstName = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF143, 0)
                                    val ID_FirstNameEditer = ID_FirstName.edit()
                                    ID_FirstNameEditer.putString("FirstName", jresult3.get("FirstName") as String)
                                    ID_FirstNameEditer.commit()

                                    val ID_LastName = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF144, 0)
                                    val ID_LastNameEditer = ID_LastName.edit()
                                    ID_LastNameEditer.putString("LastName", jresult3.get("LastName") as String)
                                    ID_LastNameEditer.commit()

                                    val ID_Dob = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF145, 0)
                                    val ID_DobEditer = ID_Dob.edit()
                                    ID_DobEditer.putString("DOB", jresult3.get("DOB") as String)
                                    ID_DobEditer.commit()

                                    val ID_Registr = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF146, 0)
                                    val ID_RegistrEditer = ID_Registr.edit()
                                    ID_RegistrEditer.putString("REGISTER", jresult3.get("REGISTER") as String)
                                    ID_RegistrEditer.commit()

                                    val ID_SendrName = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF147, 0)
                                    val ID_SendrNameEditer = ID_SendrName.edit()
                                    ID_SendrNameEditer.putString("SenderName", jresult3.get("SenderName") as String)
                                    ID_SendrNameEditer.commit()

                                    val ID_ReceivrName = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF148, 0)
                                    val ID_ReceivrNameEditer = ID_ReceivrName.edit()
                                    ID_ReceivrNameEditer.putString("ReceiverName", jresult3.get("ReceiverName") as String)
                                    ID_ReceivrNameEditer.commit()

                                    val ID_confrmacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF149, 0)
                                    val ID_confrmaccEditer = ID_confrmacc.edit()
                                    ID_confrmaccEditer.putString("ConfirmAccountNumber", jresult3.get("ConfirmAccountNumber") as String)
                                    ID_confrmaccEditer.commit()

                                    val ID_ifsc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF150, 0)
                                    val ID_ifscEditer = ID_ifsc.edit()
                                    ID_ifscEditer.putString("IFSCCode", jresult3.get("IFSCCode") as String)
                                    ID_ifscEditer.commit()

                                    val ID_imps = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF151, 0)
                                    val ID_impsEditer = ID_imps.edit()
                                    ID_impsEditer.putString("IMPS", jresult3.get("IMPS") as String)
                                    ID_impsEditer.commit()

                                    val ID_neft = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF152, 0)
                                    val ID_neftEditer = ID_neft.edit()
                                    ID_neftEditer.putString("NEFT", jresult3.get("NEFT") as String)
                                    ID_neftEditer.commit()

                                    val ID_rtgs = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF153, 0)
                                    val ID_rtgsEditer = ID_rtgs.edit()
                                    ID_rtgsEditer.putString("RTGS", jresult3.get("RTGS") as String)
                                    ID_rtgsEditer.commit()

                                    val ID_fundstat = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF154, 0)
                                    val ID_fundstatEditer = ID_fundstat.edit()
                                    ID_fundstatEditer.putString("FUNDTRANSFERSTATUS", jresult3.get("FUNDTRANSFERSTATUS") as String)
                                    ID_fundstatEditer.commit()

                                    val ID_Benflist = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF157, 0)
                                    val ID_BenflistEditer = ID_Benflist.edit()
                                    ID_BenflistEditer.putString("BeneficiaryList", jresult3.get("BeneficiaryList") as String)
                                    ID_BenflistEditer.commit()

                                    val ID_acc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF158, 0)
                                    val ID_accEditer = ID_acc.edit()
                                    ID_accEditer.putString("AccountNumber", jresult3.get("AccountNumber") as String)
                                    ID_accEditer.commit()

                                    val ID_Benfname = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF159, 0)
                                    val ID_BenfnameEditer = ID_Benfname.edit()
                                    ID_BenfnameEditer.putString("BeneficiaryName", jresult3.get("BeneficiaryName") as String)
                                    ID_BenfnameEditer.commit()

                                    val ID_Benfaccno = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF160, 0)
                                    val ID_BenfaccnoEditer = ID_Benfaccno.edit()
                                    ID_BenfaccnoEditer.putString("BeneficiaryCNo", jresult3.get("BeneficiaryCNo") as String)
                                    ID_BenfaccnoEditer.commit()

                                    val ID_Benfconfrmacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF161, 0)
                                    val ID_BenfconfrmaccEditer = ID_Benfconfrmacc.edit()
                                    ID_BenfconfrmaccEditer.putString("ConfirmBeneficiaryACNo", jresult3.get("ConfirmBeneficiaryACNo") as String)
                                    ID_BenfconfrmaccEditer.commit()

                                    val ID_Savedbenf = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF162, 0)
                                    val ID_SavedbenfEditer = ID_Savedbenf.edit()
                                    ID_SavedbenfEditer.putString("SaveBeneficiaryForFuture", jresult3.get("SaveBeneficiaryForFuture") as String)
                                    ID_SavedbenfEditer.commit()

                                    val ID_Otpmsg = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF166, 0)
                                    val ID_OtpmsgEditer = ID_Otpmsg.edit()
                                    ID_OtpmsgEditer.putString("please enter validation code senttoyourregisteredmobilenumber", jresult3.get("please enter validation code senttoyourregisteredmobilenumber") as String)
                                    ID_OtpmsgEditer.commit()

                                    val ID_Languagsp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF167, 0)
                                    val ID_LanguagspEditer = ID_Languagsp.edit()
                                    ID_LanguagspEditer.putString("Language", jresult3.get("Language") as String)
                                    ID_LanguagspEditer.commit()

                                    val ID_Mpinverifysp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF168, 0)
                                    val ID_MpinverifyspEditer = ID_Mpinverifysp.edit()
                                    ID_MpinverifyspEditer.putString("MPINVerification", jresult3.get("MPINVerification") as String)
                                    ID_MpinverifyspEditer.commit()


                                    val ID_Plsunlocksp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF169,0)
                                    val ID_PlsunlockspEditer = ID_Plsunlocksp.edit()
                                    ID_PlsunlockspEditer.putString("PleaseunlockwithyourMPIN", jresult3.get("PleaseunlockwithyourMPIN") as String)
                                    ID_PlsunlockspEditer.commit()


                                    val ID_ChangeMpinsp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF170, 0)
                                    val ID_ChangeMpinEditer = ID_ChangeMpinsp.edit()
                                    ID_ChangeMpinEditer.putString("ChangeMPIN", jresult3.get("ChangeMPIN") as String)
                                    ID_ChangeMpinEditer.commit()

                                    val ID_Namesp= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF171, 0)
                                    val ID_NamespEditer = ID_Namesp.edit()
                                    ID_NamespEditer.putString("Name", jresult3.get("Name") as String)
                                    ID_NamespEditer.commit()

                                    val ID_Mobilesp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF172, 0)
                                    val ID_MobilespEditer = ID_Mobilesp.edit()
                                    ID_MobilespEditer.putString("Mobile", jresult3.get("Mobile") as String)
                                    ID_MobilespEditer.commit()

                                    val ID_Datesp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF173, 0)
                                    val ID_DatespEditer = ID_Datesp.edit()
                                    ID_DatespEditer.putString("Date", jresult3.get("Date") as String)
                                    ID_DatespEditer.commit()

                                    val ID_Timesp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF174, 0)
                                    val ID_TimespEditer = ID_Timesp.edit()
                                    ID_TimespEditer.putString("Time", jresult3.get("Time") as String)
                                    ID_TimespEditer.commit()

                                    val ID_Reprterror = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF175, 0)
                                    val ID_ReprterrorEditer = ID_Reprterror.edit()
                                    ID_ReprterrorEditer.putString("Reportanerror", jresult3.get("Reportanerror") as String)
                                    ID_ReprterrorEditer.commit()

                                    val ID_sugstnsp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF176, 0)
                                    val ID_sugstnspEditer = ID_sugstnsp.edit()
                                    ID_sugstnspEditer.putString("Giveasuggestion", jresult3.get("Giveasuggestion") as String)
                                    ID_sugstnspEditer.commit()

                                    val ID_suggstnsp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF177, 0)
                                    val ID_suggstnspEditer = ID_suggstnsp.edit()
                                    ID_suggstnspEditer.putString("Anythingelse", jresult3.get("Anythingelse") as String)
                                    ID_suggstnspEditer.commit()

                                    val ID_Typedep = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF178, 0)
                                    val ID_TypedepEditer = ID_Typedep.edit()
                                    ID_TypedepEditer.putString("TypeofDeposit", jresult3.get("TypeofDeposit") as String)
                                    ID_TypedepEditer.commit()

                                    val ID_benfsp = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF179, 0)
                                    val ID_benfspEditer = ID_benfsp.edit()
                                    ID_benfspEditer.putString("Beneficiary", jresult3.get("Beneficiary") as String)
                                    ID_benfspEditer.commit()


                                    val ID_tenure = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF180, 0)
                                    val ID_tenureEditer = ID_tenure.edit()
                                    ID_tenureEditer.putString("Tenure", jresult3.get("Tenure") as String)
                                    ID_tenureEditer.commit()


                                    val ID_entrmnth = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF181, 0)
                                    val ID_entrmnthEditer = ID_entrmnth.edit()
                                    ID_entrmnthEditer.putString("PleaseEnterMonth", jresult3.get("PleaseEnterMonth") as String)
                                    ID_entrmnthEditer.commit()


                                    val ID_entrdy= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF182, 0)
                                    val ID_entrdyEditer = ID_entrdy.edit()
                                    ID_entrdyEditer.putString("PleaseEnterDay", jresult3.get("PleaseEnterDay") as String)
                                    ID_entrdyEditer.commit()

                                    val ID_ASSETS= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF183, 0)
                                    val ID_ASSETSEditer = ID_ASSETS.edit()
                                    ID_ASSETSEditer.putString("Assets", jresult3.get("Assets") as String)
                                    ID_ASSETSEditer.commit()

                                    val ID_LIABLTY= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF184, 0)
                                    val ID_LIABLTYEditer = ID_LIABLTY.edit()
                                    ID_LIABLTYEditer.putString("Liability", jresult3.get("Liability") as String)
                                    ID_LIABLTYEditer.commit()

                                    val ID_paymnt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF185, 0)
                                    val ID_paymntEditer = ID_paymnt.edit()
                                    ID_paymntEditer.putString("PaymentReceipt", jresult3.get("PaymentReceipt") as String)
                                    ID_paymntEditer.commit()

                                    val ID_calc= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF190, 0)
                                    val ID_calcEditer = ID_calc.edit()
                                    ID_calcEditer.putString("CALCULATE", jresult3.get("CALCULATE") as String)
                                    ID_calcEditer.commit()


                                    val ID_reset= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF189, 0)
                                    val ID_resetEditer = ID_reset.edit()
                                    ID_resetEditer.putString("RESET", jresult3.get("RESET") as String)
                                    ID_resetEditer.commit()

                                    val ID_princamt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF191, 0)
                                    val ID_princamtEditer = ID_princamt.edit()
                                    ID_princamtEditer.putString("PRINCIPALAMOUNT", jresult3.get("PRINCIPALAMOUNT") as String)
                                    ID_princamtEditer.commit()

                                    val ID_intrstrate= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF192, 0)
                                    val ID_intrstrateEditer = ID_intrstrate.edit()
                                    ID_intrstrateEditer.putString("INTERESTRATE", jresult3.get("INTERESTRATE") as String)
                                    ID_intrstrateEditer.commit()

                                    val ID_month= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF193, 0)
                                    val ID_monthEditer = ID_month.edit()
                                    ID_monthEditer.putString("MONTH", jresult3.get("MONTH") as String)
                                    ID_monthEditer.commit()

                                    val ID_emi= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF194, 0)
                                    val ID_emiEditer = ID_emi.edit()
                                    ID_emiEditer.putString("Selectemitype", jresult3.get("Selectemitype") as String)
                                    ID_emiEditer.commit()

                                    val ID_prodctdetl= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF195, 0)
                                    val ID_prodctdetlEditer = ID_prodctdetl.edit()
                                    ID_prodctdetlEditer.putString("ProductListDetails", jresult3.get("ProductListDetails") as String)
                                    ID_prodctdetlEditer.commit()

                                    val ID_loanappstatus= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF196, 0)
                                    val ID_loanappstatusEditer = ID_loanappstatus.edit()
                                    ID_loanappstatusEditer.putString("LoanApplicationStatus", jresult3.get("LoanApplicationStatus") as String)
                                    ID_loanappstatusEditer.commit()

                                    val ID_loantype= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF197, 0)
                                    val ID_loantypeEditer = ID_loantype.edit()
                                    ID_loantypeEditer.putString("SelectLoantype", jresult3.get("SelectLoantype") as String)
                                    ID_loantypeEditer.commit()

                                    val ID_loanpurpse= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF198, 0)
                                    val ID_loanpurpseEditer = ID_loanpurpse.edit()
                                    ID_loanpurpseEditer.putString("Selectloanpurpose", jresult3.get("Selectloanpurpose") as String)
                                    ID_loanpurpseEditer.commit()

                                    val ID_closd= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF199, 0)
                                    val ID_closdEditer = ID_closd.edit()
                                    ID_closdEditer.putString("Closed", jresult3.get("Closed") as String)
                                    ID_closdEditer.commit()

                                    val ID_frmdte= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF200, 0)
                                    val ID_frmdteEditer = ID_frmdte.edit()
                                    ID_frmdteEditer.putString("FromDate", jresult3.get("FromDate") as String)
                                    ID_frmdteEditer.commit()

                                    val ID_enddte= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF201, 0)
                                    val ID_enddteEditer = ID_enddte.edit()
                                    ID_enddteEditer.putString("EndDate", jresult3.get("EndDate") as String)
                                    ID_enddteEditer.commit()

                                    val ID_duedtecal= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF202, 0)
                                    val ID_duedtecalEditer = ID_duedtecal.edit()
                                    ID_duedtecalEditer.putString("DueDatesCalender", jresult3.get("DueDatesCalender") as String)
                                    ID_duedtecalEditer.commit()

                                    val ID_duedtelistforupcom= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF203, 0)
                                    val ID_duedtelistforupcomEditer = ID_duedtelistforupcom.edit()
                                    ID_duedtelistforupcomEditer.putString("Duedatelistforupcomingtwoweeks.", jresult3.get("Duedatelistforupcomingtwoweeks.") as String)
                                    ID_duedtelistforupcomEditer.commit()

                                    val ID_duedate= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF204, 0)
                                    val ID_duedateEditer = ID_duedate.edit()
                                    ID_duedateEditer.putString("Duedate", jresult3.get("Duedate") as String)
                                    ID_duedateEditer.commit()

                                    val ID_doyouwntdlte= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF205, 0)
                                    val ID_doyouwntdlteEditer = ID_doyouwntdlte.edit()
                                    ID_doyouwntdlteEditer.putString("DoYouWanttoDeleteThisAccountAndRegisterWithAnotherAccount?", jresult3.get("DoYouWanttoDeleteThisAccountAndRegisterWithAnotherAccount?") as String)
                                    ID_doyouwntdlteEditer.commit()

                                    val ID_ys= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF206, 0)
                                    val ID_ysEditer = ID_ys.edit()
                                    ID_ysEditer.putString("Yes", jresult3.get("Yes") as String)
                                    ID_ysEditer.commit()

                                    val ID_no= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF207, 0)
                                    val ID_noEditer = ID_no.edit()
                                    ID_noEditer.putString("No", jresult3.get("No") as String)
                                    ID_noEditer.commit()

                                    val ID_doyouwquit= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF208, 0)
                                    val ID_doyouwquitEditer = ID_doyouwquit.edit()
                                    ID_doyouwquitEditer.putString("DoYouWantToQuit?", jresult3.get("DoYouWantToQuit?") as String)
                                    ID_doyouwquitEditer.commit()

                                    val ID_listasondate= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF209, 0)
                                    val ID_listasondateEditer = ID_listasondate.edit()
                                    ID_listasondateEditer.putString("ListasonDate", jresult3.get("ListasonDate-Listason(now)") as String)
                                    ID_listasondateEditer.commit()

                                    val ID_bal= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF210, 0)
                                    val ID_balEditer = ID_bal.edit()
                                    ID_balEditer.putString("Balance", jresult3.get("Balance") as String)
                                    ID_balEditer.commit()

                                    val ID_min= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF211, 0)
                                    val ID_minEditer = ID_min.edit()
                                    ID_minEditer.putString("MINISTATEMENT", jresult3.get("MINISTATEMENT") as String)
                                    ID_minEditer.commit()

                                    val ID_acntstat= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF212, 0)
                                    val ID_acntstatEditer = ID_acntstat.edit()
                                    ID_acntstatEditer.putString("ACCOUNTSTATEMENT", jresult3.get("ACCOUNTSTATEMENT") as String)
                                    ID_acntstatEditer.commit()

                                    val ID_moreopt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF213, 0)
                                    val ID_moreoptEditer = ID_moreopt.edit()
                                    ID_moreoptEditer.putString("MOREOPTIONS", jresult3.get("MOREOPTIONS") as String)
                                    ID_moreoptEditer.commit()

                                    val ID_acntsummary= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF214, 0)
                                    val ID_acntsummaryEditer = ID_acntsummary.edit()
                                    ID_acntsummaryEditer.putString("AccountSummary", jresult3.get("AccountSummary") as String)
                                    ID_acntsummaryEditer.commit()

                                    val ID_standins= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF215, 0)
                                    val ID_standinsEditer = ID_standins.edit()
                                    ID_standinsEditer.putString("StandingInstruction", jresult3.get("StandingInstruction") as String)
                                    ID_standinsEditer.commit()

                                    val ID_notce= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF216, 0)
                                    val ID_notceEditer = ID_notce.edit()
                                    ID_notceEditer.putString("Notice", jresult3.get("Notice") as String)
                                    ID_notceEditer.commit()

                                    val ID_ListingDataforpast= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF217, 0)
                                    val ListingDataforpastEditer = ID_ListingDataforpast.edit()
                                    ListingDataforpastEditer.putString("ListingDataforpast", jresult3.get("ListingDataforpast .") as String)
                                    ListingDataforpastEditer.commit()


                                    val ID_days= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF218, 0)
                                    val daysEditer = ID_days.edit()
                                    daysEditer.putString("days", jresult3.get("days") as String)
                                    daysEditer.commit()



                                    val ID_youcanchangeitfromsettings= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF219, 0)
                                    val youcanchangeitfromsettingsEditer = ID_youcanchangeitfromsettings.edit()
                                    youcanchangeitfromsettingsEditer.putString("youcanchangeitfromsettings", jresult3.get("youcanchangeitfromsettings") as String)
                                    youcanchangeitfromsettingsEditer.commit()

                                    val ID_Clear= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF220, 0)
                                    val ClearEditer = ID_Clear.edit()
                                    ClearEditer.putString("Clear", jresult3.get("Clear") as String)
                                    ClearEditer.commit()



                                    val ID_LoanPeriod= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF221, 0)
                                    val LoanPeriodEditer = ID_LoanPeriod.edit()
                                    LoanPeriodEditer.putString("LoanPeriod", jresult3.get("LoanPeriod") as String)
                                    LoanPeriodEditer.commit()

                                    val ID_Weight= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF222, 0)
                                    val WeightEditer = ID_Weight.edit()
                                    WeightEditer.putString("Weight", jresult3.get("Weight") as String)
                                    WeightEditer.commit()

                                    val ID_EnterAmount= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF223, 0)
                                    val EnterAmountEditer = ID_EnterAmount.edit()
                                    EnterAmountEditer.putString("EnterAmount", jresult3.get("EnterAmount") as String)
                                    EnterAmountEditer.commit()

                                    val ID_SelectBranch= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF224, 0)
                                    val SelectBranchEditer = ID_SelectBranch.edit()
                                    SelectBranchEditer.putString("SelectBranch", jresult3.get("SelectBranch") as String)
                                    SelectBranchEditer.commit()

                                    val ID_Pleaseselectpayingtoaccountnumber= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF225, 0)
                                    val PleaseselectpayingtoaccountnumberEditer = ID_Pleaseselectpayingtoaccountnumber.edit()
                                    PleaseselectpayingtoaccountnumberEditer.putString("Pleaseselectpayingtoaccountnumber", jresult3.get("Pleaseselectpayingtoaccountnumber") as String)
                                    PleaseselectpayingtoaccountnumberEditer.commit()

                                    val ID_PayableAmount= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF226, 0)
                                    val PayableAmountEditer = ID_PayableAmount.edit()
                                    PayableAmountEditer.putString("PayableAmount", jresult3.get("PayableAmount") as String)
                                    PayableAmountEditer.commit()


                                    val ID_Atleast3digitsarerequired= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF227, 0)
                                    val Atleast3digitsarerequiredEditer = ID_Atleast3digitsarerequired.edit()
                                    Atleast3digitsarerequiredEditer.putString("Atleast3digitsarerequired", jresult3.get("Atleast3digitsarerequired.") as String)
                                    Atleast3digitsarerequiredEditer.commit()

                                    val ID_Atleast6digitsarerequired= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF228, 0)
                                    val Atleast6digitsarerequiredEditer = ID_Atleast6digitsarerequired.edit()
                                    Atleast6digitsarerequiredEditer.putString("Atleast6digitsarerequired", jresult3.get("Atleast6digitsarerequired.") as String)
                                    Atleast6digitsarerequiredEditer.commit()

                                    val ID_PleaseEnterBeneficiaryName= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF229, 0)
                                    val PleaseEnterBeneficiaryNameEditer = ID_PleaseEnterBeneficiaryName.edit()
                                    PleaseEnterBeneficiaryNameEditer.putString("PleaseEnterBeneficiaryName", jresult3.get("PleaseEnterBeneficiaryName") as String)
                                    PleaseEnterBeneficiaryNameEditer.commit()

                                    val ID_PleaseEnterValidBeneficiaryAccountNumber= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF230, 0)
                                    val PleaseEnterValidBeneficiaryAccountNumberEditer = ID_PleaseEnterValidBeneficiaryAccountNumber.edit()
                                    PleaseEnterValidBeneficiaryAccountNumberEditer.putString("PleaseEnterValidBeneficiaryAccountNumber", jresult3.get("PleaseEnterValidBeneficiaryAccountNumber") as String)
                                    PleaseEnterValidBeneficiaryAccountNumberEditer.commit()




                                    val ID_PleaseentervalidConfirmBeneficiaryaccountnumber= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF231, 0)
                                    val PleaseentervalidConfirmBeneficiaryaccountnumberEditer = ID_PleaseentervalidConfirmBeneficiaryaccountnumber.edit()
                                    PleaseentervalidConfirmBeneficiaryaccountnumberEditer.putString("PleaseentervalidConfirmBeneficiaryaccountnumber", jresult3.get("PleaseentervalidConfirmBeneficiaryaccountnumber") as String)
                                    PleaseentervalidConfirmBeneficiaryaccountnumberEditer.commit()

                                    val ID_SelectavalidAccountNumber= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF232, 0)
                                    val SelectavalidAccountNumberEditer = ID_SelectavalidAccountNumber.edit()
                                    SelectavalidAccountNumberEditer.putString("SelectavalidAccountNumber", jresult3.get("SelectavalidAccountNumber") as String)
                                    SelectavalidAccountNumberEditer.commit()

                                    val ID_PleaseEnterYourFeedback= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF233, 0)
                                    val PleaseEnterYourFeedbackEditer = ID_PleaseEnterYourFeedback.edit()
                                    PleaseEnterYourFeedbackEditer.putString("PleaseEnterYourFeedback", jresult3.get("PleaseEnterYourFeedback") as String)
                                    PleaseEnterYourFeedbackEditer.commit()

                                    val ID_Giveusacall= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF234, 0)
                                    val GiveusacallEditer = ID_Giveusacall.edit()
                                    GiveusacallEditer.putString("Giveusacall", jresult3.get("Giveusacall") as String)
                                    GiveusacallEditer.commit()

                                    val ID_Sendusamessage= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF235, 0)
                                    val SendusamessageEditer = ID_Sendusamessage.edit()
                                    SendusamessageEditer.putString("Sendusamessage", jresult3.get("Sendusamessage") as String)
                                    SendusamessageEditer.commit()

                                    val ID_Visitourlocation= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF236, 0)
                                    val VisitourlocationEditer = ID_Visitourlocation.edit()
                                    VisitourlocationEditer.putString("Visitourlocation", jresult3.get("Visitourlocation") as String)
                                    VisitourlocationEditer.commit()

                                    val ID_Aboutustext= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF237, 0)
                                    val AboutustextEditer = ID_Aboutustext.edit()
                                    AboutustextEditer.putString("Aboutustext", jresult3.get("Aboutustext") as String)
                                    AboutustextEditer.commit()

                                    val ID_Privacypolicytext= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF238, 0)
                                    val PrivacypolicytextEditer = ID_Privacypolicytext.edit()
                                    PrivacypolicytextEditer.putString("Privacypolicytext", jresult3.get("Privacypolicytext") as String)
                                    PrivacypolicytextEditer.commit()

                                    val ID_TermsandConditionstext= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF239, 0)
                                    val TermsandConditionstextEditer = ID_TermsandConditionstext.edit()
                                    TermsandConditionstextEditer.putString("TermsandConditionstext", jresult3.get("TermsandConditionstext") as String)
                                    TermsandConditionstextEditer.commit()

                                    val ID_Apply= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF240, 0)
                                    val ApplyEditer = ID_Apply.edit()
                                    ApplyEditer.putString("Apply", jresult3.get("Apply") as String)
                                    ApplyEditer.commit()

                                    val ID_acnttyp= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF241, 0)
                                    val ID_acnttypEditer = ID_acnttyp.edit()
                                    ID_acnttypEditer.putString("AccountType", jresult3.get("AccountType") as String)
                                    ID_acnttypEditer.commit()


                                    val ID_unclramt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF242, 0)
                                    val ID_unclramtEditer = ID_unclramt.edit()
                                    ID_unclramtEditer.putString("UnclearAmount", jresult3.get("UnclearAmount") as String)
                                    ID_unclramtEditer.commit()


                                    val ID_transupdte= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF244, 0)
                                    val ID_transupdteEditer = ID_transupdte.edit()
                                    ID_transupdteEditer.putString("TransactionUpdate(Days)", jresult3.get("TransactionUpdate(Days)") as String)
                                    ID_transupdteEditer.commit()

                                    val ID_updteintrvl= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF245, 0)
                                    val ID_updteintrvlEditer = ID_updteintrvl.edit()
                                    ID_updteintrvlEditer.putString("UpdateInterval", jresult3.get("UpdateInterval") as String)
                                    ID_updteintrvlEditer.commit()

                                    val ID_defltacc= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF246, 0)
                                    val ID_defltaccEditer = ID_defltacc.edit()
                                    ID_defltaccEditer.putString("DefaultAccount", jresult3.get("DefaultAccount") as String)
                                    ID_defltaccEditer.commit()

                                    val ID_giveuscall= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF247, 0)
                                    val ID_giveuscallEditer = ID_giveuscall.edit()
                                    ID_giveuscallEditer.putString("Giveusacall", jresult3.get("Giveusacall") as String)
                                    ID_giveuscallEditer.commit()

                                    val ID_sendus= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF248, 0)
                                    val ID_sendusEditer = ID_sendus.edit()
                                    ID_sendusEditer.putString("Sendusamessage", jresult3.get("Sendusamessage") as String)
                                    ID_sendusEditer.commit()

                                    val ID_visitloc= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF249, 0)
                                    val ID_visitlocEditer = ID_visitloc.edit()
                                    ID_visitlocEditer.putString("Visitourlocation", jresult3.get("Visitourlocation") as String)
                                    ID_visitlocEditer.commit()

                                    val ID_submt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF250, 0)
                                    val ID_submtEditer = ID_submt.edit()
                                    ID_submtEditer.putString("Submit", jresult3.get("Submit") as String)
                                    ID_submtEditer.commit()

                                    val ID_ownaccfndtransfr= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF251, 0)
                                    val ID_ownaccfndtransfrEditer = ID_ownaccfndtransfr.edit()
                                    ID_ownaccfndtransfrEditer.putString("OwnAccountFundTransfer", jresult3.get("OwnAccountFundTransfer") as String)
                                    ID_ownaccfndtransfrEditer.commit()

                                    val ID_othraccfndtransfr= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF252, 0)
                                    val ID_othraccfndtransfrEditer = ID_othraccfndtransfr.edit()
                                    ID_othraccfndtransfrEditer.putString("OtherAccountFundTransfer", jresult3.get("OtherAccountFundTransfer") as String)
                                    ID_othraccfndtransfrEditer.commit()

                                    val ID_transferupto= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF253, 0)
                                    val ID_transferuptoEditer = ID_transferupto.edit()
                                    ID_transferuptoEditer.putString("Transfer upto", jresult3.get("Transfer upto") as String)
                                    ID_transferuptoEditer.commit()

                                    val ID_instantly= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF254, 0)
                                    val ID_instantlyEditer = ID_instantly.edit()
                                    ID_instantlyEditer.putString("Instantly", jresult3.get("Instantly") as String)
                                    ID_instantlyEditer.commit()

                                    val ID_weight= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF255, 0)
                                    val ID_weightEditer = ID_weight.edit()
                                    ID_weightEditer.putString("Weight", jresult3.get("Weight") as String)
                                    ID_weightEditer.commit()

                                    val ID_emitype= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF256, 0)
                                    val ID_emitypeEditer = ID_emitype.edit()
                                    ID_emitypeEditer.putString("EMITYPE", jresult3.get("EMITYPE") as String)
                                    ID_emitypeEditer.commit()

                                    val ID_ENTRWEIGHT= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF257, 0)
                                    val ID_ENTRWEIGHTEditer = ID_ENTRWEIGHT.edit()
                                    ID_ENTRWEIGHTEditer.putString("Enter Weight", jresult3.get("EnterWeight") as String)
                                    ID_ENTRWEIGHTEditer.commit()

                                    val ID_Plsentrweght= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF258, 0)
                                    val ID_PlsentrweghtEditer = ID_Plsentrweght.edit()
                                    ID_PlsentrweghtEditer.putString("PleaseEnterWeight", jresult3.get("PleaseEnterWeight") as String)
                                    ID_PlsentrweghtEditer.commit()



                                    val ID_entrprincamt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF260, 0)
                                    val ID_entrprincamtEditer = ID_entrprincamt.edit()
                                    ID_entrprincamtEditer.putString("EnterPrincipalAmount", jresult3.get("EnterPrincipalAmount") as String)
                                    ID_entrprincamtEditer.commit()

                                    val ID_entrmnth1= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF261, 0)
                                    val ID_entrmnth1Editer = ID_entrmnth1.edit()
                                    ID_entrmnth1Editer.putString("EnterMonth", jresult3.get("EnterMonth") as String)
                                    ID_entrmnth1Editer.commit()

                                    val ID_entrintrstrate= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF262, 0)
                                    val ID_entrintrstrateEditer = ID_entrintrstrate.edit()
                                    ID_entrintrstrateEditer.putString("EnterInterestRate", jresult3.get("EnterInterestRate") as String)
                                    ID_entrintrstrateEditer.commit()

                                    val ID_loanpurpse1= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF263, 0)
                                    val ID_loanpurpse1Editer = ID_loanpurpse1.edit()
                                    ID_loanpurpse1Editer.putString("LoanPurpose", jresult3.get("LoanPurpose") as String)
                                    ID_loanpurpse1Editer.commit()

                                    val ID_loantyp= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF264, 0)
                                    val ID_loantypEditer = ID_loantyp.edit()
                                    ID_loantypEditer.putString("LoanType", jresult3.get("LoanType") as String)
                                    ID_loantypEditer.commit()


                                    val ID_applctnamt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF266, 0)
                                    val ID_applctnamtEditer = ID_applctnamt.edit()
                                    ID_applctnamtEditer.putString("ApplicationAmount", jresult3.get("ApplicationAmount") as String)
                                    ID_applctnamtEditer.commit()

                                    val ID_applctnno= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF267, 0)
                                    val ID_applctnnoEditer = ID_applctnno.edit()
                                    ID_applctnnoEditer.putString("ApplicationNumber", jresult3.get("ApplicationNumber") as String)
                                    ID_applctnnoEditer.commit()

                                    val ID_cusname= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF268, 0)
                                    val ID_cusnameEditer = ID_cusname.edit()
                                    ID_cusnameEditer.putString("Customer Name", jresult3.get("Customer Name") as String)
                                    ID_cusnameEditer.commit()

                                    val ID_cusid= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF269, 0)
                                    val ID_cusidEditer = ID_cusid.edit()
                                    ID_cusidEditer.putString("Customer Id", jresult3.get("Customer Id") as String)
                                    ID_cusidEditer.commit()

                                    val ID_elctrnc= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF270, 0)
                                    val ID_elctrncEditer = ID_elctrnc.edit()
                                    ID_elctrncEditer.putString("Electronicuseonly", jresult3.get("Electronicuseonly") as String)
                                    ID_elctrncEditer.commit()

                                    val ID_point1= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF271, 0)
                                    val ID_point1Editer = ID_point1.edit()
                                    ID_point1Editer.putString("Streamlinetransactions", jresult3.get("Streamlinetransactions") as String)
                                    ID_point1Editer.commit()


                                    val ID_point2= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF272, 0)
                                    val ID_point2Editer = ID_point2.edit()
                                    ID_point2Editer.putString("Enableasinglepointofcontactforcreditanddebit", jresult3.get("Enableasinglepointofcontactforcreditanddebit") as String)
                                    ID_point2Editer.commit()


                                    val ID_pont3= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF273, 0)
                                    val ID_pont3Editer = ID_pont3.edit()
                                    ID_pont3Editer.putString("Strengthenyourloanportfolio", jresult3.get("Strengthenyourloanportfolio") as String)
                                    ID_pont3Editer.commit()


                                    val ID_point4= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF274, 0)
                                    val ID_point4Editer = ID_point4.edit()
                                    ID_point4Editer.putString("Eliminatethelongqueues", jresult3.get("Eliminatethelongqueues") as String)
                                    ID_point4Editer.commit()


                                    val ID_validifsc= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF275, 0)
                                    val ID_validifscEditer = ID_validifsc.edit()
                                    ID_validifscEditer.putString("PleaseEnterValidIFSC", jresult3.get("PleaseEnterValidIFSC") as String)
                                    ID_validifscEditer.commit()

                                    val ID_benfmatch= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF276, 0)
                                    val ID_benfmatchEditer = ID_benfmatch.edit()
                                    ID_benfmatchEditer.putString("BeneficiaryAccountNumberdidntmatch", jresult3.get("BeneficiaryAccountNumberdidntmatch") as String)
                                    ID_benfmatchEditer.commit()

                                    val ID_consname= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF277, 0)
                                    val ID_consnameEditer = ID_consname.edit()
                                    ID_consnameEditer.putString("ConsumerName", jresult3.get("ConsumerName") as String)
                                    ID_consnameEditer.commit()

                                    val ID_consnum= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF278, 0)
                                    val ID_consnumEditer = ID_consnum.edit()
                                    ID_consnumEditer.putString("ConsumerNumber", jresult3.get("ConsumerNumber") as String)
                                    ID_consnumEditer.commit()

                                    val ID_sectnme= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF279, 0)
                                    val ID_sectnmeEditer = ID_sectnme.edit()
                                    ID_sectnmeEditer.putString("SectionName", jresult3.get("SectionName") as String)
                                    ID_sectnmeEditer.commit()

                                    val ID_bill= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF280, 0)
                                    val ID_billEditer = ID_bill.edit()
                                    ID_billEditer.putString("BillNumber", jresult3.get("BillNumber") as String)
                                    ID_billEditer.commit()



                                    val ID_plsentsec= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF282, 0)
                                    val ID_plsentsecEditer = ID_plsentsec.edit()
                                    ID_plsentsecEditer.putString("PleaseEnterSectionName", jresult3.get("PleaseEnterSectionName") as String)
                                    ID_plsentsecEditer.commit()

                                    val ID_plsentbill= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF283, 0)
                                    val ID_plsentbillEditer = ID_plsentbill.edit()
                                    ID_plsentbillEditer.putString("PleaseEnterBillnumber", jresult3.get("PleaseEnterBillnumber") as String)
                                    ID_plsentbillEditer.commit()


                                    val ID_plsentconsnme= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF285, 0)
                                    val ID_plsentconsnmeEditer = ID_plsentconsnme.edit()
                                    ID_plsentconsnmeEditer.putString("PleaseEnterConsumerName", jresult3.get("PleaseEnterConsumerName") as String)
                                    ID_plsentconsnmeEditer.commit()





                                    val ID_slctn= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF288, 0)
                                    val ID_slctnEditer = ID_slctn.edit()
                                    ID_slctnEditer.putString("Selection", jresult3.get("Selection") as String)
                                    ID_slctnEditer.commit()

                                    val ID_sctn= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF289, 0)
                                    val ID_sctnEditer = ID_sctn.edit()
                                    ID_sctnEditer.putString("Section", jresult3.get("Section") as String)
                                    ID_sctnEditer.commit()

                                    val ID_nottransf= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF290, 0)
                                    val ID_nottransfEditer = ID_nottransf.edit()
                                    ID_nottransfEditer.putString("NotTransferable", jresult3.get("NotTransferable") as String)
                                    ID_nottransfEditer.commit()

                                    val ID_keepcrd= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF291, 0)
                                    val ID_keepcrdEditer = ID_keepcrd.edit()
                                    ID_keepcrdEditer.putString("PleaseKeepYourCardConfidential", jresult3.get("PleaseKeepYourCardConfidential") as String)
                                    ID_keepcrdEditer.commit()

                                    val ID_ph= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF292, 0)
                                    val ID_phEditer = ID_ph.edit()
                                    ID_phEditer.putString("Phone", jresult3.get("Phone") as String)
                                    ID_phEditer.commit()





                                   /* val ID_plsentrfrst= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF303, 0)
                                    val ID_plsentrfrstEditer = ID_plsentrfrst.edit()
                                    ID_plsentrfrstEditer.putString("PleaseEnterFirstName", jresult3.get("PleaseEnterFirstName") as String)
                                    ID_plsentrfrstEditer.commit()

                                    val ID_plsslctcircle= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF304, 0)
                                    val ID_plsslctcircleEditer = ID_plsslctcircle.edit()
                                    ID_plsslctcircleEditer.putString("PleaseSelectCircle", jresult3.get("PleaseEnterLastName") as String)
                                    ID_plsslctcircleEditer.commit()*/

                                    val ID_applctndt= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF265, 0)
                                    val ID_applctndtEditer = ID_applctndt.edit()
                                    ID_applctndtEditer.putString("ApplicationDate", jresult3.get("ApplicationDate") as String)
                                    ID_applctndtEditer.commit()

                                    val ID_plsentrambw= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF322, 0)
                                    val ID_ovrdueloansEditer = ID_plsentrambw.edit()
                                    ID_ovrdueloansEditer.putString("PleaseEnterAmountbetween1and25000.0.", jresult3.get("PleaseEnterAmountbetween1and25000.0.") as String)
                                    ID_ovrdueloansEditer.commit()

                                    val ID_plsentrmpin= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF323, 0)
                                    val ID_plsentrmpinEditer = ID_plsentrmpin.edit()
                                    ID_plsentrmpinEditer.putString("pleaseentermpin", jresult3.get("pleaseentermpin") as String)
                                    ID_plsentrmpinEditer.commit()



                                    val myIntent = Intent(this@HomeActivity, HomeActivity::class.java)
                                    overridePendingTransition(0, 0)
                                    startActivity(myIntent)
                                    overridePendingTransition(0, 0)


                                } else {
                                    val builder = android.app.AlertDialog.Builder(
                                            this@HomeActivity,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: android.app.AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                Log.e(TAG,"Exception   26000   "+e.toString())
                                val builder = android.app.AlertDialog.Builder(
                                        this@HomeActivity,
                                        R.style.MyDialogTheme
                                )

                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: android.app.AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            Log.e(TAG,"Exception   260001   "+t.message)
                            val builder = android.app.AlertDialog.Builder(
                                    this@HomeActivity,
                                    R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: android.app.AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    Log.e(TAG,"Exception   260002   "+e.toString())
                    val builder = android.app.AlertDialog.Builder(this@HomeActivity, R.style.MyDialogTheme)
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: android.app.AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {
                val builder = android.app.AlertDialog.Builder(this@HomeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: android.app.AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }
    override fun onResume() {  // After a pause OR at startup
        super.onResume()
        setdefaultAccountDetails()
    }

}

