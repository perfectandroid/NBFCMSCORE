package com.perfect.nbfcmscore.Model;

public class PieStatistics {

    private String Account;
    private String Balance;



    public PieStatistics(String Account, String Balance) {
        this.Account = Account;
        this.Balance = Balance;

    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account)
    {
        this.Account = Account;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String Balance) {
        this.Balance = Balance;
    }



 /*   @Override
    public String toString() {
        return Branch;
    }
*/





}
