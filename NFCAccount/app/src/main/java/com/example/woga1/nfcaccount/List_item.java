package com.example.woga1.nfcaccount;

/**
 * Created by Jangwon on 2017-09-01.
 */

public class List_item {

    private String seller ;
    private String money ;
    private String time ;

    public void setSeller(String sellerParam) {
        seller = sellerParam ;
    }
    public void setMoney(String moneyParam) {
        money = moneyParam ;
    }
    public void setTime(String timeParam) {
        time = timeParam ;
    }

    public String getSeller() {
        return this.seller ;
    }
    public String getMoney() {
        return this.money ;
    }

    public String getTime() {
        return this.time ;
    }


}
