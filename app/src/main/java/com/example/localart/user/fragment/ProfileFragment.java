package com.example.localart.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.localart.MainActivity;
import com.example.localart.R;
import com.example.localart.user.HomeActivity;
import com.example.localart.user.OrdersActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView usernameTextView, emailTextView, tv_orders, tv_signout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        usernameTextView = view.findViewById(R.id.tv_username);
        emailTextView = view.findViewById(R.id.tv_email);
        tv_orders = view.findViewById(R.id.tv_orders);
        tv_signout = view.findViewById(R.id.tv_signout);

        tv_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrdersActivity.class);
                startActivity(intent);
            }
        });

        tv_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if the user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, update UI with the user's information
            String username = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            // Set the values to the TextViews
            usernameTextView.setText(username);
            emailTextView.setText(email);
        } else {
            // If the user is not signed in, you may want to redirect to the login screen or handle it accordingly.
        }
    }

    private void signOut() {
        mAuth.signOut();
        // After signing out, redirect the user to the homepage or login screen
        // For example, you can use Intent to navigate to the homepage activity
        // Replace HomeActivity.class with your homepage activity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();  // Close the current activity to prevent going back to it via back button
    }
}
