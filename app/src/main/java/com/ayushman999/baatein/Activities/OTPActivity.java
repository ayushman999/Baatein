package com.ayushman999.baatein.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ayushman999.baatein.R;
import com.ayushman999.baatein.databinding.ActivityOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    ActivityOtpactivityBinding binding;
    FirebaseAuth auth;
    String verificationID;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Sending OTP...");
        dialog.setCancelable(false);
        dialog.show();
        getSupportActionBar().hide();
        binding=ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        Intent data=getIntent();
        String phoneNumber=data.getStringExtra("Phone");
        binding.textviewOtpNum.setText("Verify the OTP sent to your number:\n"+phoneNumber);


        PhoneAuthOptions options= PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(OTPActivity.this, "You are in!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OTPActivity.this, "not getting it", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyID, forceResendingToken);
                        verificationID=verifyID;
                        dialog.dismiss();
                    }
                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
        binding.otpContinue.setOnClickListener(v->{
            String otp=binding.otpView.getText().toString();
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationID,otp);
            auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        startActivity(new Intent(getApplicationContext(),SetupProfileActivity.class));
                        finishAffinity();
                        Toast.makeText(OTPActivity.this, "LogIn", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(OTPActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}