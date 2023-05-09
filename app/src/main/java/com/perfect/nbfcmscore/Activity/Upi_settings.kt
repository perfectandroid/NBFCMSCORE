package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import `in`.aabhasjindal.otptextview.OtpTextView

class Upi_settings : AppCompatActivity(),View.OnClickListener {
    lateinit var ll_create_upiid:LinearLayout
    lateinit var ll_change_upiid:LinearLayout
    lateinit var ll_forgotMpin:LinearLayout
    lateinit var backBtn:ImageView
    lateinit var otpGlobal:String
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upi_settings)
        getSupportActionBar()?.hide();
        setID()
        setListners()
        val UPIPinSETSP = getSharedPreferences(Config.SHARED_PREF77, 0)
        val UPIPinSET = UPIPinSETSP.getString("UPIPinSET", "")
//        if (UPIPinSET == "true") {
//            ll_create_upiid.visibility = View.GONE
//            ll_change_upiid.visibility = View.VISIBLE
//            ll_forgotMpin.visibility = View.VISIBLE
//        } else {
//            ll_create_upiid.visibility = View.VISIBLE
//            ll_change_upiid.visibility = View.GONE
//            ll_forgotMpin.visibility = View.GONE
//        }
    }
    private fun setID() {
        backBtn = findViewById<View>(R.id.backBtn) as ImageView
        ll_create_upiid = findViewById<View>(R.id.ll_create_upiid) as LinearLayout
        ll_change_upiid = findViewById<View>(R.id.ll_change_upiid) as LinearLayout
        ll_forgotMpin = findViewById<View>(R.id.ll_forgotMpin) as LinearLayout
    }

    private fun setListners() {
        ll_create_upiid.setOnClickListener(this)
        ll_change_upiid.setOnClickListener(this)
        ll_forgotMpin.setOnClickListener(this)
        backBtn.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.backBtn->
                onBackPressed()
            R.id.ll_create_upiid->
                showBottomPin()
            R.id.ll_change_upiid->
                showBottomChangePin()
            R.id.ll_forgotMpin->
                showBottomForgetPin()
        }
    }

    private fun showBottomForgetPin() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_forget_pin)
        val lin_otp = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin_otp)
        val lin1 = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin1)
        val otp_view = bottomSheetDialog.findViewById<OtpTextView>(R.id.otp_view)
        val lin_pin = bottomSheetDialog.findViewById<LinearLayout>(R.id.lin_pin)
        val txtsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtsave)
        val imgclose = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose)
        val imgclose1 = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose1)
        val imgclose2 = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose2)
        val txtotpsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtotpsave)
        val txtnext = bottomSheetDialog.findViewById<ImageView>(R.id.txtnext)
        val txtpin = bottomSheetDialog.findViewById<EditText>(R.id.txtpin0)
        val txtpin1 = bottomSheetDialog.findViewById<EditText>(R.id.txtpin1)
        val txtpin2 = bottomSheetDialog.findViewById<EditText>(R.id.txtpin2)
        val pin = arrayOf("")
        txtnext!!.setOnClickListener {
            //sendOTPForgotPin(lin1, lin_otp)
             }
        txtsave!!.setOnClickListener {
            val pass1 = txtpin1!!.text.toString()
            val pass2 = txtpin2!!.text.toString()
            if (pass1.equals("", ignoreCase = true) || pass2.equals("", ignoreCase = true)) {
                Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else if (pass1.length < 6 || pass2.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Please check the length of pin",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (pass1 != pass2) {
                Toast.makeText(
                    applicationContext,
                    "Please check provided pin.",
                    Toast.LENGTH_SHORT
                )
            } else {
               // verifyOTPForgotPin(bottomSheetDialog, otpGlobal, pass1)
            }
        }
        txtotpsave!!.setOnClickListener {
            val otp = otp_view!!.otp
            if (otp.length != 6) {
                otp_view.showError()
            } else {
                progressDialog = ProgressDialog(this@Upi_settings, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.isIndeterminate = true
                progressDialog!!.setIndeterminateDrawable(
                    resources
                        .getDrawable(R.drawable.progress)
                )
                progressDialog!!.show()
                Handler().postDelayed({
                    progressDialog!!.dismiss()
                    if (otp == otpGlobal) {
                        lin_pin!!.visibility = View.VISIBLE
                        lin_otp!!.visibility = View.GONE
                        //
                    } else {
                        otp_view.showError()
                    }
                }, 2000)
            }
        }
        imgclose!!.setOnClickListener { bottomSheetDialog.dismiss() }
        imgclose1!!.setOnClickListener { bottomSheetDialog.dismiss() }
        imgclose2!!.setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }


    private fun showBottomChangePin() {
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.bottom_change_pin)
            val lin_otp = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin_otp)
            val otp_view: OtpTextView? = bottomSheetDialog.findViewById<OtpTextView>(R.id.otp_view)
            val lin_pin = bottomSheetDialog.findViewById<LinearLayout>(R.id.lin_pin)
            val txtsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtsave)
            val imgclose1 = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose1)
            val imgclose2 = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose2)
            val txtotpsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtotpsave)
            val txtpin = bottomSheetDialog.findViewById<EditText>(R.id.txtpin0)
            val txtpin1 = bottomSheetDialog.findViewById<EditText>(R.id.txtpin1)
            val txtpin2 = bottomSheetDialog.findViewById<EditText>(R.id.txtpin2)

            val pin = arrayOf("")
            txtsave!!.setOnClickListener {
                val passOld = txtpin!!.text.toString()
                val passNew = txtpin1!!.text.toString()
                val pass3 = txtpin2!!.text.toString()
                Log.v("dsfsdfsfds", "passOld $passOld")
                Log.v("dsfsdfsfds", "passNew $passNew")
                Log.v("dsfsdfsfds", "pass3 $pass3")
                if (passOld.equals("", ignoreCase = true) || passNew.equals(
                        "",
                        ignoreCase = true
                    ) || pass3.equals("", ignoreCase = true)
                ) {
                    Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                        .show()
                } else if (passOld.length < 6 || passNew.length < 6 || pass3.length < 6) {
                    Toast.makeText(
                        applicationContext,
                        "Please check the length of pin",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (pass3 != passNew) {
                    Toast.makeText(
                        applicationContext,
                        "Please check provided pin.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    //                    sendOTPChangePin(lin_otp, lin_pin, passOld, passNew);
                    // verifyOTPChangePin(bottomSheetDialog, "123456", passNew, passOld)
                }
            }
            txtotpsave!!.setOnClickListener {
//            val passOld = txtpin!!.text.toString()
//            val passNew = txtpin1!!.text.toString()
//            val pass3 = txtpin2!!.text.toString()
//            Log.v("dsfsdfsfds", "passOld $passOld")
//            Log.v("dsfsdfsfds", "passNew $passNew")
//            Log.v("dsfsdfsfds", "pass3 $pass3")
//            val otp: String = otp_view?.getOTP()
//            if (otp.length != 6) {
//                otp_view?.showError()
//            } else {
//               // verifyOTPChangePin(bottomSheetDialog, otp, passNew, passOld)
//            }
            }
            imgclose1!!.setOnClickListener { bottomSheetDialog.dismiss() }
            imgclose2!!.setOnClickListener { bottomSheetDialog.dismiss() }
            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()
        }

        private fun showBottomPin() {
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.bottom_upi_pin)
            val lin_no_pin = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin_no_pin)
            val lin_otp = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin_otp)
            val otp_view: OtpTextView? = bottomSheetDialog.findViewById<OtpTextView>(R.id.otp_view)
            val lin_pin = bottomSheetDialog.findViewById<LinearLayout>(R.id.lin_pin)
            val pinclick = bottomSheetDialog.findViewById<ImageView>(R.id.pinclick)
            val txtsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtsave)
            val txtotpsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtotpsave)
            val txtpin = bottomSheetDialog.findViewById<EditText>(R.id.txtpin)
            val txtpin2 = bottomSheetDialog.findViewById<EditText>(R.id.txtpin2)
            val pin = arrayOf("")
            pinclick!!.setOnClickListener {
                lin_no_pin!!.visibility = View.GONE
                lin_pin!!.visibility = View.VISIBLE
            }
            txtsave!!.setOnClickListener {
                val pass1 = txtpin!!.text.toString()
                val pass2 = txtpin2!!.text.toString()
                if (pass1.equals("", ignoreCase = true) || pass2.equals("", ignoreCase = true)) {
                    Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                        .show()
//                DialogUtil.showAlert(
//                    com.creativethoughts.iscore.Upi_main.context,
//                    "Please fill all fields"
//                )
                } else if (pass1.length < 6 || pass2.length < 6) {
                    Toast.makeText(
                        applicationContext,
                        "Please check the length of pin",
                        Toast.LENGTH_SHORT
                    ).show()
//                DialogUtil.showAlert(
//                    com.creativethoughts.iscore.Upi_main.context,
//                    "Please check the length of pin."
//                )
                } else if (pass1 != pass2) {
                    Toast.makeText(
                        applicationContext,
                        "Please check provided pin.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
//                DialogUtil.showAlert(
//                    com.creativethoughts.iscore.Upi_main.context,
//                    "Please check provided pin."
//                )
                } else {
                    pin[0] = pass1
                    //    verifyOTPNewPin(bottomSheetDialog, "123456", pin[0])
                    bottomSheetDialog.setCancelable(false)
                }
            }
            bottomSheetDialog.show()
        }
}