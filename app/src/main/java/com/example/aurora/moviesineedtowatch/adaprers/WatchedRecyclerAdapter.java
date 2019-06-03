package com.example.aurora.moviesineedtowatch.adaprers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aurora.moviesineedtowatch.tools.Constants.INCREASED_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.REDUCED_LAYOUT;
import static com.example.aurora.moviesineedtowatch.tools.Constants.SHARED_WATCHED_LAYOUT;
import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/05/19
 * Time: 16:46
 */
public class WatchedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchAdapter {

    @Inject
    SharedPreferencesSettings sharedPreferencesSettings;

    private static ClickListener clickListener;
    private static List<Movie> mMovies;
    private Context mContext;

    static class IncreasedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.watched_poster) ImageView mPoster;
        @BindView(R.id.watched_title) TextView mTitle;
        @BindView(R.id.watched_imdb) TextView mImdb;
        @BindView(R.id.watched_my_rating) TextView mMyRating;
        @BindView(R.id.watched_comment) TextView mComment;
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

    static class ReducedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.watched_poster) ImageView mPoster;
        @BindView(R.id.watched_title) TextView mTitle;
        @BindView(R.id.watched_my_rating) TextView mMyRating;
        @BindView(R.id.watched_imdb) TextView mImdb;
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

    public WatchedRecyclerAdapter(List<Movie> movies, Context context) {
        ((App) getApplicationContext()).getApplicationComponent().inject(this);
        mMovies = movies;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (sharedPreferencesSettings.getStringData(SHARED_WATCHED_LAYOUT, INCREASED_LAYOUT)) {
            case REDUCED_LAYOUT:
                itemView = inflater.inflate(R.layout.item_watched_recycler_reduced, parent, false);
                return new WatchedRecyclerAdapter.ReducedViewHolder(itemView);
            case INCREASED_LAYOUT:
                itemView = inflater.inflate(R.layout.item_watched_recycler, parent, false);
                return new WatchedRecyclerAdapter.IncreasedViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (sharedPreferencesSettings.getStringData(SHARED_WATCHED_LAYOUT, INCREASED_LAYOUT)) {
            case REDUCED_LAYOUT:
                fillReducedList(viewHolder, i);
                break;
            case INCREASED_LAYOUT:
                fillIncreasedList(viewHolder, i);
                break;
        }
    }

    private void fillIncreasedList(RecyclerView.ViewHolder viewHolder, int i) {

        IncreasedViewHolder increasedViewHolder = (IncreasedViewHolder) viewHolder;

        Movie movie = mMovies.get(i);
        assert movie != null;

        increasedViewHolder.mTitle.setText(movie.getTitle());

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


        //get imdb rating and according color
        increasedViewHolder.mImdb.setText(movie.getImdb());
        increasedViewHolder.mImdb.setBackgroundColor(mContext.getResources()
                .getColor(Extensions.chooseColor(movie.getImdb())));

        //get my rating and according color
        if (movie.getMyRating()==null) {
            increasedViewHolder.mMyRating.setVisibility(View.GONE);
        } else {
            increasedViewHolder.mMyRating.setVisibility(View.VISIBLE);
            increasedViewHolder.mMyRating.setText(movie.getMyRating());
            increasedViewHolder.mMyRating.setBackgroundColor(mContext.getResources()
                    .getColor(Extensions.chooseColor(String.valueOf(movie.getMyRating()))));
        }

        // get comment
        increasedViewHolder.mComment.setText(movie.getComment());
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

        //get my rating and according color
        if (movie.getMyRating()==null) {
            reducedViewHolder.mMyRating.setVisibility(View.GONE);
        } else {
            reducedViewHolder.mMyRating.setVisibility(View.VISIBLE);
            reducedViewHolder.mMyRating.setText(movie.getMyRating());
            reducedViewHolder.mMyRating.setBackgroundColor(mContext.getResources()
                    .getColor(Extensions.chooseColor(String.valueOf(movie.getMyRating()))));
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        WatchedRecyclerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(String movieId);
        void onMoveToOtherTab(String movieId);
        void onDeleteItem(String movieId);
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        if (direction == ItemTouchHelper.START) {
            clickListener.onMoveToOtherTab(mMovies.get(position).getId());
        } else if (direction == ItemTouchHelper.END) {
            clickListener.onDeleteItem(mMovies.get(position).getId());
        }
    }
}
