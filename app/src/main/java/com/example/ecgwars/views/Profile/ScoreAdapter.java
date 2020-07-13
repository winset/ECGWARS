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

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoresViewHolder> {

    private List<Top> topList = new ArrayList<>();




    @NonNull
    @Override
    public ScoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_item_view, parent, false);


        return new ScoresViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ScoresViewHolder holder, int position) {
        holder.bind(topList.get(position));

    }


    @Override
    public int getItemCount() {
        return topList.size();
    }

    /*public int getPosition() {
        return pos;
    }

    private void setPosition(int pos) {
        this.pos = pos;
    }*/

    public class ScoresViewHolder extends RecyclerView.ViewHolder {

        private TextView positionTextView;
        private TextView firstNameTextView;
        private TextView secondNameTextView;
        private ImageView avatarImageView;
        private TextView scoreTextView;


        public ScoresViewHolder(@NonNull View itemView) {
            super(itemView);

            positionTextView = itemView.findViewById(R.id.positionTop);
            firstNameTextView = itemView.findViewById(R.id.firstNameScore);
            secondNameTextView = itemView.findViewById(R.id.lastNameScore);
            avatarImageView  = itemView.findViewById(R.id.userScoreProfileImage);
            scoreTextView = itemView.findViewById(R.id.scoreTopTextView);
        }

        public void bind(Top top) {
            positionTextView.setText(top.getPlace());
            firstNameTextView.setText(top.getFirstName());
            secondNameTextView.setText(top.getLastName());
            scoreTextView.setText(top.getScore()+" XP");
            Picasso.get()
                    .load(top.getImageUrl())
                    .resize(150, 150)
                    .centerCrop()
                    .transform(new CropCircleTransformation())
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .error(R.drawable.ic_person_black_24dp)
                    .into(avatarImageView);

        }
    }

    public void setItems(Collection<Top> top) {
        topList.addAll(top);
        notifyDataSetChanged();
    }

}
