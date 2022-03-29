package com.example.xchange;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText,dobEditText;
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;
    private Button securityQuestionBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    private FirebaseAuth mAuth;
    final FirebaseFirestore mdb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eidtprofile);
        final FirebaseFirestore mdb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phone_number);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        dobEditText=(EditText) findViewById(R.id.settings_dob);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings_btn);



        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText,dobEditText);


        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Edit_profile.this, homeActivity.class);
                startActivity(intent);
                finish();
            }
        });



        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });


        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(Edit_profile.this);
            }
        });
    }



    private void updateOnlyUserInfo()
    {



        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("User_Name", fullNameEditText.getText().toString());
        userMap. put("Address", addressEditText.getText().toString());
        userMap. put("Number", userPhoneEditText.getText().toString());
        //ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest Creq = new UserProfileChangeRequest.Builder().setDisplayName(fullNameEditText.getText().toString()).build();
        user.updateProfile(Creq);
        mdb.collection("Users").document(mAuth.getCurrentUser().getEmail().toString()).update(userMap);
        startActivity(new Intent(Edit_profile.this, homeActivity.class));
        Toast.makeText(Edit_profile.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Edit_profile.this, Edit_profile.class));
            finish();
        }
    }




    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Adress is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "number is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(dobEditText.getText().toString())){
            Toast.makeText(this, "date-of-birth is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }



    private void uploadImage()
    {
        //Toast.makeText(Edit_profile.this,"Uploading image",Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(mAuth.getCurrentUser().getEmail().toString() + ".jpg1");

            uploadTask = fileRef.putFile(imageUri);

            /*uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })*/
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Edit_profile.this,"Failed to uplaod image!",Toast.LENGTH_LONG).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                //Toast.makeText(Edit_profile.this,"Task Started",Toast.LENGTH_LONG).show();

                if (task.isSuccessful()) {
                    //Toast.makeText(Edit_profile.this,"Task Successful",Toast.LENGTH_LONG).show();

                    Uri downloadUrl = task.getResult();
                    myUrl = downloadUrl.toString();

                    //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("User_Name", fullNameEditText.getText().toString());
                    userMap.put("Address", addressEditText.getText().toString());
                    userMap.put("Number", userPhoneEditText.getText().toString());
                    userMap.put("Image", myUrl);
                    userMap.put("Date_Of_Birth",dobEditText.getText().toString());
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest Creq = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUrl).build();
                    UserProfileChangeRequest Cre1 = new UserProfileChangeRequest.Builder().setDisplayName(fullNameEditText.getText().toString()).build();
                    user.updateProfile(Cre1);
                    user.updateProfile(Creq);
                    //ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                    mdb.collection("Users").document(mAuth.getCurrentUser().getEmail().toString()).update(userMap);
                    progressDialog.dismiss();

                    startActivity(new Intent(Edit_profile.this, homeActivity.class));
                    Toast.makeText(Edit_profile.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Edit_profile.this, "Error.", Toast.LENGTH_SHORT).show();
                }
            }
        });
                 /* uploadTask.addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {

                      }
                  })  .addOnCompleteListener(new OnCompleteListener<Uri>() {
                      @Override
                      public void onComplete(@NonNull Task<Uri> task) {
                          if (task.isSuccessful()) {
                              Uri downloadUrl = task.getResult();
                              myUrl = downloadUrl.toString();

                              //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                              HashMap<String, Object> userMap = new HashMap<>();
                              userMap.put("User_Name", fullNameEditText.getText().toString());
                              userMap.put("Address", addressEditText.getText().toString());
                              userMap.put("Number", userPhoneEditText.getText().toString());
                              //userMap.put("Image", myUrl);
                              userMap.put("Date_Of_Birth",dobEditText.getText().toString());
                              FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                              UserProfileChangeRequest Creq = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUrl).build();
                              UserProfileChangeRequest Cre1 = new UserProfileChangeRequest.Builder().setDisplayName(fullNameEditText.getText().toString()).build();
                              user.updateProfile(Cre1);
                              user.updateProfile(Creq);
                              //ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                              mdb.collection("Users").document(mAuth.getCurrentUser().getEmail().toString()).update(userMap);
                              progressDialog.dismiss();

                              startActivity(new Intent(Edit_profile.this, homeActivity.class));
                              Toast.makeText(Edit_profile.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                              finish();
                          } else {
                              progressDialog.dismiss();
                              Toast.makeText(Edit_profile.this, "Error.", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });*/
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }


    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText,final EditText dobEditText)
    {


        DocumentReference UsersRef = mdb.collection("Users").document(mAuth.getCurrentUser().getEmail().toString());//FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot dataSnapshot) {
                String image = dataSnapshot.getString("Image");
                String name = dataSnapshot.getString("User_Name");
                String phone = dataSnapshot.getString("Number");
                String address = dataSnapshot.getString("Address");
                String dob = dataSnapshot.getString("Date_Of_Birth");

                if(image!=null){
                if(image.trim().length()>0){
                Picasso.get().load(image).into(profileImageView);}}
                fullNameEditText.setText(name);
                userPhoneEditText.setText(phone);
                addressEditText.setText(address);
                dobEditText.setText(dob);

            }
        });
    }
}