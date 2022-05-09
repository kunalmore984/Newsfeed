package com.example.newsfeed;

//class to form a list of generic type of news items
public class News {

    private final String mTitle;

    private final String mAuthors;

    private final String mDate;

    private final String mSection;

    private final String mUrl;

    private final String mBody;

    public News(String title,String authors, String date, String section, String url, String body){
        mSection=section;
        mTitle= title;
        mAuthors= authors;
        mDate = date;
        mUrl = url;
        mBody=body;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthors() {
        return mAuthors;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmBody() {
        return mBody;
    }
}
