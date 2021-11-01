package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.perfect.nbfcmscore.R

class OtherBankActivity : AppCompatActivity()  , View.OnClickListener{

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "OtherBankActivity"

    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var tv_header: TextView? = null

    var ll_imps: LinearLayout? = null
    var ll_neft: LinearLayout? = null
    var ll_fundtransfer: LinearLayout? = null
    var ll_rtgs: LinearLayout? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_bank)

        setInitialise()
        setRegister()

    }

    private fun setInitialise() {
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        tv_header = findViewById<TextView>(R.id.tv_header)


        ll_imps = findViewById<LinearLayout>(R.id.ll_imps)
        ll_neft = findViewById<LinearLayout>(R.id.ll_neft)
        ll_rtgs = findViewById<LinearLayout>(R.id.ll_rtgs)
        ll_fundtransfer = findViewById<LinearLayout>(R.id.ll_fundtransfer)


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

                var intent = Intent(this@OtherBankActivity, OtherfundTransferHistory::class.java)
                intent.putExtra("trans", "FUND TRANSFER")
                startActivity(intent)
            }
        }
    }
}