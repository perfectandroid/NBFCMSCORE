package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.R
import com.squareup.picasso.Picasso
import java.util.*


class BannerAdapter(private val context: Context?, internal val images: ArrayList<String>) : PagerAdapter() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val myImageLayout = inflater.inflate(R.layout.slide, view, false)
        val myImage = myImageLayout
                .findViewById(R.id.image) as ImageView
       // myImage.setImageResource(images[position])


        try {

            Picasso.with(context).load(images.get(position)) //.placeholder(R.drawable.ban2)
                       .error(R.drawable.ban2)
                    .into(myImage)
       /* if (context != null) {
            val resId: Int =Integer.parseInt(images.get(position))
            Glide.with(context).load(resId).into(myImage);


        }*/
        }
        catch (e:Exception)
        {
            Log.i("error",e.toString())
        }
        view.addView(myImageLayout, 0)
        return myImageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}