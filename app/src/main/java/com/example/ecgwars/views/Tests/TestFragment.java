package com.example.ecgwars.views.Tests;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ecgwars.R;
import com.example.ecgwars.model.Offer;
import com.example.ecgwars.viewmodels.TestActivityViewModel;
import com.example.ecgwars.views.SignUp.onFragmentDataListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestFragment extends Fragment {

    public static final String TAG ="TestFragment";

    private TextView testIdTextView;
    private TextView textView;
    private ProgressBar textProgressBar;
    private ProgressBar imageProgressBar;
    private ImageView imageView;
    private RelativeLayout continueButton;
    private RelativeLayout skipButton;

    private RecyclerView offersRecyclerView;
    private OffersAdapter offersAdapter;

    private onFragmentChangeListener listener;

    private TestActivityViewModel testActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment, container, false);

        testActivityViewModel = new ViewModelProvider(getActivity())
                .get(TestActivityViewModel.class);

        textView = view.findViewById(R.id.testTextView);
        textProgressBar = view.findViewById(R.id.progressBarText);
        imageProgressBar = view.findViewById(R.id.progressBarImage);
        imageView = view.findViewById(R.id.testImageView);
        testIdTextView = view.findViewById(R.id.testIdTextView);
        continueButton = view.findViewById(R.id.continueTestButton);
        skipButton = view.findViewById(R.id.skipTestButton);

        setTestIdTextView();
        setImageView();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDialogFragment myDialog = new ImageDialogFragment();
                Bundle args = new Bundle();
                args.putString(ImageDialogFragment.TAG, "testImage");
                myDialog .setArguments(args);
                myDialog.show(getFragmentManager(), ImageDialogFragment.TAG);
                //listener.onFragmentChange(TestImageViewFragment.TAG);
            }
        });

        setTestText();

        offersRecyclerView = view.findViewById(R.id.offersRecyclerView);
        offersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        offersAdapter = new OffersAdapter();
        setAnswers();
        offersRecyclerView.setAdapter(offersAdapter);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testActivityViewModel.setUserAnswer(offersAdapter.getPosition());
                listener.onFragmentChange(TestAnswerFragment.TAG);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFragmentChange(TestFragment.TAG);
            }
        });

        return view;
    }




    private void setTestText() {
        final Observer<String> textObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String text) {
                // Update the UI, in this case, a TextView.
                textView.setText(text);
                textProgressBar.setVisibility(View.GONE);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        testActivityViewModel.getTestText().observe(getViewLifecycleOwner(), textObserver);
    }

    private void setImageView() {
        final Observer<String> imageObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String url) {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.home_image)
                        .error(R.drawable.home_image)
                        .into(imageView);
                imageProgressBar.setVisibility(View.GONE);
            }
        };
        testActivityViewModel.getImageUrl().observe(getViewLifecycleOwner(), imageObserver);
    }

    private void setAnswers() {
        LinkedHashSet answ = new LinkedHashSet();
        final Observer<ArrayList<String>> answerObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<String> answers) {
                for (int i = 0; i < answers.size(); i++) {
                    answ.add(new Offer(i+1+")",answers.get(i)));
                }
                offersAdapter.setItems(answ);
            }
        };
        testActivityViewModel.getAnswers().observe(getViewLifecycleOwner(), answerObserver);
    }


    private void setTestIdTextView(){
        final Observer<String> testIdObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String id) {
               testIdTextView.setText("Test № "+ id);
            }
        };
        testActivityViewModel.getTestId().observe(getViewLifecycleOwner(), testIdObserver);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentChangeListener) {
            listener = (onFragmentChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragment1DataListener");
        }
    }
}
