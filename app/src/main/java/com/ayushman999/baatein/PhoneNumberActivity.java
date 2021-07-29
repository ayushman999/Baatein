package com.ayushman999.baatein;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ayushman999.baatein.databinding.ActivityPhoneNumberBinding;

public class PhoneNumberActivity extends AppCompatActivity {
    ActivityPhoneNumberBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        getSupportActionBar().hide();
        binding=ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.continueNumber.setOnClickListener(v->{
            Intent transfer=new Intent(PhoneNumberActivity.this,OTPActivity.class);
            transfer.putExtra("Phone",binding.enterNumber.getText().toString().trim());
            startActivity(transfer);
        });
    }
}