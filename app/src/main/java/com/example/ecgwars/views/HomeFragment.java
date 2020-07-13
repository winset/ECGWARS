package com.example.ecgwars.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.ProfileActivityViewModel;
import com.example.ecgwars.views.Tests.TestActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class HomeFragment extends Fragment {

    private CardView basicCardView;
    private CardView advancedCardView;
    private CardView profiCardView;
    private TextView nameTextView;

    private ProfileActivityViewModel profileActivityViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        profileActivityViewModel = new ViewModelProvider(this)
                .get(ProfileActivityViewModel.class);
        profileActivityViewModel.getUserInfo();

        basicCardView = view.findViewById(R.id.basicCardView);
        advancedCardView = view.findViewById(R.id.advancedCardView);
        profiCardView = view.findViewById(R.id.profiCardView);
        nameTextView = view.findViewById(R.id.homeName);
        setName();



        basicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TestActivity.class);
                intent.putExtra("LVL","basicLvL");// change
                startActivity(intent);
            }
        });

        advancedCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TestActivity.class);
                intent.putExtra("LVL","advancedLvL");// change
                startActivity(intent);
            }
        });
        profiCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TestActivity.class);
                intent.putExtra("LVL","profiLvL");// change
                startActivity(intent);
            }
        });

        return view;
    }

/*
* CHANGE THIS CODE
* */
    private void setName() {
        final Observer<String> profileFisrtNameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String name) {
                // Update the UI, in this case, a TextView.
                nameTextView.setText("Hi "+ name);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        profileActivityViewModel.getFirstName().observe(getViewLifecycleOwner(), profileFisrtNameObserver);
    }
}
