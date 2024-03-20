package com.example.localart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.user.ArtDetailsActivity;
import com.example.localart.R;
import com.example.localart.dataclass.Product;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set the data to the views
        holder.artNameTextView.setText(product.getArtName());
        holder.artistNameTextView.setText(product.getArtistName());
        holder.artPriceTextView.setText(product.getArtPrice());

        // Load image using Picasso or any other image loading library
        Picasso.get().load(product.getImageUrl()).into(holder.imageView);

        // Set click listener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the ArtDetailsActivity
                Intent intent = new Intent(context, ArtDetailsActivity.class);

                // Pass product details to the ArtDetailsActivity using intent extras

                intent.putExtra("ART_NAME", product.getArtName());
                intent.putExtra("ARTIST_NAME", product.getArtistName());
                intent.putExtra("ART_PRICE", product.getArtPrice());
                intent.putExtra("IMAGE_URL", product.getImageUrl());
                intent.putExtra("SELLER_ID", product.getSellerId());
                intent.putExtra("PRODUCT_KEY",product.getProductKey());
                // Add more data as needed...

                // Start the ArtDetailsActivity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView artNameTextView;
        private final TextView artistNameTextView;
        private final TextView artPriceTextView;
        private final ImageView imageView;
        private String sellerId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artNameTextView = itemView.findViewById(R.id.artNameTextView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
            artPriceTextView = itemView.findViewById(R.id.artPriceTextView);
            imageView = itemView.findViewById(R.id.productImageView);
        }
        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }
    }
}
