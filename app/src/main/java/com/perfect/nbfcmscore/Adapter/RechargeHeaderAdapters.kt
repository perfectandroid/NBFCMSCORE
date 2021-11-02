package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONException

class RechargeHeaderAdapters(internal val mContext: Context, internal val jsInfo: ArrayList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selPos = 0
    internal var jsonObject: ArrayList<String>? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_recharge_header, parent, false
        )
        vh = MainViewHolder(v)
        return vh
    }
    override fun getItemCount(): Int {
        return jsInfo.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {

            if (holder is RechargeHeaderAdapters.MainViewHolder) {

                if (selPos == position) {
                    (holder as RechargeHeaderAdapters.MainViewHolder).tv_header!!.setBackgroundDrawable(
                        mContext.getResources().getDrawable(R.drawable.underline_tab)
                    )
                } else {
                    (holder as RechargeHeaderAdapters.MainViewHolder).tv_header!!.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.underline_trans)
                    )
                }
                holder.tv_header!!.setText(jsInfo!!.get(position))
//                holder.tv_validity!!.setText(jsonObject!!.getString("validity"))
//                holder.tv_description!!.setText(jsonObject!!.getString("description"))

                holder.tv_header!!.setTag(position)
                holder.tv_header!!.setOnClickListener(View.OnClickListener {
                 //   clickListener!!.onItemClicknew(position, holder.tv_header!!.text.toString(),"0")
                    clickListener!!.onClick(0, holder.tv_header!!.text.toString())

                    selPos = position
                    notifyDataSetChanged()

                })

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


       // internal var rltv_main: RelativeLayout? = null
        var tv_header: TextView? = null
//        var tv_validity: TextView? = null
//        var tv_description: TextView? = null


        init {

//            rltv_main = v.findViewById<View>(R.id.rltv_main) as RelativeLayout

            tv_header = v.findViewById<View>(R.id.tv_header) as TextView

        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}