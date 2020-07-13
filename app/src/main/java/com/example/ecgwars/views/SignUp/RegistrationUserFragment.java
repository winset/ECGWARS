package com.example.ecgwars.views.SignUp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.example.ecgwars.CropCircleTransformation;
import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.SignInActivityViewModel;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class RegistrationUserFragment extends Fragment {

    public static final String TAG = "RegistrationUserFragment";

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private ImageButton avatarImage;
    private RelativeLayout continueButton;
    private ImageView backImageView;

    private SignInActivityViewModel signInActivityViewModel;// Our view model

    private onFragmentDataListener listener;

    public static RegistrationUserFragment instance;

    public static RegistrationUserFragment newInstance() {
        if(instance==null){
         instance = new RegistrationUserFragment();
        }

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.registration_user_fragment, container, false);

        signInActivityViewModel = new ViewModelProvider(getActivity())
                .get(SignInActivityViewModel.class);

        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        avatarImage = view.findViewById(R.id.userAvatarRegistrationImageButton);
        continueButton = view.findViewById(R.id.continueRegistrationButton);
        backImageView = view.findViewById(R.id.backRegistrationImageView);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFragmentChange(WelcomeFragment.TAG);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstNameEditText.getText().toString().length() > 0 &&
                        lastNameEditText.getText().toString().length() > 0) {
                    signInActivityViewModel.setFistName(firstNameEditText.getText().toString());
                    signInActivityViewModel.setLastName(lastNameEditText.getText().toString());
                    Log.d("View123","First name: "+ signInActivityViewModel.getFirstName().getValue()+", last name: "+signInActivityViewModel.getLastName().getValue());
                    listener.onFragmentChange(SignUpFragment.TAG);
                }

            }
        });

        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInActivityViewModel.setUserAvatarImage(getActivity());
              //  listener.setUserAvatarImage();
            }
        });

        return view;
    }

    public void setAvatarImage(Uri uri) {
        signInActivityViewModel.setAvatarImage(uri);
        Picasso.get().load(uri)
                .resize(150, 150)
                .centerCrop()
                .transform(new CropCircleTransformation())
                .into(avatarImage);
        //avatarImage.setImageURI(uri);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentDataListener) {
            listener = (onFragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragment1DataListener");
        }
    }
}
