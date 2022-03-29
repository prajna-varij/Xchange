package com.example.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xchange.model.product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class productdetails extends AppCompatActivity {

    private Button addToCartButton;
    private ImageView productImage;
    private Button numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal";
    private FirebaseAuth mAuth;
    final FirebaseFirestore mdb = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);

        productID = getIntent().getStringExtra("pid");

        mAuth=FirebaseAuth.getInstance();
        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
       // numberButton = (Button) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);


        getProductDetails(productID);


        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                    addingTofavouriteList();

            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();

       // CheckOrderState();
    }

    private void addingTofavouriteList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTimeInMillis());

        HashMap<String, Object> fav = new HashMap<>();
        fav.put("ID",productID);
        fav.put("date", saveCurrentDate);
        fav.put("time", saveCurrentTime);

        mdb.collection("Users").document(mAuth.getCurrentUser().getEmail().toString()).collection("Favourites").document(productID).set(fav);
        Toast.makeText(productdetails.this, "Added to Favourite List.", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(productdetails.this, homeActivity.class);
        startActivity(intent);
        finish();

        /*final CollectionReference cartListRef =  mdb.collection("Users").document(mAuth.getCurrentUser().getEmail().toString()).collection("Favourites");


        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        //cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");
*/
        /*cartListRef.add("User View").addOnSuccessListener(new  mAuth.getCurrentUser().getEmail().toString())
                .child("product").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(mAuth.getCurrentUser().getEmail().toString())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(productdetails.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(productdetails.this, homeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });*/
    }

    private void getProductDetails(String productID)
    {
        DocumentReference productref = mdb.collection("product").document("test");

        productref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                product products = documentSnapshot.toObject(product.class);
                productName.setText(products.getPname());
                productPrice.setText(products.getPrice());
                productDescription.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(productImage);
            }
        });
        /*productref.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    product products = dataSnapshot.getValue(product.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

  /*  private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}
