package com.ayushman999.baatein.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ayushman999.baatein.R;
import com.ayushman999.baatein.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberActivity extends AppCompatActivity {
    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        getSupportActionBar().hide();
        binding=ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth= FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        binding.continueNumber.setOnClickListener(v->{
            Intent transfer=new Intent(PhoneNumberActivity.this,OTPActivity.class);
            transfer.putExtra("Phone",binding.enterNumber.getText().toString().trim());
            startActivity(transfer);
        });
    }
}