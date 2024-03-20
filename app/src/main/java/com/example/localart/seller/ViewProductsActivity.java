package com.example.localart.seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localart.R;
import com.example.localart.adapter.ViewProductAdapter;
import com.example.localart.dataclass.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ViewProductsActivity extends AppCompatActivity
        implements ViewProductAdapter.OnDeleteButtonClickListener, ViewProductAdapter.OnEditButtonClickListener {

    private RecyclerView recyclerView;
    private ViewProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        productList = new ArrayList<>();
        productAdapter = new ViewProductAdapter(productList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        // Set the click listeners
        productAdapter.setOnDeleteButtonClickListener(this);
        productAdapter.setOnEditButtonClickListener(this);

        // Fetch products from Firebase
        fetchProducts();
    }

    private void fetchProducts() {
        // Get the currently authenticated user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // The user is signed in
            String currentUserId = currentUser.getUid();

            // Initialize Firebase Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference productsRef = database.getReference("products");

            // Query products for the current user
            Query userProductsQuery = productsRef.orderByChild("sellerId").equalTo(currentUserId);

            userProductsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productList.clear();

                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        if (product != null) {
                            // Save the product key for deletion
                            product.setProductKey(productSnapshot.getKey());
                            productList.add(product);
                        }
                    }

                    // Update the RecyclerView
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        } else {
            // No user is signed in, handle accordingly
        }
    }

    @Override
    public void onDeleteButtonClick(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProductsActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this product?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked Yes, delete the product
                deleteProduct(product);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked No, do nothing or handle accordingly
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }



    private void deleteProduct(Product product) {
        // Check if the product belongs to the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getUid().equals(product.getSellerId())) {
            // User ID matches, proceed with deletion
            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
            DatabaseReference productRef = productsRef.child(product.getProductKey());

            productRef.removeValue().addOnSuccessListener(aVoid -> {
                // Product deleted successfully
                Toast.makeText(ViewProductsActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                // Failed to delete product
                Toast.makeText(ViewProductsActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Show an error message or handle accordingly
            Toast.makeText(ViewProductsActivity.this, "You don't have permission to delete this product", Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    public void onEditButtonClick(Product product) {
        // Handle edit action, you can open a dialog or navigate to an edit activity
        // For example, opening a dialog for editing product details
        showEditProductDialog(product);
    }

    private void showEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Product");

        // Inflate a custom layout for the dialog
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edit_product, null);

        // Find views in the custom layout
        EditText etEditArtName = viewInflated.findViewById(R.id.etEditArtName);
        EditText etEditArtistName = viewInflated.findViewById(R.id.etEditArtistName);
        EditText etEditArtPrice = viewInflated.findViewById(R.id.etEditArtPrice);

        // Set existing product details in the dialog
        etEditArtName.setText(product.getArtName());
        etEditArtistName.setText(product.getArtistName());
        etEditArtPrice.setText(product.getArtPrice());

        builder.setView(viewInflated);

        // Set up buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get edited values from the dialog
                String editedArtName = etEditArtName.getText().toString().trim();
                String editedArtistName = etEditArtistName.getText().toString().trim();
                String editedArtPrice = etEditArtPrice.getText().toString().trim();

                // Update the product details in Firebase Database
                updateProductDetails(product, editedArtName, editedArtistName, editedArtPrice);

                // Close the dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked cancel, close the dialog
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void updateProductDetails(Product product, String editedArtName, String editedArtistName, String editedArtPrice) {
        // Check if the product belongs to the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getUid().equals(product.getSellerId())) {
            // User ID matches, proceed with updating
            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
            DatabaseReference productRef = productsRef.child(product.getProductKey());

            // Update the product details
            productRef.child("artName").setValue(editedArtName);
            productRef.child("artistName").setValue(editedArtistName);
            productRef.child("artPrice").setValue(editedArtPrice);

            // Notify the user about the successful update
            Toast.makeText(ViewProductsActivity.this, "Product details updated", Toast.LENGTH_SHORT).show();
        } else {
            // Show an error message or handle accordingly
            Toast.makeText(ViewProductsActivity.this, "You don't have permission to update this product", Toast.LENGTH_SHORT).show();
        }
    }
}