package com.perfect.nbfcmscore.Model;

public class Splitupdetail {


        private String Accountno;
        private String Fkaccount;
        private String submodule;
        private String branchname;


        public Splitupdetail(String Accountno, String Fkaccount,String submodule,String branchname) {
            this.Accountno = Accountno;
            this.Fkaccount = Fkaccount;
            this.submodule = submodule;
            this.branchname = branchname;

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

       public String getBranchname() {
        return branchname;
    }

      public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    @Override
    public String toString() {
        return Accountno;
    }




}
