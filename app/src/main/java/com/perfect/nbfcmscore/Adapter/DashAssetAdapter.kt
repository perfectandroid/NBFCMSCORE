package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DashAssetAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    val color1 = intArrayOf(
        R.color.color_asset1,
        R.color.color_asset2,
        R.color.color_asset3,
        R.color.color_asset4,
        R.color.color_asset5,
        R.color.color_asset6,
        R.color.color_asset7,
        R.color.color_asset8,
        R.color.color_asset9,
        R.color.color_asset10

    )


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_dash_assets, parent, false
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

            if (holder is DashAssetAdapter.MainViewHolder) {


                holder.tv_asset_color!!.setBackgroundResource(color1[position])
                holder.tv_asset_name!!.setText(jsonObject!!.getString("Account"))




            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var tv_asset_color: TextView? = null
        var tv_asset_name: TextView? = null

        init {

            tv_asset_color = v.findViewById<View>(R.id.tv_asset_color) as TextView
            tv_asset_name = v.findViewById<View>(R.id.tv_asset_name) as TextView



        }
    }

}