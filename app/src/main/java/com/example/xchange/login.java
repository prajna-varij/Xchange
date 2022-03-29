package com.example.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
Button button;
EditText email,password;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.button);
        email = findViewById(R.id.editemail);
        password = findViewById(R.id.editpassword);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        mAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    String lemail,lpassword;
                    lemail=email.getText().toString().trim();
                    lpassword=password.getText().toString().trim();

                    if (TextUtils.isEmpty(lemail) || !lemail.matches(emailPattern)) {
                        Toast.makeText(getApplicationContext(),
                                "Please enter valid email address!!",
                                Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    if(lpassword.length()<6){
                        password.setError("Password should contain minimum 6 values");
                        password.setText(null);
                        return;
                    }
                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(login.this,"Logging In...",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(login.this, homeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this,"Error Logging in, Please Try Again!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }
}