package com.example.rest_api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Table {


    @SerializedName("table")
    private String tableNme;
    @SerializedName("rates")
    private List<RatesAll> ratesAlls;

    public Table getTableNme() {
        return Table.this;
    }

    public List<RatesAll> getRatesAlls() {
        return ratesAlls;
    }

    @NonNull
    @Override
    public String toString() {


        return (tableNme);
    }
}