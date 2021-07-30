package com.ayushman999.baatein.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ayushman999.baatein.R;
import com.ayushman999.baatein.Models.User;
import com.ayushman999.baatein.databinding.ActivitySetupProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupProfileActivity extends AppCompatActivity {
    ActivitySetupProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        binding=ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        dialog=new ProgressDialog(this);
        dialog.setMessage("Setting up your profile...");
        dialog.setCancelable(false);

        binding.setupImage.setOnClickListener(v->{
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,45);
        });

        binding.continueSetup.setOnClickListener(v->{

            String name=binding.nameBox.getText().toString().trim();
            if(name.isEmpty())
            {
                binding.nameBox.setError("Please type a name");
                return;
            }
            dialog.show();
            if(selectedImage!=null)
            {
                StorageReference reference=storage.getReference().child("profiles").child(auth.getUid());
                reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl=uri.toString();
                                    String uid=auth.getUid();
                                    String phoneNum=auth.getCurrentUser().getPhoneNumber();
                                    String name=binding.nameBox.getText().toString().trim();
                                    User user=new User(uid,name,phoneNum,imageUrl);
                                    database.getReference().child("Users")
                                            .child(uid)
                                            .setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                    dialog.dismiss();
                                                    finish();

                                                }
                                            });
                                }
                            });
                        }

                    }
                });
            }
            else
            {
                String uid=auth.getUid();
                String phoneNum=auth.getCurrentUser().getPhoneNumber();
                User user=new User(uid,name,phoneNum,"no image");
                database.getReference().child("Users")
                        .child(uid)
                        .setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                dialog.dismiss();
                                finish();

                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
        {
            if(data.getData()!=null)
            {
                binding.setupImage.setImageURI(data.getData());
                selectedImage=data.getData();
            }
        }
    }
}