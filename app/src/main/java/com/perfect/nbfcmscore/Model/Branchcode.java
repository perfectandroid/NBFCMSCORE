package com.perfect.nbfcmscore.Model;

public class Branchcode {

    public String Branchname;
    public String Branchcode;

    public Branchcode(String Branchname, String Branchcode) {
        this.Branchname = Branchname;
        this.Branchcode = Branchcode;

    }
    public String getBranchname() {
        return Branchname;
    }

    public void setBranchname(String Branchname)
    {
        this.Branchname = Branchname;
    }

    public String getBranchcode() {
        return Branchcode;
    }

    public void setBranchcode(String Branchcode) {

        this.Branchcode = Branchcode;
    }

}
