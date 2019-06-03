package com.example.aurora.moviesineedtowatch.ui.main.watchedtab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.WatchedRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.adaprers.swipe.ItemTouchCallback;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.watchedfragment.DaggerWatchedFragmentScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.watchedfragment.WatchedFragmentScreenModule;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.eventbus.EventsForUpdateList;
import com.example.aurora.moviesineedtowatch.ui.manage.ManageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:17
 */
public class WatchedFragment extends Fragment implements WatchedFragmentScreen.View {

    @Inject
    WatchedFragmentPresenter mPresenter;

    @BindView(R.id.watched_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWatchedFragmentScreenComponent.builder()
                .watchedFragmentScreenModule(new WatchedFragmentScreenModule(this))
                .build().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watched, container, false);
        ButterKnife.bind(this, view);
        mPresenter.loadWatchedMovies();
        return view;
    }

    @Override
    public void movieWatchedDetails(String movieId) {
        Intent intent = new Intent(getActivity(), ManageActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        startActivity(intent);
    }

    @Override
    public WatchedRecyclerAdapter initRecyclerView(List<Movie> movies) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final WatchedRecyclerAdapter mAdapter = new WatchedRecyclerAdapter(movies, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchCallback(mAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return mAdapter;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventsForUpdateList event) {
        mPresenter.updateList(mRecyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
