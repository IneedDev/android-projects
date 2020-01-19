package rest_api.example.android_basic_app_tutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_basic_app_tutorial.R;

public class MainActivity extends Activity implements View.OnClickListener {

    TextView textView;
    Button clickMebutton1, clickMebutton2, clickMebutton3, next, mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);
        textView.setText("Test from MainActivity class");

        clickMebutton1 = findViewById(R.id.button6);
        clickMebutton1.setOnClickListener(this);

        clickMebutton2 = findViewById(R.id.button7);
        clickMebutton2.setOnClickListener(this);


        clickMebutton3 = findViewById(R.id.button3);
        clickMebutton3.setOnClickListener(this);

        mess = findViewById(R.id.button10);
        mess.setOnClickListener(this);

        next = findViewById(R.id.button9);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO
//                toggle between activieties
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button6:
                textView.setText("Button text from button 6");
                break;
            case R.id.button7:
                textView.setText("Button text from button 7");
                break;
            case R.id.button3:
                Toast.makeText(this,"Toast popup from button", Toast.LENGTH_LONG).show();
                break;
            case R.id.button10:
                //toggle view + send message
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("mess", "Message from Main Activity");
                startActivity(intent);
                break;
        }
    }
}
