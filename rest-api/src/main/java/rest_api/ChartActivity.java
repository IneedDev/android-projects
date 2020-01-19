package rest_api;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.example.rest_api.R;
import rest_api.model.Rate;
import rest_api.model.Rates;
import rest_api.model.service.JsonPlaceHolderApi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartActivity extends AppCompatActivity implements View.OnClickListener{

    TextView ratesText, t_chart_min, t_chart_max, t_chart_value_max, t_chart_value_min;
    Spinner currency;
    Button submit_chart;
    String previous = "";

    private static List<String> rateList;

    public ChartActivity() {
    }

    public List<String> getRateList() {
        return rateList;
    }

    public void setRateList(List<String> rateList) {
        this.rateList = rateList;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        DataBaseManager dataBaseManager = new DataBaseManager(this);

        Intent intent = getIntent();

        ratesText = (TextView) findViewById(R.id.rates);

        submit_chart = findViewById(R.id.btn_chart_converter);
        submit_chart.setOnClickListener(this);

        currency =(Spinner) findViewById(R.id.spinner_chart);

        getValueFromPreviousIntent(intent);

        Cartesian line = AnyChart.line();
        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);

        List<String> rateList = getDataValuesFromDatabase(dataBaseManager);

        line.data(prepareDataValuesForChart(rateList));
        anyChartView.setChart(line);

        t_chart_min = findViewById(R.id.t_chart_min);
        t_chart_min.setText("Minimum");

        t_chart_max = findViewById(R.id.t_chart_max);
        t_chart_max.setText("Maximum");

        t_chart_value_max = findViewById(R.id.t_chart_value_max);
        //t_chart_value_max.setText(getMaxValue(rateList));

        t_chart_value_min = findViewById(R.id.t_chart_value_min);
       // t_chart_value_min.setText(getMinValue(rateList));

        ratesText.setText(convertCurrency(String.valueOf(intent.getStringExtra("fromCurrency")),Double.valueOf(intent.getStringExtra("amount")),
                String.valueOf(intent.getStringExtra("toCurrency"))));


        rateList.clear();
        dataBaseManager.truncateRatesSpecificTable("RATES_SPECIFIC");

    }

    private void clearIntent() {
        getIntent().removeExtra("fromCurrency");
        getIntent().removeExtra("toCurrency");
        getIntent().removeExtra("amount");
    }

    private String convertCurrency(String from, Double amount, String to) {
        final DataBaseManager dataBaseManager = new DataBaseManager(this);
        DecimalFormat df2 = new DecimalFormat("#.##");
        Double result;
        if (String.valueOf(from).equals("PLN")) {
            result = amount / Double.valueOf(dataBaseManager.getRateValue(to));
        } else if (String.valueOf(to).equals("PLN")) {
            result = amount * Double.valueOf(dataBaseManager.getRateValue(to));
        } else {
            result = (amount * Double.parseDouble(dataBaseManager.getRateValue(from))) / Double.parseDouble(dataBaseManager.getRateValue(to));
        }
        ;
        return String.valueOf(df2.format(result));
    }


    private void getValueFromPreviousIntent(Intent intent) {
        if (!intent.getStringExtra("cod").isEmpty()) {
            getResoinseToRateChart(intent.getStringExtra("cod").toLowerCase());
        } else {
            getResoinseToRateChart("usd");
        }
    }

    private String getMinValue(List<String> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Comparator.comparing(String::toString));
            String min = list.get(0);
            return min;

        }
        return null;
    }

    private String getMaxValue(List<String> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Comparator.comparing(String::toString));
            String max = list.get(list.size()-1);
            return max;

        }
        return null;
    }

    private List<String> getDataValuesFromDatabase(DataBaseManager dataBaseManager) {
        List<String> sqlList = new ArrayList<>();
        ChartActivity chartActivity = new ChartActivity();

        Cursor cursor = dataBaseManager.getRateValueForChart();
        while (cursor.moveToNext()) {
            sqlList.add(cursor.getString(0));
        }
        //ratesText.setText(currency.getSelectedItem().toString() + "   " + previous);
        chartActivity.setRateList(sqlList);
        return sqlList;
    }

    private List<DataEntry> prepareDataValuesForChart(List<String> sqlList) {
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < sqlList.size(); i++) {
            data.add(new ValueDataEntry(i, Double.valueOf(sqlList.get(i))));
        }
        return data;
    }


    public Rates getResoinseToRateChart(String cod) {
        final DataBaseManager dataBaseManager = new DataBaseManager(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nbp.pl/api/exchangerates/rates/a/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<Rates> call = jsonPlaceHolderApi.getRatesByCodAndDate(cod);

        call.enqueue(new Callback<Rates>() {
            @Override
            public void onResponse(Call <Rates> call, Response <Rates> response) {
                if (!response.isSuccessful()) {
                    //ratesText.setText("Code " + response.message());

                }
                dataBaseManager.truncateRatesSpecificTable("RATES_SPECIFIC");

                List<Rate> rates = response.body().getRates();
                for (Rate rate : rates) {
                    dataBaseManager.addCurrencyRateForChart(rate.getMid());
//                    ratesText.append(rate.getMid() + "\n");
                }
            }

            @Override
            public void onFailure(Call<Rates> call, Throwable t) {
                //ratesText.setText(t.getLocalizedMessage());

            }

        });
        return null;
        }

    @Override
    public void onClick(View v) {
        DataBaseManager dataBaseManager = new DataBaseManager(this);
        switch (v.getId()) {
            case R.id.btn_chart_converter:
                Intent intent = new Intent(ChartActivity.this, ConverterActivity.class);
                clearIntent();
                startActivity(intent);
                break;
        }

    }

}
