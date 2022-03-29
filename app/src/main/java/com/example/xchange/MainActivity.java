package com.example.xchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        mAuth = FirebaseAuth.getInstance();
if(mAuth.getCurrentUser()!=null){
    //startActivity(new Intent(MainActivity.this,homeActivity.class));
    //finish();
}
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


            final Button bli=findViewById(R.id.button);
            final TextView tvsu=findViewById(R.id.textview3);
            final TextView skip=findViewById(R.id.skip);

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,homeActivity.class));
                    finish();
                }
            });

            bli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(MainActivity.this,login.class));
                    finish();
                    // Toast.makeText(getApplicationContext(),"Login task should be included",Toast.LENGTH_SHORT).show();
                }
            });
            tvsu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(MainActivity.this,signup_activity.class);
                    MainActivity.this.startActivity(intent);

                }
            });


        }
    }


