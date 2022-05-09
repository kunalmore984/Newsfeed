package com.example.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private final String mUrl;
    private String TAG = NewsLoader.class.getName();

    public NewsLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null || mUrl.length() <1){
            return null;
        }
        List<News> news = QueryUtlis.fetchresults(mUrl);
        return news;
    }
}
