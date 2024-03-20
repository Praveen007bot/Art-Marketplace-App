package com.example.localart.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.R;
import com.example.localart.dataclass.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartItemList;

    public CartAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }



    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartItem cartItem = cartItemList.get(position);





        String imageUrl = cartItem.getImageUrl();
        if (imageUrl != null) {


            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .into(holder.cartArtImageView);
        } else {

        }

        // Set the data to the views
        holder.cartArtNameTextView.setText(cartItem.getArtName());
        holder.cartArtistNameTextView.setText(cartItem.getArtistName());
        holder.cartArtPriceTextView.setText(cartItem.getArtPrice());

        // Set click listener for the remove button
        holder.removeFromCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call a method to handle item removal from the cart
                removeFromCart(position);
            }
        });
    }

    public double calculateTotalAmount() {
        double totalAmount = 0.0;

        for (CartItem cartItem : cartItemList) {
            // Assuming artPrice is in string format
            String artPriceStr = cartItem.getArtPrice();

            // Check if the string is not empty or contains only whitespaces
            if (artPriceStr != null && !artPriceStr.trim().isEmpty()) {
                try {
                    // Remove any non-numeric characters or symbols
                    artPriceStr = artPriceStr.replaceAll("[^\\d.]", "");

                    // Convert the cleaned string to double
                    double artPrice = Double.parseDouble(artPriceStr);

                    totalAmount += artPrice;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // Add a log statement to identify the problematic item
                    Log.e("CartAdapter", "Error parsing artPrice for item: " + cartItem.getArtName());
                }
            }
        }

        return totalAmount;
    }







    @Override
    public int getItemCount() {
        return cartItemList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView cartArtNameTextView;
        private final TextView cartArtistNameTextView;
        private final TextView cartArtPriceTextView;
        private final ImageButton removeFromCartButton;
        public ImageView cartArtImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartArtNameTextView = itemView.findViewById(R.id.cartArtNameTextView);
            cartArtistNameTextView = itemView.findViewById(R.id.cartArtistNameTextView);
            cartArtPriceTextView = itemView.findViewById(R.id.cartArtPriceTextView);
            removeFromCartButton = itemView.findViewById(R.id.removeFromCart);
            cartArtImageView = itemView.findViewById(R.id.cartArtImageView);
        }
    }


    public void removeFromCart(int position) {
        // Get the item to be removed
        CartItem removedItem = cartItemList.get(position);

        // Remove the item from the list
        cartItemList.remove(position);

        // Notify the adapter that the data set has changed
        notifyDataSetChanged();

        // Remove the item from the Firebase database
        removeItemFromFirebase(removedItem);
    }

    private void removeItemFromFirebase(CartItem cartItem) {
        // Initialize Firebase Database reference for the user's cart
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("cart").child(userId);

        // Find the corresponding item in the Firebase database and remove it
        Query query = cartRef.orderByChild("artName").equalTo(cartItem.getArtName());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue(); // Remove the item from the database
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error appropriately
            }
        });
    }

}
