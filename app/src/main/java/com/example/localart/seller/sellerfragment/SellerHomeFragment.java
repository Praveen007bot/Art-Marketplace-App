package com.example.localart.seller.sellerfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.localart.seller.AddproductsActivity;
import com.example.localart.R;
import com.example.localart.seller.ViewProductsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SellerHomeFragment extends Fragment {

CardView cv_addProduct;
CardView cv_viewProducts;

DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        cv_viewProducts = view.findViewById(R.id.cv_viewProducts);

        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the count of children (i.e., size of the list)
                long size = dataSnapshot.getChildrenCount();
                // Use the size variable as needed (e.g., display it, perform further operations)
                // For example, you can set it to a TextView
                TextView viewProductsTV = view.findViewById(R.id.viewProductsTV);

                viewProductsTV.setText(String.valueOf(size));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

        cv_viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), ViewProductsActivity.class);
                startActivity(intent2);
            }
        });
        


        // Assuming you have a card view with id cv_addProduct
        view.findViewById(R.id.cv_addProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddProductActivity when the card is clicked
                Intent intent = new Intent(getActivity(), AddproductsActivity.class);
                startActivity(intent);
            }
        });
    }
}