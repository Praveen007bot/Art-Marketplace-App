package com.example.localart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.R;
import com.example.localart.dataclass.Order;
import com.example.localart.user.ArtDetailsActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private  List<Order> orderList;
    private OnOrderClickListener onOrderClickListener;


    public OrderAdapter(List<Order> orderList, OnOrderClickListener onOrderClickListener) {
        this.orderList = orderList;
        this.onOrderClickListener = onOrderClickListener;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view, onOrderClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order order = orderList.get(position);

        // Bind order information to the ViewHolder
        holder.textViewOrderId.setText(order.getOrderId());
        holder.textViewAmount.setText(String.valueOf(order.getAmount()));

        // Set click listener
        holder.itemOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOrderClickListener.onOrderClick(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewOrderId, textViewAmount;
        LinearLayout itemOrderLL;
        OnOrderClickListener onOrderClickListener;

        public OrderViewHolder(@NonNull View itemView, OnOrderClickListener onOrderClickListener) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            itemOrderLL = itemView.findViewById(R.id.itemOrderLL);

            this.onOrderClickListener = onOrderClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onOrderClickListener.onOrderClick(position);
            }
        }
    }

    // Interface to handle click events
    public interface OnOrderClickListener {
        void onOrderClick(int position);

    }
}
