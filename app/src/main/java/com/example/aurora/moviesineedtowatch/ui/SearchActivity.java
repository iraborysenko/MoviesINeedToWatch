package com.example.aurora.moviesineedtowatch.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.API;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.SearchBuilder;

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
import java.util.LinkedList;
import java.util.List;

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

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tlMarksTable);

        List<String> competitoinsIDs = new LinkedList<String>();
        List<String> marks = new LinkedList<String>();
        List<String> numOfQuestions = new LinkedList<String>();


// adding static values to test the layout. use your dynamic data here
        competitoinsIDs.add("123");
        competitoinsIDs.add("124");
        competitoinsIDs.add("125");

        marks.add("56");
        marks.add("57");
        marks.add("58");

        numOfQuestions.add("10");
        numOfQuestions.add("11");
        numOfQuestions.add("12");

        TableRow tableRows[] = new TableRow[competitoinsIDs.size()];
        for (int i = 0; i < tableRows.length; i++) {

            tableRows[i] = new TableRow(this);
            tableRows[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tableRows[i].setWeightSum(2.0f);
            tableRows[i].setPadding(5, 5, 5, 5);

            TextView text = new TextView(this);
            text.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
                    android.widget.TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
            text.setText(competitoinsIDs.get(i));
            tableRows[i].addView(text);

            text = new TextView(this);
            text.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
                    android.widget.TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
            text.setText(marks.get(i));
            tableRows[i].addView(text);

            text = new TextView(this);
            text.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,
                    android.widget.TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
            text.setText(numOfQuestions.get(i));
            tableRows[i].addView(text);

            tableLayout.addView(tableRows[i]);


//        for (int i=0; i<4;i++) {
//            LinearLayout linearLayout = new LinearLayout(this);
//            TextView ProgrammaticallyTextView = new TextView(this);
//            ProgrammaticallyTextView.setText(" TextView Programmatically Example." + i);
//            ProgrammaticallyTextView.setTextSize(22);
//            ProgrammaticallyTextView.setPadding(20, 300, 20, 100);
//
//            linearLayout.addView(ProgrammaticallyTextView);
//
//            this.setContentView(linearLayout, new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        }


            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connMgr != null;
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new TMDBSearchManager().execute();
            } else {
                TextView textView = new TextView(this);
                textView.setText("No network connection.");
                setContentView(textView);
                Log.e(Const.ERR, "stepErr");
            }

        }
    }

        class TMDBSearchManager extends AsyncTask {

            @Override
            protected ArrayList<SearchBuilder> doInBackground(Object... params) {
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

            ArrayList<SearchBuilder> search() throws IOException {
                // Build URL
                String stringBuilder = "http://api.themoviedb.org/3/search/movie" +
                        "?api_key=" + API.KEY + "&query=day%20after%20tomorrow";
                URL url = new URL(stringBuilder);
                Log.e(Const.SEE, url.toString());
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

            private ArrayList<SearchBuilder> parseResult(String result) {
                ArrayList<SearchBuilder> results = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray array = (JSONArray) jsonObject.get("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonMovieObject = array.getJSONObject(i);

                        JSONArray gjIds = jsonMovieObject.getJSONArray("genre_ids");
                        ArrayList<Integer> gIds = new ArrayList<>();
                        for (int j=0; j<gjIds.length(); j++) {
                            gIds.add( gjIds.getInt(j) );
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

                Log.e(Const.SEE, results.get(1).getOriginalTitle());
                Log.e(Const.SEE, results.get(3).getGenreIds().toString());
                return results;
            }


            String stringify(InputStream stream) throws IOException {
                Reader reader = new InputStreamReader(stream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                return bufferedReader.readLine();
            }

        }
    }
