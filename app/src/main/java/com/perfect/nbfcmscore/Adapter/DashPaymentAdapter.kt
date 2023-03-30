package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DashPaymentAdapter (internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    internal var jsonObject: JSONObject? = null
    var status: String? = ""
    val color3 = intArrayOf(
        R.color.color_payment1,
        R.color.color_payment2,
        R.color.color_payment3,
        R.color.color_payment4
    )

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_dash_payments, parent, false
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

            if (holder is DashPaymentAdapter.MainViewHolder) {


                holder.tv_payment_color!!.setBackgroundResource(color3[position])
                val str1 = jsonObject!!.getString("TransType")
                if (str1.equals("R")) {
                    status = "Receipt"
                } else if (str1.equals("P")) {
                    status = "Payment"
                }

                holder.tv_payment_name!!.setText(status)


            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var tv_payment_color: TextView? = null
        var tv_payment_name: TextView? = null

        init {

            tv_payment_color = v.findViewById<View>(R.id.tv_payment_color) as TextView
            tv_payment_name = v.findViewById<View>(R.id.tv_payment_name) as TextView



        }
    }
}