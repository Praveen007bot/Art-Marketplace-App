package com.example.localart.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.R;
import com.example.localart.adapter.ProductAdapter;
import com.example.localart.dataclass.Product;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private ProductAdapter productAdapter;
    private List<Product> productList;

    private DatabaseReference databaseReference;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(com.example.localart.R.layout.fragment_shop, container, false);

        // Initialize RecyclerView and its layout manager
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Initialize the list to hold products
        productList = new ArrayList<>();

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        productAdapter = new ProductAdapter(productList, requireContext());
        recyclerView.setAdapter(productAdapter);



        // Fetch data from Firebase Database
        fetchProducts();

        return view;
    }

    private void fetchProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear(); // Clear existing data

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error appropriately
            }
        });
    }
}
