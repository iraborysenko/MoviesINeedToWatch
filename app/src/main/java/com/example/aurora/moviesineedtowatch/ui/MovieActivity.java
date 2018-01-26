package com.example.aurora.moviesineedtowatch.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.API;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.aurora.moviesineedtowatch.tmdb.Const.genres;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time: 20:43
 */
public class MovieActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    private static class TMDBMovieManager extends AsyncTask {

        @Override
        protected MovieBuilder doInBackground(Object... params) {
            try {
                return search();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            Log.e(Const.DEBUG, "we're on the onPostExecute");
        }

        MovieBuilder search() throws IOException {
            // Build URL
            String stringBuilder = "http://api.themoviedb.org/3/movie/585" +
                    "?api_key=" + API.KEY;
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

            Log.e(Const.SEE, genres.get(12)[0]);
            MovieBuilder movie_data = null;
            try {
                JSONObject jsonMovieObject = new JSONObject(result);

                //parsing genres ids
                JSONArray ids = jsonMovieObject.getJSONArray("genres");
                ArrayList<Integer> arrGenres = new ArrayList<>();
                for (int i = 0; i < ids.length(); i++) {
                    JSONObject jObject = ids.getJSONObject(i);
                    arrGenres.add(Integer.parseInt(jObject.getString("id")));
                    Log.e(Const.ERR, jObject.getString("id"));
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
                Log.e(Const.SEE, arrCountries.toString());

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

                Log.e(Const.ERR, movie_data.getGenresIds().toString());

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
}
