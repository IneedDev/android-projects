package com.example.app_enum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the widget reference from XML layout
        mSpinner =(Spinner) findViewById(R.id.spnStates);

        // Bind Spinner to states enum
        mSpinner.setAdapter(new ArrayAdapter<States>(this,
                android.R.layout.simple_list_item_1, States.values()));
    }
}
