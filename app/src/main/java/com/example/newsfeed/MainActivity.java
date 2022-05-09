package com.example.newsfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<News>> {

    //TODO:do the beautification of the app i.e adjust color, body of the story, etc....
    //adapter instance.
    private NewsAdapter newsAdapter;
    //Constant for loader init....
    private static int NEWS_ID = 1;
    //empty view instance....
    private TextView emptyview ;
    private ProgressBar progressBar;
    //URL for parsing...
    private String REQUEST_URL = "https://content.guardianapis.com/search?q=debate&to-date=2022-01-05&api-key=e64296a9-e53c-4aa1-aca3-d0105c581e89&page-size=50&order-by=newest&show-tags=contributor&show-fields=bodyText";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Add title, short info about the news, date and author name of the article....
         * Guardian api key = e64296a9-e53c-4aa1-aca3-d0105c581e89
         * URL api = https://content.guardianapis.com/search?q=debate&to-date=2022-01-05&api-key=e64296a9-e53c-4aa1-aca3-d0105c581e89&page-size=50&order-by=newest&show-tags=contributor&show-fields=bodyText
         */
        ListView newslist = (ListView) findViewById(R.id.news_list);
        emptyview = (TextView) findViewById(R.id.empty_view);
        progressBar =(ProgressBar)findViewById(R.id.progressbar);
        newslist.setEmptyView(emptyview);
        newsAdapter = new NewsAdapter(MainActivity.this, new ArrayList<>());
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnectedOrConnecting()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_ID,null,this);
        }else {
            progressBar.setVisibility(View.GONE);
            emptyview.setText("No Internet");
        }
        newslist.setAdapter(newsAdapter);
        newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = newsAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getmUrl());
                Intent i = new Intent(Intent.ACTION_VIEW,newsUri);
                startActivity(i);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        NewsLoader newsLoader = new NewsLoader(MainActivity.this,REQUEST_URL);
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        emptyview.setText("No News Today");
        progressBar.setVisibility(View.GONE);
        newsAdapter.clear();;
        if (data !=null && !data.isEmpty()){
            newsAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}