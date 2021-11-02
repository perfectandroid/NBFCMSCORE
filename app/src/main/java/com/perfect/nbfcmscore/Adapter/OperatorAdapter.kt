package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.squareup.picasso.Picasso
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

class OperatorAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null
    val TAG: String = "OperatorAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.operator_list, parent, false
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

            if (holder is OperatorAdapter.MainViewHolder) {
                holder.tv_adp_operator!!.setText(jsonObject!!.getString("ProvidersName"))

                val imagepath = Config.IMAGE_URL+jsonObject!!.getString("ProvidersImagePath")

                Log.e(TAG,"IMGEURL  48   "+imagepath)
                // Set Image & Lang All Category
//                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.applogo)
//                    .into((holder).im_operator)
//
//                Picasso.with(mContext).load(imagepath).into((holder).im_operator)

//                Glide.with(mContext).load(imagepath).placeholder(R.drawable.applogo)
//                    .into((holder).im_operator!!);

//                Glide.with(context)
//                    .load(Uri.parse("file:///android_asset/"+fileName))
//                    .into(imageView);

                holder.ll_adp_operator!!.setOnClickListener(View.OnClickListener {
                    clickListener!!.onClick(position,"operator")

                })

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        internal var ll_adp_operator: LinearLayout? = null
//        internal var ll_map: LinearLayout? = null
//        internal var ll_call: LinearLayout? = null
        var tv_adp_operator: TextView? = null
        var im_operator: ImageView? = null
//        var tv_BankName: TextView? = null
//        var tv_Address: TextView? = null
//        var tv_call: TextView? = null
//        var tv_mobile: TextView? = null

        init {

            ll_adp_operator = v.findViewById<View>(R.id.ll_adp_operator) as LinearLayout
//            ll_map = v.findViewById<View>(R.id.ll_map) as LinearLayout
//            ll_call = v.findViewById<View>(R.id.ll_call) as LinearLayout
//
            tv_adp_operator = v.findViewById<View>(R.id.tv_adp_operator) as TextView
            im_operator = v.findViewById<View>(R.id.im_operator) as ImageView
//            tv_Address = v.findViewById<View>(R.id.tv_Address) as TextView
//            tv_call = v.findViewById<View>(R.id.tv_call) as TextView
//            tv_mobile = v.findViewById<View>(R.id.tv_mobile) as TextView

        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }

}