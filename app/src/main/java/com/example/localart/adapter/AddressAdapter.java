package com.example.localart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localart.R;
import com.example.localart.dataclass.Address;

import java.util.List;

// AddressAdapter.java
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    public interface OnAddressSelectedListener {
        void onAddressSelected(Address selectedAddress);
    }

    private List<Address> addressList;
    private int selectedPosition = -1;
    private OnAddressSelectedListener onAddressSelectedListener;

    public AddressAdapter(List<Address> addressList, OnAddressSelectedListener listener) {
        this.addressList = addressList;
        this.onAddressSelectedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.bind(address, position);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RadioButton radioButton;
        private TextView nameTextView;
        private TextView phoneTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
        }

        public void bind(final Address address, final int position) {
            nameTextView.setText("Name: " + address.getName());
            phoneTextView.setText("Phone: " + address.getPhone());

            radioButton.setChecked(position == selectedPosition);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateRadioButtonSelection(position);
                    if (onAddressSelectedListener != null) {
                        onAddressSelectedListener.onAddressSelected(addressList.get(position));
                    }
                }
            });
        }
    }

    private void updateRadioButtonSelection(int newSelectedPosition) {
        if (selectedPosition != newSelectedPosition) {
            selectedPosition = newSelectedPosition;
            notifyDataSetChanged();
        }
    }

    public Address getSelectedAddress() {
        if (selectedPosition != -1) {
            return addressList.get(selectedPosition);
        }
        return null;
    }
}
