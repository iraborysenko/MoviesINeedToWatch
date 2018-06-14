package com.example.aurora.moviesineedtowatch.ui;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.lang;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.Movie;

import org.json.JSONArray;
import org.json.JSONException;

import io.realm.Realm;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:43
 */
public class MovieActivity extends AppCompatActivity {

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

    private Realm mRealm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_movie);

        mTitle = findViewById(R.id.title);
        mOTitle = findViewById(R.id.otitle);
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

        mRealm = MainActivity.getRealm();

        String movieId = getIntent().getStringExtra("EXTRA_MOVIE_ID");
        String dataLang = getIntent().getStringExtra("EXTRA_DATA_LANG");
        setMovieInfo(movieId, dataLang);
    }

    private void setMovieInfo(String movieId, String dataLang) {

        Movie curMovie = mRealm.where(Movie.class)
                .equalTo("id", movieId)
                .equalTo("savedLang", dataLang)
                .findFirst();
        assert curMovie != null;

        mTitle.setText(curMovie.getTitle());
        mOTitle.setText(String.format("%s %s", curMovie.getOriginalLanguage(), curMovie.getOriginalTitle()));
        mTMDb.setText(curMovie.getVoteAverage());
        mIMDb.setText(curMovie.getImdb());
        mTagline.setText(curMovie.getTagline());
        mRuntime.setText(curMovie.getRuntime());
        mYear.setText(curMovie.getReleaseDate());
        mOverview.setText(curMovie.getOverview());

        //get poster
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this)
                .asBitmap()
                .load(stringToBitmap(curMovie.getPosterBitmap()))
                .apply(options)
                .into(mImage);

        //get genres
        StringBuilder genresString = new StringBuilder();
        try {
            JSONArray ids = new JSONArray(curMovie.getGenresIds());
            if (ids.length() == 0) {
                genresString = new StringBuilder("not defined");
            } else {
                int index = (Objects.equals(dataLang, "true"))?0:1;
                for (int i=0; i<ids.length(); i++)
                    genresString.append(genres.get(ids.get(i))[index]).append("\n");
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
                    countriesString.append(obj.getDisplayCountry(lang.get(dataLang))).append("\n");
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

    public static String bitmapToString(Bitmap in){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(),Base64.DEFAULT);
    }
    public static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}