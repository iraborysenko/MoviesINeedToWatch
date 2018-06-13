package com.example.aurora.moviesineedtowatch.adaprer;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.ui.MovieActivity.stringToBitmap;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/18
 * Time: 19:13
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private static ClickListener clickListener;
    private static RealmResults<Movie> mMovies;
    private Context mContext;
    private Resources mResources;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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

    public MainRecyclerAdapter(RealmResults<Movie> movies, Context context, Resources resources) {
        mMovies = movies;
        mContext = context;
        mResources = resources;
    }

    @NonNull
    @Override
    public MainRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_recycler_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder movieViewHolder, int i) {

        Movie movie = mMovies.get(i);
        assert movie != null;

        // get poster
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext)
                .asBitmap()
                .load(stringToBitmap(movie.getPosterBitmap()))
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
                    genresString.append(genres.get(ids.get(j))[index]).append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieViewHolder.mGenres.setText(String.valueOf(genresString.toString()));

        //get imdb rating and according color
        movieViewHolder.mImdb.setText(movie.getImdb());
        movieViewHolder.mImdb.setBackgroundColor(mResources.getColor(chooseColor(movie.getImdb())));

        //get the remaining items
        movieViewHolder.mTitle.setText(movie.getTitle());
        movieViewHolder.mTagline.setText(movie.getTagline());
        movieViewHolder.mYear.setText(movie.getReleaseDate());

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MainRecyclerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, String movieId, String dataLang);
        void onItemLongClick(View v, String movieId, String dataLang);
    }

    private int chooseColor(String imdbRating) {
        int color = R.color.OutOfBound;
        if (imdbRating.equals("none")) {
            color = R.color.NoMovie;
        } else {
            float rating = Float.parseFloat(imdbRating);
            if (rating<5.0f) {
                color = R.color.VeryBad;
            } else if (5.0f<= rating && rating<=5.9f) {
                color = R.color.Bad;
            } else if (6.0f<= rating && rating<=6.5f) {
                color = R.color.Avarage;
            } else if (6.6f<= rating && rating<=6.8f) {
                color = R.color.AboveAvarage;
            } else if (6.9f<= rating && rating<=7.2f) {
                color = R.color.Intermediate;
            } else if (7.3f<= rating && rating<=7.7f) {
                color = R.color.Good;
            } else if (7.8f<= rating && rating<=8.1f) {
                color = R.color.VeryGood;
            } else if (8.2f<= rating && rating<=8.5f) {
                color = R.color.Great;
            } else if (8.6f<= rating && rating<=8.9f) {
                color = R.color.Adept;
            } else if ( rating >= 9.0f) {
                color = R.color.Unicum;
            }
        }

        return color;
    }
}
