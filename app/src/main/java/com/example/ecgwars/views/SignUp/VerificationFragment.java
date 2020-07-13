package com.example.ecgwars.views.SignUp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.SignInActivityViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class VerificationFragment extends Fragment {

    public static final String TAG = "VerificationFragment";

    private ImageView backImageView;
    private RelativeLayout continueButton;
    private TextView resendCodeTextView;
    private PinView pinView;

    private onFragmentDataListener listener;

    private SignInActivityViewModel signInActivityViewModel;// Our view model

    public static VerificationFragment newInstance() {

        VerificationFragment fragment = new VerificationFragment();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verification_fragment, container, false);

        signInActivityViewModel = new ViewModelProvider(getActivity())
                .get(SignInActivityViewModel.class);

        backImageView = view.findViewById(R.id.backVerificationImageView);
        continueButton = view.findViewById(R.id.continueVerificationButton);
        resendCodeTextView = view.findViewById(R.id.resendVerificationCode);
        pinView = view.findViewById(R.id.pinView);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFragmentChange(SignUpFragment.TAG);
            }
        });


        resendCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signInActivityViewModel.number!=null){
                    signInActivityViewModel.resendCode(signInActivityViewModel.number,getActivity());
                }

              /*  if(getArguments().getString("number")!=null){
                    listener.resendCode(getArguments().getString("number"));
                }*/
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pinView.getItemCount()==6){
                    signInActivityViewModel.verifyCode(pinView.getText().toString());
                   // listener.verifyCode(pinView.getText().toString());
                    Log.d(TAG,"Verification code: "+pinView.getText().toString());
                }else {
                    Toast.makeText(getContext(),
                            "Enter full code "
                            , Toast.LENGTH_LONG).show();
                }

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
