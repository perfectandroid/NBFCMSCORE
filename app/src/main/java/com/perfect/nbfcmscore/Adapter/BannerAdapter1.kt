package com.perfect.nbfcmscore.Adapter

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.ImageView
import com.perfect.nbfcmscore.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import java.util.ArrayList

class BannerAdapter1(private val context: Context, mResources: List<String>) : PagerAdapter() {
    var layoutInflater: LayoutInflater
    var mResources: List<String> = ArrayList()
    override fun getCount(): Int {
        return mResources.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.slide, container, false)
        val imageView = itemView.findViewById<View>(R.id.image) as ImageView
      //  Picasso.with(context).load(mResources[position]).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageView)
        PicassoTrustAll.getInstance(context)!!.load(mResources.get(position))/*.error(R.drawable.no_image)*/.into(imageView)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    init {
        this.mResources = mResources
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}