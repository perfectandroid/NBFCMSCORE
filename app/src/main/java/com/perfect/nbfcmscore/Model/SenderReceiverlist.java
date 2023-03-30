package com.perfect.nbfcmscore.Model;

public class SenderReceiverlist {


    public String userId;
    public String fkSenderId;
    public String senderName;
    public String senderMobile;
    public String receiverAccountno;
    public String mode;


        public SenderReceiverlist(String userId, String fkSenderId, String senderName, String senderMobile,
                                  String receiverAccountno,String mode) {
            this.userId = userId;
            this.fkSenderId = fkSenderId;
            this.senderName = senderName;
            this.senderMobile = senderMobile;
            this.receiverAccountno = receiverAccountno;
            this.mode = mode;

        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {

            this.userId = userId;
        }

        public String getFkSenderId() {
            return fkSenderId;
        }

        public void setFkSenderId(String fkSenderId) {

            this.fkSenderId = fkSenderId;
        }

        public String getSenderName() {
        return senderName;
    }

        public void setSenderName(String senderName) {

            this.senderName = senderName;
    }

       public String getSenderMobile() {
        return senderMobile;
    }

      public void setSenderMobile(String senderMobile) {

        this.senderMobile = senderMobile;
    }
    public String getReceiverAccountno()
    {
        return receiverAccountno;
    }

    public void setReceiverAccountno(String receiverAccountno) {

        this.receiverAccountno = receiverAccountno;
    }
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {

        this.mode = mode;
    }

    @Override
    public String toString() {
        return senderName+"("+senderMobile+")";
    }




}
