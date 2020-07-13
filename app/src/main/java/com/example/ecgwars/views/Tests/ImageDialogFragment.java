package com.example.ecgwars.views.Tests;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.TestActivityViewModel;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class ImageDialogFragment extends DialogFragment {
    public static final String TAG = "ImageDialogFragment";

    private ImageView ivClose;
    private ImageView ivNext;
    private ImageView ivPrev;
    private PhotoView ivImage;

    private String mode;
    private int position;
    private TestActivityViewModel testActivityViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Bundle arg = this.getArguments();
        mode = arg.getString(TAG);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_dialog_fragment, container, false);

        testActivityViewModel = new ViewModelProvider(getActivity())
                .get(TestActivityViewModel.class);

        ivClose = view.findViewById(R.id.iv_close);
        ivNext = view.findViewById(R.id.iv_arrow_right);
        ivPrev = view.findViewById(R.id.iv_arrow_left);
        ivImage = view.findViewById(R.id.photo_view);

        ivImage.setMaximumScale(10);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        switch (mode) {
            case "testImage":
                String url = testActivityViewModel.getImageUrl().getValue();

                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.home_image)
                        .error(R.drawable.home_image)
                        .into(ivImage);

                ivNext.setVisibility(View.GONE);
                ivPrev.setVisibility(View.GONE);
                break;

            case "answImages":
                Bundle arg = this.getArguments();
                position = arg.getInt("position");
                String url1 = testActivityViewModel.getAnswImagesUrl().getValue().get(position);
                int size = testActivityViewModel.getAnswImagesUrl().getValue().size();
                if (size == 1) {
                    ivNext.setVisibility(View.GONE);
                    ivPrev.setVisibility(View.GONE);
                }
                if (position == 0 && size > 1) {
                    ivPrev.setVisibility(View.GONE);
                }
                if (position == size - 1) {
                    ivNext.setVisibility(View.GONE);
                }
                ivNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position++ < size -1) {
                        //    position++;
                            String url1 = testActivityViewModel.getAnswImagesUrl().getValue().get(position);
                            Picasso.get()
                                    .load(url1)
                                    .placeholder(R.drawable.home_image)
                                    .error(R.drawable.home_image)
                                    .into(ivImage);
                            if (position == size - 1) {
                                ivNext.setVisibility(View.GONE);
                            }
                            ivPrev.setVisibility(View.VISIBLE);
                        }


                    }
                });
                ivPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position-- > 0) {
                          //  position--;
                            String url1 = testActivityViewModel.getAnswImagesUrl().getValue().get(position);
                            Picasso.get()
                                    .load(url1)
                                    .placeholder(R.drawable.home_image)
                                    .error(R.drawable.home_image)
                                    .into(ivImage);
                            if (position == 0 ) {
                                ivPrev.setVisibility(View.GONE);
                            }
                            ivNext.setVisibility(View.VISIBLE);
                        }

                    }

                });
                Picasso.get()
                        .load(url1)
                        .placeholder(R.drawable.home_image)
                        .error(R.drawable.home_image)
                        .into(ivImage);
                break;
        }


        return view;
    }


}
