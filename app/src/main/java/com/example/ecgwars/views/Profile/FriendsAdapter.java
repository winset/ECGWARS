package com.example.ecgwars.views.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecgwars.CropCircleTransformation;
import com.example.ecgwars.R;
import com.example.ecgwars.model.Top;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<Top> friendsList = new ArrayList<>();


    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_item_view, parent, false);


        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        holder.bind(friendsList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder {

        private TextView positionTextView;
        private TextView firstNameTextView;
        private TextView secondNameTextView;
        private ImageView avatarImageView;
        private TextView scoreTextView;


        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.positionTop);
            firstNameTextView = itemView.findViewById(R.id.firstNameScore);
            secondNameTextView = itemView.findViewById(R.id.lastNameScore);
            avatarImageView  = itemView.findViewById(R.id.userScoreProfileImage);
            scoreTextView = itemView.findViewById(R.id.scoreTopTextView);
        }

        public void bind(Top friends) {
            positionTextView.setText(friends.getPlace());
            firstNameTextView.setText(friends.getFirstName());
            secondNameTextView.setText(friends.getLastName());
            scoreTextView.setText(friends.getScore()+" XP");
            Picasso.get()
                    .load(friends.getImageUrl())
                    .resize(150, 150)
                    .centerCrop()
                    .transform(new CropCircleTransformation())
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .error(R.drawable.ic_person_black_24dp)
                    .into(avatarImageView);

        }
    }
    public void setItems(Collection<Top> friends) {
        friendsList.addAll(friends);
        notifyDataSetChanged();
    }
}
