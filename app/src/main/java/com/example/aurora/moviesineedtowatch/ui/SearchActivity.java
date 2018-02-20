package com.example.aurora.moviesineedtowatch.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.API;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.DB;
import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;
import com.example.aurora.moviesineedtowatch.tmdb.SearchBuilder;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.EN;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_PATH;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_SIZE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMDb_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.QUERY;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TMDB_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.TMDB_SEARCH;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.ruLocale;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:41
 */
public class SearchActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search);

        final EditText editText = findViewById(R.id.search_query);
        ImageButton button = findViewById(R.id.search_button);

        editText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    runSearch(editText);
                    Log.e(Const.SEE, "Enter!");
                    return true;
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                runSearch(editText);
            }
        });
    }

    void runSearch(EditText editText) {
        String searchQuery = editText.getText().toString();
        Log.e(Const.DEBUG, searchQuery);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new TMDBSearchManager().execute(searchQuery);
        } else {
            TextView textView = new TextView(SearchActivity.this);
            textView.setText("No network connection.");
            setContentView(textView);
            Log.e(Const.ERR, "stepErr");
        }
    }

    class TMDBSearchManager extends AsyncTask <String, Void, ArrayList<SearchBuilder>>{

        @Override
        protected ArrayList<SearchBuilder> doInBackground(String... params) {
            String searchQuery = params[0];
            try {
                return search(searchQuery);
            } catch (IOException e) {
                return null;
            }
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(ArrayList<SearchBuilder> search) {
            Log.e(Const.DEBUG, String.valueOf(search.size()));

            TableLayout mTable = findViewById(R.id.searchTable);

            for (int i = 0; i < search.size(); i++) {
                RelativeLayout tr = new RelativeLayout(SearchActivity.this);

                final String movieId = String.valueOf(search.get(i).getId());

                //get poster
                ImageView mPoster = new ImageView(SearchActivity.this);
                mPoster.setId(1);
                String imagePath = IMAGE_PATH + IMAGE_SIZE[3] + search.get(i).getPosterPath();
                DownloadImageTask r = (DownloadImageTask) new DownloadImageTask().execute(imagePath);

                try {
                    mPoster.setImageBitmap(r.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //title
                TextView mTitle = new TextView(SearchActivity.this);
                mTitle.setId(2);
                mTitle.setText(search.get(i).getTitle());

                //original title
                TextView mOTitle = new TextView(SearchActivity.this);
                mOTitle.setId(3);
                mOTitle.setText(search.get(i).getOriginalTitle());

                //get genres
                String genresString = "";
                if (search.get(i).getGenreIds().isEmpty()) {
                    genresString = "not defined";
                } else {
                    for (Integer genreId : search.get(i).getGenreIds()) {
                        genresString += genres.get(genreId)[0] + "\n";
                    }
                }
                TextView mGenres = new TextView(SearchActivity.this);
                mGenres.setId(4);
                mGenres.setText(String.valueOf(genresString));

                //get year
                TextView mYear = new TextView(SearchActivity.this);
                mYear.setId(5);
                mYear.setText(search.get(i).getReleaseDate().subSequence(0, 4));

                //get TMDb rating
                TextView mTMDb = new TextView(SearchActivity.this);
                mTMDb.setId(6);
                mTMDb.setText(Float.toString(search.get(i).getVoteAverage()));


                RelativeLayout.LayoutParams posterParams = new RelativeLayout.LayoutParams(230, RelativeLayout.LayoutParams.WRAP_CONTENT);
                posterParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                tr.addView(mPoster, posterParams);

                RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleParams.addRule(RelativeLayout.ALIGN_TOP);
                mTitle.setGravity(Gravity.CENTER);
                tr.addView(mTitle, titleParams);

                RelativeLayout.LayoutParams oTitleParams = new RelativeLayout.LayoutParams(
                        230, RelativeLayout.LayoutParams.WRAP_CONTENT);
                oTitleParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                oTitleParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                tr.addView(mOTitle, oTitleParams);

                RelativeLayout.LayoutParams yearParams = new RelativeLayout.LayoutParams(
                        100, RelativeLayout.LayoutParams.WRAP_CONTENT);
                yearParams.addRule(RelativeLayout.RIGHT_OF, mPoster.getId());
                yearParams.addRule(RelativeLayout.BELOW, mOTitle.getId());
                tr.addView(mYear, yearParams);

                RelativeLayout.LayoutParams genresParams = new RelativeLayout.LayoutParams(
                        170, RelativeLayout.LayoutParams.WRAP_CONTENT);
                genresParams.addRule(RelativeLayout.RIGHT_OF, mOTitle.getId());
                genresParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                tr.addView(mGenres, genresParams);

                RelativeLayout.LayoutParams tmdbParams = new RelativeLayout.LayoutParams(
                        100, RelativeLayout.LayoutParams.WRAP_CONTENT);
                tmdbParams.addRule(RelativeLayout.RIGHT_OF, mGenres.getId());
                tmdbParams.addRule(RelativeLayout.BELOW, mTitle.getId());
                tr.addView(mTMDb, tmdbParams);
                Log.e(Const.SEE, String.valueOf(search.get(i).getId()));
                tr.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        new TMDBMovieManager().execute(movieId);
                    }
                });


                mTable.addView(tr);
            }
            Log.e(Const.DEBUG, "we're on the onPostExecute");
        }

        ArrayList<SearchBuilder> search(String searchQuery) throws IOException {

            String encodedQuery = URLEncoder.encode(searchQuery, "UTF-8");
            String stringBuilder = TMDB_SEARCH + "?api_key=" + API.KEY + QUERY + encodedQuery;

            URL url = new URL(stringBuilder);
            Log.e(Const.SEE, url.toString());

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
                return parseSearch(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private ArrayList<SearchBuilder> parseSearch(String result) {
            ArrayList<SearchBuilder> results = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray array = (JSONArray) jsonObject.get("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonMovieObject = array.getJSONObject(i);
                    JSONArray gjIds = jsonMovieObject.getJSONArray("genre_ids");
                    ArrayList<Integer> gIds = new ArrayList<>();
                    for (int j = 0; j < gjIds.length(); j++) {
                        gIds.add(gjIds.getInt(j));
                    }
                    SearchBuilder.Builder searchBuilder = SearchBuilder.newBuilder(
                            Integer.parseInt(jsonMovieObject.getString("id")),
                            jsonMovieObject.getString("title"))
                            .setOriginalTitle(jsonMovieObject.getString("original_title"))
                            .setPosterPath(jsonMovieObject.getString("poster_path"))
                            .setReleaseDate(jsonMovieObject.getString("release_date"))
                            .setVoteAverage(Float.parseFloat(jsonMovieObject.getString("vote_average")))
                            .setGenreIds(gIds);
                    results.add(searchBuilder.build());
                }
            } catch (JSONException e) {
                System.err.println(e);
                Log.d(Const.DEBUG, "Error parsing JSON. String was: " + result);
            }
            return results;
        }

        String stringify(InputStream stream) throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }

    private class TMDBMovieManager extends AsyncTask <String, Void, MovieBuilder>{

        @Override
        protected MovieBuilder doInBackground(String... params) {
            String movieId = params[0];
            try {
                return movie(movieId);
            } catch (IOException e) {
                return null;
            }
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        protected void onPostExecute(MovieBuilder movie) {
            DB db1 = new DB(SearchActivity.this);
//            SQLiteDatabase db = db1.getWritableDatabase();
//            db1.onUpgrade(db, 1,2);
            db1.addMovie(movie);
            
            Toast.makeText(SearchActivity.this, "Movie added to the wish list", Toast.LENGTH_SHORT).show();
            Log.e(Const.DEBUG, "we're on the onPostExecute of the movie");
        }

        MovieBuilder movie(String movieId) throws IOException {

            String stringBuilder = TMDB_MOVIE + movieId + EN + "api_key=" + API.KEY;
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
                return parseMovie(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private MovieBuilder parseMovie(String result) {
            MovieBuilder movie_data = null;
            try {
                JSONObject jsonMovieObject = new JSONObject(result);

                String id = jsonMovieObject.getString("id");
                String title = jsonMovieObject.getString("title");
                String original_title = jsonMovieObject.getString("original_title");
                String original_language = jsonMovieObject.getString("original_language");
                String poster_path = jsonMovieObject.getString("poster_path");
                String overview = jsonMovieObject.getString("overview");
                String release_data = (String) jsonMovieObject.getString("release_date").subSequence(0, 4);
                String tagline = jsonMovieObject.getString("tagline");
                String runtime = "";
                if(jsonMovieObject.getString("runtime") == "null") {
                    runtime = "none";
                }else {
                    runtime = jsonMovieObject.getString("runtime") + " min";
                }
                float tmdb = Float.parseFloat(jsonMovieObject.getString("vote_average"));
                int vote_count = Integer.parseInt(jsonMovieObject.getString("vote_count"));

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

                //get imdb rating
                String imdbId = "";
                String rating = "";
                if(jsonMovieObject.getString("imdb_id")==null){
                    imdbId = "none";
                    rating = "-";
                } else {
                    imdbId = jsonMovieObject.getString("imdb_id");

                    Document doc = null;
                    try {
                        doc = Jsoup.connect(IMDb_MOVIE + jsonMovieObject.getString("imdb_id")).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Element rat = doc.select("span[itemprop = ratingValue]").first();
                    rating = rat.ownText();
                }

                //get picture bitmap
                String urldisplay = IMAGE_PATH + IMAGE_SIZE[3] + jsonMovieObject.getString("poster_path");

                Bitmap img = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    img = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                MovieBuilder.Builder movieBuilder = MovieBuilder.newBuilder(
                        Integer.parseInt(id), title)
                        .setImdbID(imdbId)
                        .setImdb(rating)
                        .setOriginalTitle(original_title)
                        .setOriginalLanguage(original_language)
                        .setOverview(overview)
                        .setPosterPath(poster_path)
                        .setPosterBitmap(img)
                        .setReleaseDate(release_data)
                        .setTagline(tagline)
                        .setRuntime(runtime)
                        .setVoteAverage(tmdb)
                        .setVoteCount(vote_count)
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

        DownloadImageTask() {
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
        }
    }
}
