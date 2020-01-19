package rest_api;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rest_api.R;

import org.json.JSONException;
import org.json.JSONObject;

import rest_api.model.RatesAll;
import rest_api.model.Table;
import rest_api.model.service.JsonPlaceHolderApi;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConverterActivity extends AppCompatActivity implements View.OnClickListener {

    String fromAmount, toAmount;
    Button submit, btn_check_chart;
    EditText from, to;
    TextView textHeader, result, t_chart_min, t_chart_max;
    Spinner fromCurrency, toCurrancy;
    private JSONObject json;

    public ConverterActivity() {
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter_activity);


        final DataBaseManager dataBaseManager = new DataBaseManager(this);

        btn_check_chart = findViewById(R.id.btn_check_chart);
        btn_check_chart.setOnClickListener(this);

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

                try {
                    ConverterActivity converterActivity = new ConverterActivity();
                    JSONObject json2 = new JSONObject(response.body().toString());
                    converterActivity.setJson(json2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<RatesAll> ratesAllList = tableList.get(0).getRatesAlls();
                for (RatesAll ratesAll : ratesAllList) {
                    dataBaseManager.addCurrencyRate(ratesAll.getCode(), ratesAll.getMid());
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
            case R.id.btn_check_chart:
                Intent intent = new Intent(ConverterActivity.this, ChartActivity.class);
                intent.putExtra("cod", String.valueOf(fromCurrency.getSelectedItem()));
                intent.putExtra("fromCurrency", String.valueOf(fromCurrency.getSelectedItem()));
                intent.putExtra("toCurrency", String.valueOf(toCurrancy.getSelectedItem()));
                intent.putExtra("amount", String.valueOf(from.getText().toString()));
                startActivity(intent);
                break;
        }

    }
}