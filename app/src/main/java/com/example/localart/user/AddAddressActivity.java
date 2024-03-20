package com.example.localart.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.localart.R;
import com.example.localart.dataclass.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// AddAddressActivity.java
public class AddAddressActivity extends AppCompatActivity {

    private EditText etName, etPhone, etHouseNumber, etCity, etState, etPincode;
    private Button btnSubmit;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etHouseNumber = findViewById(R.id.etHouseNumber);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etPincode = findViewById(R.id.etPincode);

        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddressToFirebase();
                Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
                startActivity(intent);
            }

        });
    }

    private void addAddressToFirebase() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference addressesRef = mDatabase.child("addresses").child(userId);

        // Generate a unique ID for the new address
        String addressId = addressesRef.push().getKey();
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String houseNumber = etHouseNumber.getText().toString();
        String city = etCity.getText().toString();
        String state = etState.getText().toString();
        String pincode = etPincode.getText().toString();

        Address address = new Address(name, phone, houseNumber, city, state, pincode);

        // Store the address under the user's addresses node with the generated addressId
        addressesRef.child(addressId).setValue(address);
    }
}
