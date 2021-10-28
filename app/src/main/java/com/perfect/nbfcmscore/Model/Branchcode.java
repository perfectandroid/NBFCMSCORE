package com.perfect.nbfcmscore.Model;

public class Branchcode {


        private String Branch;
        private String id;



        public Branchcode(String Branch, String id) {
            this.Branch = Branch;
            this.id = id;

        }

        public String getBranch() {
            return Branch;
        }

        public void setBranch(String Branch) {
            this.Branch = Branch;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }



    @Override
    public String toString() {
        return Branch;
    }




}
