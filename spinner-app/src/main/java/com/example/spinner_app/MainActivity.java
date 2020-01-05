package com.example.spinner_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Spinner spinner1, toCurrancy;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        addListenerOnButton();
//        addListenerOnSpinnerItemSelection();

    }


//    public void addListenerOnSpinnerItemSelection() {
//        spinner1 = (Spinner) findViewById(R.id.spinner1);
//        spinner1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) new CustomOnItemSelectedListener());
//
//        toCurrancy = (Spinner) findViewById(R.id.toCurrancy);
//        toCurrancy.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) new CustomOnItemSelectedListener());
//
//    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        toCurrancy =(Spinner) findViewById(R.id.toCurrancy);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
                                "\n zmienna " + String.valueOf(toCurrancy.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}