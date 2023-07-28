package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.R

class OtherBankActivity : AppCompatActivity()  , View.OnClickListener{

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "OtherBankActivity"
    var submode:String?=null
    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var tv_header: TextView? = null
    var ll_imps: LinearLayout? = null
    var ll_neft: LinearLayout? = null
    var ll_fundtransfer: LinearLayout? = null
    var ll_rtgs: LinearLayout? = null
    var tv_imps: TextView? = null
    var tv_neft: TextView? = null
    var tv_rtgs: TextView? = null
    var tv_fndtransfer: TextView? = null

    var ll_imps_main: LinearLayout? = null
    var ll_neft_main: LinearLayout? = null
    var ll_rtgs_main: LinearLayout? = null
    var ll_fundtransfer_main: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_bank)

        setInitialise()
        setRegister()

        val ImpsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF151, 0)
        tv_imps!!.setText(ImpsSP.getString("IMPS", null))

        val NeftSP = applicationContext.getSharedPreferences(Config.SHARED_PREF152, 0)
        tv_neft!!.setText(NeftSP.getString("NEFT", null))

        val RtgsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF153, 0)
        tv_rtgs!!.setText(RtgsSP.getString("RTGS", null))

        val fundtransfrstatsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF154, 0)
        tv_fndtransfer!!.setText(fundtransfrstatsSP.getString("FUNDTRANSFERSTATUS", null))

        val OthrbnkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF64, 0)
        tv_header!!.setText(OthrbnkSP.getString("OtherBank", null))

        ApplayLicence()

    }

    private fun ApplayLicence() {
        val LicenceImpsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF293,0)
        val LicenceNeftSP = applicationContext.getSharedPreferences(Config.SHARED_PREF295,0)
        val LicenceRtgsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF298,0)

        if(!LicenceImpsSP.getString("LicenceImps",null).equals("true")){
            ll_imps_main!!.visibility = View.GONE
        }
        if(!LicenceNeftSP.getString("LicenceNeft",null).equals("true")){
            ll_neft!!.visibility = View.GONE
        }

        if(!LicenceRtgsSP.getString("LicenceRtgs",null).equals("true")){
            ll_rtgs_main!!.visibility = View.GONE
        }



    }

    private fun setInitialise() {
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        tv_imps = findViewById<TextView>(R.id.tv_imps)
        tv_neft = findViewById<TextView>(R.id.tv_neft)
        tv_rtgs = findViewById<TextView>(R.id.tv_rtgs)
        tv_fndtransfer = findViewById<TextView>(R.id.tv_fndtransfer)


        tv_header = findViewById<TextView>(R.id.tv_header)
        ll_imps = findViewById<LinearLayout>(R.id.ll_imps)
        ll_neft = findViewById<LinearLayout>(R.id.ll_neft)
        ll_rtgs = findViewById<LinearLayout>(R.id.ll_rtgs)
        ll_fundtransfer = findViewById<LinearLayout>(R.id.ll_fundtransfer)

        ll_neft_main = findViewById<LinearLayout>(R.id.ll_neft_main)
        ll_imps_main = findViewById<LinearLayout>(R.id.ll_imps_main)
        ll_rtgs_main = findViewById<LinearLayout>(R.id.ll_rtgs_main)
        ll_fundtransfer_main = findViewById<LinearLayout>(R.id.ll_fundtransfer_main)


    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)

        ll_imps!!.setOnClickListener(this)
        ll_neft!!.setOnClickListener(this)
        ll_rtgs!!.setOnClickListener(this)
        ll_fundtransfer!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                // onBackPressed()
                startActivity(Intent(this@OtherBankActivity, HomeActivity::class.java))
                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@OtherBankActivity, HomeActivity::class.java))
                finish()
            }

            R.id.ll_imps ->{
//                startActivity(Intent(this@OtherBankActivity, OtherBankFundTransferActivity::class.java))
//                finish()

                var intent = Intent(this@OtherBankActivity, OtherBankFundTransferActivity::class.java)
                intent.putExtra("TYPE", "IMPS")
                startActivity(intent)
            }

            R.id.ll_neft ->{

                var intent = Intent(this@OtherBankActivity, OtherBankFundTransferActivity::class.java)
                intent.putExtra("TYPE", "NEFT")
                startActivity(intent)
            }

            R.id.ll_rtgs ->{

                var intent = Intent(this@OtherBankActivity, OtherBankFundTransferActivity::class.java)
                intent.putExtra("TYPE", "RTGS")
                startActivity(intent)


            }
            R.id.ll_fundtransfer ->{
                submode="0"
                var intent = Intent(this@OtherBankActivity, OtherfundTransferHistory::class.java)
                intent.putExtra("submode", submode)
                startActivity(intent)
            }

        }
    }
    override fun onResume() {
        super.onResume()
        IdleUtil.startLogoutTimer(this, this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        IdleUtil.startLogoutTimer(this, this)
    }
}