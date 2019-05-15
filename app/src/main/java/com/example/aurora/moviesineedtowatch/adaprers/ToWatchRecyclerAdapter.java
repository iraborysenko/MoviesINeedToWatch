package com.example.aurora.moviesineedtowatch.adaprers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tools.Extensions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aurora.moviesineedtowatch.tools.Constants.genres;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/18
 * Time: 19:13
 */
public class ToWatchRecyclerAdapter extends RecyclerView.Adapter<ToWatchRecyclerAdapter.ViewHolder> {

    private static ClickListener clickListener;
    private static List<Movie> mMovies;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.movie_add_edit_button) ImageButton mAddEditButton;
        @BindView(R.id.movie_poster) ImageView mPoster;
        @BindView(R.id.movie_title) TextView mTitle;
        @BindView(R.id.movie_tagline) TextView mTagline;
        @BindView(R.id.movie_genres) TextView mGenres;
        @BindView(R.id.movie_year) TextView mYear;
        @BindView(R.id.movie_imdb) TextView mImdb;
        String movieId;
        String dataLang;

        ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {
            movieId = Objects.requireNonNull(mMovies.get(getAdapterPosition())).getId();
            dataLang = Objects.requireNonNull(mMovies.get(getAdapterPosition())).getSavedLang();
            clickListener.onItemClick(v, movieId, dataLang);
        }

        @Override
        public boolean onLongClick(View v) {
            movieId = Objects.requireNonNull(mMovies.get(getAdapterPosition())).getId();
            dataLang = Objects.requireNonNull(mMovies.get(getAdapterPosition())).getSavedLang();
            clickListener.onItemLongClick(v, movieId, dataLang);
            return true;
        }
    }

    public ToWatchRecyclerAdapter(List<Movie> movies, Context context) {
        mMovies = movies;
        mContext = context;
    }

    @NonNull
    @Override
    public ToWatchRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_to_watch_recycler, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder movieViewHolder, int i) {

        Movie movie = mMovies.get(i);
        assert movie != null;

        movieViewHolder.mTitle.setText(movie.getTitle());

        // get tagline
        if (!movie.getTagline().equals(""))
            movieViewHolder.mTagline.setText(movie.getTagline());
        else movieViewHolder.mTagline.setVisibility(View.GONE);

        // get poster
        RequestOptions options = new RequestOptions()
                .error(R.drawable.noposter)
                .skipMemoryCache(true)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext)
                .asBitmap()
                .load(Extensions.stringToBitmap(movie.getPosterBitmap()))
                .apply(options)
                .into(movieViewHolder.mPoster);

        //get genres
        StringBuilder genresString = new StringBuilder();
        try {
            JSONArray ids = new JSONArray(movie.getGenresIds());
            if (ids.length() == 0) {
                genresString = new StringBuilder("not defined");
            } else {
                int index = (Objects.equals(movie.getSavedLang(), "true"))?0:1;
                for (int j=0; j<ids.length(); j++)
                    genresString.append(Objects.requireNonNull(genres.get(ids.get(j)))[index]).append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieViewHolder.mGenres.setText(genresString.toString());

        //get imdb rating and according color
        movieViewHolder.mImdb.setText(movie.getImdb());
        movieViewHolder.mImdb.setBackgroundColor(mContext.getResources()
                .getColor(Extensions.chooseColor(movie.getImdb())));

        //get year
        movieViewHolder.mYear.setText(movie.getReleaseDate().subSequence(0, 4));

        //add-edit button click listener
        movieViewHolder.mAddEditButton.setOnClickListener(view ->
            clickListener.onAddEditButtonClick(movie.getId())
        );
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ToWatchRecyclerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, String movieId, String dataLang);
        void onItemLongClick(View v, String movieId, String dataLang);
        void onAddEditButtonClick(String movieId);
    }
}
