package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.util.Log
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

class HomeFundTransferAdapter (internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_home_fundtransfer, parent, false
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

            if (holder is HomeFundTransferAdapter.MainViewHolder) {

//                if (position == 1){
////                    holder.itemView.visibility = View.GONE
////                    val params = holder.itemView.layoutParams
////                    params.height = 0
////                    params.width = 0
////                    holder.itemView.layoutParams = params
//                    holder.ll_fund!!.visibility = View.GONE
//                }else{
                Log.v("dfsdddddddd","FundImage "+jsonObject!!.getInt("FundImage"))
                    holder.txtv_fund!!.setText(jsonObject!!.getString("Fundlabel"))
                    holder.im_fund!!.setImageResource(jsonObject!!.getInt("FundImage"))
//                }


                holder.ll_fund!!.setTag(position)
                holder.ll_fund!!.setOnClickListener(View.OnClickListener {
                    clickListener!!.onClick(position,"fund")

                })

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


       // internal var ll_adp_account: LinearLayout? = null
        var im_fund: ImageView? = null
        var txtv_fund: TextView? = null
     //   var card_fund: CardView? = null
        var ll_fund: LinearLayout? = null


        init {

            im_fund = v.findViewById<View>(R.id.im_fund) as ImageView
            txtv_fund = v.findViewById<View>(R.id.txtv_fund) as TextView
         //   card_fund = v.findViewById<View>(R.id.card_fund) as CardView
            ll_fund = v.findViewById<View>(R.id.ll_fund) as LinearLayout


        }
    }


    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}