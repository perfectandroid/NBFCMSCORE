package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.PassbookTransactionDetailsActivity
import com.perfect.nbfcmscore.Activity.ProductListDetailsActivity
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ProductListAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_product_list, parent, false
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

            if (holder is ProductListAdapter.MainViewHolder) {

              /*  val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(jsonObject!!.getString("HolidayDate"))
                val formattedDatesString = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(date)*/

                    try {
                        val imagepath = Config.IMAGE_URL+jsonObject!!.getString("ProductImagePath")
                        PicassoTrustAll.getInstance(mContext)!!.load(imagepath).error(android.R.color.transparent).into(holder.img_product!!)
                    }catch (e: Exception){

                    }

                holder.tv_caption!!.setText(jsonObject!!.getString("ProductCaption"))
                holder.tv_prdctdetl!!.setText(jsonObject!!.getString("ProductDetails"))

                holder.ll_productlist!!.setTag(
                        position
                )

                holder.ll_productlist!!.setOnClickListener(
                        View.OnClickListener {
                            try {
                                jsonObject = jsInfo!!.getJSONObject(position)
                                val i = Intent(mContext, ProductListDetailsActivity::class.java)
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("FK_Product", jsonObject!!.getString("FK_Product"))
                                i.putExtra("ProductCaption", jsonObject!!.getString("ProductCaption"))
                                mContext!!.startActivity(i)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        })



            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



        var tv_caption: TextView? = null
        var tv_prdctdetl: TextView? = null
        var img_product: ImageView? = null
        public var ll_productlist: LinearLayout? = null

        init {
            tv_caption = v.findViewById<View>(R.id.tv_caption) as TextView
            tv_prdctdetl = v.findViewById<View>(R.id.tv_prdctdetl) as TextView
            ll_productlist = v.findViewById<LinearLayout>(R.id.ll_productlist)
            img_product = v.findViewById<ImageView>(R.id.img_product)

        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}