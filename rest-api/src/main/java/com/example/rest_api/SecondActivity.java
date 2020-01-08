package com.example.rest_api;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rest_api.model.Currancy;
import com.example.rest_api.model.Post;
import com.example.rest_api.model.RatesAll;
import com.example.rest_api.model.Table;
import com.example.rest_api.service.JsonPlaceHolderApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    Button back, google;
    TextView textViewResultNbp, text_view_result_nbp_right;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final DataBaseManager dataBaseManager = new DataBaseManager(this);


        textViewResultNbp = findViewById(R.id.text_view_result_nbp);
        text_view_result_nbp_right = findViewById(R.id.text_view_result_nbp_right);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nbp.pl/api/exchangerates/tables/A/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Table>> call = jsonPlaceHolderApi.getTable();

        call.enqueue(new Callback <List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>>  call, Response<List<Table>>  response) {
                if (!response.isSuccessful()) {
                    textViewResultNbp.setText("Code " + response.code());
                    text_view_result_nbp_right.setText("Code " + response.code());
                    return;
                }

                List<Table> tableList = response.body();

                List<RatesAll> ratesAllList = tableList.get(0).getRatesAlls();

                for (RatesAll ratesAll : ratesAllList) {
                    textViewResultNbp.append(ratesAll.getCode() + "\n");
                    text_view_result_nbp_right.append(ratesAll.getMid() + "\n");
                    //dataBaseManager.addCurrencyRate(ratesAll.getCode(), ratesAll.getMid());
                }

            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                textViewResultNbp.setText(t.getMessage());

            }
        });

//        ******** SINGLE CURRANCY  *********

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.nbp.pl/api/exchangerates/rates/a/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//        Call<Currancy> call = jsonPlaceHolderApi.getCurancy();
//
//        call.enqueue(new Callback<Currancy>() {
//            @Override
//            public void onResponse(Call<Currancy> call, Response<Currancy> response) {
//
//                if (!response.isSuccessful()) {
//                    textViewResultNbp.setText("Code: " + response.code());
//                    return;
//                }
//
//                Currancy currancies = response.body();
//                textViewResultNbp.append(currancies.getCode()+"  " + currancies.getRates().get(0).getMid());
//            }
//
//            @Override
//            public void onFailure(Call<Currancy> call, Throwable t) {
//                textViewResultNbp.setText(t.getMessage());
//            }
//        });

        back = findViewById(R.id.button2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO
//                toggle between activieties
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //refer to www

        google = findViewById(R.id.button4);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.pl"));
                startActivity(intent);
            }
        });
//
//        String mess = getIntent().getStringExtra("mess");
//        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
            }

            private void iterateTextView() {
                for (int i = 0; i < text_view_result_nbp_right.length(); i++) {
                     text_view_result_nbp_right.getText();

                }
            }

    @Override
    public void onClick(View v) {

    }
}