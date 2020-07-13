package com.example.ecgwars.views.SignUp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.SignInActivityViewModel;
import com.hbb20.CountryCodePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class LogInFragment extends Fragment {

    public static final String TAG = "LogInFragment";
    public static final String GOOGLE_LOG_IN = "GoogleLogIn";
    public static final String FACEBOOK_LOG_IN = "FacebookLogIn";
    public static final String TWITTER_LOG_IN = "TwitterLogIn";

    private onFragmentDataListener listener;

    private Button googleLogInButton;
    private Button facebookLogInButton;
    private Button twitterLogInButton;
    private ImageView backImageView;
    private TextView sendCodeTextView; //временно пока не пределаю в кардвью и добавлю кнопку//уже не временно :)))
    private EditText phoneNumberEditText;

    String number;

    private CountryCodePicker codePicker;

    private SignInActivityViewModel signInActivityViewModel;// Our view model

    public static LogInFragment newInstance() {

        LogInFragment fragment = new LogInFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_in_fragment, container, false);

        signInActivityViewModel = new ViewModelProvider(getActivity())
                .get(SignInActivityViewModel.class);

        sendCodeTextView = view.findViewById(R.id.sendCodeLogInTextView);
        phoneNumberEditText = view.findViewById(R.id.mobilePhoneLogInEditText);
        backImageView = view.findViewById(R.id.logInBackImageView);
        googleLogInButton = view.findViewById(R.id.googleLogInButton);
        facebookLogInButton = view.findViewById(R.id.facebookLogInButton);
        twitterLogInButton = view.findViewById(R.id.twitterLogInButton);

        codePicker = (CountryCodePicker) view.findViewById(R.id.countryCodeLogIn);
        codePicker.registerCarrierNumberEditText(phoneNumberEditText);


        googleLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.logInSocial(GOOGLE_LOG_IN);
            }
        });

        facebookLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.logInSocial(FACEBOOK_LOG_IN);
            }
        });

        twitterLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.logInSocial(TWITTER_LOG_IN);
            }
        });


        sendCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(codePicker.isValidFullNumber()){
                    number = codePicker.getFullNumberWithPlus();
                        signInActivityViewModel.userPhoneNumber(number,getActivity());
                  //  listener.userPhoneNumber(number);
                    listener.onFragmentChange(VerificationFragment.TAG);
                }else {
                    Toast.makeText(getContext(),"Invalid phone number"
                            ,Toast.LENGTH_LONG).show();
                }
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFragmentChange(WelcomeFragment.TAG);
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
