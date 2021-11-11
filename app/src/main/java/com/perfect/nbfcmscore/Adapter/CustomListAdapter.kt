package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.perfect.nbfcmscore.Model.BarcodeAgainstCustomerAccountList
import com.perfect.nbfcmscore.R
import java.util.*

class CustomListAdapter(var mContext: Context, arraylist: ArrayList<BarcodeAgainstCustomerAccountList>) : BaseAdapter() {
    var inflater: LayoutInflater
    private val arraylist: ArrayList<BarcodeAgainstCustomerAccountList>

    inner class ViewHolder {
        var tvAccount: TextView? = null
    }

    override fun getCount(): Int {
        return arraylist.size
    }

    override fun getItem(position: Int): BarcodeAgainstCustomerAccountList {
        return arraylist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var view = view
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.single_item_account, null)
            holder.tvAccount = view.findViewById<View>(R.id.tvAccount) as TextView
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        holder.tvAccount!!.text = arraylist[position].accountNumber
        return view
    }

    init {
        inflater = LayoutInflater.from(mContext)
        this.arraylist = arraylist
    }
}