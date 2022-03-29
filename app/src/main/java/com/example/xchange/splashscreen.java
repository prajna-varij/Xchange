package com.example.xchange;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class splashscreen extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    @Override
    @SuppressLint("MissingSuperCall")
    public void onCreate (Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);

        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Handler han=new Handler();
            han.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(splashscreen.this,homeActivity.class);
                    splashscreen.this.startActivity(intent);
                    finish();
                }
            },1000);
        }

        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }

    }
}