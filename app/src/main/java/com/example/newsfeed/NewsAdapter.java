package com.example.newsfeed;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> news){
        super(context,0,news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);
        }

        News current_news = getItem(position);
        //Instances of news_item.xml
        TextView title = (TextView) listView.findViewById(R.id.title);
        TextView section = (TextView) listView.findViewById(R.id.section);
        TextView date = (TextView)  listView.findViewById(R.id.date);
        TextView authors = (TextView) listView.findViewById(R.id.authors);
        TextView body = (TextView)listView.findViewById(R.id.body);

        //setting texts to appropriate text views...
        title.setText(current_news.getmTitle());
        section.setText(current_news.getmSection());
        String mDate = current_news.getmDate();
        date.setText(TextUtils.substring(mDate,0,10));
        authors.setText(current_news.getmAuthors());
        body.setText(current_news.getmBody()+"....READ MORE");
        return listView;
    }
}
