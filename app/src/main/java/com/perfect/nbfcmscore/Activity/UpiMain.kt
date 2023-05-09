package com.perfect.nbfcmscore.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import `in`.aabhasjindal.otptextview.OtpTextView
import java.util.*

class UpiMain : AppCompatActivity(), View.OnClickListener {

    var llmobile: LinearLayout? = null
    var llupi: LinearLayout? = null
    var llqr: LinearLayout? = null
    var llmore: LinearLayout? = null
    var llmobilemain: LinearLayout? = null
    var linPin: LinearLayout? = null
    var llHistory: LinearLayout? = null
    var nestedScrollView: NestedScrollView? = null
    var nestedScrollView_scan: NestedScrollView? = null
    var txt_upi_scan: TextView? = null
    var textupi: TextView? = null
    var btn_clear_upi: TextView? = null
    var btn_submit: TextView? = null
    var btn_submit_upi: TextView? = null
    var select_contact_image: ImageView? = null
    var othersUPI: String? = null
    var profile_image_main: ConstraintLayout? = null
    var linmain: LinearLayout? = null
    var linmain2: LinearLayout? = null
    var llSetting: LinearLayout? = null
    var edtmobile: EditText? = null
    var btn_keypad_back: ImageView? = null
    var btn_keypad_success: ImageView? = null
    var backBtn2: ImageView? = null
    var edtAmountUpi: EditText? = null
    var edtamount: EditText? = null
    var edtMobileUpi: EditText? = null
    var edtUpi: EditText? = null
    var edtAmountUpi_scan: EditText? = null
    var btn_clear: TextView? = null
    var txt_reciver: TextView? = null
    var txt_sending: TextView? = null
    var edtMobileUpi_scan: EditText? = null
    var edtAddress: TextInputEditText? = null
    var edtEmail: TextInputEditText? = null
    var edtName: TextInputEditText? = null
    val PERMISSIONS_REQUEST_READ_CONTACTS = 2
    val PICK_CONTACT_0 = 0
    val PICK_CONTACT_1 = 1
    val PICK_CONTACT_2 = 2
    var paymentType = 0
    var isPinVisible = false
    lateinit var btnShwPassFirst: Button
    lateinit var btnShwPassSecond: Button
    lateinit var btnShwPassThird: Button
    lateinit var btnShwPassFourth: Button
    lateinit var btnShwPassFifth: Button
    lateinit var btnShwPassSixth: Button
    var firstLetter: String? = null
    var secondLetter: String? = null
    var thirdLetter: String? = null
    var fourthLetter: String? = null
    var fifthLetter: String? = null
    var sixthLetter: String? = null
    var counter = 0
    var upiType = 0
    lateinit var btnArray: Array<Button>
    lateinit var btnOthersArray: Array<ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upi_main)
        getSupportActionBar()?.hide();
        setID()
        setRegister()
        if (intent.getStringExtra("from") != null) {
            if (intent.getStringExtra("from").equals("barcode", ignoreCase = true)) {
                othersUPI = intent.getStringExtra("upi")
                loadToScan()
            } else {
                loadToMobile()
            }
        } else {
            loadToMobile()
        }
    }

    private fun setID() {
        select_contact_image = findViewById<View>(R.id.select_contact_image) as ImageView
        btn_keypad_success = findViewById<View>(R.id.btn_keypad_success) as ImageView
        edtAddress = findViewById<View>(R.id.edtAddress) as TextInputEditText
        edtEmail = findViewById<View>(R.id.edtEmail) as TextInputEditText
        edtName = findViewById<View>(R.id.edtName) as TextInputEditText
        backBtn2 = findViewById<View>(R.id.backBtn2) as ImageView
        btn_keypad_back = findViewById<View>(R.id.btn_keypad_back) as ImageView
        llmobile = findViewById<View>(R.id.llmobile) as LinearLayout
        llHistory = findViewById<View>(R.id.llHistory) as LinearLayout
        linPin = findViewById<View>(R.id.linPin) as LinearLayout
        llSetting = findViewById<View>(R.id.llSetting) as LinearLayout
        linmain = findViewById<View>(R.id.linmain) as LinearLayout
        linmain2 = findViewById<View>(R.id.linmain2) as LinearLayout
        txt_upi_scan = findViewById<View>(R.id.txt_upi_scan) as TextView
        btn_clear_upi = findViewById<View>(R.id.btn_clear_upi) as TextView
        textupi = findViewById<View>(R.id.textupi) as TextView
        txt_sending = findViewById<View>(R.id.txt_sending) as TextView
        txt_reciver = findViewById<View>(R.id.txt_reciver) as TextView
        btn_submit = findViewById<View>(R.id.btn_submit) as TextView
        btn_submit_upi = findViewById<View>(R.id.btn_submit_upi) as TextView
        btn_clear = findViewById<View>(R.id.btn_clear) as TextView
        edtmobile = findViewById<View>(R.id.edtmobile) as EditText
        edtUpi = findViewById<View>(R.id.edtUpi) as EditText
        edtAmountUpi = findViewById<View>(R.id.edtAmountUpi) as EditText
        edtAmountUpi_scan = findViewById<View>(R.id.edtAmountUpi_scan) as EditText
        edtamount = findViewById<View>(R.id.edtamount) as EditText
        edtMobileUpi = findViewById<View>(R.id.edtMobileUpi) as EditText
        edtMobileUpi_scan = findViewById<View>(R.id.edtMobileUpi_scan) as EditText
        llupi = findViewById<View>(R.id.llupi) as LinearLayout
        llqr = findViewById<View>(R.id.llqr) as LinearLayout
        llmore = findViewById<View>(R.id.llmore) as LinearLayout
        nestedScrollView = findViewById<View>(R.id.nestedScrollView) as NestedScrollView
        nestedScrollView_scan = findViewById<View>(R.id.nestedScrollView_scan) as NestedScrollView
        llmobilemain = findViewById<View>(R.id.llmobilemain) as LinearLayout
        profile_image_main = findViewById<View>(R.id.profile_image_main) as ConstraintLayout
    }

    private fun setRegister() {
        llmobile?.setOnClickListener(this)
        llSetting?.setOnClickListener(this)
        backBtn2?.setOnClickListener(this)
        btn_submit?.setOnClickListener(this)
        btn_submit_upi?.setOnClickListener(this)
        btn_clear?.setOnClickListener(this)
        btn_clear_upi?.setOnClickListener(this)
        select_contact_image?.setOnClickListener(this)
        llHistory?.setOnClickListener(this)
        llupi?.setOnClickListener(this)
        llqr?.setOnClickListener(this)
        profile_image_main?.setOnClickListener(this)
        btn_keypad_success?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.llmobile) {
            loadToMobile()
        } else if (v?.id == R.id.btn_submit) {
            submitMobilePayment()
        } else if (v?.id == R.id.btn_keypad_success) {
            pinFinalSuccess()
        } else if (v?.id == R.id.btn_submit_upi) {
            submitUpiPayment()
        } else if (v?.id == R.id.backBtn2) {
            onBackPressed()
         } else if (v?.id == R.id.llSetting) {
            startActivity(Intent(this, Upi_settings::class.java))
        } else if (v?.id == R.id.btn_clear) {
            onClickClear()
        } else if (v?.id == R.id.btn_clear_upi) {
            clear()
        } else if (v?.id == R.id.select_contact_image) {
            requestContactPermission(0)
        } else if (v?.id == R.id.llupi) {
            loadToUpi()
        } else if (v?.id == R.id.llqr) {
            startActivity(Intent(this, BarcodeMain::class.java))
        } else if (v?.id == R.id.profile_image_main) {
            startActivity(Intent(this, Upi_share::class.java))
        } else if (v?.id == R.id.llHistory) {
            startActivity(Intent(this, UserOnboardUPI::class.java))
        } else if (v?.id == R.id.btn_keypad_back) {
            pinBackPressed()
        } else {
            val btn = v as Button
            if (Arrays.asList(*btnArray).contains(btn)) {
                //if (counter > 3)
                if (counter > 5) return
                counter++
                when (counter) {
                    1 -> {
                        btnShwPassFirst.setBackgroundResource(R.drawable.show_upi_btn)
                        firstLetter = btn.text.toString()
                    }
                    2 -> {
                        btnShwPassSecond.setBackgroundResource(R.drawable.show_upi_btn)
                        secondLetter = btn.text.toString()
                    }
                    3 -> {
                        btnShwPassThird.setBackgroundResource(R.drawable.show_upi_btn)
                        thirdLetter = btn.text.toString()
                    }
                    4 -> {
                        btnShwPassFourth.setBackgroundResource(R.drawable.show_upi_btn)
                        fourthLetter = btn.text.toString()
                    }
                    5 -> {
                        btnShwPassFifth.setBackgroundResource(R.drawable.show_upi_btn)
                        fifthLetter = btn.text.toString()
                    }
                    6 -> {
                        btnShwPassSixth.setBackgroundResource(R.drawable.show_upi_btn)
                        sixthLetter = btn.text.toString()
                        if (firstLetter!!.isEmpty() || secondLetter!!.isEmpty() || thirdLetter!!.isEmpty() || fourthLetter!!.isEmpty() || fifthLetter!!.isEmpty() || sixthLetter!!.isEmpty()) return
                    }
                    else -> {}
                }
            }
        }
    }

    private fun pinFinalSuccess() {
        val pin =
            firstLetter + secondLetter + thirdLetter + fourthLetter + fifthLetter + sixthLetter
        Log.v("sfdsfdsddd", "pin $pin")
        try {
            val pinInt = pin.toInt()
            if (pin.length != 6) {
                Toast.makeText(this, "Invalid Pin", Toast.LENGTH_SHORT).show()

            } else {
                verifyPin(pin)
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Invalid Pin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyPin(pin: String) {

    }

    private fun pinBackPressed() {
        if (counter > 0) counter--
        when (counter) {
            5 -> {
                btnShwPassFirst.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassSecond.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassThird.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassFourth.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassFifth.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassSixth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                sixthLetter = null
            }
            4 -> {
                btnShwPassFirst.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassSecond.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassThird.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassFourth.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassFifth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassSixth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                fifthLetter = null
                sixthLetter = null
            }
            3 -> {
                btnShwPassFirst.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassSecond.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassThird.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassFourth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassFifth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassSixth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                fourthLetter = null
                fifthLetter = null
                sixthLetter = null
            }
            2 -> {
                btnShwPassFirst.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassSecond.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassThird.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassFourth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassFifth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassSixth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                thirdLetter = null
                fourthLetter = null
                fifthLetter = null
                sixthLetter = null
            }
            1 -> {
                btnShwPassFirst.setBackgroundResource(R.drawable.show_upi_btn)
                btnShwPassSecond.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassThird.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassFourth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassFifth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassSixth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                secondLetter = null
                thirdLetter = null
                fourthLetter = null
                fifthLetter = null
                sixthLetter = null
            }
            0 -> {
                btnShwPassFirst.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassSecond.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassThird.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassFourth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassFifth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                btnShwPassSixth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
                firstLetter = null
                secondLetter = null
                thirdLetter = null
                fourthLetter = null
                fifthLetter = null
                sixthLetter = null
            }
            else -> {}
        }
        return
    }


    private fun submitMobilePayment() {
        hideKeyboard()
        paymentType = 0
        paymentThroughMobile()
    }

    private fun submitUpiPayment() {
        hideKeyboard()
        paymentType = 1
        paymentThroughUpi()
    }

    fun loadToMobile() {
        llmobile?.background = ContextCompat.getDrawable(this, R.drawable.bg_upi_select)
        llupi?.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
        llqr?.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
        llmobilemain?.setVisibility(View.VISIBLE)
        nestedScrollView?.setVisibility(View.GONE)
        nestedScrollView_scan?.setVisibility(View.GONE)
    }

    fun loadToUpi() {
        llmobile?.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
        llupi?.background = ContextCompat.getDrawable(this, R.drawable.bg_upi_select)
        llqr?.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
        llmobilemain!!.visibility = View.GONE
        nestedScrollView!!.setVisibility(View.VISIBLE)
        nestedScrollView_scan!!.setVisibility(View.GONE)
    }

    fun loadToScan() {
        llmobile?.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
        llupi?.background = ContextCompat.getDrawable(this, R.drawable.background_transparent)
        llqr?.background = ContextCompat.getDrawable(this, R.drawable.bg_upi_select)
        llmobilemain!!.visibility = View.GONE
        nestedScrollView!!.setVisibility(View.GONE)
        nestedScrollView_scan!!.setVisibility(View.VISIBLE)
//        val pn: String = cobrandPrepaidSdkkit.getUpiQRvalues(
//            com.creativethoughts.iscore.Upi_main.othersUPI + " Technologies&mc=5734&tr=UPITXN005&tn=UPI%20Transactions&am=&mam=1&cu=INR&refUrl = https://acepay.co.in/api/UPI-API/callback-url",
//            "pa"
//        )
//        txt_upi_scan!!.text = pn


        //  String pn=cobrandPrepaidSdkkit.getUpiQRvalues("upi://pay?pa=AcewareTechnologies@yesbank&pn=Ac e ware Technologies&mc=5734&tr=UPITXN005&tn=UPI%20Transactions&am=&mam=1&cu=INR&refUrl = https://acepay.co.in/api/UPI-API/callback-url","pn");
        //  String pn=cobrandPrepaidSdkkit.getUpiQRvalues("upi://pay?pa=AcewareTechnologies@yesbank&pn=Ac e ware Technologies&mc=5734&tr=UPITXN005&tn=UPI%20Transactions&am=&mam=1&cu=INR&refUrl = https://acepay.co.in/api/UPI-API/callback-url","pn");

        txt_upi_scan?.setText(othersUPI);
    }

    private fun bottomSheetSettings() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val lay: View = layoutInflater.inflate(R.layout.bottomsheet_upi, null)
        bottomSheetDialog.setContentView(lay)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        //   bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.window!!.findViewById<View>(R.id.rl_main)
            .setBackgroundResource(android.R.color.transparent)
        // bottomSheetDialog.setContentView(R.layout.bottomsheet_upi);
        val llSetting = lay.findViewById<LinearLayout>(R.id.llSetting)
        val llHistory = lay.findViewById<LinearLayout>(R.id.llHistory)
        llHistory.setOnClickListener {
            Toast.makeText(this, "This feature will unlock soon", Toast.LENGTH_SHORT)
                .show()
            //                Intent iH = new Intent(getApplicationContext(), Upi_History_Activity.class);
//                startActivity(iH);
//                bottomSheetDialog.dismiss();
        }
        llSetting.setOnClickListener {
//            val iS = Intent(applicationContext, Upi_Setting_Activity::class.java)
//            startActivity(iS)
//            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    fun requestContactPermission(type: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Read Contacts permission")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener {
                        requestPermissions(
                            arrayOf<String>(Manifest.permission.READ_CONTACTS),
                            PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this, arrayOf<String>(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
                getContacts(type)
            }
        } else {
            getContacts(type)
        }
    }

    private fun getContacts(type: Int) {
        val intent = Intent(
            Intent.ACTION_PICK,
            ContactsContract.Contacts.CONTENT_URI
        )
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        if (type == 0) {
            startActivityForResult(intent, PICK_CONTACT_0)
        } else if (type == 1) {
            startActivityForResult(intent, PICK_CONTACT_1)
        } else {
            startActivityForResult(intent, PICK_CONTACT_2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_CONTACT_0 && resultCode == RESULT_OK && applicationContext != null) {
            try {
                val uriContact = data!!.data!!
                val cursor = applicationContext.contentResolver.query(
                    uriContact, null, null, null, null
                )!!
                cursor.moveToFirst()
                @SuppressLint("Range") val tempContact =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                edtmobile?.setText(
                    extractPhoneNumber(
                        tempContact
                    )
                )
                closeCursor(cursor)
            } catch (e: Exception) {
                //  if(IScoreApplication.DEBUG)Log.e("contact ex", e.toString());
            }
        } else if (requestCode == PICK_CONTACT_1 && resultCode == RESULT_OK && applicationContext != null) {
            try {
                val uriContact = data!!.data!!
                val cursor = applicationContext.contentResolver.query(
                    uriContact, null, null, null, null
                )!!
                cursor.moveToFirst()
                @SuppressLint("Range") val tempContact =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                edtMobileUpi?.setText(
                    extractPhoneNumber(
                        tempContact
                    )
                )
                closeCursor(cursor)
            } catch (e: Exception) {
                //  if(IScoreApplication.DEBUG)Log.e("contact ex", e.toString());
            }
        } else if (requestCode == PICK_CONTACT_2 && resultCode == RESULT_OK && applicationContext != null) {
            try {
                val uriContact = data!!.data!!
                val cursor = applicationContext.contentResolver.query(
                    uriContact, null, null, null, null
                )!!
                cursor.moveToFirst()
                @SuppressLint("Range") val tempContact =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                edtMobileUpi_scan?.setText(
                    extractPhoneNumber(
                        tempContact
                    )
                )
                closeCursor(cursor)
            } catch (e: Exception) {
                //  if(IScoreApplication.DEBUG)Log.e("contact ex", e.toString());
            }
        }
    }

    private fun extractPhoneNumber(resultPhoneNumber: String): String? {
        var result: String
        try {
            result = resultPhoneNumber.replace("\\D+".toRegex(), "")
            if (result.length > 10) {
                result = result.substring(result.length - 10, result.length)
            }
        } catch (e: java.lang.Exception) {
            result = ""
        }
        return result
    }

    private fun closeCursor(cursor: Cursor) {
        try {
            cursor.close()
        } catch (e: java.lang.Exception) {
            // if(IScoreApplication.DEBUG) Log.e("Null pointer ex", e.toString());
        }
    }

    fun onClickClear() {
        edtamount?.setText("")
        edtmobile?.setText("")
        edtamount?.setError(null)
        edtmobile?.setError(null)
    }

    fun clear() {
        edtAmountUpi?.setText("")
        edtAddress?.setText("")
        edtMobileUpi?.setText("")
        edtUpi?.setText("")
        edtName?.setText("")
        edtEmail?.setText("")
        edtAmountUpi?.setError(null)
        edtUpi?.setError(null)
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }

    private fun paymentThroughMobile() {
        val UPIPinSETSP = getSharedPreferences(Config.SHARED_PREF347, 0)
        val UPIPinSET = UPIPinSETSP.getString("UPIPinSET", "true")
        if (!validateMobile()!!) {
            return
        } else if (!validateAmount()!!) {
            return
        } else {
            if (UPIPinSET == "false") {
                showBottomPin()
            } else {
                showPinLayout()
            }
        }
    }

    private fun paymentThroughUpi() {
        val UPIPinSETSP = getSharedPreferences(Config.SHARED_PREF77, 0)
        val UPIPinSET = UPIPinSETSP.getString("UPIPinSET", "false")
        if (!validateAmount2()!!) {
            return
        } else if (!validateUPI2()!!) {
            return
        } else {
            if (UPIPinSET == "false") {
                showBottomPin()
            } else {
                showPinLayout()
            }
        }
    }

    private fun validateMobile(): Boolean? {
        val mobileno = edtmobile?.getText().toString().trim { it <= ' ' }
        return if (mobileno.isEmpty()) {
            edtmobile?.setError("Field cannot be empty")
            false
        } else if (mobileno.length < 10 || mobileno.length > 11) {
            edtmobile?.setError("please enter valid mobile number")
            false
        } else {
            edtmobile?.setError(null)
            true
        }
    }

    private fun validateAmount2(): Boolean? {
        val amount = edtAmountUpi?.getText().toString()
            .trim { it <= ' ' }
        return if (amount.isEmpty()) {
            edtAmountUpi?.setError("Field cannot be empty")
            false
        } else {
            edtAmountUpi?.setError(null)
            true
        }
    }

    private fun validateUPI2(): Boolean? {
        val upiPattern = "[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}"
        val upi = edtUpi?.getText().toString().trim { it <= ' ' }
        return if (upi.isEmpty()) {
            edtUpi?.setError("Field cannot be empty")
            false
        } else if (!upi.matches(Regex(upiPattern))) {
            edtUpi?.setError("please enter valid upi")
            false
        } else {
            edtUpi?.setError(null)
            true
        }
    }

    private fun validateAmount(): Boolean? {
        edtamount?.setKeyListener(
            DigitsKeyListener.getInstance(
                true,
                true
            )
        )
        //  edtamount.addTextChangedListener(new DecimalFilter(edtamount, Upi_main.this));
        val amount =
            edtamount?.getText().toString().trim { it <= ' ' }
        return if (amount.isEmpty()) {
            edtamount?.setError("Field cannot be empty")
            false
        } else {
            edtamount?.setError(null)
            true
        }
    }

    private fun showBottomPin() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_upi_pin)
        val lin_no_pin = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin_no_pin)
        val lin_otp = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin_otp)
        val otp_view: OtpTextView? = bottomSheetDialog.findViewById<OtpTextView>(R.id.otp_view)
        val lin_pin = bottomSheetDialog.findViewById<LinearLayout>(R.id.lin_pin)
        val pinclick = bottomSheetDialog.findViewById<ImageView>(R.id.pinclick)
        val txtsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtsave)
        val txtotpsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtotpsave)
        val txtpin = bottomSheetDialog.findViewById<EditText>(R.id.txtpin)
        val txtpin2 = bottomSheetDialog.findViewById<EditText>(R.id.txtpin2)
        val pin = arrayOf("")
        pinclick!!.setOnClickListener {
            lin_no_pin!!.visibility = View.GONE
            lin_pin!!.visibility = View.VISIBLE
        }
        txtsave!!.setOnClickListener {
            val pass1 = txtpin!!.text.toString()
            val pass2 = txtpin2!!.text.toString()
            if (pass1.equals("", ignoreCase = true) || pass2.equals("", ignoreCase = true)) {
                Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
//                DialogUtil.showAlert(
//                    com.creativethoughts.iscore.Upi_main.context,
//                    "Please fill all fields"
//                )
            } else if (pass1.length < 6 || pass2.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Please check the length of pin",
                    Toast.LENGTH_SHORT
                ).show()
//                DialogUtil.showAlert(
//                    com.creativethoughts.iscore.Upi_main.context,
//                    "Please check the length of pin."
//                )
            } else if (pass1 != pass2) {
                Toast.makeText(applicationContext, "Please check provided pin.", Toast.LENGTH_SHORT)
                    .show()
//                DialogUtil.showAlert(
//                    com.creativethoughts.iscore.Upi_main.context,
//                    "Please check provided pin."
//                )
            } else {
                pin[0] = pass1
                //    verifyOTPNewPin(bottomSheetDialog, "123456", pin[0])
                bottomSheetDialog.setCancelable(false)
            }
        }
        bottomSheetDialog.show()
    }

    fun showPinLayout() {
        isPinVisible = true
        linmain?.setVisibility(View.GONE)
        linmain2?.setVisibility(View.GONE)
        linPin?.setVisibility(View.VISIBLE)
        btnShwPassFirst = findViewById<Button>(R.id.btn_pswrd_one)
        btnShwPassSecond = findViewById<Button>(R.id.btn_pswrd_two)
        btnShwPassThird = findViewById<Button>(R.id.btn_pswrd_three)
        btnShwPassFourth = findViewById<Button>(R.id.btn_pswrd_four)
        btnShwPassFifth = findViewById<Button>(R.id.btn_pswrd_five)
        btnShwPassSixth = findViewById<Button>(R.id.btn_pswrd_six)
        val btnKeyPadOne = findViewById<Button>(R.id.btn_keypad_one)
        val btnKeyPadTwo = findViewById<Button>(R.id.btn_keypad_two)
        val btnKeyPadThree = findViewById<Button>(R.id.btn_keypad_three)
        val btnKeyPadFour = findViewById<Button>(R.id.btn_keypad_four)
        val btnKeyPadFive = findViewById<Button>(R.id.btn_keypad_five)
        val btnKeyPadSix = findViewById<Button>(R.id.btn_keypad_six)
        val btnKeyPadSeven = findViewById<Button>(R.id.btn_keypad_seven)
        val btnKeyPadEight = findViewById<Button>(R.id.btn_keypad_eight)
        val btnKeyPadNine = findViewById<Button>(R.id.btn_keypad_nine)
        val btnKeyPadZero = findViewById<Button>(R.id.btn_keypad_zero)
        val btnKeyPadSuccess = findViewById<ImageView>(R.id.btn_keypad_success)
        val btnKeyPadBack = findViewById<ImageView>(R.id.btn_keypad_back)
        btnArray = arrayOf<Button>(
            btnKeyPadOne, btnKeyPadTwo, btnKeyPadThree,
            btnKeyPadFour, btnKeyPadFive, btnKeyPadSix,
            btnKeyPadSeven, btnKeyPadEight, btnKeyPadNine, btnKeyPadZero
        )
        btnOthersArray = arrayOf<ImageView>(btnKeyPadSuccess, btnKeyPadBack)
        counter = 0
        btnShwPassFirst.setBackgroundResource(R.drawable.dash_minus_substract_icon)
        btnShwPassSecond.setBackgroundResource(R.drawable.dash_minus_substract_icon)
        btnShwPassThird.setBackgroundResource(R.drawable.dash_minus_substract_icon)
        btnShwPassFourth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
        btnShwPassFifth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
        btnShwPassSixth.setBackgroundResource(R.drawable.dash_minus_substract_icon)
        firstLetter = null
        secondLetter = null
        thirdLetter = null
        fourthLetter = null
        fifthLetter = null
        sixthLetter = null
        for (btn in btnArray) {
            btn.setOnClickListener(this)
        }
        for (btn in btnOthersArray) {
            btn.setOnClickListener(this)
        }
        if (paymentType == 0) {
            upiType = 0
            var strMobileNumber = edtmobile?.getText().toString()
                .trim { it <= ' ' }
            var strAmount = edtamount?.getText().toString()
                .trim { it <= ' ' }
            txt_reciver?.setText(strMobileNumber)
            txt_sending?.setText("\u20b9" + strAmount)
            textupi?.setText("You are transferring money from your account to $strMobileNumber")
        } else if (paymentType == 1) {
            upiType = 1
            var strAmount = edtAmountUpi?.getText().toString()
                .trim { it <= ' ' }
            var strUpiId =
                edtUpi?.getText().toString().trim { it <= ' ' }
            txt_reciver?.setText(strUpiId)
            txt_sending?.setText("\u20b9" + strAmount)
            textupi?.setText("You are transferring money from your account to $strUpiId")
        } else {
            upiType = 2
            var strUpiId = txt_upi_scan!!.text.toString().trim { it <= ' ' }
            var strAmount = edtAmountUpi_scan?.getText().toString()
                .trim { it <= ' ' }
            txt_reciver?.setText(strUpiId)
            txt_sending?.setText("\u20b9" + strAmount)
            textupi?.setText("You are transferring money from your account to $strUpiId")
        }
    }

    override fun onBackPressed() {
        if (isPinVisible) {
            isPinVisible = false
            linmain!!.visibility = View.VISIBLE
            linmain2!!.visibility = View.VISIBLE
            linPin!!.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }


}