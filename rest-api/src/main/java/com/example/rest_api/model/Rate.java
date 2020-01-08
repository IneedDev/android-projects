package com.example.rest_api.model;

import com.google.gson.annotations.SerializedName;

public class Rate {

    private String effectiveDate;
    @SerializedName("mid")
    private String midPrice;

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getMidPrice() {
        return midPrice;
    }

    public void setMidPrice(String midPrice) {
        this.midPrice = midPrice;
    }


    @Override
    public String toString() {
        return "Rate{" +
                "effectiveDate='" + effectiveDate + '\'' +
                ", midPrice='" + midPrice + '\'' +
                '}';
    }
}
