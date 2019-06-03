package com.example.aurora.moviesineedtowatch.adaprers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.App;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.swipe.ItemTouchAdapter;
import com.example.aurora.moviesineedtowatch.dagger.SharedPreferencesSettings;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tools.Extensions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aurora.moviesineedtowatch.tools.Constants.INCREASED_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.REDUCED_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.SHARED_TO_WATCH_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.genres;
import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 04/06/18
 * Time: 19:13
 */
public class ToWatchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchAdapter {

    @Inject
    SharedPreferencesSettings sharedPreferencesSettings;

    private static ClickListener clickListener;
    private static List<Movie> mMovies;
    private Context mContext;

    static class ReducedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_poster) ImageView mPoster;
        @BindView(R.id.movie_title) TextView mTitle;
        @BindView(R.id.movie_imdb) TextView mImdb;
        String movieId;
        String dataLang;

        ReducedViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {
            movieId = Objects.requireNonNull(mMovies.get(getAdapterPosition())).getId();
            dataLang = Objects.requireNonNull(mMovies.get(getAdapterPosition())).getSavedLang();
            clickListener.onItemClick(movieId);
        }
    }

    static class IncreasedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_add_edit_button) ImageButton mAddEditButton;
        @BindView(R.id.movie_poster) ImageView mPoster;
        @BindView(R.id.movie_title) TextView mTitle;
        @BindView(R.id.movie_tagline) TextView mTagline;
        @BindView(R.id.movie_genres) TextView mGenres;
        @BindView(R.id.movie_year) TextView mYear;
        @BindView(R.id.movie_imdb) TextView mImdb;
        String movieId;
        String dataLang;

        IncreasedViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {
            movieId = Objects.requireNonNull(mMovies.get(getAdapterPosition())).getId();
            dataLang = Objects.requireNonNull(mMovies.get(getAdapterPosition())).getSavedLang();
            clickListener.onItemClick(movieId);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (sharedPreferencesSettings.getStringData(SHARED_TO_WATCH_LAYOUT, INCREASED_LAYOUT)) {
            case REDUCED_LAYOUT:
                itemView = inflater.inflate(R.layout.item_to_watch_recycler_reduced, parent, false);
                return new ReducedViewHolder(itemView);
            case INCREASED_LAYOUT:
                itemView = inflater.inflate(R.layout.item_to_watch_recycler, parent, false);
                return new IncreasedViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (sharedPreferencesSettings.getStringData(SHARED_TO_WATCH_LAYOUT, INCREASED_LAYOUT)) {
            case REDUCED_LAYOUT:
                fillReducedList(viewHolder, i);
                break;
            case INCREASED_LAYOUT:
                fillIncreasedList(viewHolder, i);
                break;
        }
    }

    public ToWatchRecyclerAdapter(List<Movie> movies, Context context) {
        ((App) getApplicationContext()).getApplicationComponent().inject(this);
        mMovies = movies;
        mContext = context;
    }

    private void fillReducedList(RecyclerView.ViewHolder viewHolder, int i) {

        ReducedViewHolder reducedViewHolder = (ReducedViewHolder) viewHolder;

        Movie movie = mMovies.get(i);
        assert movie != null;

        reducedViewHolder.mTitle.setText(movie.getTitle());

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
                .into(reducedViewHolder.mPoster);

        //get imdb rating and according color
        reducedViewHolder.mImdb.setText(movie.getImdb());
        reducedViewHolder.mImdb.setBackgroundColor(mContext.getResources()
                .getColor(Extensions.chooseColor(movie.getImdb())));
    }

    private void fillIncreasedList(RecyclerView.ViewHolder viewHolder, int i) {

        IncreasedViewHolder increasedViewHolder = (IncreasedViewHolder) viewHolder;

        Movie movie = mMovies.get(i);
        assert movie != null;

        increasedViewHolder.mTitle.setText(movie.getTitle());

//        // get tagline
        if (!movie.getTagline().equals(""))
            increasedViewHolder.mTagline.setText(movie.getTagline());
        else increasedViewHolder.mTagline.setVisibility(View.GONE);

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
                .into(increasedViewHolder.mPoster);

//        //get genres
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
        increasedViewHolder.mGenres.setText(genresString.toString());

        //get imdb rating and according color
        increasedViewHolder.mImdb.setText(movie.getImdb());
        increasedViewHolder.mImdb.setBackgroundColor(mContext.getResources()
                .getColor(Extensions.chooseColor(movie.getImdb())));

//        //get year
        increasedViewHolder.mYear.setText(movie.getReleaseDate().subSequence(0, 4));

//        //add-edit button click listener
        increasedViewHolder.mAddEditButton.setOnClickListener(view ->
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
        void onItemClick(String movieId);
        void onAddEditButtonClick(String movieId);
        void onMoveToOtherTab(String movieId);
        void onDeleteItem(String movieId);
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        if (direction == ItemTouchHelper.START) {
            clickListener.onDeleteItem(mMovies.get(position).getId());
        } else if (direction == ItemTouchHelper.END) {
            clickListener.onMoveToOtherTab(mMovies.get(position).getId());
        }
    }
}
