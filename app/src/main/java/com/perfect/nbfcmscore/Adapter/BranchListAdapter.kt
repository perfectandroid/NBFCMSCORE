package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class BranchListAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_branch_list, parent, false
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
            if (holder is BranchListAdapter.MainViewHolder) {
                holder.tv_BranchName!!.setText(jsonObject!!.getString("BranchName"))
                holder.tv_BankName!!.setText(jsonObject!!.getString("BankName"))

                var address = ""
                if(!jsonObject!!.getString("Address").equals("")){
                    address = jsonObject!!.getString("Address")
                }
                if(!jsonObject!!.getString("Place").equals("")){
                    address = address+" , "+jsonObject!!.getString("Place")
                }
                if(!jsonObject!!.getString("Post").equals("")){
                    address = address+" , "+jsonObject!!.getString("Post")
                }
                if(!jsonObject!!.getString("District").equals("")){
                    address = address+" , "+jsonObject!!.getString("District")
                }

                holder.tv_Address!!.setText(address)

                holder.ll_branchrow!!.setOnClickListener(View.OnClickListener {
                    //  Log.e("ScratchCard","ScratchCard  114    "+jsonObject.getString("CRAmount") );
                    clickListener!!.onClick(position)

                })



            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        internal var ll_branchrow: LinearLayout? = null
        var tv_BranchName: TextView? = null
        var tv_BankName: TextView? = null
        var tv_Address: TextView? = null

        init {

            ll_branchrow = v.findViewById<View>(R.id.ll_branchrow) as LinearLayout
            tv_BranchName = v.findViewById<View>(R.id.tv_BranchName) as TextView
            tv_BankName = v.findViewById<View>(R.id.tv_BankName) as TextView
            tv_Address = v.findViewById<View>(R.id.tv_Address) as TextView

        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}