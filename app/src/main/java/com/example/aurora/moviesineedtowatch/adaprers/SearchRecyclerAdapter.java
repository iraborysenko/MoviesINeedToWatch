package com.example.aurora.moviesineedtowatch.adaprers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aurora.moviesineedtowatch.tools.Constants.IMAGE_PATH;
import static com.example.aurora.moviesineedtowatch.tools.Constants.IMAGE_SIZE;
import static com.example.aurora.moviesineedtowatch.tools.Constants.genres;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 11/06/18
 * Time: 17:22
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private static ClickListener clickListener;
    private static FoundMovie[] mSearch;
    private Context mContext;
    private Switch mSwitch;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.s_movie_poster) ImageView mPoster;
        @BindView(R.id.s_movie_title) TextView mTitle;
        @BindView(R.id.s_movie_original_title) TextView mOTitle;
        @BindView(R.id.s_movie_genres) TextView mGenres;
        @BindView(R.id.s_movie_year) TextView mYear;
        @BindView(R.id.s_movie_tmdb) TextView mTmdb;
        String movieId;

        ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {
            movieId = Objects.requireNonNull(mSearch[getAdapterPosition()]).getId();
            clickListener.onItemClick(v, movieId);
        }

        @Override
        public boolean onLongClick(View v) {
            movieId = Objects.requireNonNull(mSearch[getAdapterPosition()]).getId();
            clickListener.onItemLongClick(v, movieId);
            return true;
        }
    }

    public SearchRecyclerAdapter(FoundMovie[] search, Context context, Switch switcher) {
        mSearch = search;
        mContext = context;
        mSwitch = switcher;
    }

    @NonNull
    @Override
    public SearchRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_recycler, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder movieViewHolder, int i) {

        FoundMovie movie = mSearch[i];
        assert movie != null;

        // get poster
        String imagePath = IMAGE_PATH + IMAGE_SIZE[3] + movie.getPosterPath();
        RequestOptions options = new RequestOptions()
                .error(R.drawable.noposter)
                .skipMemoryCache(true);

        Glide.with(mContext)
                .load(imagePath)
                .apply(options)
                .into(movieViewHolder.mPoster);

        //get genres
        StringBuilder genresString = new StringBuilder();
        if (movie.getGenreIds().isEmpty())
            genresString = new StringBuilder("not defined");
        else {
            for (Integer genreId : movie.getGenreIds()) {
                genresString
                        .append(Objects.requireNonNull(genres.get(genreId))[mSwitch.isChecked() ? 0 : 1]).append("\n");
            }
        }
        movieViewHolder.mGenres.setText(String.valueOf(genresString.toString()));

        //get the remaining items
        movieViewHolder.mTitle.setText(movie.getTitle());
        movieViewHolder.mOTitle.setText(movie.getOriginalTitle());
        movieViewHolder.mYear.setText(movie.getReleaseDate());
        movieViewHolder.mTmdb.setText(movie.getVoteAverage());
    }

    @Override
    public int getItemCount() {
        return mSearch.length;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        SearchRecyclerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, String movieId);
        void onItemLongClick(View v, String movieId);
    }
}
