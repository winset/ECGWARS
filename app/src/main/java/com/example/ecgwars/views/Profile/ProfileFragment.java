package com.example.ecgwars.views.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecgwars.CropCircleTransformation;
import com.example.ecgwars.R;
import com.example.ecgwars.viewmodels.ProfileActivityViewModel;
import com.example.ecgwars.views.SettingActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileFragment extends Fragment {

    private ImageView settingsImageView;
    private ImageButton profileImageButton;
    private TextView nameTextView;
    private TextView experienceTextView;
    private TextView badgesTextView;
    private TextView friendsTextView;
    private TextView scoresTextView;

    private LinearLayout searchLayout;
    private EditText searchText;
    private ImageButton searchButton;
    private Boolean searchButtonChange = true;

    private RecyclerView recyclerView;
    private ScoreAdapter scoreAdapter;
    private FriendsAdapter friendsAdapter;

    private ArrayList<String> add = new ArrayList<>();

    private ProfileActivityViewModel profileActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);


        profileActivityViewModel = new ViewModelProvider(getActivity())
                .get(ProfileActivityViewModel.class);
        profileActivityViewModel.getScores();
        profileActivityViewModel.getFriendsId();

        settingsImageView = view.findViewById(R.id.settingsProfileImageView);
        profileImageButton = view.findViewById(R.id.userAvatarProfileImageButton);
        nameTextView = view.findViewById(R.id.profileName);
        experienceTextView = view.findViewById(R.id.experienceProfile);
        badgesTextView = view.findViewById(R.id.badgesTextView);
        friendsTextView = view.findViewById(R.id.friendsTextView);
        scoresTextView = view.findViewById(R.id.scoresTextView);
        searchLayout = view.findViewById(R.id.search_friends);
        searchText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);

        recyclerView = view.findViewById(R.id.recyclerViewProfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        scoreAdapter = new ScoreAdapter();
        friendsAdapter = new FriendsAdapter();

        setTop();
        setFriends();

        searchLayout.setVisibility(View.GONE);
        badgesTextView.setTextColor(getResources().getColor(R.color.blackText));
        setProfileImage();
        setName();
        setScore();
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(getActivity(), SettingActivity.class);
                startActivity(settings);
            }
        });

        badgesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgesTextView.setTextColor(getResources().getColor(R.color.blackText));
                friendsTextView.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
                scoresTextView.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
                searchLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                friendsTextView.setClickable(true);
            }
        });

        friendsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgesTextView.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
                friendsTextView.setTextColor(getResources().getColor(R.color.blackText));
                scoresTextView.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
                searchLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                friendsTextView.setClickable(false);
                recyclerView.setAdapter(friendsAdapter);

            }
        });

        scoresTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgesTextView.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
                friendsTextView.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
                scoresTextView.setTextColor(getResources().getColor(R.color.blackText));
                searchLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(scoreAdapter);
                friendsTextView.setClickable(true);

            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchButtonChange) {
                    profileActivityViewModel.getFriendsId();
                    LinkedHashSet empty = new LinkedHashSet();
                    empty.clear();
                    SearchAdapter searchAdapter = new SearchAdapter(new ClickHandler() {
                        @Override
                        public void onMyButtonClicked(final String id) {
                            if (id != null && !add.contains(id)) {
                                add.add(id);
                                profileActivityViewModel.addFriend(id);
                            } else {
                                Log.d("Add Friend", "Friend already added !!!");
                            }
                        }
                    });

                    profileActivityViewModel.search.setValue(empty);
                    profileActivityViewModel.friends.setValue(empty);
                    profileActivityViewModel.searchFriends(searchText.getText().toString());
                    setSearch(searchAdapter);

                    recyclerView.setAdapter(searchAdapter);
                    searchButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
                    searchButtonChange = false;
                } else {
                    LinkedHashSet empty = new LinkedHashSet();
                    empty.clear();
                    profileActivityViewModel.friends.setValue(empty);
                    profileActivityViewModel.frinedsIdClear();
                    friendsAdapter = new FriendsAdapter();
                    profileActivityViewModel.getFriendsId();
                    searchButtonChange = true;
                    searchButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_white_24dp));
                    recyclerView.setAdapter(friendsAdapter);
                }

            }
        });

        return view;
    }

    private void setProfileImage() {
        // Create the observer which updates the UI.
        final Observer<String> profileImageObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String url) {
                Picasso.get()
                        .load(url)
                        .resize(150, 150)
                        .centerCrop()
                        .transform(new CropCircleTransformation())
                        .placeholder(R.drawable.ic_person_black_24dp)
                        .error(R.drawable.ic_person_black_24dp)
                        .into(profileImageButton);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        profileActivityViewModel.getAvatarUrl().observe(getViewLifecycleOwner(), profileImageObserver);
    }

    private void setName() {
        final Observer<String> profileFisrtNameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String name) {
                // Update the UI, in this case, a TextView.
                nameTextView.setText(profileActivityViewModel.getFirstName().getValue() +
                        " " + profileActivityViewModel.getSecondName().getValue());
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        profileActivityViewModel.getFirstName().observe(getViewLifecycleOwner(), profileFisrtNameObserver);
        profileActivityViewModel.getSecondName().observe(getViewLifecycleOwner(), profileFisrtNameObserver);
    }

    private void setScore() {
        final Observer<String> scoreObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String score) {
                // Update the UI, in this case, a TextView.
                experienceTextView.setText(score + " XP");
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        profileActivityViewModel.getScore().observe(getViewLifecycleOwner(), scoreObserver);
    }

    private void setTop() {
        final Observer<LinkedHashSet> topObserver = new Observer<LinkedHashSet>() {
            @Override
            public void onChanged(@Nullable final LinkedHashSet top) {
                scoreAdapter.setItems(top);
            }
        };
        profileActivityViewModel.getTopScores().observe(getViewLifecycleOwner(), topObserver);
    }

    private void setFriends() {
        final Observer<LinkedHashSet> friendsObserver = new Observer<LinkedHashSet>() {
            @Override
            public void onChanged(@Nullable final LinkedHashSet friends) {
                friendsAdapter.setItems(friends);
            }
        };
        profileActivityViewModel.getFriends().observe(getViewLifecycleOwner(), friendsObserver);
    }

    private void setSearch(SearchAdapter searchAdapter) {
        final Observer<LinkedHashSet> searchObserver = new Observer<LinkedHashSet>() {
            @Override
            public void onChanged(@Nullable final LinkedHashSet search) {
                searchAdapter.setItems(search);
            }
        };
        profileActivityViewModel.getSearch().observe(getViewLifecycleOwner(), searchObserver);
    }


}

