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
    val CERT_NAME = "staticvm.pem"  //QA
 // val BASE_URL = "https://202.164.150.65:14262/NbfcAndroidAPI/api/"  //DEVELOPMENT
  //  val IMAGE_URL = "https://202.164.150.65:14262/NbfcAndroidAPI/"  ///DEVELOPMENT

  // val BASE_URL="https://202.164.150.65:14262/NbfcAndroidAPIQA/api/"  //Qa new ip
   // val IMAGE_URL="https://202.164.150.65:14262/NbfcAndroidAPIQA/"


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
    const val SHARED_PREF24 = "DefaultAccount1"
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
    const val SHARED_PREF62 = "NotificationandMessages"
    const val SHARED_PREF63 = "OwnBank"
    const val SHARED_PREF64 = "OtherBank"
    const val SHARED_PREF65 = "QuickPay"
    const val SHARED_PREF66 = "PrepaidMobile"
    const val SHARED_PREF67 = "PostpaidMobile"
    const val SHARED_PREF68 = "Landline"
    const val SHARED_PREF69 = "DTH"
    const val SHARED_PREF70 = "DataCard"
    const val SHARED_PREF71 = "KSEB"
    const val SHARED_PREF72 = "History"
    const val SHARED_PREF73 = "Dashboard"
    const val SHARED_PREF74 = "VirtualCard"
    const val SHARED_PREF75 = "BranchDetails"
    const val SHARED_PREF76 = "LoanApplication"
    const val SHARED_PREF77 = "LoanStatus"
    const val SHARED_PREF78 = "ProductDetails"
    const val SHARED_PREF79 = "EMICalculator"
    const val SHARED_PREF80 = "DepositCalculator"
    const val SHARED_PREF81 = "GoldLoanEligibileCalculator"
    const val SHARED_PREF82 = "Enquires"
    const val SHARED_PREF83 = "HolidayList"
    const val SHARED_PREF84 = "ExecutiveCallBack"
    const val SHARED_PREF85 = "DEPOSIT"
    const val SHARED_PREF86 = "LOAN"
    const val SHARED_PREF87 = "Active"
    const val SHARED_PREF88 = "Deposit"
    const val SHARED_PREF89 = "Loan"
    const val SHARED_PREF90 = "OWNACCOUNT"
    const val SHARED_PREF91 = "OTHERACCOUNT"
    const val SHARED_PREF92 = "SelectYourAccount"
    const val SHARED_PREF93 = "PayingFrom"
    const val SHARED_PREF94 = "PayingTo"
    const val SHARED_PREF95 = "AmountPayable"
    const val SHARED_PREF96 = "Remark"
    const val SHARED_PREF97 = "PAY"
    const val SHARED_PREF98 = "ReceiverAccountType"
    const val SHARED_PREF99 = "ConfirmAccountNo"
    const val SHARED_PREF100 = "Scan"
    const val SHARED_PREF101 = "SelectYourAccount"
    const val SHARED_PREF102 = "RechargeHistory"
    const val SHARED_PREF103 = "FRONTVIEW"
    const val SHARED_PREF104 = "BACKVIEW"
    const val SHARED_PREF105 = "PurposeofVirtualCard"
    const val SHARED_PREF106 = "quit"
    const val SHARED_PREF107 = "AccountNo"
    const val SHARED_PREF108 = "EnterDistrict"
    const val SHARED_PREF109 = "BankDetails"
    const val SHARED_PREF110 = "MobileNumber"
    const val SHARED_PREF111 = "Operator"
    const val SHARED_PREF112 = "Circle"
    const val SHARED_PREF113 = "Amount"
    const val SHARED_PREF114 = "RECHARGE"
    const val SHARED_PREF115 = "SelectOperator"
    const val SHARED_PREF116 = "SubscriberID"
    const val SHARED_PREF117 = "Account"
    const val SHARED_PREF118="ViewAllAccounts"
    const val SHARED_PREF119= "AvailableBalance"
    const val SHARED_PREF120= "LastLogin"
    const val SHARED_PREF121= "AccountDetails"
    const val SHARED_PREF122= "FundTransfer"
    const val SHARED_PREF123= "RechargeBills"
    const val SHARED_PREF124= "ReportsOtherServices"
    const val SHARED_PREF125= "ToolsSettings"

    const val SHARED_PREF126= "Selectaperiodofyourchoice"
    const val SHARED_PREF127= "OR"
    const val SHARED_PREF128= "Selectacustomdateofyourchoice."
    const val SHARED_PREF129= "View"
    const val SHARED_PREF130= "Download"
    const val SHARED_PREF131= "LastMonth"
    const val SHARED_PREF132= "Last3Months"
    const val SHARED_PREF133= "Last6Months"
    const val SHARED_PREF134= "Last1Year"

    const val SHARED_PREF135= "SelectAccount"
    const val SHARED_PREF136= "SelectSender"
    const val SHARED_PREF137= "SelectReceiver"
    const val SHARED_PREF138= "AddNewSender"
    const val SHARED_PREF139= "AddNewReceiver"
    const val SHARED_PREF140= "MPIN"
    const val SHARED_PREF141= "ForgotMPIN"
    const val SHARED_PREF142= "MAKEPAYMENT"
    const val SHARED_PREF143= "FirstName"
    const val SHARED_PREF144= "LastName"
    const val SHARED_PREF145= "DOB"
    const val SHARED_PREF146= "REGISTER"
    const val SHARED_PREF147= "SenderName"
    const val SHARED_PREF148= "ReceiverName"
    const val SHARED_PREF149= "ConfirmAccountNumber"
    const val SHARED_PREF150= "IFSCCode"

    const val SHARED_PREF151= "IMPS"
    const val SHARED_PREF152= "NEFT"
    const val SHARED_PREF153= "RTGS"
    const val SHARED_PREF154= "FUNDTRANSFERSTATUS"

    const val SHARED_PREF155= "StatementAccountNumber"
    const val SHARED_PREF156= "StatementSubModule"

    const val SHARED_PREF157= "BeneficiaryList"
    const val SHARED_PREF158= "AccountNumber"
    const val SHARED_PREF159= "BeneficiaryName"
    const val SHARED_PREF160= "BeneficiaryCNo"
    const val SHARED_PREF161= "ConfirmBeneficiaryACNo"
    const val SHARED_PREF162= "SaveBeneficiaryForFuture"
    const val SHARED_PREF163= "baseurl"
    const val SHARED_PREF164= "sslcertificate"
    const val SHARED_PREF165= "ImageURL"

    const val SHARED_PREF166= "please enter validation code senttoyourregisteredmobilenumber"
    const val SHARED_PREF167= "Language"

    const val SHARED_PREF168= "MPINVerification"
    const val SHARED_PREF169= "PleaseunlockwithyourMPIN"
    const val SHARED_PREF170= "ChangeMPIN"

    const val SHARED_PREF171= "Name"
    const val SHARED_PREF172= "Mobile"
    const val SHARED_PREF173= "Date"
    const val SHARED_PREF174= "Time"

    const val SHARED_PREF175= "Reportanerror"
    const val SHARED_PREF176= "Giveasuggestion"
    const val SHARED_PREF177= "Anythingelse"

    const val SHARED_PREF178= "TypeofDeposit"
    const val SHARED_PREF179= "Beneficiary"
    const val SHARED_PREF180= "Tenure"
    const val SHARED_PREF181= "PleaseEnterMonth"
    const val SHARED_PREF182= "PleaseEnterDay"

    const val SHARED_PREF183= "Assets"
    const val SHARED_PREF184= "Liability"
    const val SHARED_PREF185= "PaymentReceipt"




    const val SHARED_PREF189= "RESET"
    const val SHARED_PREF190= "CALCULATE"

    const val SHARED_PREF191= "PRINCIPALAMOUNT"
    const val SHARED_PREF192= "INTERESTRATE"
    const val SHARED_PREF193= "MONTH"
    const val SHARED_PREF194= "Selectemitype"
    const val SHARED_PREF195= "ProductListDetails"

    const val SHARED_PREF196="LoanApplicationStatus"
    const val SHARED_PREF197="SelectLoantype"
    const val SHARED_PREF198="Selectloanpurpose"

    const val SHARED_PREF199="Closed"

    const val SHARED_PREF200="FromDate"
    const val SHARED_PREF201="EndDate"

    const val SHARED_PREF202="DueDatesCalender"
    const val SHARED_PREF203="Duedatelistforupcomingtwoweeks"

    const val SHARED_PREF204="Duedate"
    const val SHARED_PREF205="DoYouWanttoDeleteThisAccountAndRegisterWithAnotherAccount?"
    const val SHARED_PREF206="Yes"
    const val SHARED_PREF207="No"
    const val SHARED_PREF208="DoYouWantToQuit?"

    const val SHARED_PREF209="ListasonDate"
    const val SHARED_PREF210="Balance"

    const val SHARED_PREF211="MINISTATEMENT"
    const val SHARED_PREF212="ACCOUNTSTATEMENT"
    const val SHARED_PREF213="MOREOPTIONS"

    const val SHARED_PREF214="AccountSummary"
    const val SHARED_PREF215="StandingInstruction"
    const val SHARED_PREF216="Notice"


    const val SHARED_PREF217="ListingDataforpast"
    const val SHARED_PREF218="days"
    const val SHARED_PREF219="youcanchangeitfromsettings"
    const val SHARED_PREF220="Clear"

    const val SHARED_PREF221="LoanPeriod"
    const val SHARED_PREF222="Weight"
    const val SHARED_PREF223="EnterAmount"
    const val SHARED_PREF224="SelectBranch"
    const val SHARED_PREF225="Pleaseselectpayingtoaccountnumber"
    const val SHARED_PREF226="PayableAmount"
    const val SHARED_PREF227="Atleast3digitsarerequired"
    const val SHARED_PREF228="Atleast6digitsarerequired"
    const val SHARED_PREF229="PleaseEnterBeneficiaryName"
    const val SHARED_PREF230="PleaseEnterValidBeneficiaryAccountNumber"

    const val SHARED_PREF231="PleaseentervalidConfirmBeneficiaryaccountnumber"
    const val SHARED_PREF232="SelectavalidAccountNumber"
    const val SHARED_PREF233="PleaseEnterYourFeedback"
    const val SHARED_PREF234="Giveusacall"
    const val SHARED_PREF235="Sendusamessage"
    const val SHARED_PREF236="Visitourlocation"
    const val SHARED_PREF237="Aboutustext"
    const val SHARED_PREF238="Privacypolicytext"
    const val SHARED_PREF239="TermsandConditionstext"
    const val SHARED_PREF240="Apply"

    const val SHARED_PREF241="AccountType"
    const val SHARED_PREF242="UnclearAmount"

    const val SHARED_PREF243="Pleaseselectpayingtoaccountnumber"
    const val SHARED_PREF244="TransactionUpdate(Days)"
    const val SHARED_PREF245="UpdateInterval"
    const val SHARED_PREF246="DefaultAccount"
    const val SHARED_PREF247="Giveusacall"
    const val SHARED_PREF248="Sendusamessage"
    const val SHARED_PREF249="Visitourlocation"
    const val SHARED_PREF250="Submit"

    const val SHARED_PREF251="OwnAccountFundTransfer"
    const val SHARED_PREF252="OtherAccountFundTransfer"

    const val SHARED_PREF253="Transfer upto"
    const val SHARED_PREF254="Instantly"
    const val SHARED_PREF255="Weight"
    const val SHARED_PREF256="EMITYPE"
    const val SHARED_PREF257="Enter Weight"
    const val SHARED_PREF258="PleaseEnterWeight"
    const val SHARED_PREF259="Pleaseenteramount"
    const val SHARED_PREF260="EnterPrincipalAmount"
    const val SHARED_PREF261="EnterMonth"
    const val SHARED_PREF262="EnterInterestRate"

    const val SHARED_PREF263="LoanPurpose"
    const val SHARED_PREF264="LoanType"
    const val SHARED_PREF265="ApplicationDate"
    const val SHARED_PREF266="ApplicationAmount"
    const val SHARED_PREF267="ApplicationNumber"

    const val SHARED_PREF268="Customer Name"
    const val SHARED_PREF269="Customer Id"
    const val SHARED_PREF270="Electronicuseonly"

    const val SHARED_PREF271="Streamlinetransactions"
    const val SHARED_PREF272="Enableasinglepointofcontactforcreditanddebit"
    const val SHARED_PREF273="Strengthenyourloanportfolio"
    const val SHARED_PREF274="Eliminatethelongqueues"

    const val SHARED_PREF275="PleaseEnterValidIFSC"
    const val SHARED_PREF276="BeneficiaryAccountNumberdidntmatch"

    const val SHARED_PREF277="ConsumerName"
    const val SHARED_PREF278="ConsumerNumber"
    const val SHARED_PREF279="SectionName"
    const val SHARED_PREF280="BillNumber"

    const val SHARED_PREF281="PleaseEnterMobileNumber"
    const val SHARED_PREF282="PleaseEnterSectionName"
    const val SHARED_PREF283="PleaseEnterBillnumber"
    const val SHARED_PREF284="PleaseSelectAccount"
    const val SHARED_PREF285="PleaseEnterConsumerName"
    const val SHARED_PREF286="PleaseEnterConsumerNumber"
    const val SHARED_PREF287="PleaseEnterValidMobileNumber"

    const val SHARED_PREF288="Selection"
    const val SHARED_PREF289="Section"

    const val SHARED_PREF290="NotTransferable"
    const val SHARED_PREF291="PleaseKeepYourCardConfidential"
    const val SHARED_PREF292="Phone"

    const val SHARED_PREF293="LicenceImps"
    const val SHARED_PREF294="LicenceKseb"
    const val SHARED_PREF295="LicenceNeft"
    const val SHARED_PREF296="LicenceQuickPay"
    const val SHARED_PREF297="LicenceRecharge"
    const val SHARED_PREF298="LicenceRtgs"

    const val SHARED_PREF299="Address1"
    const val SHARED_PREF300="Email1"

    const val SHARED_PREF301="PleaseSelectOperator"
    const val SHARED_PREF302="PleaseSelectCircle"
    const val SHARED_PREF303="PleaseEnterFirstName"
    const val SHARED_PREF304="PleaseEnterLastName"

    const val SHARED_PREF305="LICENSEDTO"
    const val SHARED_PREF306="TECHNOLOGYPARTNER"
    const val SHARED_PREF307 ="TermsandConditionstext"

    const val SHARED_PREF308 ="Gender1"
    const val SHARED_PREF309 ="DateofBirth1"
    const val SHARED_PREF310 ="CustomerNumber1"

    const val SHARED_PREF322 ="PleaseEnterAmountbetween1and25000.0."





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

        val sslcertificateSP = context.applicationContext.getSharedPreferences(Config.SHARED_PREF164, 0)
        val sslcertificate = sslcertificateSP.getString("sslcertificate", null)

        val cf = CertificateFactory.getInstance("X.509")
        //  InputStream caInput = getResources().openRawResource(Common.getCertificateAssetName());
        // File path: app\src\main\res\raw\your_cert.cer
        val caInput = context!!.assets.open(sslcertificate!!)
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

    fun hideSoftKeyBoards(context: Context) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = View(context)
            imm?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }
    }


}
