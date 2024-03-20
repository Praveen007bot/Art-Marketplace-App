package com.example.localart.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localart.HelperClass;
import com.example.localart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText et_SignupName, et_SignupEmail, et_SignupUserName, et_SignupPassword;
    TextView loginRedirectText;
    Button btn_Signup;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_SignupName = findViewById(R.id.et_SignupName);
        et_SignupEmail = findViewById(R.id.et_SignupEmail);
        et_SignupUserName = findViewById(R.id.et_SignupUserName);
        et_SignupPassword = findViewById(R.id.et_SignupPassword);
        btn_Signup = findViewById(R.id.btn_Signup);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        auth = FirebaseAuth.getInstance();

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the email and password from the EditText fields
                String email = et_SignupEmail.getText().toString().trim();
                String password = et_SignupPassword.getText().toString().trim();

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // User creation success
                                FirebaseUser user = auth.getCurrentUser();
                                String name = et_SignupName.getText().toString();

                                String username = et_SignupUserName.getText().toString();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();
                                user.updateProfile(profileUpdates);

                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("users");





                                HelperClass helperClass = new HelperClass(name, email, username, password);
                                reference.child(username).setValue(helperClass);
                                // Handle additional actions or navigate to the next screen
                                Toast.makeText(SignupActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // If user creation fails, log the error message
                                Exception exception = task.getException();
                                Toast.makeText(SignupActivity.this, "User creation failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
