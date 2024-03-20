package com.example.localart.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.localart.R;
import com.example.localart.databinding.ActivitySellerHomeBinding;
import com.example.localart.seller.sellerfragment.ProductsFragment;
import com.example.localart.seller.sellerfragment.SellerHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellerHomeActivity extends AppCompatActivity {

    ActivitySellerHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new SellerHomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerHomeActivity.this, AddproductsActivity.class);
                startActivity(intent);
            }
        });


        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_sellerhome) {
                    replaceFragment(new SellerHomeFragment());
                } else if (item.getItemId() == R.id.nav_products) {
                    replaceFragment(new ProductsFragment());
                }

                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}