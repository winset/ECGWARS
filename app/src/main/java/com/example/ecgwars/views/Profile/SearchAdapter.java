package com.example.ecgwars.views.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecgwars.CropCircleTransformation;
import com.example.ecgwars.R;
import com.example.ecgwars.model.Top;
import com.example.ecgwars.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>  {

    private final ClickHandler clickHandler;

    public SearchAdapter(final ClickHandler clickHandler) {
        super();
        this.clickHandler = clickHandler;
    }
    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private List<User> friendsList = new ArrayList<>();

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_view, parent, false);


        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(friendsList.get(position));
        holder.clickHandler = this.clickHandler;
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public String getId(){
        return id;
    }
    public void clear(){
        friendsList.clear();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        private ClickHandler clickHandler;

        private TextView firstNameTextView;
        private TextView secondNameTextView;
        private ImageView avatarImageView;
        private ImageButton addFriendButton;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            firstNameTextView = itemView.findViewById(R.id.firstNameSearch);
            secondNameTextView = itemView.findViewById(R.id.lastNameSearch);
            avatarImageView  = itemView.findViewById(R.id.userSearchProfileImage);
            addFriendButton = itemView.findViewById(R.id.addFriendButton);
        }
        public void bind(User user) {

            firstNameTextView.setText(user.getFirstName());
            secondNameTextView.setText(user.getLastName());
            Picasso.get()
                    .load(user.getAvatar())
                    .resize(150, 150)
                    .centerCrop()
                    .transform(new CropCircleTransformation())
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .error(R.drawable.ic_person_black_24dp)
                    .into(avatarImageView);
            addFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickHandler != null) {
                        clickHandler.onMyButtonClicked(user.getId());
                    }
                   // setId();
                }
            });

        }
    }
    public void setItems(Collection<User> friends) {
        friendsList.addAll(friends);
        notifyDataSetChanged();
    }
}
