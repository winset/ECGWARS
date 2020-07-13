package com.example.ecgwars.views.SignUp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.SignInActivityViewModel;
import com.hbb20.CountryCodePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SignUpFragment extends Fragment {

    public static final String TAG = "SignUpFragment";

    private RelativeLayout continueButton;
    private EditText phoneNumberEditText;
    private ImageView backImageView;

    private CountryCodePicker codePicker;

    private onFragmentDataListener listener;

    private SignInActivityViewModel signInActivityViewModel;// Our view model

    private String number;


    public static SignUpFragment newInstance() {

        SignUpFragment fragment = new SignUpFragment();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);


        signInActivityViewModel = new ViewModelProvider(getActivity())
                .get(SignInActivityViewModel.class);

        continueButton = view.findViewById(R.id.continueSighUpButton);
        phoneNumberEditText = view.findViewById(R.id.mobilePhoneEditText);
        backImageView = view.findViewById(R.id.backSignUpImageView);

        codePicker = (CountryCodePicker) view.findViewById(R.id.countryCode);
        codePicker.registerCarrierNumberEditText(phoneNumberEditText);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codePicker.isValidFullNumber()) {
                    number = codePicker.getFullNumberWithPlus();
                    signInActivityViewModel.userPhoneNumber(number, getActivity());
                    listener.onFragmentChange(VerificationFragment.TAG);
                } else {
                    Toast.makeText(getContext(), "Invalid phone number"
                            , Toast.LENGTH_LONG).show();
                }

            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFragmentChange(RegistrationUserFragment.TAG);
            }
        });

        return view;
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
