package com.example.newsfeed;

import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtlis {
    //Tag for the log messages...
    private static final String TAG = QueryUtlis.class.getName();
    //private constructor to so that no objects of this class are made...
    private QueryUtlis(){
    }
    //main method to return the list of the news
    public static List<News> extractnewsfromjson(String newsJSON){
        //checking whether the string received is empty
        if (TextUtils.isEmpty(newsJSON)){
            return null;
        }
        ArrayList<News> newslist =new ArrayList<>();
        String contributor = "Contributed by : ";
        //parsing the json....
        try{
            //root of the json file....
            JSONObject root =new JSONObject(newsJSON);
            //first object of the json...
            JSONObject response = root.getJSONObject("response");
            //results array of the json where most of our data is stored.....
            JSONArray results = response.getJSONArray("results");
            for (int i=0;i<results.length();i++){
                //fetching results from array beginning with 0th index....
                JSONObject res = results.getJSONObject(i);
                String section = res.getString("sectionName");
                String date = res.getString("webPublicationDate");
                String title = res.getString("webTitle");
                String url = res.getString("webUrl");
                JSONArray tag = res.getJSONArray("tags");
                //A new json object in the results array which stores the body of the news article.....
                JSONObject fields = res.getJSONObject("fields");
                String body = fields.getString("bodyText");
                //another array within results array.....
                for (int j=0;j<tag.length();j++){
                    JSONObject tagobj = tag.getJSONObject(j);
                    contributor = contributor +", " +tagobj.getString("webTitle");
                }
                Log.v(TAG,"Cheking the outputs from json : "+section + " "+date+" "+title+" "+contributor+" "+url+""+TextUtils.substring(body,0,50));
                News news = new News(title,contributor,date,section,url,TextUtils.substring(body,0,90));
                newslist.add(news);
            }
        }catch (JSONException jsonException){
            Log.e(TAG,"JSON error : "+jsonException);
        }
        return newslist;
    }

    public static List<News> fetchresults(String jsonurl){
        URL url = createurl(jsonurl);
        String json = "";
        try {
            json = makehttprequest(url);
        }catch (IOException e){
            Log.e(TAG,"Error in fetching json or some IO error : "+e);
            return null;
        }
        List<News> newsList = extractnewsfromjson(json);
        return newsList;
    }

    private static URL createurl(String url){
        URL newurl = null;
        try {
            newurl =new URL(url);
        }catch (MalformedURLException e){
            Log.e(TAG,"error forming the url : "+e);
            return null;
        }
        return newurl;
    }

    private static String makehttprequest(URL reqUrl) throws IOException{
        String jsonresponse = "";
        HttpURLConnection httpURLConnection =null;
        InputStream inputStream = null;
        if (reqUrl == null){
            return jsonresponse;
        }
        try{
            //open the url connection....
            httpURLConnection = (HttpURLConnection) reqUrl.openConnection();
            //Set the request method to GET or POST....
            httpURLConnection.setRequestMethod("GET");
            //Set connection read and connect timeout...
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            //now finally establish the connection.....
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonresponse = readfromstream(inputStream);
            }else {
                Log.e(TAG," error code : "+httpURLConnection.getResponseCode());
            }
        }catch (IOException ioException){
            Log.e(TAG,"ioexception : "+ioException);
        }finally {
            //finally block to disconnect the connection to internet
            if (httpURLConnection !=null){
                httpURLConnection.disconnect();
            }
            if (inputStream !=null){
                inputStream.close();
            }
        }
        return jsonresponse;
    }

    private static String readfromstream(InputStream inputStream) throws IOException{
        StringBuilder result =new StringBuilder();
        if (inputStream != null){
            //instance of inputstremreader.....
            InputStreamReader inputStreamReader =new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null){
                result.append(line);
                line = bufferedReader.readLine();
            }
        }
        return result.toString();
    }
}
