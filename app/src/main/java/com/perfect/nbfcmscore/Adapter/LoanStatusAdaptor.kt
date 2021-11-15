package com.perfect.nbfcmscore.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.AccountDetailsActivity
import com.perfect.nbfcmscore.Activity.OTPActivity
import com.perfect.nbfcmscore.Activity.WelcomeActivity
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class LoanStatusAdaptor(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.loanstatuslist, parent, false
        )
        vh = MainViewHolder(v)
        return vh
    }

    override fun getItemCount(): Int {
        return jsInfo.length()
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            jsonObject = jsInfo.getJSONObject(position)
            if (holder is MainViewHolder) {
                holder.tvType!!.setText(jsonObject!!.getString("LoanType") )
                holder.tvPurpose!!.setText(jsonObject!!.getString("LoanPurpose"))
                holder.tvStatus!!.setText(jsonObject!!.getString("LoanStatus") )
                holder.tvApplicationNo!!.setText(jsonObject!!.getString("ApplicationNumber"))
                holder.tvPeriod!!.setText(jsonObject!!.getString("LoanPeriod"))
                holder.tvdate!!.setText(jsonObject!!.getString("ApplicationDate") )
                holder.tvamount!!.setText( "\u20B9 " + Config.getDecimelFormate(jsonObject!!.getDouble("ApplicationAmount")))

                }
            } catch (e: JSONException) {
            e.printStackTrace()
            }
}

inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



internal var llmain: LinearLayout? = null
var tvType: TextView? = null
var tvPurpose: TextView? = null
var tvamount: TextView? = null
var tvdate: TextView? = null
var tvStatus: TextView? = null
var tvPeriod: TextView? = null
var tvApplicationNo: TextView? = null



    init {

    llmain = v.findViewById<View>(R.id.llmain) as LinearLayout
        tvPurpose = v.findViewById<View>(R.id.tvPurpose) as TextView
        tvType = v.findViewById<View>(R.id.tvType) as TextView
        tvamount = v.findViewById<View>(R.id.tvamount) as TextView
        tvdate = v.findViewById<View>(R.id.tvdate) as TextView
        tvStatus = v.findViewById<View>(R.id.tvStatus) as TextView
        tvApplicationNo = v.findViewById<View>(R.id.tvApplicationNo) as TextView
        tvPeriod = v.findViewById<View>(R.id.tvPeriod) as TextView


    }
    }
}