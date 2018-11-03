package com.goopam.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //Ligne sa reprezante Base Url ki pral la pou Upload Images yo
    String imageBaseUrl;
    //Ligne sa reprezante size Images yo
    String posterSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        // Premyeman, Pran Url Image lan
        imageBaseUrl = images.getString("secure_base_url");
        // Dezyeman, Pran size poster film lan
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");

        posterSize = posterSizeOptions.optString(3, "w342");

    }

    //Helper method for creating Urls
    public String getImageUrl (String size, String path){
        return String.format("%s%s%s",imageBaseUrl,size,path);
    }
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
