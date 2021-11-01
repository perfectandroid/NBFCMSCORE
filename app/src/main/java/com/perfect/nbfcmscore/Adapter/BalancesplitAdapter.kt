package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class BalancesplitAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.activity_splitlist, parent, false
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
            if (holder is BalancesplitAdapter.MainViewHolder) {
//                ((MainViewHolder)holder).tvkey.setText(jsonObject.getString("Key"));
//                ((MainViewHolder)holder).tvvalue.setText(jsonObject.getString("Value"));
                if (jsonObject!!.getString("Key") == "NetAmount") {
                    val date = Calendar.getInstance().time
                    val df = SimpleDateFormat("dd-MM-yyyy")
                    val formattedDate = df.format(date)
                    holder.tvkey!!.setText("Due As On $formattedDate")
                    holder.tvvalue!!.setText(jsonObject!!.getString("Value"))
                    holder.v_sep!!.visibility=View.VISIBLE
                    holder.tvkey!!.setTextColor(Color.parseColor("#B22222"))
                    holder.tv_sep!!.setTextColor(Color.parseColor("#B22222"))
                    holder.tvvalue!!.setTextColor(Color.parseColor("#B22222"))

                } else {
                    holder.tvkey!!.setText(jsonObject!!.getString("Key"))
                    holder.tvvalue!!.setText(jsonObject!!.getString("Value"))
                    holder.v_sep!!.visibility=View.GONE

                }
                //                Log.e(TAG,"Value   57   "+jsonObject.getString("Value"));
//                double num =Double.parseDouble(""+jsonObject.getString("Value"));
//                ((MainViewHolder)holder).tvvalue.setText(""+ CommonUtilities.getDecimelFormate(num));
                if (jsonObject!!.getString("Key") == "Pending Installment") {
                    holder.tvkey!!.setTextColor(Color.parseColor("#000000"))
                    holder.tv_sep!!.setTextColor(Color.parseColor("#000000"))
                    holder.tvvalue!!.setTextColor(Color.parseColor("#000000"))
                    holder.llDetails!!.setBackgroundColor(Color.parseColor("#B0E0E6"))

                }
                if (jsonObject!!.getString("Key") == "BalanceInstallment") {
                    holder.itemView.visibility = View.GONE
                    val params = holder.itemView.layoutParams
                    params.height = 0
                    params.width = 0
                    holder.itemView.layoutParams = params
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }



    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        var tvkey: TextView? = null
        var tvvalue:TextView? = null
        var tv_sl_no:TextView? = null
        var tv_sep:TextView? = null
        var llDetails: LinearLayout? = null
        var imSeperator: ImageView? = null
        var v_sep: View? = null

        init {

            tv_sl_no = v.findViewById(R.id.tv_sl_no)
            v_sep = v.findViewById(R.id.v_sep)
            tvvalue = v.findViewById(R.id.tvvalue)
            tv_sep = v.findViewById(R.id.tv_sep)
            tvkey = v.findViewById(R.id.tvkey)
            llDetails = v.findViewById(R.id.llDetails)

        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}