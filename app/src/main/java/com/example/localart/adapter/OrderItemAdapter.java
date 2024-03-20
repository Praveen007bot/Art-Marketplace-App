package com.example.localart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.R;
import com.example.localart.dataclass.OrderItem;

import java.util.List;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;



public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderItem> orderItemList;

    public OrderItemAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);


        holder.textViewArtName.setText(orderItem.getArtName());
        holder.textViewArtPrice.setText(orderItem.getArtPrice());
        holder.textViewArtistName.setText(orderItem.getArtistName());

        // Load image using Picasso
        Picasso.get().load(orderItem.getImageUrl()).into(holder.imageViewProduct);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
        notifyDataSetChanged();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewArtName;
        TextView textViewArtPrice;
        TextView textViewArtistName;
        ImageView imageViewProduct;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewArtName = itemView.findViewById(R.id.textViewArtName);
            textViewArtPrice = itemView.findViewById(R.id.textViewArtPrice);
            textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}

