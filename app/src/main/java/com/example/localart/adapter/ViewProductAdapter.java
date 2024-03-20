package com.example.localart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.localart.R;
import com.example.localart.dataclass.Product;

import java.util.List;

public class ViewProductAdapter extends RecyclerView.Adapter<ViewProductAdapter.ViewHolder> {

    private final List<Product> productList;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    private OnEditButtonClickListener onEditButtonClickListener;

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(Product product);
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClick(Product product);
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener onDeleteButtonClickListener) {
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener onEditButtonClickListener) {
        this.onEditButtonClickListener = onEditButtonClickListener;
    }

    public ViewProductAdapter(List<Product> productList, OnDeleteButtonClickListener onDeleteButtonClickListener, OnEditButtonClickListener onEditButtonClickListener) {
        this.productList = productList;
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
        this.onEditButtonClickListener = onEditButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_viewproduct, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvArtName.setText(product.getArtName());
        holder.tvArtPrice.setText(product.getArtPrice());
        holder.tvArtistName.setText(product.getArtistName());

        // Load image using Glide or your preferred image loading library
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image) // Placeholder image resource
                .error(R.drawable.error_image) // Error image resource
                .into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvArtName, tvArtPrice, tvArtistName;
        ImageView ivProductImage;
        Button btnDelete, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvArtName = itemView.findViewById(R.id.tvArtName);
            tvArtPrice = itemView.findViewById(R.id.tvArtPrice);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            btnDelete = itemView.findViewById(R.id.btn_deleteProduct);
            btnEdit = itemView.findViewById(R.id.btn_editproduct);

            // Set up onClickListener for delete button
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onDeleteButtonClickListener != null) {
                        onDeleteButtonClickListener.onDeleteButtonClick(productList.get(position));
                    }
                }
            });

            // Set up onClickListener for edit button
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onEditButtonClickListener != null) {
                        onEditButtonClickListener.onEditButtonClick(productList.get(position));
                    }
                }
            });
        }
    }
}
