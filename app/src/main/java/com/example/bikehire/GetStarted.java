package com.example.bikehire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GetStarted extends AppCompatActivity {

    private Button getStarted;
    private Button report;
    private Button admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        getStarted = findViewById(R.id.getstartedbtn);
        report = findViewById(R.id.reportbtn);
        admin = findViewById(R.id.adminbtn);


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStarted.this, MapsActivity.class));
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStarted.this, choosegallery.class));
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStarted.this,crudpanel.class));
            }
        });

    }
}