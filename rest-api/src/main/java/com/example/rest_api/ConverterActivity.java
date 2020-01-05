package com.example.rest_api;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rest_api.model.Rates;
import com.example.rest_api.model.RatesAll;
import com.example.rest_api.model.Table;
import com.example.rest_api.service.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConverterActivity extends AppCompatActivity implements View.OnClickListener {

    String fromAmount, toAmount;

    Button submit;
    EditText from, to;
    TextView textHeader;
    Spinner fromCurrency, toCurrancy;

    List<Table> tableList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter_activity);

        submit = findViewById(R.id.btnSubmit);
        submit.setOnClickListener(this);

        from = (EditText) findViewById(R.id.from_amount);
        to = (EditText) findViewById(R.id.to_amount);

        toCurrancy =(Spinner) findViewById(R.id.toCurrency);
        fromCurrency = (Spinner) findViewById(R.id.fromCurrency);

    }

    private List<Table> getRequestToRatesProvider() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nbp.pl/api/exchangerates/tables/A/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Table>> call = jsonPlaceHolderApi.getTable();

        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>>  call, Response<List<Table>> response) {
                if (!response.isSuccessful()) {
                    textHeader.setText("Code " + response.code());

                }
                List<Table> tableList = response.body();
                List<RatesAll> ratesAllList = tableList.get(0).getRatesAlls();
                for (RatesAll ratesAll : ratesAllList) {
                    textHeader.append(ratesAll.getCode() + "\n");

                }
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                textHeader.setText(t.getMessage());

            }
        });
        return null;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                fromAmount = from.getText().toString();
                toAmount = to.getText().toString();
                Toast.makeText(ConverterActivity.this, "Convert \nfrom: " + fromAmount +" "+String.valueOf(fromCurrency.getSelectedItem())+
                        "\nto " + toAmount +" "+String.valueOf(toCurrancy.getSelectedItem()) + "result " + convertCurrency(from.toString(), to.toString())  , Toast.LENGTH_LONG).show();
                break;
        }

    }

    private String convertCurrency(String from, String to) {

        List<RatesAll> ratesAllList3 = new ArrayList<>();


        int convertion;
        String tempRate="";
        int tempRateTo = 0;
//        100 USD - EUR

//          1 USD - 3,8 PL
        //  1 EUR - 4 PLN

        //  100 USD - 380 PLN

        //  380 PLN -  95 EUR



        for (RatesAll ratesAll : ratesAllList3) {
            if (ratesAll.getCurrancy().equals(to)) {
                tempRateTo = Integer.parseInt(ratesAll.getCurrancy());
            }
        }


        return tempRate;
    }
}