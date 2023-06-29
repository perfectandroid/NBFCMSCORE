package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aceware.cobrandprepaidkit.CobrandPrepaidSdkkit
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import `in`.aabhasjindal.otptextview.OtpTextView
import org.json.JSONException
import org.json.JSONObject

class Kyc2 : AppCompatActivity() {

    var cobrandPrepaidSdkkit: CobrandPrepaidSdkkit? = null
    private var upiBankCode: String = ""
    private var mobile: String = ""
    private var strAgentEmail: String = ""
    var TAG = "KYC MAIN 2"
    lateinit var textDialogue: TextView
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc2)
        var objString = intent.getStringExtra("obj")

        cobrandPrepaidSdkkit = CobrandPrepaidSdkkit(this)
        val AceMoneyUpiBankCodeSP =
            applicationContext.getSharedPreferences(Config.SHARED_PREF350, 0)
        upiBankCode = AceMoneyUpiBankCodeSP.getString("AceMoneyUpiBankCode", "").toString()
        val mobileNoSP = getSharedPreferences(Config.SHARED_PREF2, 0)
        mobile = mobileNoSP.getString("CusMobile", "").toString()
        val AgentEmailSP = applicationContext.getSharedPreferences(Config.SHARED_PREF351, 0)
        strAgentEmail = AgentEmailSP.getString("AgentEmail", "").toString()


        mobile = "8075115147"
        upiBankCode = "ATA000823"
        strAgentEmail = "kscbp373@gmail.com"
        var obj = JSONObject(objString)
        registerPrepaidCardUser1(obj)


    }

    fun registerPrepaidCardUser1(obj: JSONObject) {
        val status: String
        val message: String
        val name: String
        val dob: String
        val address: String
        val aadhar: String
        val gender: String
        val city: String
        val pincode: String
        try {
            status = obj.getString("status")
            message = obj.getString("message")
            name = "DIVIN T"
            dob = "1996-01-11"
            address = "THEVARPADATH HOUSE,ARIYOOR POST"
            aadhar = ""
            gender = "Male"
            city = "ARIYOOR "
            pincode = "678583"
            val map = java.util.ArrayList<String>()
            map.add(strAgentEmail)
            map.add(aadhar)
            map.add(address)
            map.add(dob)
            map.add("")
            map.add(name)
            map.add(gender)
            map.add(name)
            map.add(city)
            map.add(mobile)
            map.add("")
            map.add(pincode)
            Log.e(TAG, "map $map")
            cobrandPrepaidSdkkit?.initService(upiBankCode, "registerCredUsers", map)
            val dialog1: Dialog? = showLoadingDialog("Registering prepaid card user")
            cobrandPrepaidSdkkit?.setResponseCall(object : CobrandPrepaidSdkkit.ResponseListener {
                override fun onSuccess(s: String, jsonObject: JsonObject) {
                       dialog1?.dismiss()
                    progressDialog?.dismiss()
                    Log.e(TAG, "s_registerPrepaid json $jsonObject")
                    Log.e(TAG, "s_registerPrepaid s $s")
                    if (jsonObject["status"].asBoolean == true) {
                        registerUserAPI()
                    } else {
                    }
                }

                override fun onFailure(s: String, jsonObject: JsonObject) {
                       dialog1?.dismiss()
                    progressDialog?.dismiss()
                    registerUserAPI()
                    //                    {"status":false,"data":[],"message":"success","errors":[{"message":"Invalid Credentials, do not match our records"}]}
                    Log.e(TAG, "f_registerPrepaid json $jsonObject")
                    Log.e(TAG, "f_registerPrepaid s $s")
                    //                    try {
                    //                        if (jsonObject.get("status").getAsBoolean() == false) {
                    //                            JSONObject object = null;
                    //                            try {
                    //                                object = new JSONObject(jsonObject.toString());
                    //
                    //                                JSONObject jsonObject1 = object.getJSONArray("errors").getJSONObject(0);
                    //                                Log.e(TAG, "713  :  " + jsonObject1.getString("message"));
                    //                                AlertDialog.Builder builder = new AlertDialog.Builder(KycMain.this);
                    //                                builder.setMessage("Alert: " + jsonObject1.getString("message"))
                    //                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    //                                            @Override
                    //                                            public void onClick(DialogInterface dialog, int which) {
                    //                                                dialog.dismiss();
                    //
                    //                                            }
                    //                                        });
                    //                                AlertDialog alert = builder.create();
                    //                                alert.show();
                    //                            } catch (JSONException e) {
                    //                                throw new RuntimeException(e);
                    //                            }
                    //
                    //                        } else {
                    //
                    //                        }
                    //                    }catch (Exception e)
                    //                    {
                    //
                    //                    }
                }
            })
        } catch (e: JSONException) {
            throw java.lang.RuntimeException(e)
        }
    }

    fun showLoadingDialog(msg: String?): Dialog? {
        progressDialog = ProgressDialog(this@Kyc2, R.style.Progress)
        progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setIndeterminate(true)
        progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
        progressDialog!!.show()
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_loading)
        textDialogue = dialog.findViewById<TextView>(R.id.text)
        textDialogue.setText(msg)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.show()
        return dialog
    }

    private fun registerUserAPI() {
        val map = java.util.ArrayList<String>()
        map.add("8075115147")
        //        map.add(mobile);
        cobrandPrepaidSdkkit!!.initService(upiBankCode, "registerUserApi", map)
        cobrandPrepaidSdkkit!!.setResponseCall(object : CobrandPrepaidSdkkit.ResponseListener {
            override fun onSuccess(s: String, jsonObject: JsonObject) {
                Log.e(TAG, "s_registerUserAPI json $jsonObject")
                Log.e(TAG, "s_registerUserAPI s $s")
                //                {"status":false,"meta":{"code":200,"message":"success"},"message":"success","data":[],"errors":[{"message":"Min KYC Completed. Please wait for the approval of card activation"}]}
                showBottomOtp()
                //                try {
//                    if (jsonObject.get("status").getAsBoolean() == true) {
//                        showBottomOtp();
//
//                    } else {
//                        JSONObject object = null;
//                        try {
//                            object = new JSONObject(jsonObject.toString());
//
//                            JSONObject jsonObject1 = object.getJSONArray("errors").getJSONObject(0);
//                            Log.e(TAG, "713  :  " + jsonObject1.getString("message"));
//                            AlertDialog.Builder builder = new AlertDialog.Builder(KycMain.this);
//                            builder.setMessage("Alert: " + jsonObject1.getString("message"))
//                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//
//                                        }
//                                    });
//                            AlertDialog alert = builder.create();
//                            alert.show();
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }catch (Exception e)
//                {
//
//                }
            }

            override fun onFailure(s: String, jsonObject: JsonObject) {
                Log.e(TAG, "f_registerUserAPI json $jsonObject")
                Log.e(TAG, "f_registerUserAPI s $s")
            }
        })
    }

    private fun showBottomOtp() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_register_user_otp)
        val otp_view: OtpTextView? = bottomSheetDialog.findViewById<OtpTextView>(R.id.otp_view)
        val imgclose = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose)
        val txtotpsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtotpsave)
        val pin = arrayOf("")
        txtotpsave!!.setOnClickListener {
            val otp: String = otp_view!!.getOTP()
            if (otp.length != 6) {
                otp_view.showError()
            } else {
                registerUserOtp(otp, "", bottomSheetDialog)
            }
        }
        imgclose!!.setOnClickListener { bottomSheetDialog.dismiss()
        finish()
        }
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }

    private fun registerUserOtp(otp: String, otpRef: String, bottomSheetDialog: BottomSheetDialog) {
        val map = java.util.ArrayList<String>()
        map.add(mobile)
        map.add(otpRef) // take from response of registerUserApi
        map.add(otp)
        cobrandPrepaidSdkkit!!.initService(upiBankCode, "registerUserOtp", map)
        cobrandPrepaidSdkkit!!.setResponseCall(object : CobrandPrepaidSdkkit.ResponseListener {
            override fun onSuccess(s: String, jsonObject: JsonObject) {
                bottomSheetDialog.dismiss()
                try {
                    if (jsonObject["status"].asBoolean == true) {
                        var `object`: JSONObject? = null
                        try {
                            `object` = JSONObject(jsonObject.toString())
                            val token = ""
                            val jsonObject1 = `object`.getJSONArray("data").getJSONObject(0)
                            Log.e(TAG, "713  :  " + jsonObject1.getString("message"))
                            val builder = AlertDialog.Builder(this@Kyc2)
                            builder.setMessage("Alert: " + jsonObject1.getString("message"))
                                .setPositiveButton(
                                    "Ok"
                                ) { dialog, which ->
                                    dialog.dismiss()
                                    showBottomAadhar(token)
                                }
                            val alert = builder.create()
                            alert.show()
                        } catch (e: JSONException) {
                            throw java.lang.RuntimeException(e)
                        }
                    } else {
                        var `object`: JSONObject? = null
                        try {
                            `object` = JSONObject(jsonObject.toString())
                            val jsonObject1 = `object`.getJSONArray("errors").getJSONObject(0)
                            Log.e(TAG, "713  :  " + jsonObject1.getString("message"))
                            val builder = AlertDialog.Builder(this@Kyc2)
                            builder.setMessage("Alert: " + jsonObject1.getString("message"))
                                .setPositiveButton(
                                    "Ok"
                                ) { dialog, which -> dialog.dismiss() }
                            val alert = builder.create()
                            alert.show()
                        } catch (e: JSONException) {
                            throw java.lang.RuntimeException(e)
                        }
                    }
                } catch (e: java.lang.Exception) {
                }
            }

            override fun onFailure(s: String, jsonObject: JsonObject) {
                bottomSheetDialog.dismiss()
            }
        })
    }

    private fun showBottomAadhar(token: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_forget_aadhar)
        val edtAadhar = bottomSheetDialog.findViewById<EditText>(R.id.aadhar)
        val imgclose = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose)
        val txtotpsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtotpsave)
        val pin = arrayOf("")
        txtotpsave!!.setOnClickListener {
            val aadhar = edtAadhar!!.text.toString()
            if (aadhar.length != 12) {
                Toast.makeText(
                    this@Kyc2,
                    "Please provide valid aadhar number",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                registerUserMiniKyc(aadhar, token)
            }
        }
        imgclose!!.setOnClickListener { bottomSheetDialog.dismiss()
        finish()
        }
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }

    private fun registerUserMiniKyc(aadhar: String, token: String) {
        val map = java.util.ArrayList<String>()
        map.add(mobile)
        map.add(token) // take from response of registerUserOtp
        map.add(aadhar)
        cobrandPrepaidSdkkit!!.initService(upiBankCode, "registerUserMinkyc", map)
        cobrandPrepaidSdkkit!!.setResponseCall(object : CobrandPrepaidSdkkit.ResponseListener {
            override fun onSuccess(s: String, jsonObject: JsonObject) {}
            override fun onFailure(s: String, jsonObject: JsonObject) {}
        })
    }
}