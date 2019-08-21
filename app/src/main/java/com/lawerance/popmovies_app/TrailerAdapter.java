package com.lawerance.popmovies_app;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lawerance.popmovies_app.MoviesAPI.Result;
import com.lawerance.popmovies_app.MoviesAPI.videos;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/original";

    private ArrayList<videos> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public TrailerAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
    }

    public ArrayList<videos> getMovies() {
        return movieResults;
    }

    public void setMovies(ArrayList<videos> movieResults) {
        this.movieResults = movieResults;
    }


    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.trailer_layout, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        videos videos = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.mTrailerTitle.setText(videos.getName());






                break;

            case LOADING:
                break;
        }

    }


    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(videos r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(ArrayList<videos> moveResults) {
        for (videos videos : moveResults) {
            add(videos);
        }
    }

    public void remove(Result r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        movieResults.clear();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new videos());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        videos videos = getItem(position);

        if (videos != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public videos getItem(int position) {
        return movieResults.get(position);
    }


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView mTrailerTitle;
        private ImageView mTrailerThumnail;
        private ProgressBar mProgress;

        public MovieVH(View itemView) {
            super(itemView);

            mTrailerTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}