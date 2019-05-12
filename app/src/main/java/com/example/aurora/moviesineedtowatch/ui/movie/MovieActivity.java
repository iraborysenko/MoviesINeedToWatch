package com.example.aurora.moviesineedtowatch.ui.movie;

import static com.example.aurora.moviesineedtowatch.tools.Constants.genres;
import static com.example.aurora.moviesineedtowatch.tools.Constants.lang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.dagger.blocks.moviescreen.DaggerMovieScreenComponent;
import com.example.aurora.moviesineedtowatch.dagger.blocks.moviescreen.MovieScreenModule;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;
import com.example.aurora.moviesineedtowatch.tools.Extensions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:43
 */
public class MovieActivity extends AppCompatActivity implements MovieScreen.View {

    private TextView mTitle;
    private TextView mOTitle;
    private TextView mIMDb;
    private TextView mTMDb;
    private ImageView mImage;
    private TextView mTagline;
    private TextView mYear;
    private TextView mRuntime;
    private TextView mGenres;
    private TextView mOverview;
    private TextView mCountries;
    private TextView mCompanies;
    private TextView mReleaseDate;

    @Inject
    MoviePresenter moviePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_movie);

        mTitle = findViewById(R.id.title);
        mOTitle = findViewById(R.id.original_title);
        mImage = findViewById(R.id.poster);
        mIMDb = findViewById(R.id.imdb);
        mTMDb = findViewById(R.id.tmdb);
        mTagline = findViewById(R.id.tagline);
        mYear = findViewById(R.id.year);
        mRuntime = findViewById(R.id.runtime);
        mGenres = findViewById(R.id.genres);
        mOverview = findViewById(R.id.overview);
        mCountries = findViewById(R.id.countries);
        mCompanies = findViewById(R.id.companies);
        mReleaseDate = findViewById(R.id.release_date);

        DaggerMovieScreenComponent.builder()
                .movieScreenModule(new MovieScreenModule(this))
                .build().inject(this);

        String movieId = getIntent().getStringExtra("EXTRA_MOVIE_ID");
        String dataLang = getIntent().getStringExtra("EXTRA_DATA_LANG");

        moviePresenter.loadSelectedMovie(movieId, dataLang);
    }

    @Override
    public void setMovieInfo(Movie curMovie) {

        mTitle.setText(curMovie.getTitle());
        mOTitle.setText(String.format("%s %s", curMovie.getOriginalLanguage(), curMovie.getOriginalTitle()));
        mTMDb.setText(curMovie.getVoteAverage());
        mIMDb.setText(curMovie.getImdb());
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
        mGenres.setText(String.valueOf(genresString.toString()));

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
    }
}