package com.example.ecgwars.views.News;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ecgwars.R;
import com.example.ecgwars.model.Article;
import com.example.ecgwars.viewmodels.NewsActivityViewModel;
import com.example.ecgwars.views.HomeActivity;
import com.example.ecgwars.views.Profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.LinkedHashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;


    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int page = 2;
    int firstVisibleItem, visibleItemCount, totalItemCount;


    private NewsActivityViewModel newsActivityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsActivityViewModel = new ViewModelProvider(this)
                .get(NewsActivityViewModel.class);


        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Log.d("News","Item clicked: "+ article.getUrl());
                Intent intent = new Intent(NewsActivity.this, NewsWebActivity.class);
                intent.putExtra("url",article.getUrl());
                startActivity(intent);

            }
        });
        newsRecyclerView.setAdapter(newsAdapter);
        newsActivityViewModel.getNews(1);
         loadNextDataFromApi();

        newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = newsRecyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    Log.i("Yaeye!", "end called");

                    // Do something
                   // newsActivityViewModel.getNews(page);
                  //  loadNextDataFromApi(page);
                    newsActivityViewModel.getNews(page);
                    page++;

                    loading = true;
                }
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.navigation_news);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent a = new Intent(NewsActivity.this, HomeActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigation_news:
                        break;
                    case R.id.navigation_profile:
                        Intent b = new Intent(NewsActivity.this, ProfileActivity.class);
                        startActivity(b);
                        break;
                }
                return false;
            }
        });
    }

    public void loadNextDataFromApi() {
       // newsActivityViewModel.getNews(offset);
        LinkedHashSet news = new LinkedHashSet();
        final Observer<List<Article>> newsObserver = new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> articles) {
                for(int i = 0;i<articles.size();i++){
                    news.clear();
                    String title = articles.get(i).getTitle();
                    String author = articles.get(i).getAuthor();
                    String description = articles.get(i).getDescription();
                    String imageUrl = articles.get(i).getUrlToImage();
                    String url = articles.get(i).getUrl();
                    news.add(new Article(title,author,description,imageUrl,url));
                }
                Log.d("News","Hashset size: "+ news.size());
                newsAdapter.setItems(news);
                newsAdapter.notifyDataSetChanged();
            }
        };
        newsActivityViewModel.getArticles().observe(this, newsObserver);
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }
}
