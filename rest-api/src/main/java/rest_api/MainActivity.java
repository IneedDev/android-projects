package rest_api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rest_api.R;
import rest_api.model.Post;
import rest_api.model.service.JsonPlaceHolderApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textViewResult;
    Button btn_converter, btn_rates, second, btn_maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btn_converter = findViewById(R.id.btn_converter);
        btn_converter.setOnClickListener(this);

        btn_maps = findViewById(R.id.btn_maps);
        btn_maps .setOnClickListener(this);

        btn_rates = findViewById(R.id.btn_rates);
        btn_rates.setOnClickListener(this);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

//        threads issue

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
//                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.button1:
////                Intent intent = new Intent(MainActivity.this, RatesActivity.class);
//                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
//                final DataBaseManager dataBaseManager = new DataBaseManager(this);
//
//
//                startActivity(intent);
//
//
//                break;

            case R.id.btn_rates:
//                Intent intent = new Intent(MainActivity.this, RatesActivity.class);
                Intent intent3 = new Intent(MainActivity.this, RatesActivity.class);

                startActivity(intent3);


                break;
            case R.id.btn_converter:
                Intent intent2 = new Intent(MainActivity.this, ConverterActivity.class);

                startActivity(intent2);
                break;

            case R.id.btn_maps:
                Intent intent4= new Intent(MainActivity.this, MapsActivity.class);

                startActivity(intent4);
                break;
        }

    }
}
