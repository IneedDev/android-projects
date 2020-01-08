package com.example.rest_api;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.example.rest_api.model.Rate;
import com.example.rest_api.model.RatesAll;
import com.example.rest_api.model.Table;
import com.example.rest_api.model.Table2;
import com.example.rest_api.service.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner urrencyChart;
    TextView ratesText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);

        final DataBaseManager dataBaseManager = new DataBaseManager(this);


        ratesText = (TextView) findViewById(R.id.rates);

        Pie pie = AnyChart.pie();

        Cartesian line = AnyChart.line();

        List<DataEntry> data = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            data.add(new ValueDataEntry(i, i++));
        }

//        pie.data(data);
        line.data(data);

//        anyChartView.setChart(pie);
        anyChartView.setChart(line);

        getResoinseToRateChart();



        //dataBaseManager.addCurrencyRateForChart();


    }

    public List<Rate> getResoinseToRateChart() {
        final DataBaseManager dataBaseManager = new DataBaseManager(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nbp.pl/api/exchangerates/rates/a/gbp/2012-01-01/2012-01-31/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<Rate> call = jsonPlaceHolderApi.getTable2();
        call.enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(Call <Rate>  call, Response <Rate> response) {
                if (!response.isSuccessful()) {
                    ratesText.setText("Code " + response.code());

                }
                ratesText.setText(response.body().toString());

//                for (Rate rate : rates) {
//                    dataBaseManager.addCurrencyRateForChart(rate.getMidPrice());
//                    ratesText.setText(rate.getMidPrice());
//                }
            }

            @Override
            public void onFailure(Call<Rate> call, Throwable t) {
                ratesText.setText(t.getMessage());

            }

        });
        return null;
        }

    @Override
    public void onClick(View v) {

    }
}
