package com.example.aurora.moviesineedtowatch.ui.manage;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.dagger.blocks.managescreen.DaggerManageScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.managescreen.ManageScreenModule;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tools.Extensions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.example.aurora.moviesineedtowatch.tools.Constants.genres;
import static com.example.aurora.moviesineedtowatch.tools.Constants.lang;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 02/03/18
 * Time: 18:41
 */
public class ManageActivity extends AppCompatActivity implements ManageScreen.View {

    @BindView(R.id.poster) ImageView mImage;
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.original_title) TextView mOTitle;
    @BindView(R.id.imdb) TextView mImdb;
    @BindView(R.id.tagline) TextView mTagline;
    @BindView(R.id.year) TextView mYear;
    @BindView(R.id.runtime) TextView mRuntime;
    @BindView(R.id.genres) TextView mGenres;
    @BindView(R.id.overview) TextView mOverview;
    @BindView(R.id.countries) TextView mCountries;
    @BindView(R.id.companies) TextView mCompanies;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.comment) TextView mComment;
    @BindView(R.id.type_comment_edit_text) EditText mEditTextComment;
    @BindView(R.id.save_comment_button) ImageButton mSaveCommentButton;
    @BindView(R.id.my_rating) TextView mMyRating;
    @BindView(R.id.rating_picker) NumberPicker mRatingPicker;
    @BindView(R.id.move_movie_button) Button mMoveMovieButton;

    @Inject
    ManagePresenter mPresenter;

    final String[] values= {"0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String movieId;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_manage);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorWatchedTab));

        ButterKnife.bind(this);

        DaggerManageScreenComponent.builder()
                .manageScreenModule(new ManageScreenModule(this))
                .build().inject(this);

        setupRatingPicker();

        mEditTextComment.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                hideEditText();
                return true;
            }
            return false;
        });

        movieId = getIntent().getStringExtra("EXTRA_MOVIE_ID");
        mPresenter.loadWatchedMovie(movieId);
    }

    private void setupRatingPicker() {
        mRatingPicker.setMinValue(0);
        mRatingPicker.setMaxValue(values.length-1);
        mRatingPicker.setValue(values.length-3);
        mRatingPicker.setDisplayedValues(values);
        mRatingPicker.setWrapSelectorWheel(true);
    }

    @OnClick(R.id.rating_picker)
    void setMyRating() {
        String selected = values[mRatingPicker.getValue()];
        mMyRating.setText(selected);
        mMyRating.setBackgroundColor(getResources()
                .getColor(Extensions.chooseColor(String.valueOf(mRatingPicker.getValue()))));
        mRatingPicker.setVisibility(View.GONE);
    }

    @OnLongClick(R.id.my_rating)
    boolean displayRatingPicker() {
        mRatingPicker.setVisibility(View.VISIBLE);
        return false;
    }

    @OnLongClick(R.id.comment)
    boolean displayEditText() {
        mComment.setVisibility(View.GONE);
        mEditTextComment.setVisibility(View.VISIBLE);
        mSaveCommentButton.setVisibility(View.VISIBLE);
        Extensions.showSoftKeyboard(this, mEditTextComment);
        return false;
    }

    @OnClick(R.id.save_comment_button)
    void hideEditText() {
        Extensions.hideSoftKeyboard(this, mSaveCommentButton);
        mEditTextComment.setVisibility(View.GONE);
        mSaveCommentButton.setVisibility(View.GONE);
        mComment.setVisibility(View.VISIBLE);
        mComment.setText(mEditTextComment.getText().toString());
    }

    @OnClick(R.id.move_movie_button)
    void saveUserData() {
        String commentStr = null;
        String myRatingStr = null;
        if(!Objects.equals(mComment.getText().toString(), getResources().getString(R.string.type_comment)))
            commentStr = mComment.getText().toString();
        if(!Objects.equals(mMyRating.getText().toString(), getResources().getString(R.string.my_rating)))
            myRatingStr = mMyRating.getText().toString();
        mPresenter.saveUserData(movieId, commentStr, myRatingStr);
        finish();
    }

    @Override
    public void setMovieInfo(Movie curMovie) {
        mTitle.setText(curMovie.getTitle());
        mOTitle.setText(String.format("%s %s", curMovie.getOriginalLanguage(), curMovie.getOriginalTitle()));
        mImdb.setText(curMovie.getImdb());
        mRuntime.setText(curMovie.getRuntime());
        mReleaseDate.setText(curMovie.getReleaseDate());

        //get year
        mYear.setText(curMovie.getReleaseDate().subSequence(0, 4));

        //get tagline
        if (!curMovie.getTagline().equals(""))
            mTagline.setText(curMovie.getTagline());
        else mTagline.setVisibility(View.GONE);

        //get overview
        if (!curMovie.getOverview().equals(""))
            mOverview.setText(curMovie.getOverview());
        else mOverview.setVisibility(View.GONE);

        //get poster
        RequestOptions options = new RequestOptions()
                .error(R.drawable.noposter)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this)
                .asBitmap()
                .load(Extensions.stringToBitmap(curMovie.getPosterBitmap()))
                .apply(options)
                .into(mImage);

        //get genres
        StringBuilder genresString = new StringBuilder();
        try {
            JSONArray ids = new JSONArray(curMovie.getGenresIds());
            if (ids.length() == 0) {
                genresString = new StringBuilder("not defined");
            } else {
                int index = (Objects.equals(curMovie.getSavedLang(), "true"))?0:1;
                for (int i=0; i<ids.length(); i++)
                    genresString.append(Objects.requireNonNull(genres.get(ids.get(i)))[index]).append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mGenres.setText(genresString.toString());

        //get countries
        StringBuilder countriesString = new StringBuilder();
        try {
            JSONArray ids = new JSONArray(curMovie.getCountrsArr());
            if (ids.length() == 0) {
                countriesString = new StringBuilder("not defined");
            } else {
                for (int i=0; i<ids.length(); i++) {
                    Locale obj = new Locale("", ids.get(i).toString());
                    countriesString.append(obj.getDisplayCountry(lang.get(curMovie.getSavedLang()))).append("\n");
                }
            }
            mCountries.setText(countriesString.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //get companies
        StringBuilder companiesString = new StringBuilder();
        if (Objects.equals(curMovie.getCompsArr(), "[]")) {
            companiesString = new StringBuilder("not defined");
        } else {
            String delims = ", |\\[|]";
            String[] tokens = curMovie.getCompsArr().split(delims);
            for (String token : tokens) companiesString.append(token).append("\n");
        }
        mCompanies.setText(companiesString.toString().trim());

        //get comment
        if (curMovie.getComment()!=null)
            mComment.setText(curMovie.getComment());

        //get rating
        if (curMovie.getMyRating()!=null) {
            mMyRating.setBackgroundColor(getResources()
                    .getColor(Extensions.chooseColor(String.valueOf(curMovie.getMyRating()))));
            mMyRating.setText(curMovie.getMyRating());
        } else mMyRating.setText(getResources().getString(R.string.my_rating));
    }
}
