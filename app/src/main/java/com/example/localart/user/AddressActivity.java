package com.example.localart.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.localart.R;
import com.example.localart.RecyclerData;
import com.example.localart.adapter.AddressAdapter;
import com.example.localart.dataclass.CartItem;
import com.example.localart.dataclass.Address;
import com.example.localart.dataclass.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddressActivity extends AppCompatActivity implements PaymentResultWithDataListener, AddressAdapter.OnAddressSelectedListener {

    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;

    private List<CartItem> cartItemList = new ArrayList<>();
    private Address selectedAddress;
    private Integer paisaAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Checkout.preload(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressList = new ArrayList<>();
        addressAdapter = new AddressAdapter(addressList, this);

        recyclerView.setAdapter(addressAdapter);
        cartItemList = new ArrayList<>();

        String totalAmount = RecyclerData.getInstance().getTotalAmount();
        String rupeeAmountWithoutDecimal = totalAmount.split("\\.")[0];
        paisaAmount = Integer.parseInt(rupeeAmountWithoutDecimal) * 100;

        Button btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address selectedAddress = addressAdapter.getSelectedAddress();
                // Check if an address is selected
                if (selectedAddress != null) {
                    startPayment(paisaAmount);
                } else {
                    // Show a message indicating that an address needs to be selected
                    Toast.makeText(AddressActivity.this, "Please select an address before making a payment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the AddAddressActivity or AddAddressFragment to add a new address
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });

        // Load addresses from Firebase or your data source
        loadAddresses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver to avoid IntentReceiverLeaked error
        Checkout.clearUserData(getApplicationContext());
    }

    private void loadAddresses() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference addressesRef = FirebaseDatabase.getInstance().getReference().child("addresses").child(userId);

        addressesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (addressList == null) {
                    addressList = new ArrayList<>();  // Initialize addressList if null
                } else {
                    addressList.clear();  // Clear the list if it's not null
                }

                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    Address address = addressSnapshot.getValue(Address.class);
                    if (address != null) {
                        addressList.add(address);
                    }
                }

                addressAdapter.notifyDataSetChanged();

                // Call fetchCartItems after loading addresses
                fetchCartItems(userId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error appropriately
            }
        });
    }



    private void startPayment(int amount) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_zAeMUkRB7xCEGH");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Local Art");
            options.put("description", "Purchase Description");
            options.put("currency", "INR");
            options.put("amount", amount);

            checkout.open(AddressActivity.this, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData data) {
        try {
            // Handle payment success
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();


            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                String userId = currentUser.getUid();
                String orderId = generateOrderId(); // Generate a unique order ID on the client-side
                double amount = paisaAmount;
                String placedOn = getCurrentTimestamp();

                if (selectedAddress != null) {
                    Order order = new Order();
                    order.setOrderId(orderId);
                    order.setAmount(amount);
                    order.setPlacedOn(placedOn);
                    order.setUserId(userId);
                    order.setShippingAddress(selectedAddress);

                    DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
                    ordersRef.child(userId).child(orderId).setValue(order, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.e("Firebase", "Order write failed", databaseError.toException());
                                Toast.makeText(AddressActivity.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Firebase", "Order write succeeded");
                                Toast.makeText(AddressActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    storeOrderItems(orderId, cartItemList);
                    removeItemsFromCart(userId);
                } else {
                    Log.w("Address", "No address selected");
                    Toast.makeText(this, "Please select an address before making a payment", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error handling payment success. Please contact support.", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateOrderId() {
        // Implement your logic to generate a unique order ID on the client-side
        return "ORDER_" + System.currentTimeMillis();
    }




    @Override
    public void onPaymentError(int code, String response, PaymentData data) {
        // Handle payment failure
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddressSelected(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    private void removeItemsFromCart(String userId) {
        // Get a reference to the user's cart
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        // Attach a listener to read the data
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    // Get the unique key for each item
                    String itemKey = itemSnapshot.getKey();

                    // Remove the item from the cart
                    DatabaseReference itemRef = cartRef.child(itemKey);
                    itemRef.removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("Firebase", "Error reading cart data: " + databaseError.getMessage());
            }
        });
    }
    private void fetchCartItems(String userId) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartItemList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = itemSnapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItemList.add(cartItem);
                    }
                }

                // Now that you have the cart items, proceed to make the payment or handle as needed.
                // You may want to store the cart items in a class variable to use them later.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("Firebase", "Error reading cart data: " + databaseError.getMessage());
            }
        });
    }

    private void storeOrderItems(String orderId, List<CartItem> cartItemList) {
        DatabaseReference orderItemsRef = FirebaseDatabase.getInstance().getReference("orderItems").child(orderId);

        for (CartItem cartItem : cartItemList) {
            String productKey = cartItem.getProductKey();


            // Create a Map to store OrderItem details
            Map<String, Object> orderItemDetails = new HashMap<>();
            orderItemDetails.put("productKey", productKey);


            // Use push() to generate a unique key for each order item
            orderItemsRef.push().setValue(orderItemDetails);
        }
    }

}
