package com.example.ecgwars.views.Tests;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.TestActivityViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import pl.droidsonroids.gif.GifImageView;

public class ResultDialog extends DialogFragment {

    public static final String TAG = "ResultDialog";

    private TestActivityViewModel testActivityViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        testActivityViewModel = new ViewModelProvider(getActivity())
                .get(TestActivityViewModel.class);


        LayoutInflater inflater = getActivity().getLayoutInflater();


        View v = inflater.inflate(R.layout.result_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        GifImageView gifImageView = v.findViewById(R.id.gifImageView);
        TextView resultText = v.findViewById(R.id.resultText);
        TextView expText = v.findViewById(R.id.expTextView);
        TextView bottomText = v.findViewById(R.id.bottomResultText);

        int exp =50;
        if(testActivityViewModel.getUserAnswer().getValue()+1 ==
        testActivityViewModel.getTruAnswer().getValue()){
            Log.d("TestActivity","User answer "+testActivityViewModel.getUserAnswer().getValue());
            Log.d("TestActivity","Tru answer "+testActivityViewModel.getTruAnswer().getValue());

            gifImageView.setImageResource(R.drawable.confetti);
            testActivityViewModel.increaseScore();
            testActivityViewModel.addUserTestList();
            resultText.setText("You are awesome");
            expText.setText("+ "+ exp+ " XP");
            bottomText.setText("Congratulations on the correct answer");

        }else {
            gifImageView.setImageResource(R.drawable.rain);
            expText.setVisibility(View.GONE);
            resultText.setText("You are failed");
            bottomText.setText("Maybe next time");
        }



        return builder.show();
    }
}
