package com.example.ecgwars.viewmodels;

import android.util.Log;

import com.example.ecgwars.Server.JsonNewsApi;
import com.example.ecgwars.model.ArtcileInfo;
import com.example.ecgwars.model.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivityViewModel extends ViewModel {

    private static final String apiKey = "002b4defe9dd4b51ada5bb13667ff905";
    public static final String countryCode = "us";
    public static final String category = "health";
    public static final String pageSize = "6";

    private MutableLiveData<List<Article>> articles = new MutableLiveData<>();

    private JsonNewsApi jsonNewsApi;

    public LiveData<List<Article>> getArticles() {
        return articles;
    }


    public void getNews(int page) {
        if(articles.getValue() !=null){
            articles.getValue().clear();
        }

        List<Article> articlesList = new ArrayList<>();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonNewsApi jsonNewsApi = retrofit.create(JsonNewsApi.class);


        Call<ArtcileInfo> call = jsonNewsApi.getNews(apiKey, category, countryCode, pageSize,page+"");

        call.enqueue(new Callback<ArtcileInfo>() {
            @Override
            public void onResponse(Call<ArtcileInfo> call, Response<ArtcileInfo> response) {

                if (!response.isSuccessful()) {
                    Log.d("News", "Error " + response.code());
                    return;
                } else {
                    Log.d("News", "Response is successful");
                   ArtcileInfo articleInfoList = response.body();

                        Log.d("News", "Article count " + articleInfoList.getTotalResults());
                        for (int i = 0; i < articleInfoList.getArticles().size(); i++) {
                            Article article = articleInfoList.getArticles().get(i);
                            Log.d("News", "Title: " + article.getTitle());
                            Log.d("News", "Author: " + article.getAuthor());
                            Log.d("News", "Description: " + article.getDescription());
                            articlesList.add(article);
                            articles.setValue(articlesList);
                        }
                    Log.d("News", "Size: " + articlesList.size());

                }
            }

            @Override
            public void onFailure(Call<ArtcileInfo> call, Throwable t) {
                Log.d("News", "Failed "+ t.getMessage());
            }
        });

    }

}
