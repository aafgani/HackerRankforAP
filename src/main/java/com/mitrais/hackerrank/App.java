package com.mitrais.hackerrank;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Movies : " );

        try {
            String[] movies = getMoviesTitle("https://jsonmock.hackerrank.com/api/movies/search/?Title=spiderman");
            for (int i=0;i<movies.length;i++){
                System.out.println((i+1)+". "+movies[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String[] getMoviesTitle(String s) {
        String[] rtn = new String[0];
        
        try {
            String output = consumeAPI(s);
            Gson gson = new Gson();
            Resp resp = gson.fromJson(output, Resp.class);
            List<Movies> movies = resp.getData();

            for(int i=resp.getPage()+1; i<=resp.getTotal_pages();i++){
                String url = s+"&page="+i;
                output = consumeAPI(url);
                resp = gson.fromJson(output, Resp.class);
                for (Movies m:resp.getData()){
                    movies.add(m);
                }
            }

            Collections.sort(movies,Movies.SortMoviesbyTitle);
            rtn = movies.stream().map(m -> m.getTitle()).collect(Collectors.toList()).toArray(new String[movies.size()] );

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  rtn;
    }

    private static String consumeAPI(String s) throws IOException {
        URL url = new URL(s);//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output, output2="";
        while ((output = br.readLine()) != null) {
            output2 = output2.concat(output);
        }
        conn.disconnect();

        return output2;
    }
}

class Movies{
    private String Poster;
    private String Year;
    private String Title;
    private String imdbID;

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        this.Poster = poster;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbId) {
        this.imdbID = imdbId;
    }

    public static Comparator<Movies> SortMoviesbyTitle
            = new Comparator<Movies>() {
        @Override
        public int compare(Movies f1, Movies f2) {
            //ascending
            return f1.getTitle().compareTo(f2.getTitle());

            //descending
            //return f2name.compareTo(f1name);
        }
    };
}

class Resp{
    private int page;
    private int per_page;
    private int total_pages;
    private List<Movies> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movies> getData() {
        return data;
    }

    public void setData(List<Movies> data) {
        this.data = data;
    }
}