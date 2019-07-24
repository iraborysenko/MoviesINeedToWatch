package com.example.aurora.moviesineedtowatch.ui.main.towatchtab;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprers.swipe.ItemTouchCallback;
import com.example.aurora.moviesineedtowatch.adaprers.ToWatchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.SharedPreferencesSettings;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.towatchfragment.DaggerToWatchFragmentScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.mainsreen.towatchfragment.ToWatchFragmentScreenModule;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tmdb.eventbus.EventsForUpdateList;
import com.example.aurora.moviesineedtowatch.ui.manage.ManageActivity;
import com.example.aurora.moviesineedtowatch.ui.movie.MovieActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 08/05/19
 * Time: 20:16
 */
public class ToWatchFragment extends Fragment implements ToWatchFragmentScreen.View {

    @Inject
    ToWatchFragmentPresenter mPresenter;

    @Inject
    SharedPreferencesSettings sharedPreferencesSettings;

    @BindView(R.id.to_watch_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.find_movie_edit_text)
    EditText mFindText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerToWatchFragmentScreenComponent.builder()
                .toWatchFragmentScreenModule(new ToWatchFragmentScreenModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build().inject(this);
    }

    @OnEditorAction(R.id.find_movie_edit_text)
    boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String filter = mFindText.getText().toString();
            mPresenter.getFilteredMovies(filter);
        }
        return true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_towatch, container, false);
        ButterKnife.bind(this, view);
        mPresenter.loadToWatchMovies();
        return view;
    }

    @Override
    public void movieToWatchDetails(String movieId) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        startActivity(intent);
    }

    @Override
    public void movieAddEditDetails(String movieId) {
        Intent intent = new Intent(getActivity(), ManageActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        startActivity(intent);
    }

    @Override
    public ToWatchRecyclerAdapter initRecyclerView(List<Movie> movies) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final ToWatchRecyclerAdapter mAdapter = new ToWatchRecyclerAdapter(movies, getApplicationContext());
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
        mFindText.setHint(getResources().getString(R.string.find_movie));
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
