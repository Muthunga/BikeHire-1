package com.example.bikehire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Payment extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private Button btn;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btn = findViewById(R.id.getLocation);
        tv = findViewById(R.id.tvgetLocation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager. PERMISSION_GRANTED){
                        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
                                    Double lat = location.getLatitude();
                                    Double longitude = location.getLongitude();



                                    Toast.makeText(Payment.this, "Success", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Payment.this ,HireNow.class));
                                    finish();
                                    tv.setText(lat+" , " +longitude); 

                                }
                            }
                        });
                        //get the location here
                    }
                }
            }
        });


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
    }
}