package com.example.arafat.map04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        String country = intent.getStringExtra("country");
        String dist = intent.getStringExtra("dist");

        textView.setText(String.format("%s %s", country, dist));

    }
}
