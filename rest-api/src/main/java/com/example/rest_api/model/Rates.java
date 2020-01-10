package com.example.rest_api.model;

import java.util.List;

public class Rates {


    private String table;
    private String currency;
    private String code;

    private List<Rate> rates;

    public String getTable() {
        return table;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCode() {
        return code;
    }

    public List<Rate> getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "table='" + table + '\'' +
                ", currency='" + currency + '\'' +
                ", code='" + code + '\'' +
                ", rates=" + rates +
                '}';
    }
}
