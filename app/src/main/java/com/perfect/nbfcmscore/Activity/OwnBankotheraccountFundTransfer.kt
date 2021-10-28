package com.perfect.nbfcmscore.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import java.util.*

class OwnBankotheraccountFundTransfer : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var arrayList1 = ArrayList<String>()
    private var tv_account_no: TextView? = null
    private var tv_branch_name: TextView? = null
    public var tv_balance: TextView? = null
    public var spn_account_type: Spinner? = null
    public var BranchName: String? = null
    public var Balance: String? = null
    public var Acnt: String? = null
    private var jresult: JSONArray? = null

    var compareValue = "Select Account"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherbankfundtransfer)

        setRegViews()
       // getAccountnumber()

    }



    private fun setRegViews() {

        tv_account_no = findViewById<TextView>(R.id.tv_account_no)
        tv_branch_name = findViewById<TextView>(R.id.tv_branch_name)
        spn_account_type = findViewById<Spinner>(R.id.spn_account_type)
        tv_balance = findViewById<TextView>(R.id.tv_balance)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        spn_account_type!!.onItemSelectedListener = this

        BranchName = intent.getStringExtra("Branch")
        Balance = intent.getStringExtra("Balance")
        Acnt = intent.getStringExtra("A/c")
        Log.i("Details", BranchName + Balance + Acnt)

        val amt1 =Balance!!.toDouble()
        tv_account_no!!.text=Acnt
        tv_balance!!.text="\u20B9 " + Config.getDecimelFormate(amt1)

        tv_branch_name!!.text=BranchName


        val items = ArrayList<String>()
        items.add(getString(R.string.savings_bank))
        items.add(getString(R.string.current_account))
        items.add(getString(R.string.cash_credit))

        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item_dark, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_account_type!!.setAdapter(spinnerAdapter)
        spn_account_type!!.setOnItemSelectedListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(
                    Intent(
                        this@OwnBankotheraccountFundTransfer,
                        HomeActivity::class.java
                    )
                )
            }

        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }



    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}