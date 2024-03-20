package com.example.localart.seller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localart.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddproductsActivity extends AppCompatActivity {

    private EditText etArtName, etArtistName, etArtPrice;
    private Button btnChooseImage, btnAddProduct;

    static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproducts);

        etArtName = findViewById(R.id.et_artName);
        etArtistName = findViewById(R.id.et_artistName);
        etArtPrice = findViewById(R.id.et_artPrice);
        btnChooseImage = findViewById(R.id.btn_chooseImage);
        btnAddProduct = findViewById(R.id.btn_addProduct);

        storageReference = FirebaseStorage.getInstance().getReference("product_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        auth = FirebaseAuth.getInstance();

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProductData();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // You can set an ImageView to preview the chosen image if needed.
        }
    }

    private void uploadProductData() {
        final String artName = etArtName.getText().toString().trim();
        final String artistName = etArtistName.getText().toString().trim();
        final String artPrice = etArtPrice.getText().toString().trim();

        if (artName.isEmpty() || artistName.isEmpty() || artPrice.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image to Firebase Storage
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        UploadTask uploadTask = fileReference.putFile(imageUri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    saveProductDataToDatabase(artName, artistName, artPrice, downloadUri.toString());
                } else {
                    Toast.makeText(AddproductsActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveProductDataToDatabase(String artName, String artistName, String artPrice, String imageUrl) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String sellerId = currentUser.getUid();

            DatabaseReference productRef = databaseReference.push();
            String productKey = productRef.getKey();  // Get the product key

            Map<String, Object> productData = new HashMap<>();
            productData.put("artName", artName);
            productData.put("artistName", artistName);
            productData.put("artPrice", artPrice);
            productData.put("imageUrl", imageUrl);
            productData.put("sellerId", sellerId);
            productData.put("productKey", productKey);  // Add the product key

            productRef.setValue(productData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddproductsActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddproductsActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle the case where there is no authenticated user
            Toast.makeText(AddproductsActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }
}
