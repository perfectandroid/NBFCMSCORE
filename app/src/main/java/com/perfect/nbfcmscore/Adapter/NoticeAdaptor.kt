package com.perfect.nbfcmscore.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class NoticeAdaptor(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.noticelist, parent, false
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
                var inPositn = position+1
                holder.txt_slno!!.setText(""+inPositn)
                holder.txt_NoticeTypeName!!.setText(jsonObject!!.getString("NoticeTypeName"))
                holder.txt_AccountType!!.setText(jsonObject!!.getString("AccountType") )
                holder.txt_AccountNo!!.setText(jsonObject!!.getString("AccountNo"))
                holder.txt_NoticeDate!!.setText(jsonObject!!.getString("NoticeDate") )
                holder.txt_DueAmount!!.setText("₹ "+ Config.getDecimelFormate(jsonObject!!.getDouble("DueAmount")))

                }
            } catch (e: JSONException) {
            e.printStackTrace()
            }
}

inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



internal var llmain: LinearLayout? = null
var txt_slno: TextView? = null
var txt_NoticeTypeName: TextView? = null
var txt_AccountType: TextView? = null
var txt_AccountNo: TextView? = null
var txt_NoticeDate: TextView? = null
var txt_DueAmount: TextView? = null



    init {

        llmain = v.findViewById<View>(R.id.llmain) as LinearLayout
        txt_slno = v.findViewById<View>(R.id.txt_slno) as TextView
        txt_NoticeTypeName = v.findViewById<View>(R.id.txt_NoticeTypeName) as TextView
        txt_AccountType = v.findViewById<View>(R.id.txt_AccountType) as TextView
        txt_AccountNo = v.findViewById<View>(R.id.txt_AccountNo) as TextView
        txt_NoticeDate = v.findViewById<View>(R.id.txt_NoticeDate) as TextView
        txt_DueAmount = v.findViewById<View>(R.id.txt_DueAmount) as TextView


    }
    }
}