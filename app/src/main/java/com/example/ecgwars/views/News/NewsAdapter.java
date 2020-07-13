package com.example.ecgwars.views.News;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecgwars.CropCircleTransformation;
import com.example.ecgwars.R;
import com.example.ecgwars.model.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private List<Article> articleList = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public NewsAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item_view, parent, false);


        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(articleList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public String getId() {
        return id;
    }

    public void clear() {
        articleList.clear();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {


        private TextView tittleTextView;
        private TextView authorTextView;
        private TextView descriptionTextView;
        private ImageView imageView;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            tittleTextView = itemView.findViewById(R.id.articleTittle);
            authorTextView = itemView.findViewById(R.id.articleAuthor);
            descriptionTextView = itemView.findViewById(R.id.articleDescription);
            imageView = itemView.findViewById(R.id.articleImage);
        }

        public void bind(Article article) {

            if(article.getAuthor()==null){
                authorTextView.setVisibility(View.GONE);
            }
            if(article.getUrlToImage()==null){
                imageView.setVisibility(View.GONE);
            }
            if(article.getTitle()==null){
                tittleTextView.setVisibility(View.GONE);
            }
            if(article.getDescription()==null){
                descriptionTextView.setVisibility(View.GONE);
            }
            tittleTextView.setText(article.getTitle());
            authorTextView.setText(article.getAuthor());
            descriptionTextView.setText(article.getDescription());
            Picasso.get()
                    .load(article.getUrlToImage())
                    .into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(article);
                }
            });
        }
    }

    public void setItems(Collection<Article> articles) {
        articleList.addAll(articles);
        notifyDataSetChanged();
    }


}
