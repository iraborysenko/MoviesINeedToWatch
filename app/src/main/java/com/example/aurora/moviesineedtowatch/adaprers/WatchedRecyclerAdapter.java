package com.example.aurora.moviesineedtowatch.adaprers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.swipe.ItemTouchAdapter;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tools.Extensions;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/05/19
 * Time: 16:46
 */
public class WatchedRecyclerAdapter extends RecyclerView.Adapter<WatchedRecyclerAdapter.ViewHolder>
        implements ItemTouchAdapter {

    private static ClickListener clickListener;
    private static List<Movie> mMovies;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.watched_poster) ImageView mPoster;
        @BindView(R.id.watched_title) TextView mTitle;
        @BindView(R.id.watched_imdb) TextView mImdb;
        @BindView(R.id.watched_my_rating) TextView mMyRating;
        @BindView(R.id.watched_comment) TextView mComment;
        String movieId;
        String dataLang;

        ViewHolder(View v) {
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
        mMovies = movies;
        mContext = context;
    }

    @NonNull
    @Override
    public WatchedRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_watched_recycler, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder movieViewHolder, int i) {

        Movie movie = mMovies.get(i);
        assert movie != null;

        movieViewHolder.mTitle.setText(movie.getTitle());

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


        //get imdb rating and according color
        movieViewHolder.mImdb.setText(movie.getImdb());
        movieViewHolder.mImdb.setBackgroundColor(mContext.getResources()
                .getColor(Extensions.chooseColor(movie.getImdb())));

        //get my rating and according color
        if (movie.getMyRating()==null) {
            movieViewHolder.mMyRating.setVisibility(View.GONE);
        } else {
            movieViewHolder.mMyRating.setVisibility(View.VISIBLE);
            movieViewHolder.mMyRating.setText(movie.getMyRating());
            movieViewHolder.mMyRating.setBackgroundColor(mContext.getResources()
                    .getColor(Extensions.chooseColor(String.valueOf(movie.getMyRating()))));
        }

        // get comment
        movieViewHolder.mComment.setText(movie.getComment());
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
