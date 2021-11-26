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
        R.color.dashboard15,
        R.color.dashboard14,
        R.color.dashboard13,
        R.color.dashboard12,
        R.color.dashboard11,
        R.color.dashboard10,
        R.color.dashboard9,
        R.color.dashboard8,
        R.color.dashboard7,
        R.color.dashboard6,
        R.color.dashboard5,
        R.color.dashboard4,
        R.color.dashboard3,
        R.color.dashboard2,
        R.color.dashboard1,
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