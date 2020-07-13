package com.example.ecgwars.views.Tests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.TestActivityViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class TestImageViewFragment extends Fragment {

    public static final String TAG = "TestImageViewFragment";


    private WebView webView;
    private TestActivityViewModel testActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_image_view_fragment, container, false);

        testActivityViewModel = new ViewModelProvider(getActivity())
                .get(TestActivityViewModel.class);


        webView = view.findViewById(R.id.webView1);
        webView.setInitialScale((int) (100*webView.getScaleY()));
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setPadding(0, 0, 0, 0);
        webView.loadUrl(testActivityViewModel.getImageUrl().getValue());

        return view;
    }
}
