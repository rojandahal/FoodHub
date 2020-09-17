package com.example.foodapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.MainActivity;
import com.example.foodapp.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(mUser!=null){
            //Toast.makeText(this,"User is Signed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }else {
            //Toast.makeText(this, "User is NOTSigned", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
