package com.goopam.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.goopam.flicks.models.Config;
import com.goopam.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //Ligne sa reprezante deklaratyon constant, ki se Base Url Api ke nou pral use lan
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //Ligne sa reprezante deklaratyon constant, ki se parameter name Api ke nou pral use lan
    public final static String  API_KEY_PARAM= "api_key";

    public final static String TAG = "MovieListActivity";

    AsyncHttpClient client;

    ArrayList<Movie> movies;
    // The recycle View
    RecyclerView rvMovies;
    //Ligne sa pral gen pou l lier RecyclerView an avek Adapter an
    MovieAdapter adapter;
    //Ligne sa reprezante configuration pou Image yo
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        client = new AsyncHttpClient();
        movies = new ArrayList<>();
        //Initialize the adapter
        adapter = new MovieAdapter(movies);
        // Ligne code sa ap gen pou l trete RecyclerView an, e konekte yon Layout manager avek Adapter an
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);
        getConfiguration();

        getNowPlaying();
    }

    private  void getNowPlaying(){

        //Ligne sa reprezante kreyasyon Url lan
        String url = API_BASE_URL + "/movie/now_playing";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //Ligne code sa pral execute yon a Get request kote, lap atann a yon response kap vini sou fom yon objet JSON

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Donk, si requette la bon, ansanm de komand sa yo ap exekite
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++)
                    {
                     Movie movie = new Movie(results.getJSONObject(i));
                     movies.add(movie);
                //Ligne code sa, ap gen pou fe Adapter a konnen, ke gen yon Row ki ajouter
                        adapter.notifyItemInserted(movies.size() -1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    LogError("Failed to parse now playing movies", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
             LogError("Failed to get data from now playing endpoint", throwable, true);
            }
        });


    }
    private void getConfiguration(){
        //Ligne sa reprezante kreyasyon Url lan
        String url = API_BASE_URL + "/configuration";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //Ligne code sa pral execute yon a Get request kote, lap atann a yon response kap vini sou fom yon objet JSON

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        //Donk, si requette la bon, ansanm de komand sa yo ap exekite
                try {
                    config = new Config(response);

                    Log.i(TAG,
                            String.format("Loaded Configuration with imageBaseUrl %s and posterSize %s",
                                    config.getImageBaseUrl(),
                                    config.getPosterSize()));

                        adapter.setConfig(config);
                        //Pass config to Adapter
                    getNowPlaying();
                } catch (JSONException e) {
                    LogError("Failed parsing configuration", e, true);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogError("Failed getting configuration", throwable, true);
            }

        });
    }

    //Ligne sa ap la pou jere Erreur yo, au cas ou requete lan ta fail, e lap tou kreye yon log e alerte user a sa
    private  void  LogError(String message, Throwable error, boolean alertUser){
        Log.e(TAG, message, error);
        if (alertUser) {
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
        }
    }
}
