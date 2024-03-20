package com.example.localart.user;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.localart.R;
import com.example.localart.databinding.ActivityHomeBinding;
import com.example.localart.user.fragment.CartFragment;
import com.example.localart.user.fragment.HomeFragment;
import com.example.localart.user.fragment.ProfileFragment;
import com.example.localart.user.fragment.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.navigation.setBackground(null);


        binding.navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    replaceFragment(new HomeFragment());
                } else if (item.getItemId() == R.id.nav_shop) {
                    replaceFragment(new ShopFragment());
                } else if (item.getItemId() == R.id.nav_cart) {
                    replaceFragment(new CartFragment());
                } else if (item.getItemId() == R.id.nav_profile) {
                    replaceFragment(new ProfileFragment());
                }

                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }
}
