package com.example.localart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.R;
import com.example.localart.dataclass.FeaturedProduct;

import java.util.List;

public class FeaturedProductsAdapter extends RecyclerView.Adapter<FeaturedProductsAdapter.FeaturedProductViewHolder> {

    private List<FeaturedProduct> featuredProducts;

    public FeaturedProductsAdapter(List<FeaturedProduct> featuredProducts) {
        this.featuredProducts = featuredProducts;
    }

    @NonNull
    @Override
    public FeaturedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured_product, parent, false);
        return new FeaturedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedProductViewHolder holder, int position) {
        FeaturedProduct product = featuredProducts.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return featuredProducts.size();
    }

    public class FeaturedProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView;
        private ImageView productImageView;

        public FeaturedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.textViewProductName);
            productImageView = itemView.findViewById(R.id.imageViewProduct);
        }

        public void bind(FeaturedProduct product) {
            // Bind data to your UI elements
            productNameTextView.setText(product.getProductName());
            productImageView.setImageResource(product.getProductImageResId());
        }
    }
}
