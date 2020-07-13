package com.example.ecgwars.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecgwars.R;
import com.example.ecgwars.views.SignUp.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    public static final String TAG = "SettingActivity";

    private Button signOutButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        // Log.d(TAG,"Current user UID: "+mAuth.getCurrentUser().getUid());


        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mAuth.signOut();
                    Toast.makeText(getApplicationContext(), "User Sign out!",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingActivity.this, SignInActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "onClick: Exception " + e.getMessage(), e);
                }
            }
        });
    }
}
