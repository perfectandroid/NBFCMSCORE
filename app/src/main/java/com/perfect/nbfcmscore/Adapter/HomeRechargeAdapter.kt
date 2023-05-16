package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HomeRechargeAdapter (internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_home_recharge, parent, false
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

            if (holder is HomeRechargeAdapter.MainViewHolder) {

                holder.txtv_recharge!!.setText(jsonObject!!.getString("Rechlabel"))
                holder.im_recharge!!.setImageResource(jsonObject!!.getInt("RechImage"))



                holder.ll_recharge!!.setTag(position)
                holder.ll_recharge!!.setOnClickListener(View.OnClickListener {
                    clickListener!!.onClick(position,"rech")

                })

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        // internal var ll_adp_account: LinearLayout? = null
        var im_recharge: ImageView? = null
        var txtv_recharge: TextView? = null
        var card_recharge: LinearLayout? = null
        var ll_recharge: LinearLayout? = null


        init {

            im_recharge = v.findViewById<View>(R.id.im_recharge) as ImageView
            txtv_recharge = v.findViewById<View>(R.id.txtv_recharge) as TextView
            card_recharge = v.findViewById<View>(R.id.card_recharge) as LinearLayout
            ll_recharge = v.findViewById<View>(R.id.ll_recharge) as LinearLayout


        }
    }


    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}