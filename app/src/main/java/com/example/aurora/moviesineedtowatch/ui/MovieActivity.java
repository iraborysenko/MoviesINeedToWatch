package com.example.aurora.moviesineedtowatch.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.API;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.EN;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_PATH;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_SIZE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMDb_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.Q;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.RU;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TMDB_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.ruLocale;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:43
 */
public class MovieActivity extends AppCompatActivity {

    TextView mTitle;
    TextView mOTitle;
    TextView mIMDb;
    TextView mTMDb;
    TextView mTagline;
    TextView mYear;
    TextView mRuntime;
    TextView mGenres;
    TextView mOverview;
    TextView mCountries;
    TextView mCompanies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movie);

        mTitle = findViewById(R.id.title);
        mOTitle = findViewById(R.id.otitle);
        mIMDb = findViewById(R.id.imdb);
        mTMDb = findViewById(R.id.tmdb);
        mTagline = findViewById(R.id.tagline);
        mYear = findViewById(R.id.year);
        mRuntime = findViewById(R.id.runtime);
        mGenres = findViewById(R.id.genres);
        mOverview = findViewById(R.id.overview);
        mCountries = findViewById(R.id.countries);
        mCompanies = findViewById(R.id.companies);

        //CONNECTION PART
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new TMDBMovieManager().execute();
        } else {
            TextView textView = new TextView(this);
            textView.setText("No network connection.");
            setContentView(textView);
            Log.e(Const.ERR, "stepErr");
        }
        //END of CONNECTION PART
    }

    private class TMDBMovieManager extends AsyncTask {

        @Override
        protected MovieBuilder doInBackground(Object... params) {
            try {
                return search();
            } catch (IOException e) {
                return null;
            }
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        protected void onPostExecute(Object result) {
            MovieBuilder movie = (MovieBuilder) result;

            mTitle.setText(movie.getTitle());
            mOTitle.setText(String.format("%s %s", movie.getOriginalTitle(), movie.getOriginalLanguage()));
            mTMDb.setText(Float.toString(movie.getVoteAverage()));
            mTagline.setText(movie.getTagline());
            mRuntime.setText(String.format("%d min", movie.getRuntime()));
            mYear.setText(movie.getReleaseDate().subSequence(0, 4));
            String genresString = "";
            if(movie.getGenresIds().isEmpty())
            {
                genresString = "not defined";
            } else {
                for (Integer genreId: movie.getGenresIds()) {
                    genresString += genres.get(genreId)[0] + "\n";
                }
            }
            mGenres.setText(String.valueOf(genresString));
            mOverview.setText(movie.getOverview());
            String countriesString = "";
            if(movie.getCountrs().isEmpty())
            {
                countriesString = "not defined";
            } else {
                for (String countryId : movie.getCountrs()) {
                    Locale obj = new Locale("", countryId);
                    countriesString += obj.getDisplayCountry(ruLocale) + "\n";
                }
            }
            mCountries.setText(countriesString);
            String companiesString = "";
            if(movie.getComps().isEmpty() )
            {
                companiesString = "not defined";
            } else {
                for (String company : movie.getComps()) {
                    companiesString += company + "\n";
                }
            }
            mCompanies.setText(companiesString);
            String imagePath = IMAGE_PATH + IMAGE_SIZE[3] + movie.getPosterPath();
            Log.e(Const.SEE, imagePath);
            new DownloadImageTask((ImageView) findViewById(R.id.poster))
                    .execute(imagePath);
            new IMDbrating(mIMDb).execute(movie.getImdbID());
            Log.e(Const.DEBUG, "we're on the onPostExecute");
        }

        MovieBuilder search() throws IOException {

//            String stringBuilder = TMDB_MOVIE + "1268" + Q + "api_key=" + API.KEY;
            String stringBuilder = TMDB_MOVIE + "585" + EN + "api_key=" + API.KEY;
            URL url = new URL(stringBuilder);
            Log.e(Const.TAG,url.toString());

            InputStream stream = null;
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.connect();
                int responseCode = conn.getResponseCode();
                Log.e(Const.DEBUG, "The response code is: " + responseCode + " " + conn.getResponseMessage());
                stream = conn.getInputStream();
                return parseResult(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private MovieBuilder parseResult(String result) {
            MovieBuilder movie_data = null;
            try {
                JSONObject jsonMovieObject = new JSONObject(result);

                //parsing genres ids
                JSONArray ids = jsonMovieObject.getJSONArray("genres");
                ArrayList<Integer> arrGenres = new ArrayList<>();
                for (int i = 0; i < ids.length(); i++) {
                    JSONObject jObject = ids.getJSONObject(i);
                    arrGenres.add(Integer.parseInt(jObject.getString("id")));
                }

                //parsing production companies
                JSONArray companies = jsonMovieObject.getJSONArray("production_companies");
                ArrayList<String> arrCompanies = new ArrayList<>();
                for (int i = 0; i < companies.length(); i++) {
                    JSONObject jObject = companies.getJSONObject(i);
                    arrCompanies.add(jObject.getString("name"));
                }

                //parsing production countries
                JSONArray countries = jsonMovieObject.getJSONArray("production_countries");
                ArrayList<String> arrCountries = new ArrayList<>();
                for (int i = 0; i < countries.length(); i++) {
                    JSONObject jObject = countries.getJSONObject(i);
                    arrCountries.add(jObject.getString("iso_3166_1"));
                }

                MovieBuilder.Builder movieBuilder = MovieBuilder.newBuilder(
                        Integer.parseInt(jsonMovieObject.getString("id")),
                        jsonMovieObject.getString("title"))
                        .setImdbID(jsonMovieObject.getString("imdb_id"))
                        .setOriginalTitle(jsonMovieObject.getString("original_title"))
                        .setOriginalLanguage(jsonMovieObject.getString("original_language"))
                        .setOverview(jsonMovieObject.getString("overview"))
                        .setPosterPath(jsonMovieObject.getString("poster_path"))
                        .setReleaseDate(jsonMovieObject.getString("release_date"))
                        .setTagline(jsonMovieObject.getString("tagline"))
                        .setRuntime(Integer.parseInt(jsonMovieObject.getString("runtime")))
                        .setVoteAverage(Float.parseFloat(jsonMovieObject.getString("vote_average")))
                        .setVoteCount(Integer.parseInt(jsonMovieObject.getString("vote_count")))
                        .setGenresIds(arrGenres)
                        .setComps(arrCompanies)
                        .setCountrs(arrCountries);
                movie_data = movieBuilder.build();
            } catch (JSONException e) {
                System.err.println(e);
                Log.e(Const.DEBUG, "Error parsing JSON. String was: " + result);
            }
            return movie_data;
        }

        String stringify(InputStream stream) throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap img = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class IMDbrating extends AsyncTask<String, Void, String>{
        TextView mIMDb;

        IMDbrating(TextView mIMDb) {
            this.mIMDb = mIMDb;
        }

        protected String doInBackground(String... urls) {
            String imdbId = urls[0];
            Document doc = null;
            try {
                doc = Jsoup.connect(IMDb_MOVIE + imdbId).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element rating = doc.select("span[itemprop = ratingValue]").first();
            return rating.ownText();
        }

        protected void onPostExecute(String result) {
            mIMDb.setText(result);
            Log.e(Const.DEBUG, "IMDb onPostExecute");
        }
    }
}