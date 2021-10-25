package com.perfect.nbfcmscore.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.perfect.nbfcmscore.R

class OwnBankFundTransfer : AppCompatActivity(), View.OnClickListener {
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    private var tv_account_no: TextView? = null
    private var tv_branch_name: TextView? = null
    private var tv_balance: TextView? = null
    private var BranchName: String? = null
    private var Balance: String? = null
    private var Acnt: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ownbankfundtransfer)

        setRegViews()
        BranchName = intent.getStringExtra("Branch")
        Balance = intent.getStringExtra("Balance")
        Acnt = intent.getStringExtra("A/c")
    }

    private fun setRegViews() {

        tv_account_no = findViewById<TextView>(R.id.tv_account_no)
        tv_branch_name = findViewById<TextView>(R.id.tv_branch_name)
        tv_balance = findViewById<TextView>(R.id.tv_balance)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        tv_account_no!!.text=Acnt
        tv_balance!!.text=Balance
        tv_branch_name!!.text=BranchName
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@OwnBankFundTransfer, HomeActivity::class.java))
            }
        }
    }
}