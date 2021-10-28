package com.perfect.nbfcmscore.Model;

public class Splitupdetail {


        private String Accountno;
        private String Fkaccount;
        private String submodule;


        public Splitupdetail(String Accountno, String Fkaccount,String submodule) {
            this.Accountno = Accountno;
            this.Fkaccount = Fkaccount;
            this.submodule = submodule;

        }

        public String getAccountno() {
            return Accountno;
        }

        public void setAccountno(String Accountno) {
            this.Accountno = Accountno;
        }

        public String getFkaccount() {
            return Fkaccount;
        }

        public void setFkaccount(String Fkaccount) {
            this.Fkaccount = Fkaccount;
        }

        public String getSubmodule() {
        return submodule;
    }

        public void setSubmodule(String submodule) {
        this.submodule = submodule;
    }

    @Override
    public String toString() {
        return Accountno;
    }




}
