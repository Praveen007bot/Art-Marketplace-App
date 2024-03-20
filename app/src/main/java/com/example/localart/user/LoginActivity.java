package com.example.localart.user;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity {

    EditText et_loginEmail, et_loginPassword;
    Button btn_login;
    TextView signupRedirectText;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.localart.R.layout.activity_login);

        et_loginEmail = findViewById(com.example.localart.R.id.et_loginEmail);
        et_loginPassword = findViewById(com.example.localart.R.id.et_loginPassword);
        signupRedirectText = findViewById(com.example.localart.R.id.signupRedirectText);
        btn_login = findViewById(R.id.btn_login);
        auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmail() && validatePassword()) {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkUser() {
        String userEmail = et_loginEmail.getText().toString().trim();
        String userPassword = et_loginPassword.getText().toString().trim();

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        if (currentUser != null) {
                            // Access the current user's information
                            String currentUserId = currentUser.getUid();
                            String currentUserEmail = currentUser.getEmail();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            // Pass the current user data using intent extras if needed
                            intent.putExtra("userId", currentUserId);
                            intent.putExtra("userEmail", currentUserEmail);
                            startActivity(intent);
                        }
                    } else {
                        // Authentication failed
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        handleAuthError(errorCode);
                    }
                });
    }

    private void handleAuthError(String errorCode) {
        // Handle specific authentication errors
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                et_loginEmail.setError("Invalid email address");
                et_loginEmail.requestFocus();
                break;
            case "ERROR_WRONG_PASSWORD":
                et_loginPassword.setError("Wrong password");
                et_loginPassword.requestFocus();
                break;
            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public Boolean validateEmail() {
        String val = et_loginEmail.getText().toString();
        if (val.isEmpty()) {
            et_loginEmail.setError("Email cannot be empty");
            return false;
        } else {
            et_loginEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = et_loginPassword.getText().toString();
        if (val.isEmpty()) {
            et_loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            et_loginPassword.setError(null);
            return true;
        }
    }
}
