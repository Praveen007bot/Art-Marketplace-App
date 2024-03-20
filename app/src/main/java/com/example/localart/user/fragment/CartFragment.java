package com.example.localart.user.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.localart.RecyclerData;
import com.example.localart.dataclass.Product;
import com.example.localart.user.AddressActivity;
import com.example.localart.R;
import com.example.localart.adapter.CartAdapter;
import com.example.localart.dataclass.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CartItem> cartItemList;
    private CartAdapter cartAdapter;
    private TextView totalAmountTextView;
    private Button btn_checkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItemList);
        recyclerView.setAdapter(cartAdapter);
        btn_checkout = view.findViewById(R.id.btn_checkout);


        totalAmountTextView = view.findViewById(R.id.totalAmountTextView);


        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), AddressActivity.class);

                // Assuming cartItemList is a List<CartItem>
                List<Product> productList = convertCartItemToProduct(cartItemList);

                // Pass the productList to the intent
                intent.putParcelableArrayListExtra("cartItemList", new ArrayList<>(productList));

                startActivity(intent);
            }
            private List<Product> convertCartItemToProduct(List<CartItem> cartItemList) {
                List<Product> productList = new ArrayList<>();

                for (CartItem cartItem : cartItemList) {
                    Product product = new Product(cartItem);
                    productList.add(product);
                }


                return productList;
            }


        });



        // Set up Firebase Database reference for the user's cart
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("cart").child(userId);

        // Listen for changes in the cart data
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartItemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItemList.add(cartItem);
                    }
                }

                cartAdapter.notifyDataSetChanged();

                // Update total amount
                updateTotalAmount();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error appropriately
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void updateTotalAmount() {
        double totalAmount = cartAdapter.calculateTotalAmount();
        totalAmountTextView.setText("Total Amount: " + totalAmount);
        RecyclerData.getInstance().setTotalAmount(String.valueOf(totalAmount));

    }
}
