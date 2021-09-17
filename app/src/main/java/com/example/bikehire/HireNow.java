package com.example.bikehire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HireNow extends AppCompatActivity {
    RecyclerView recyclerView;
    private DatabaseReference reference;
    //myAdapter myadapter;
    List<Modelll> list;
    Adapter myViewHolder;


    //private ActivityMainBinding binding;

    private Button mpayment, btn_googlepay;
    private int PAYPAL_REQ_CODE = 12;
    private  static PayPalConfiguration paypalConfig =new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalClientIDConfigClass.PAYPAL_CLIENT_ID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hire_now);
        btn_googlepay = findViewById(R.id. googlepaybtn);

        reference = FirebaseDatabase.getInstance().getReference().child("stations");
        recyclerView = findViewById(R.id.stationslist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        mpayment = findViewById(R.id.paymentBtn);

        Intent intent =new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);


        mpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaypalPaymentMethod();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Modelll mystation = dataSnapshot.getValue(Modelll.class);
                    list.add(mystation);
                }
                myViewHolder=new Adapter(list);
                //myViewHolder.notifyDataSetChanged();
                recyclerView.setAdapter(myViewHolder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //recyclerView.setHasFixedSize(false);


    }




    private void PaypalPaymentMethod() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(1),
                "USD",
                "Test Payment", PayPalPayment.PAYMENT_INTENT_SALE
                );
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PAYPAL_REQ_CODE){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Payment Made Succesfully", Toast.LENGTH_LONG).show();

            }else{
                    Toast.makeText(this, "Payment Unsuccessful", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}