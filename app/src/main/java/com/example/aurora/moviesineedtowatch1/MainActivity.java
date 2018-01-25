package com.example.aurora.moviesineedtowatch1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import com.example.aurora.moviesineedtowatch1.MovieBuilder.Builder;

import static com.example.aurora.moviesineedtowatch1.Const.genres;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new TMDBQueryManager().execute();
        } else {
            TextView textView = new TextView(this);
            textView.setText("No network connection.");
            setContentView(textView);
            Log.e(Const.ERR, "stepErr");
        }

    }

    private class TMDBQueryManager extends AsyncTask {

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

        public MovieBuilder search() throws IOException {
            // Build URL
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/movie/585");
            stringBuilder.append("?api_key=" + API.KEY);
            URL url = new URL(stringBuilder.toString());
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
            String streamAsString = result;
            MovieBuilder movie_data = null;
            try {
                JSONObject jsonMovieObject = new JSONObject(streamAsString);
//                JSONArray array = (JSONArray) jsonObject.get("results");
//                JSONArray arr = jsonObject.getJSONArray("movie_data");
//                for (int i = 0; i < jsonMovieObject.length(); i++) {
//                    JSONObject jsonMovieObject = jsonObject.getJSONObject(i);
                JSONArray items = jsonMovieObject.getJSONArray("genres");
                Log.e(Const.ERR, Integer.toString(items.length()));
                Builder movieBuilder = MovieBuilder.newBuilder(
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
                        .setVoteCount(Integer.parseInt(jsonMovieObject.getString("vote_count")));
                movie_data = movieBuilder.build();

                Log.e(Const.ERR, movieBuilder.build().getTitle());
                Log.e(Const.ERR, movie_data.getImdbID());
                Log.e(Const.ERR, movie_data.getOverview());

            } catch (JSONException e) {
                System.err.println(e);
                Log.e(Const.DEBUG, "Error parsing JSON. String was: " + streamAsString);
            }
            return movie_data;
        }

        public String stringify(InputStream stream) throws IOException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }

    }
}