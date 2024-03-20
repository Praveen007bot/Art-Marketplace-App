package com.example.localart;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

import com.example.localart.seller.SellerLoginActivity;
import com.example.localart.user.LoginActivity;
import com.google.firebase.FirebaseApp;

public class    MainActivity extends AppCompatActivity {

    Button btn_toAdmin, btn_toCustomer;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Add this in your Application class or main activity's onCreate method
        FirebaseApp.initializeApp(this);




        btn_toAdmin = findViewById(R.id.btn_toAdmin);
        btn_toCustomer = findViewById(R.id.btn_toCustomer);

        btn_toAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iadmin = new Intent(MainActivity.this , SellerLoginActivity.class);
                startActivity(iadmin);
            }
        });

        btn_toCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent icustomer = new Intent(MainActivity.this , LoginActivity.class);
                startActivity(icustomer);
            }
        });



    }
}