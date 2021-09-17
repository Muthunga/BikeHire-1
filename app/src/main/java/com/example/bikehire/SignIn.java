package com.example.bikehire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignIn extends AppCompatActivity {
    private EditText mEmail, mPass;
    private TextView mTextView, forgotPassword;
    private Button signInBtn;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail = findViewById(R.id.email_reg);
        mPass = findViewById(R.id.pass_reg);
        signInBtn = findViewById(R.id.registration_btn);
        mTextView = findViewById(R.id.textview1);
        mAuth = FirebaseAuth.getInstance();
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,forgotpassword.class));
            }
        });





        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(SignIn.this,MainActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    } private void loginUser(){
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!pass.isEmpty()){
               mAuth.signInWithEmailAndPassword(email , pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       Toast.makeText(SignIn.this,"Login Successfull !!", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(SignIn.this, GetStarted.class));
                       finish();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                  Toast.makeText(SignIn.this,"Login Failed !!", Toast.LENGTH_SHORT).show();
                   }
               });


            }else {
                mPass.setError("Empty Fields Are Not Allowed");
            }
        }else if (email.isEmpty()){
            mEmail.setError("Empty Fields Are Not Allowed");
        }else{
            mEmail.setError("Please Enter Correct Email");
        }


    }
}