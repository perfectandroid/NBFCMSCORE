package com.perfect.nbfcmscore.Model;

public class Beneflist {


    public String ID_Benefit;
    public String BenName;



        public Beneflist(String ID_Benefit, String BenName) {
            this.ID_Benefit = ID_Benefit;
            this.BenName = BenName;


        }

        public String getID_Benefit() {
            return ID_Benefit;
        }

        public void setID_Benefit(String ID_Benefit) {

            this.ID_Benefit = ID_Benefit;
        }

        public String getBenName() {
            return BenName;
        }

        public void setBenName(String BenName) {

            this.BenName = BenName;
        }


    @Override
    public String toString() {
        return BenName;
    }




}
