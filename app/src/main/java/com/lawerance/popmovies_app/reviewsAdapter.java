package com.lawerance.popmovies_app;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lawerance.popmovies_app.MoviesAPI.Result;
import com.lawerance.popmovies_app.MoviesAPI.comment;
import com.lawerance.popmovies_app.MoviesAPI.videos;

import java.util.ArrayList;

public class reviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/original";

    private ArrayList<comment> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public reviewsAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
    }

    public ArrayList<comment> getMovies() {
        return movieResults;
    }

    public void setMovies(ArrayList<comment> movieResults) {
        this.movieResults = movieResults;
    }


    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.review_layout, parent, false);
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

        comment comment = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.mAuthorName.setText(comment.getAuthor());
                movieVH.mComment.setText(comment.getContent());





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


    public void add(comment r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(ArrayList<comment> moveResults) {
        for (comment comment : moveResults) {
            add(comment);
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
        add(new comment());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        comment comment = getItem(position);

        if (comment != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public comment getItem(int position) {
        return movieResults.get(position);
    }


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView mAuthorName;
        private TextView mComment;

        public MovieVH(View itemView) {
            super(itemView);

            mAuthorName = (TextView) itemView.findViewById(R.id.auther_name);
            mComment=itemView.findViewById(R.id.comment);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}