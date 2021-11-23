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

class DashLiabilityAdapter (internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    internal var jsonObject: JSONObject? = null
    val color2 = intArrayOf(
        R.color.color_liability1,
        R.color.color_liability2,
        R.color.color_liability3,
        R.color.color_liability4,
        R.color.color_liability5,
        R.color.color_liability6,
        R.color.color_liability7,
        R.color.color_liability8,
        R.color.color_liability9,
        R.color.color_liability10
    )

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_dash_liability, parent, false
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

            if (holder is DashLiabilityAdapter.MainViewHolder) {


                holder.tv_liability_color!!.setBackgroundResource(color2[position])
                holder.tv_liability_name!!.setText(jsonObject!!.getString("Account"))

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var tv_liability_color: TextView? = null
        var tv_liability_name: TextView? = null

        init {

            tv_liability_color = v.findViewById<View>(R.id.tv_liability_color) as TextView
            tv_liability_name = v.findViewById<View>(R.id.tv_liability_name) as TextView



        }
    }

}