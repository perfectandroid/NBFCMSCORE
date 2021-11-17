package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R

class DepositCalculatorActivity : AppCompatActivity(),View.OnClickListener,AdapterView.OnItemSelectedListener {

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null

    var spn_deposit_type: Spinner? = null
    var spn_beneficiary: Spinner? = null
    var spn_tenure: Spinner? = null

    var etxt_amount: EditText? = null
    var edt_txt_tenure: EditText? = null

    var btn_submit: Button? = null
    var tv_header: TextView? = null

    var deposittype = arrayOfNulls<String>(0)
    var benefcry = arrayOfNulls<String>(0)
    var tenure = arrayOfNulls<String>(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        setRegViews()


    }

    private fun setRegViews() {
        imgBack = findViewById(R.id.imgBack)
        imgHome = findViewById(R.id.imgHome)

        spn_deposit_type = findViewById(R.id.spn_deposit_type)
        spn_beneficiary = findViewById(R.id.spn_beneficiary)
        spn_tenure = findViewById(R.id.spn_tenure)

        etxt_amount = findViewById(R.id.etxt_amount)
        edt_txt_tenure = findViewById(R.id.edt_txt_tenure)

        spn_deposit_type = findViewById(R.id.spn_deposit_type)

        tv_header = findViewById(R.id.tv_header)

        btn_submit = findViewById(R.id.btn_submit)

        btn_submit!!.setOnClickListener(this)

        spn_deposit_type!!.setOnItemSelectedListener(this)
        spn_beneficiary!!.setOnItemSelectedListener(this)
        spn_tenure!!.setOnItemSelectedListener(this)

        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)

        deposittype = arrayOf<String?>("Fixed Deposit", "Cumulative Deposit")
        getDepositType()

        benefcry = arrayOf<String?>("Normal", "Senior Citizen")
        getBenefcryType()

        tenure = arrayOf<String?>("Day", "Month")
        getTenure()



    }

    private fun getTenure() {
        val cc: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, tenure)
        cc.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_tenure!!.adapter = cc
    }

    private fun getBenefcryType() {
        val bb: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, benefcry)
        bb.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_beneficiary!!.adapter = bb
    }

    private fun getDepositType() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, deposittype)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_deposit_type!!.adapter = aa
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@DepositCalculatorActivity, HomeActivity::class.java))
            }
            R.id.btn_submit -> {
                if (isValid()){


                }
            }

        }

    }

    private fun isValid(): Boolean {

        val amt = etxt_amount!!.text.toString()
        val tnr = edt_txt_tenure!!.text.toString()


        if (TextUtils.isEmpty(amt)) {
            etxt_amount!!.error = "Please Enter Amount"
            return false
        }
        etxt_amount!!.setError(null)

        if (TextUtils.isEmpty(tnr)) {
            edt_txt_tenure!!.setError("Please Enter A Value")
            return false
        }

        etxt_amount!!.setError(null)


        return true

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if(spn_tenure!!.selectedItem.toString().equals("Day"))
        {
            edt_txt_tenure!!.setHint("Please Enter Day")
        }
        else if(spn_tenure!!.selectedItem.toString().equals("Month"))
        {
            edt_txt_tenure!!.setHint("Please Enter Month")
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}