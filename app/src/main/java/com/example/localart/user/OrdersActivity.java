package com.example.localart.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.R;
import com.example.localart.adapter.OrderAdapter;
import com.example.localart.dataclass.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

        // Retrieve orders from Firebase
        getOrdersFromFirebase();
    }

    // Retrieve orders from Firebase
    // Retrieve orders from Firebase based on the user ID
    private void getOrdersFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders").child(userId);

            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Order> orderList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Order order = snapshot.getValue(Order.class);
                        if (order != null) {
                            orderList.add(order);
                        }
                    }

                    // Now you have the list of orders
                    // Pass it to your adapter and update the RecyclerView
                    recyclerViewOrders.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
                    orderAdapter = new OrderAdapter(orderList, OrdersActivity.this);
                    recyclerViewOrders.setAdapter(orderAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(OrdersActivity.this, "Failed to retrieve orders", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(OrdersActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onOrderClick(int position) {
        if (orderAdapter != null) {
            Order order = orderAdapter.getOrderList().get(position);

            // Open the OrderItemDetailsActivity and pass the order ID
            Intent intent = new Intent(OrdersActivity.this, OrderItemDetailsActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            startActivity(intent);
        }
    }
}
