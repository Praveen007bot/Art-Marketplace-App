package com.example.localart.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.localart.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    EditText et_editName, et_editEmail, et_editUserName, et_editPassword;
    Button btn_editSave;
    String nameUser, emailUser, usernameUser, passwordUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        et_editName = findViewById(R.id.et_editName);
        et_editEmail = findViewById(R.id.et_editEmail);
        et_editUserName = findViewById(R.id.et_editUserName);
        et_editPassword = findViewById(R.id.et_editPassword);
        btn_editSave = findViewById(R.id.btn_editSave);

        showData();

        btn_editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged()) {
                    Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isNameChanged(){
        if (!nameUser.equals(et_editName.getText().toString())){
            reference.child(usernameUser).child("name").setValue(et_editName.getText().toString());
            nameUser = et_editName.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isEmailChanged(){
        if (!emailUser.equals(et_editName.getText().toString())){
            reference.child(usernameUser).child("email").setValue(et_editEmail.getText().toString());
            emailUser = et_editEmail.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isPasswordChanged(){
        if (!passwordUser.equals(et_editPassword.getText().toString())){
            reference.child(usernameUser).child("password").setValue(et_editPassword.getText().toString());
            passwordUser = et_editPassword.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public void showData(){
        Intent intent = getIntent();

        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");

        et_editName.setText(nameUser);
        et_editEmail.setText(emailUser);
        et_editUserName.setText(usernameUser);
        et_editPassword.setText(passwordUser);
    }
}