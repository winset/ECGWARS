package com.example.ecgwars.views.Tests;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.TestActivityViewModel;
import com.example.ecgwars.views.HomeActivity;


import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class TestActivity extends AppCompatActivity implements onFragmentChangeListener {

    private TestFragment testFragment;





    private TestActivityViewModel testActivityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testActivityViewModel = new ViewModelProvider(this)
                .get(TestActivityViewModel.class);

        testActivityViewModel.getExcludeTest();


        testFragment = new TestFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.containerTests, testFragment);
        fragmentTransaction.commit();

        final Observer<Boolean> passAllObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean passAll) {
                if(passAll){
                    Toast.makeText(getApplicationContext(),"You pass all tests",Toast.LENGTH_LONG).show();
                    Intent home = new Intent(TestActivity.this, HomeActivity.class);
                    startActivity(home);
                }
            }
        };
        testActivityViewModel.getPassAll().observe(this,passAllObserver);
    }

    @Override
    public void onFragmentChange(String tag) {
        Log.d("TestActivity", "TAG == " + tag);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (tag) {

            case TestAnswerFragment.TAG:
                TestAnswerFragment testAnswerFragment = new TestAnswerFragment();
                fragmentTransaction.replace(R.id.containerTests,testAnswerFragment);
                fragmentTransaction.commit();
                break;
            case TestFragment.TAG:
                testActivityViewModel.clearAnswers();
                testActivityViewModel.getExcludeTest();
                testFragment = new TestFragment();
                fragmentTransaction.replace(R.id.containerTests,testFragment);
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }
}
