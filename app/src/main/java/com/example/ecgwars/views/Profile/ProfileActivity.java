package com.example.ecgwars.views.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.ProfileActivityViewModel;
import com.example.ecgwars.views.HomeActivity;
import com.example.ecgwars.views.News.NewsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class ProfileActivity extends AppCompatActivity {

    private ProfileActivityViewModel profileActivityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        profileActivityViewModel = new ViewModelProvider(this)
                .get(ProfileActivityViewModel.class);
        profileActivityViewModel.getUserInfo();

        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.containerProfile, profileFragment);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.navigation_profile);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent a = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigation_news:
                        Intent b = new Intent(ProfileActivity.this, NewsActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigation_profile:
                        break;
                }
                return false;
            }
        });
    }
}
