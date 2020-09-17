package com.example.foodapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.MainActivity;
import com.example.foodapp.R;
import com.example.foodapp.model.UserDetails;
import com.example.foodapp.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyOtp extends AppCompatActivity {

    private static final String TAG = "Verify OTP";
    private EditText otpNumber;
    private Button verifyBtn;
    private ProgressBar loadingBar;
    TextView errorMessage;
    String first_name ;
    String last_name ;
    String pass;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        loadingBar = findViewById(R.id.OtpProgressBar);
        otpNumber = findViewById(R.id.otpNumber);
        verifyBtn = findViewById(R.id.verifyOTP);
        errorMessage = findViewById(R.id.otpError);

        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        pass = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");
        final String verification = getIntent().getStringExtra("verification");

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String otp = otpNumber.getText().toString();
                if (otp.isEmpty()) {
                    //Error Message is shown when the otp is empty
                    errorMessage.setText(R.string.otp_error_msg);
                    errorMessage.setVisibility(View.VISIBLE);
                }else
                verifyOtp(verification);
            }
        });
    }

    private void verifyOtp(String verificationId) {
        final String otp = otpNumber.getText().toString();

        if (otp.isEmpty()) {
            //Error Message is shown when the otp is empty
            errorMessage.setText(R.string.otp_error_msg);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            //When the otp is filled then it will check with the credentials or Otp passed as sms
            loadingBar.setVisibility(View.VISIBLE);
            verifyBtn.setEnabled(false);

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            UserDetails userDetails = new UserDetails(first_name,last_name,phone,pass);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            myRef.child(phone).setValue(userDetails);

                            Intent intent = new Intent(VerifyOtp.this, MainActivity.class);
                            intent.putExtra("first_name", first_name);
                            intent.putExtra("last_name", last_name);
                            intent.putExtra("password", pass);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                            Toast.makeText(VerifyOtp.this,"Registration Successful!",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + userDetails.getFirst_name() + userDetails.getLast_name());

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                errorMessage.setText(R.string.verificationCode_error);
                                errorMessage.setVisibility(View.VISIBLE);
                                verifyBtn.setEnabled(true);
                                loadingBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            verifyBtn.setEnabled(false);
            Toast.makeText(this,"Registration Successful!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeFragment.class));
        }
    }
}