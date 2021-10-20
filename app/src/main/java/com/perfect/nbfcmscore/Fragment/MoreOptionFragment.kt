package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.bizcorelite.Api.ApiInterface
import com.perfect.nbfcmscore.Activity.MpinActivity
import com.perfect.nbfcmscore.Adapter.AccountLsitAdaptor
import com.perfect.nbfcmscore.Adapter.LanguageLsitAdaptor
import com.perfect.nbfcmscore.Adapter.MinistatementAdaptor
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MoreOptionFragment : Fragment(){

    private var progressDialog: ProgressDialog? = null
    var rv_ministatementlist: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            R.layout.fragment_moreoption, container,
            false
        )

     //   rv_ministatementlist = v.findViewById<View>(R.id.rv_ministatementlist) as RecyclerView?


        return v
    }

}