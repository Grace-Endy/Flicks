package com.goopam.flicks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.goopam.flicks.models.Config;
import com.goopam.flicks.models.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    //Ligne code sa pral gen poul display List film yo
    ArrayList<Movie> movies;
    Config config;
    //Context for rendering
    Context context;


    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }
    public void setConfig(Config config) {
        this.config = config;
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Ligne sa ap la, pou l load data film yo, nan yon position ki byen specifique
        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        //Ligne sa pral gen pou l kreye lien pou Poster Image yo
        String theSize  = config.getPosterSize();
        if(theSize != null){
            String imageUrl = config.getImageUrl(theSize, movie.getPosterPath());
            Glide.with(context).load(imageUrl).into(holder.ivPosterImage);
        }

        // Load image using Glide

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0  or corners with no crop

//        Glide.with(context).load(movie.getPosterPath()).into(holder.ivPosterImage);

    }
//Ligne sa ap responsab pou l display nombre de element kap gen nan liste lan
    @Override
    public int getItemCount() {
        return movies.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public  ViewHolder(View itemView) {
            super(itemView);

            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle =(TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
