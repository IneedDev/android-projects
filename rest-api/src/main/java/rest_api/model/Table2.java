package rest_api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Table2 {


    @SerializedName("table")
    private String tableNme;
    @SerializedName("rates")
    private Rate rates;


    public String getTableNme() {
        return tableNme;
    }

    public Rate getRatesAlls() {
        return rates;
    }

    @NonNull
    @Override
    public String toString() {


        return (tableNme);
    }

}
