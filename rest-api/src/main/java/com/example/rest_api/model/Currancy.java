package com.example.rest_api.model;


import java.util.List;

public class Currancy {

    private String code;
    private List<Rates> rates;

    public String getCode() {
        return code;
    }

    public List<Rates> getRates() {
        return rates;
    }
}
