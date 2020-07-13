package com.example.ecgwars.views.Tests;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.TestActivityViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class TestAnswerFragment extends Fragment {

    public static final String TAG  = "TestAnswerFragment";

    private TextView answerText;
    private ProgressBar textProgressBar;
    private RelativeLayout okTestButton;
    private ListView listImages;

    private onFragmentChangeListener listener;

    private TestActivityViewModel testActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_answer_fragment, container, false);

        ResultDialog resultDialog = new ResultDialog();
        resultDialog.show(getFragmentManager(),ResultDialog.TAG);


        testActivityViewModel = new ViewModelProvider(getActivity())
                .get(TestActivityViewModel.class);

        textProgressBar = view.findViewById(R.id.progressBarAnswerText);
        answerText = view.findViewById(R.id.testAnswerTextView);
        okTestButton = view.findViewById(R.id.okTestButton);

        listImages = view.findViewById(R.id.answerImages);
        setText();

        final Observer<ArrayList<String>> imagesObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<String> URLs) {
                // Update the UI, in this case, a TextView.
                ImagesArrayAdapter adapter = new ImagesArrayAdapter(getContext(),URLs );
                listImages.setAdapter(adapter);
                setListViewHeightBasedOnChildren(listImages);
                textProgressBar.setVisibility(View.GONE);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        testActivityViewModel.getAnswImagesUrl().observe(getViewLifecycleOwner(), imagesObserver);

        okTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFragmentChange(TestFragment.TAG);
            }
        });
        listImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ImageDialogFragment myDialog = new ImageDialogFragment();
                Bundle args = new Bundle();
                args.putString(ImageDialogFragment.TAG, "answImages");
                args.putInt("position",position);
                myDialog .setArguments(args);
                myDialog.show(getFragmentManager(), ImageDialogFragment.TAG);
            }
        });

        return view;
    }

    private void setText(){
        final Observer<String> textObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String text) {
                answerText.setText(text);
                textProgressBar.setVisibility(View.GONE);
            }
        };

        testActivityViewModel.getAnswText().observe(getViewLifecycleOwner(), textObserver);
    }



    public class ImagesArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> url;

        public ImagesArrayAdapter(Context context, ArrayList<String> url) {
            super(context, -1, url);
            this.context = context;
            this.url = url;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View imagesView = inflater.inflate(R.layout.image_item_view, parent, false);
            ImageView imageView = imagesView.findViewById(R.id.answerImageView);

            Picasso.get()
                    .load(url.get(position))
                    .placeholder(R.drawable.home_image)
                    .error(R.drawable.home_image)
                    .into(imageView);

            return imagesView;
        }
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
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
