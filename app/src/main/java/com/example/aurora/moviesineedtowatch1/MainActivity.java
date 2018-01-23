package com.example.aurora.moviesineedtowatch1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.example.aurora.moviesineedtowatch1.MovieResult.Builder;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "values";
    private final String TMDB_API_KEY = "ffd306e2816e78d8da9d87e05072bf55";
    private static final String DEBUG_TAG = "TMDBQueryManager";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        Log.e(TAG, "step");
        // Get the intent to get the query.
        Intent intent = getIntent();
//        String query = intent.getStringExtra(MainActivity.EXTRA_QUERY);

        // Check if the NetworkConnection is active and connected.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.e(TAG, "step1");
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e(TAG, "step2");
            new TMDBQueryManager().execute();
        } else {
            TextView textView = new TextView(this);
            textView.setText("No network connection.");
            setContentView(textView);
            Log.e(TAG, "stepErr");
        }

    }

    private class TMDBQueryManager extends AsyncTask {

        private final String TMDB_API_KEY = "ffd306e2816e78d8da9d87e05072bf55";
        private static final String DEBUG_TAG = "TMDBQueryManager";

        @Override
        protected ArrayList<MovieResult> doInBackground(Object... params) {
            try {
                Log.e(TAG, "step3");
                return search();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            Log.e(TAG, "we're on the onPostExecute");
        }

        ;


        public ArrayList<MovieResult> search() throws IOException {
            // Build URL
            Log.e(TAG, "step4");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/movie/585");
            stringBuilder.append("?api_key=" + TMDB_API_KEY);
//            stringBuilder.append("&query=" + query);
            URL url = new URL(stringBuilder.toString());
            Log.e(TAG, "step5");
            Log.e(TAG,url.toString());

            InputStream stream = null;
            try {
//                // Establish a connection
//                Log.e(TAG, "step6");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                Log.e(TAG, "step6");
//                try {
//                    InputStream in = new BufferedInputStream(conn.getInputStream());
////                    readStream(in);
//                    Log.e(TAG, "step6");
//                    Log.e(DEBUG_TAG, in.toString());
//                } finally {
//                    conn.disconnect();
//                }
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                Log.e(TAG, "step7");
                conn.setConnectTimeout(15000 /* milliseconds */);
                Log.e(TAG, "step7");
                conn.setRequestMethod("GET");
                Log.e(TAG, "step7");
                conn.addRequestProperty("Accept", "application/json"); // Required to get TMDB to play nicely.
                Log.e(TAG, "step7");
                conn.setDoInput(true);
                Log.e(TAG, "step7");
                conn.connect();
                Log.e(TAG, "step7");
                int responseCode = conn.getResponseCode();
                Log.e(DEBUG_TAG, "The response code is: " + responseCode + " " + conn.getResponseMessage());
                Log.e(TAG, "step8");
                stream = conn.getInputStream();
                Log.e(TAG, stream.toString());
                return parseResult(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private  ArrayList<MovieResult> parseResult(String result) {
            String streamAsString = result;
            ArrayList<MovieResult> results = new ArrayList<MovieResult>();
            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                JSONArray array = (JSONArray) jsonObject.get("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonMovieObject = array.getJSONObject(i);
                    Builder movieBuilder = MovieResult.newBuilder(
                            Integer.parseInt(jsonMovieObject.getString("id")),
                            jsonMovieObject.getString("title"))
                            .setBackdropPath(jsonMovieObject.getString("backdrop_path"))
                            .setOriginalTitle(jsonMovieObject.getString("original_title"))
                            .setPopularity(jsonMovieObject.getString("popularity"))
                            .setPosterPath(jsonMovieObject.getString("poster_path"))
                            .setReleaseDate(jsonMovieObject.getString("release_date"));
                    results.add(movieBuilder.build());
                }
            } catch (JSONException e) {
                System.err.println(e);
                Log.e(DEBUG_TAG, "Error parsing JSON. String was: " + streamAsString);
            }
            Log.e(DEBUG_TAG, "step9");
            Log.e(DEBUG_TAG, results.toString());
            return results;
        }

        public String stringify(InputStream stream) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
//        }
    }

    }
}