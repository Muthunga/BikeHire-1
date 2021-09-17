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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private EditText mEmail, mPass, mfullnames, mAge;
    private TextView mTextView;
    private Button signUpBtn;

private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfullnames = findViewById(R.id.fullnames);
        mAge = findViewById(R.id.age);
        mEmail = findViewById(R.id.email_reg);
        mPass = findViewById(R.id.pass_reg);
        mTextView = findViewById(R.id.textview1);
        signUpBtn = findViewById(R.id.registration_btn);
        createUser();
        mAuth = FirebaseAuth.getInstance();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, SignIn.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }
    private void createUser(){
        String fullname =mfullnames.getText().toString();
        String age = mAge.getText().toString();
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();
        if(fullname.isEmpty()){
            mfullnames.setError("Fullname is required");
            mfullnames.requestFocus();
            return;
        }
        if(age.isEmpty()){
            mAge.setError("Age is required");
            mAge.requestFocus();
            return;
        }

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!pass.isEmpty()){
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Userinformation user =new Userinformation(fullname, age,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MainActivity.this,"Registered Successfully !!", Toast.LENGTH_SHORT).show();


                                    }

                                }
                            });


                        }       Toast.makeText(MainActivity.this,"Registered Successfully !!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this ,SignIn.class));
                                finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Registration Error !!", Toast.LENGTH_SHORT).show();
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