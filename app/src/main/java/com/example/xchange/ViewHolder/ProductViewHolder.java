package com.example.xchange.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.Interface.ItemClickListner;
import com.example.xchange.R;
import com.example.xchange.productdetails;
import com.google.firebase.auth.FirebaseAuth;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public CheckBox favcheck;
    public ItemClickListner listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        favcheck=itemView.findViewById(R.id.checkbox_favourite);

        //favcheck.setOnCheckedChangeListener(this);

    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

       /* if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText( productdetails.this, "please loggin First...",Toast.LENGTH_LONG).show();
            return;
        }
        if(isChecked){

        }*/
    }
}

