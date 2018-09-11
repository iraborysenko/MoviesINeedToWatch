package com.example.aurora.moviesineedtowatch.ui.search;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_LANG_KEY;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.adaprer.SearchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.SharedPreferencesSettings;
import com.example.aurora.moviesineedtowatch.dagger.component.DaggerSearchScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.dagger.module.screens.SearchScreenModule;
import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:41
 */
public class SearchActivity extends AppCompatActivity implements SearchScreen.View {

    @Inject
    SharedPreferencesSettings sharedPreferencesSettings;

    @Inject
    SearchPresenter searchPresenter;

    @BindView(R.id.switchToEN) Switch mSwitch;
    @BindView(R.id.notificationField) TextView mNotificationField;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.search_query) EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search);

        DaggerSearchScreenComponent.builder()
                .searchScreenModule(new SearchScreenModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build().inject(this);

        ButterKnife.bind(this);

        editText.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                searchPresenter.editSearchField(editText.getText().toString());
                return true;
            }
            return false;
        });

        mSwitch.setChecked(sharedPreferencesSettings.getData(SHARED_LANG_KEY));
    }

    @OnClick(R.id.switchToEN)
    void saveSwitchState() {
        sharedPreferencesSettings.putData(SHARED_LANG_KEY, mSwitch.isChecked());
    }

    @OnClick(R.id.search_button)
    void searchFieldButtonClicked() {
        searchPresenter.editSearchField(editText.getText().toString());
    }

    @Override
    public void initRecyclerView(FoundMovie[] search) {
        final RecyclerView mRecyclerView = findViewById(R.id.search_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SearchRecyclerAdapter mAdapter =
                new SearchRecyclerAdapter(search, getApplicationContext(), mSwitch);
        mRecyclerView.setAdapter(mAdapter);
        searchPresenter.recyclerViewListener(mAdapter);
    }

    @Override
    public void setNotificationField(String totalResult) {
        mNotificationField.setText(String.format("Total amount: %s", totalResult));
        mNotificationField.setTextSize(20);
    }

    @Override
    public Boolean getSwitchValue() {
        return mSwitch.isChecked();
    }

    @Override
    public void setProgressBarVisible() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProgressBarInvisible() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAddedMovieToast(String movieTitle) {
        Toast.makeText(SearchActivity.this, "Movie \""+ movieTitle
                + "\" added to the wish list", Toast.LENGTH_SHORT).show();
    }
}