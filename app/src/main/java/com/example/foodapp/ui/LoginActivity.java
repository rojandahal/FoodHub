package com.example.foodapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodapp.MainActivity;
import com.example.foodapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login activity";
    private Button go;
    private Button register;
    private TextInputLayout phone;
    private TextInputLayout pass;
    String phoneNum;
    String password;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        go = findViewById(R.id.loginButton);
        register = findViewById(R.id.signupButton);
        loading = findViewById(R.id.LoginProgressBar);

        register.setOnClickListener(this);
        go.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                phoneNum = "+977" + phone.getEditText().getText().toString();
                password = pass.getEditText().getText().toString();
                if(isEmpty()){
                    break;
                }
                go.setEnabled(false);
                loading.isIndeterminate();
                loading.setVisibility(View.VISIBLE);
                //Log.d(TAG, "onCreate: " + phoneNum + password);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        if (dataSnapshot.hasChild(phoneNum)) {
                            if (dataSnapshot.child(phoneNum).child("password").getValue().equals(password)) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Log.d(TAG, "onDataChange: Login Successful");
                            } else {
                                go.setEnabled(true);
                                loading.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            go.setEnabled(true);
                            loading.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "User ID not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                break;

            case R.id.signupButton:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    private boolean isEmpty() {
        if(phoneNum.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter login details!",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}