package com.perfect.nbfcmscore.Model;

public class SenderReceiver {
    public String StatusCode;
    public String Status;
    public String ID_Sender;
    public String ID_Receiver;
    public String otpRefNo;
    public String message;


    public SenderReceiver(String StatusCode, String Status, String ID_Sender, String ID_Receiver,
                              String otpRefNo,String message) {
        this.StatusCode = StatusCode;
        this.Status = Status;
        this.ID_Sender = ID_Sender;
        this.ID_Receiver = ID_Receiver;
        this.otpRefNo = otpRefNo;
        this.message = message;

    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {

        this.StatusCode = StatusCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {

        this.Status = Status;
    }

    public String getID_Sender() {
        return ID_Sender;
    }

    public void setID_Sender(String ID_Sender) {

        this.ID_Sender = ID_Sender;
    }

    public String getID_Receiver() {
        return ID_Receiver;
    }

    public void setID_Receiver(String ID_Receiver) {

        this.ID_Receiver = ID_Receiver;
    }
    public String getOtpRefNo()
    {
        return otpRefNo;
    }

    public void setOtpRefNo(String otpRefNo) {

        this.otpRefNo = otpRefNo;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

   /* @Override
    public String toString() {
        return senderName;
    }*/



}
