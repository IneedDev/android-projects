package rest_api.example.android_basic_app_tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android_basic_app_tutorial.R;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    Button back, google;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


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

        String mess = getIntent().getStringExtra("mess");
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }
}
