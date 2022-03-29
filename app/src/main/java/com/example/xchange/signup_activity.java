package com.example.xchange;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class signup_activity extends AppCompatActivity {


    DatePickerDialog picker;
    private FirebaseAuth mAuth;
    public void onCreate (Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_signup_activity);
        final EditText name=findViewById(R.id.editname);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final EditText number=findViewById(R.id.editnumber);
        final EditText email=findViewById(R.id.editemail);
        final EditText dob=findViewById(R.id.editdob);
        final Button continu=findViewById(R.id.buco);
        final ImageView cal=findViewById(R.id.imagecal);

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                picker=new DatePickerDialog(signup_activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob.setText(dayOfMonth + "/" + (month+1)+ "/" + year);
                    }
                },day,month,year);
                picker.show();
            }
        });

        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameCheck,numberChecc,dobCheck,emailCheck;

                emailCheck = email.getText().toString().trim();
                numberChecc=number.getText().toString().trim();
                nameCheck=name.getText().toString().trim();
                dobCheck=dob.getText().toString().trim();

                if (TextUtils.isEmpty(nameCheck)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter Name!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(emailCheck) || !emailCheck.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter valid email address!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(numberChecc) || numberChecc.length() != 10) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter valid mobile number!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(dobCheck)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter Date of Birth!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

               Intent intent=new Intent(signup_activity.this,signup1.class);
                String Email=emailCheck;
                intent.putExtra("Phone",numberChecc);
                intent.putExtra("Name",nameCheck);
                intent.putExtra("DOB",dobCheck);
                intent.putExtra("email_key",Email);
                signup_activity.this.startActivity(intent);
                finish();
            }
        });
    }
}