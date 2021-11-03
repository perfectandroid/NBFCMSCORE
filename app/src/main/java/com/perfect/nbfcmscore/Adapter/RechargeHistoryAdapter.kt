package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RechargeHistoryAdapter (internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    internal var jsonObject: JSONObject? = null


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_recent_recharge, parent, false
        )
        vh = MainViewHolder(v)
        return vh
    }

    override fun getItemCount(): Int {
        return jsInfo.length()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {

            jsonObject = jsInfo.getJSONObject(position)

            if (holder is RechargeHistoryAdapter.MainViewHolder) {


                holder.tv_name!!.setText(jsonObject!!.getString("ProvidersName"))
                holder.tv_nunber!!.setText(jsonObject!!.getString("MobileNo"))
                holder.tv_last_recharge!!.setText("Last Recharge ₹ "+jsonObject!!.getString("RechargeRs")+" on "+jsonObject!!.getString("RechargeDate")+" , "+jsonObject!!.getString("StatusType"))



            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        internal var ll_recent: LinearLayout? = null

        var img_operator: ImageView? = null

        var tv_name: TextView? = null
        var tv_BankName: TextView? = null
        var tv_nunber: TextView? = null
        var tv_last_recharge: TextView? = null

        init {

            ll_recent = v.findViewById<View>(R.id.ll_recent) as LinearLayout

            img_operator = v.findViewById<View>(R.id.img_operator) as ImageView

            tv_name = v.findViewById<View>(R.id.tv_name) as TextView
            tv_nunber = v.findViewById<View>(R.id.tv_nunber) as TextView
            tv_last_recharge = v.findViewById<View>(R.id.tv_last_recharge) as TextView


        }
    }
}