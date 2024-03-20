package com.example.localart.user;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localart.R;
import com.example.localart.dataclass.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ArtDetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_details);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database reference for the user's cart
        String userId = mAuth.getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference().child("cart").child(userId);

        // Initialize views
        ImageView artImageView = findViewById(R.id.artImageView);
        TextView artNameTextView = findViewById(R.id.artNameTextView);
        TextView artistNameTextView = findViewById(R.id.artistNameTextView);
        TextView artPriceTextView = findViewById(R.id.artPriceTextView);
        Button addToCartButton = findViewById(R.id.addToCartButton);

        // Set onClickListener for the Add to Cart button
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Add to Cart button click
                addToCart();
            }
        });

        // Retrieve intent data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String artName = extras.getString("ART_NAME", "");
            String artistName = extras.getString("ARTIST_NAME", "");
            String artPrice = extras.getString("ART_PRICE", "");
            String imageUrl = extras.getString("IMAGE_URL", "");
            String sellerId = extras.getString("SELLER_ID", "");

            // Set data to the views
            Picasso.get().load(imageUrl).into(artImageView);
            artNameTextView.setText(artName);
            artistNameTextView.setText(artistName);
            artPriceTextView.setText(artPrice);

        }
    }

    private void addToCart() {
        // Retrieve intent data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String artName = extras.getString("ART_NAME", "");
            String artistName = extras.getString("ARTIST_NAME", "");
            String artPrice = extras.getString("ART_PRICE", "");
            String sellerId = extras.getString("SELLER_ID", "");
            String imageUrl = extras.getString("IMAGE_URL","");
            String productKey = extras.getString("PRODUCT_KEY","");


            // Log the retrieved productKey to check its value
            Log.d("Debug", "Product Key: " + productKey);

            // Get the user ID
            String userId = mAuth.getCurrentUser().getUid();

            // Check if the product is already in the user's cart
            cartRef.orderByChild("artName").equalTo(artName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                // Product not in the cart, proceed to add it

                                addProductToCart(productKey, artName, artPrice, artistName, sellerId, imageUrl);
                                Toast.makeText(ArtDetailsActivity.this, "Sucessfully added to cart", Toast.LENGTH_SHORT).show();
                            } else {
                                // Product already in the cart, show toast message
                                Toast.makeText(ArtDetailsActivity.this, "Already added to cart", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error appropriately
                        }
                    });
        }
    }

    private void addProductToCart(String productKey, String artName, String artPrice, String artistName, String sellerId, String imageUrl) {
        // Create a unique cart item ID (you may use the product ID or generate a unique ID)
        String cartItemId = cartRef.push().getKey();

        // Create a CartItem object with the productKey
        CartItem cartItem = new CartItem(productKey, artName, artPrice, artistName, sellerId, imageUrl);

        // Add the cart item to the user's cart in the database
        cartRef.child(cartItemId).setValue(cartItem);
    }




}
