package com.example.foodapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.MainActivity;
import com.example.foodapp.R;
import com.example.foodapp.model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "REGISTER ACTIVITY";

    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout phoneNumber;
    private TextInputLayout password;
    private TextView errorMessage;
    private ProgressBar loadingBar;
    private Button signInPrompt;
    private Button signUp;
    String first_name;
    String last_name;
    String pass;
    String phone2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        signInPrompt = findViewById(R.id.signInPrompt);
        signUp = findViewById(R.id.signupButton);
        errorMessage = findViewById(R.id.errorText);
        loadingBar = findViewById(R.id.registerProgressBar);

        signUp.setOnClickListener(this);
        signInPrompt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        errorMessage.setVisibility(View.INVISIBLE);

        switch (v.getId()) {
            case R.id.signInPrompt:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
            case R.id.signupButton:
                signUp.setEnabled(false);
                loadingBar.setVisibility(View.VISIBLE);
                checkEmptyFields();
                break;
        }
    }

    private void checkEmptyFields() {
        first_name = firstName.getEditText().getText().toString();
        last_name = lastName.getEditText().getText().toString();
        pass = password.getEditText().getText().toString();
        String phone = phoneNumber.getEditText().getText().toString();
        Log.d(TAG, "verifyPhone: " + phone);

        if (first_name.isEmpty() || last_name.isEmpty() ||
                pass.isEmpty() || phone.isEmpty()) {
            errorMessage.setText(R.string.error_message);
            signUp.setEnabled(true);
            errorMessage.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (phone.length() != 10) {
            errorMessage.setText(R.string.valid_phone);
            errorMessage.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
            signUp.setEnabled(true);
        }
        phone2 = "+977" + phone;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.hasChild(phone2)) {
                    signUp.setEnabled(true);
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(R.string.phone_register);
                    loadingBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "onDataChange: " + dataSnapshot.child(phone2).getKey());
                }else {
                    verifyPhone(phone2);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void verifyPhone(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);

            Toast.makeText(RegisterActivity.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull final String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(RegisterActivity.this, VerifyOtp.class);
                    intent.putExtra("first_name", first_name);
                    intent.putExtra("last_name", last_name);
                    intent.putExtra("password", pass);
                    intent.putExtra("phone", phone2);
                    intent.putExtra("verification",verificationId);
                    startActivity(intent);
                }
            }, 1000);
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            UserDetails userDetails = new UserDetails(first_name, last_name, phone2, pass);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            myRef.child(phone2).setValue(userDetails);

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.putExtra("first_name", first_name);
                            intent.putExtra("last_name", last_name);
                            intent.putExtra("password", pass);
                            intent.putExtra("phone", phone2);
                            startActivity(intent);

                            Log.d(TAG, "onComplete: " + userDetails.getFirst_name() + userDetails.getLast_name());


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                errorMessage.setText(R.string.verificationCode_error);
                                errorMessage.setVisibility(View.VISIBLE);
                                loadingBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
    }
}