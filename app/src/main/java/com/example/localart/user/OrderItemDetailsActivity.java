package com.example.localart.user;

import static com.razorpay.AppSignatureHelper.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.R;
import com.example.localart.adapter.OrderItemAdapter;
import com.example.localart.dataclass.Order;
import com.example.localart.dataclass.OrderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderItemDetailsActivity extends AppCompatActivity {
    TextView textViewOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_details);
        textViewOrderId = findViewById(R.id.textViewOrderId);

        // Retrieve the order information from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("orderId")) {
            String orderId = intent.getStringExtra("orderId");
            getOrderItemsFromFirebase(orderId);
        } else {
            // Handle case where orderId is not passed properly
            Log.e(TAG, "No orderId found in intent");
            finish(); // Close activity if orderId is not available
        }

        textViewOrderId.setText(intent.getStringExtra("orderId"));
        // Assuming you have a reference to your RecyclerView
        RecyclerView recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);

        // Set up the RecyclerView with a layout manager
        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter (an empty list initially)
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(new ArrayList<>());

        // Set the adapter to the RecyclerView
        recyclerViewOrderItems.setAdapter(orderItemAdapter);
    }

    private void getOrderItemsFromFirebase(String orderId) {
        DatabaseReference orderItemsRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("orderItems")
                .child(orderId);

        orderItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<OrderItem> orderItemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productKey = snapshot.child("productKey").getValue(String.class);

                    // Fetch product information from Firebase based on productKey
                    DatabaseReference productRef = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("products")
                            .child(productKey);

                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                            if (productSnapshot.exists()) {
                                String artName = productSnapshot.child("artName").getValue(String.class);
                                String artPrice = productSnapshot.child("artPrice").getValue(String.class);
                                String artistName = productSnapshot.child("artistName").getValue(String.class);
                                String imageUrl = productSnapshot.child("imageUrl").getValue(String.class);
                                String sellerId = productSnapshot.child("sellerId").getValue(String.class);

                                // Create an OrderItem object with product details
                                OrderItem orderItem = new OrderItem(orderId, productKey, artName, artPrice, artistName, imageUrl, sellerId);
                                orderItemList.add(orderItem);

                                // Update the RecyclerView with the new data
                                RecyclerView recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
                                OrderItemAdapter orderItemAdapter = new OrderItemAdapter(orderItemList);
                                recyclerViewOrderItems.setAdapter(orderItemAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Log.e(TAG, "Database error: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

}

