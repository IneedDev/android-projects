package com.example.rest_api;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rest_api.model.RatesAll;
import com.example.rest_api.model.Table;
import com.example.rest_api.service.JsonPlaceHolderApi;

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
    TextView textHeader, result;
    Spinner fromCurrency, toCurrancy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter_activity);


        final DataBaseManager dataBaseManager = new DataBaseManager(this);


        submit = findViewById(R.id.btnSubmit);
        submit.setOnClickListener(this);

        from = (EditText) findViewById(R.id.from_amount);

        toCurrancy =(Spinner) findViewById(R.id.toCurrency);
        fromCurrency = (Spinner) findViewById(R.id.fromCurrency);

        result = (TextView) findViewById(R.id.result);

        getRequestToRatesProvider();

    }

    private List<Table> getRequestToRatesProvider() {
        final DataBaseManager dataBaseManager = new DataBaseManager(this);

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
                    dataBaseManager.addCurrencyRate(ratesAll.getCode(),ratesAll.getMid());
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
                Toast.makeText(ConverterActivity.this, "Convert \nfrom: " + fromAmount +" "+String.valueOf(fromCurrency.getSelectedItem())+
                        "\nto " + toAmount +" "+String.valueOf(toCurrancy.getSelectedItem()) + "  result " +  convertCurrency(String.valueOf(fromCurrency.getSelectedItem()),Double.valueOf(fromAmount), String.valueOf(toCurrancy.getSelectedItem())), Toast.LENGTH_LONG).show();
                result.setText(convertCurrency(String.valueOf(fromCurrency.getSelectedItem()),Double.valueOf(fromAmount), String.valueOf(toCurrancy.getSelectedItem())));
                break;
        }

    }

    private String convertCurrency(String from, Double amount, String to) {
        final DataBaseManager dataBaseManager = new DataBaseManager(this);
        return String.valueOf(from).equals("PLN") ? String.valueOf(amount / Double.valueOf(dataBaseManager.getRateValue(to))) : String.valueOf((amount * Double.parseDouble(dataBaseManager.getRateValue(from))) / Double.parseDouble(dataBaseManager.getRateValue(to)));
    }


}