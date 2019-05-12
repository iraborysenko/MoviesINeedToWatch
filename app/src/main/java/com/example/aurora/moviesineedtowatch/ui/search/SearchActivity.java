package com.example.aurora.moviesineedtowatch.ui.search;

import static com.example.aurora.moviesineedtowatch.tools.Constants.MESSAGE_NO_SEARCH_DATA;
import static com.example.aurora.moviesineedtowatch.tools.Constants.SHARED_LANG_KEY;

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
import com.example.aurora.moviesineedtowatch.adaprers.SearchRecyclerAdapter;
import com.example.aurora.moviesineedtowatch.dagger.SharedPreferencesSettings;
import com.example.aurora.moviesineedtowatch.dagger.blocks.searchscreen.DaggerSearchScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.module.ContextModule;
import com.example.aurora.moviesineedtowatch.dagger.module.SharedPreferencesModule;
import com.example.aurora.moviesineedtowatch.dagger.blocks.searchscreen.SearchScreenModule;
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
    @BindView(R.id.search_recycler_view) RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search);

        DaggerSearchScreenComponent.builder()
                .searchScreenModule(new SearchScreenModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .contextModule(new ContextModule(this))
                .build().inject(this);

        ButterKnife.bind(this);

        editText.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                verifyEditTextValue();
                return true;
            }
            return false;
        });

        mSwitch.setChecked(sharedPreferencesSettings.getData(SHARED_LANG_KEY));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchPresenter.clearDisposable();
    }

    @OnClick(R.id.switchToEN)
    void saveSwitchState() {
        sharedPreferencesSettings.putData(SHARED_LANG_KEY, mSwitch.isChecked());
    }

    @OnClick(R.id.search_button)
    void searchFieldButtonClicked() {
        verifyEditTextValue();
    }

    @Override
    public void initRecyclerView(FoundMovie[] search) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SearchRecyclerAdapter mAdapter =
                new SearchRecyclerAdapter(search, getApplicationContext(), mSwitch);
        mRecyclerView.setAdapter(mAdapter);
        searchPresenter.recyclerViewListener(mAdapter);
    }

    @Override
    public void setNotificationField(String requiredString) {
        mNotificationField.setText(requiredString);
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
    public void showAddedMovieToast(String movieAddMessage) {
        Toast.makeText(SearchActivity.this, movieAddMessage, Toast.LENGTH_SHORT).show();
    }

    private void verifyEditTextValue() {
        if (!editText.getText().toString().equals(""))
            searchPresenter.editSearchField(editText.getText().toString());
        else
            Toast.makeText(SearchActivity.this, MESSAGE_NO_SEARCH_DATA,
                    Toast.LENGTH_SHORT).show();
    }
}