package com.example.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup1 extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate (Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_signup1);
        final FirebaseFirestore mdb = FirebaseFirestore.getInstance();
        final Button buco1=findViewById(R.id.bocu1);
        final EditText password=findViewById(R.id.editpassword);
        final EditText repassword=findViewById(R.id.editrepassword);
        mAuth=FirebaseAuth.getInstance();

        buco1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String passwordCheck,repasswordChect,rEmail;
                Intent intent=getIntent();
                final String Email = intent.getStringExtra("email_key");
                final String Number = intent.getStringExtra("Phone");
                final String Name = intent.getStringExtra("Name");
                final String DOB = intent.getStringExtra("DOB");
                rEmail=Email;
                passwordCheck=password.getText().toString();
                repasswordChect=repassword.getText().toString();

                if(passwordCheck.length() < 6){
                    password.setError("Password should contain minimum 6 values");
                    //password.setText(null);
                    repassword.setText(null);
                    return;
                }
                if(!repasswordChect.equals(passwordCheck)){
                    Toast.makeText(getApplicationContext(),
                            Email,
                            Toast.LENGTH_SHORT)
                            .show();
                    repassword.setText(null);
                    repassword.setError("Password did not match");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(rEmail,repasswordChect)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest Creq = new UserProfileChangeRequest.Builder().setDisplayName(Name).build();
                                    user.updateProfile(Creq);
                                    Toast.makeText(getApplicationContext(),"Success",
                                            Toast.LENGTH_LONG).show();
                                    Map<String , Object> details = new HashMap<>();
                                    details.put("User_Name",Name);
                                    details.put("Email",rEmail);
                                    details.put("Number",Number);
                                    details.put("Date_Of_Birth",DOB);
                                    details.put("Address","ABC");
                                    mdb.collection("Users").document(rEmail).set(details);
                                    Intent intent=new Intent(signup1.this, login.class);
                                    signup1.this.startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"error"+task.getException(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }
        });



    }
}