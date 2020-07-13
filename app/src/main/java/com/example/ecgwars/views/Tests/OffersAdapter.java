package com.example.ecgwars.views.Tests;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecgwars.R;
import com.example.ecgwars.model.Offer;
import com.example.ecgwars.viewmodels.TestActivityViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {

    private List<Offer> offersList = new ArrayList<>();


    private int checkedPosition = -1;
    private int pos;


    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_view, parent, false);


        return new OffersViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.OffersViewHolder holder, int position) {
        holder.bind(offersList.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedPosition = position;
                notifyDataSetChanged();
            }
        });
        if (checkedPosition == position) {
            holder.linearLayout.setBackgroundResource(R.drawable.offer_selected_background);
            holder.offerTextView.setTextColor(holder.offerTextView.getContext()
                    .getResources().getColor(android.R.color.white));
            holder.numberTextView.setTextColor(holder.numberTextView.getContext()
                    .getResources().getColor(android.R.color.white));
            setPosition(checkedPosition);

        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.offer_background);
            holder.offerTextView.setTextColor(holder.offerTextView.getContext()
                    .getResources().getColor(android.R.color.darker_gray));
            holder.numberTextView.setTextColor(holder.numberTextView.getContext()
                    .getResources().getColor(android.R.color.darker_gray));
        }

    }


    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public int getPosition() {
        return pos;
    }

    private void setPosition(int pos) {
        this.pos = pos;
    }

    public class OffersViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTextView;
        private TextView offerTextView;
        private LinearLayout linearLayout;

        public OffersViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTextView = itemView.findViewById(R.id.offerNumber);
            offerTextView = itemView.findViewById(R.id.offerText);
            linearLayout = itemView.findViewById(R.id.offer_item);
        }

        public void bind(Offer offer) {
            numberTextView.setText(offer.getNumber());
            offerTextView.setText(offer.getText());
        }
    }

    public void setItems(Collection<Offer> offers) {
        offersList.addAll(offers);
        notifyDataSetChanged();
    }

}
