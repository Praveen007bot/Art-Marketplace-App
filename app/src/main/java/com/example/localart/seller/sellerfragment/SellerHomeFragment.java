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

import com.example.localart.seller.AddproductsActivity;
import com.example.localart.R;
import com.example.localart.seller.ViewProductsActivity;


public class SellerHomeFragment extends Fragment {

CardView cv_addProduct;
CardView cv_viewProducts;
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