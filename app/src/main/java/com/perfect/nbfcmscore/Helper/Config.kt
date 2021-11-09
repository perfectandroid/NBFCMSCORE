package com.perfect.nbfcmscore.Helper

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.text.DecimalFormat
import javax.net.ssl.*

object Config {
    val CERT_NAME = "static-vm.pem"  //QA
  val BASE_URL = "https://202.164.150.65:14262/NbfcAndroidAPI/api/"  //DEVELOPMENT
    val IMAGE_URL = "https://202.164.150.65:14262/NbfcAndroidAPI/"  ///DEVELOPMENT*/

 /*  val BASE_URL="https://202.164.150.65:14262/NbfcAndroidAPIQA/api/"  //Qa new ip
    val IMAGE_URL="https://202.164.150.65:14262/NbfcAndroidAPIQA/"*/




    const val SHARED_PREF = "loginsession"
    const val SHARED_PREF1 = "FK_Customer"
    const val SHARED_PREF2 = "CusMobile"
    const val SHARED_PREF3 = "CustomerName"
    const val SHARED_PREF4 = "Address"
    const val SHARED_PREF5 = "Email"
    const val SHARED_PREF6 = "Gender"
    const val SHARED_PREF7 = "DateOfBirth"
    const val SHARED_PREF8 = "Token"
    const val SHARED_PREF9 = "ID_Languages"
    const val SHARED_PREF10 = "AppStoreLink"
    const val SHARED_PREF11= "PlayStoreLink"
    const val SHARED_PREF12 = "ProductName"
    const val SHARED_PREF13 = "CompanyLogoImageCode"
    const val SHARED_PREF14 = "AppIconImageCode"
    const val SHARED_PREF15 = "ResellerName"
    const val SHARED_PREF16 = "FK_Account"
    const val SHARED_PREF17 = "SubModule"
    const val SHARED_PREF18 = "Status"
    const val SHARED_PREF19 = "CustomerNumber"
    const val SHARED_PREF20 = "CustomerNumber"

    const val SHARED_PREF21 = "updateDays"
    const val SHARED_PREF22 = "updateHour"
    const val SHARED_PREF23 = "updateMinute"
    const val SHARED_PREF24 = "DefaultAccount"
    const val SHARED_PREF25 = "DefaultFK_Account"
    const val SHARED_PREF26 = "DefaultSubModule"
    const val SHARED_PREF27 = "DefaultBalance"
    const val SHARED_PREF28 = "DefaultBranchName"

    const val SHARED_PREF29 = "LastLoginTime"
    const val SHARED_PREF30 = "ContactNumber"
    const val SHARED_PREF31 = "ContactEmail"
    const val SHARED_PREF32 = "ContactAddress"
    const val SHARED_PREF33 = "IsNBFC"

    const val SHARED_PREF34 = "welcome"
    const val SHARED_PREF35 = "fasterwaytohelpyou"
    const val SHARED_PREF36 = "sigin"
    const val SHARED_PREF37 = "registernow"
    const val SHARED_PREF38 = "SelectLanguage"
    const val SHARED_PREF39 = "Skip"
    const val SHARED_PREF40 = "Let'sgetstarted"
    const val SHARED_PREF41 = "pleaseenteryourpersonalinformation"
    const val SHARED_PREF42 = "entermobilenumber"
    const val SHARED_PREF43 = "enter last4digitofa/cno"
    const val SHARED_PREF44 = "continue"
    const val SHARED_PREF45 = "loginwithmobilenumber"
    const val SHARED_PREF46 = "enteryourmobilenumberwewillsentyouOTPtoverify"
    const val SHARED_PREF47 = "user login verified"
    const val SHARED_PREF48 = "Otpverification"
    const val SHARED_PREF49 =  "please enter validation code senttoyourregisteredmobilenumber"
    const val SHARED_PREF50 = "Myaccounts"
    const val SHARED_PREF51 = "passbook"
    const val SHARED_PREF52 = "quickbalance"
    const val SHARED_PREF53 = "duereminder"
    const val SHARED_PREF54 = "aboutus"
    const val SHARED_PREF55 = "contactus"
    const val SHARED_PREF56 = "feedback"
    const val SHARED_PREF57 = "privacypolicy"
    const val SHARED_PREF58 = "termsandconditions"
    const val SHARED_PREF59 = "statement"
    const val SHARED_PREF60 = "settings"
    const val SHARED_PREF61 = "logout"



    fun getHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, session -> true }
    }

    fun getWrappedTrustManagers(trustManagers: Array<TrustManager>): Array<TrustManager> {
        val originalTrustManager = trustManagers[0] as X509TrustManager
        return arrayOf(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return originalTrustManager.acceptedIssuers
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>?, authType: String) {
                try {
                    if (certs != null && certs.size > 0) {
                        certs[0].checkValidity()
                    } else {
                        originalTrustManager.checkClientTrusted(certs, authType)
                    }
                } catch (e: CertificateException) {
                    Log.w("checkClientTrusted", e.toString())
                }

            }

            override fun checkServerTrusted(certs: Array<X509Certificate>?, authType: String) {
                try {
                    if (certs != null && certs.size > 0) {
                        certs[0].checkValidity()
                    } else {
                        originalTrustManager.checkServerTrusted(certs, authType)
                    }
                } catch (e: CertificateException) {
                    Log.w("checkServerTrusted", e.toString())
                }

            }
        })
    }

    @Throws(
        CertificateException::class,
        KeyStoreException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        KeyManagementException::class
    )
    fun getSSLSocketFactory(context: Context): SSLSocketFactory {
        val cf = CertificateFactory.getInstance("X.509")
        //  InputStream caInput = getResources().openRawResource(Common.getCertificateAssetName());
        // File path: app\src\main\res\raw\your_cert.cer
        val caInput = context!!.assets.open(CERT_NAME)
        val ca = cf.generateCertificate(caInput)
        caInput.close()
        val keyStore = KeyStore.getInstance("BKS")
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)
        val wrappedTrustManagers = getWrappedTrustManagers(tmf.trustManagers)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, wrappedTrustManagers, null)
        return sslContext.socketFactory
    }


    object Utils {
        fun hideSoftKeyBoard(context: Context, view: View) {
            try {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                // TODO: handle exception
                e.printStackTrace()
            }
        }
    }

    fun getDecimelFormateForEditText(amount: Double): String? {
        val fmt = DecimalFormat("#,##,##,##,###")
        var amt = fmt.format(amount)
        if (amt.substring(0, 1) == ".") {
            amt = "0$amt"
        }
        return amt
    }

    fun getDecimelFormate(amount: Double): String? {
        val fmt = DecimalFormat("#,##,##,##,###.00")
        var amt = fmt.format(amount)
        if (amt.substring(0, 1) == ".") {
            amt = "0$amt"
        }
        return amt
    }
}
