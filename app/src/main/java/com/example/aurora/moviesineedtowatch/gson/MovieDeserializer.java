package com.example.aurora.moviesineedtowatch.gson;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.MovieBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static android.content.Context.MODE_PRIVATE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_PATH;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMAGE_SIZE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.IMDb_MOVIE;
import static com.example.aurora.moviesineedtowatch.tmdb.Const.SHARED_REFERENCES;

import android.content.Context;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 15/03/18
 * Time: 18:41
 */

public class MovieDeserializer implements JsonDeserializer<MovieBuilder> {
    private Context cntxt;

    public MovieDeserializer(Context context){
        this.cntxt=context;
    }

    @Override
    public MovieBuilder deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String id = jsonObject.get("id").getAsString();
        String title = jsonObject.get("title").getAsString();
        String originalTitle = jsonObject.get("original_title").getAsString();
        String originalLanguage = jsonObject.get("original_language").getAsString();
        String overview = jsonObject.get("overview").getAsString();
        String tagline = jsonObject.get("tagline").getAsString();

        //get language
        SharedPreferences settings = cntxt.getSharedPreferences(SHARED_REFERENCES, MODE_PRIVATE);
        boolean s = settings.getBoolean("lang_key", false);

        String savedLang = String.valueOf(s);

        //parsing yearParams
        String releaseDate;
        if (Objects.equals(jsonObject.get("release_date").getAsString(), ""))
            releaseDate = "----";
        else
            releaseDate = (String) jsonObject.get("release_date").getAsString().subSequence(0, 4);

        //parsing runtime
        String runtime;
        String jsonRuntime = jsonObject.get("runtime").getAsString();
        if(Objects.equals(jsonRuntime, "null") || Objects.equals(jsonRuntime, "0")) {
            runtime = "unknown";
        }else {
            int duration = Integer.parseInt(jsonObject.get("runtime").getAsString());
            int hours = duration / 60;
            int minutes = duration % 60;
            if (s)
                runtime = hours + "h " + minutes + "min";
            else
                runtime = hours + "ч " + minutes + "мин";
        }

        String tmdb;
        float tmdb_rating = Float.parseFloat(jsonObject.get("vote_average").getAsString());
        if (tmdb_rating == 0.0f) tmdb = "none";
        else tmdb = String.valueOf(tmdb_rating);

        int voteCount = jsonObject.get("vote_count").getAsInt();

        //get imdb rating
        String imdbId;
        String rating;
        String json_IMDB_result = jsonObject.get("imdb_id").getAsString();
        if(json_IMDB_result.equals("null") || json_IMDB_result.equals("") ){
            imdbId = "none";
            rating = "none";
        } else {
            imdbId = jsonObject.get("imdb_id").getAsString();

            Document doc = null;
            try {
                doc = Jsoup.connect(IMDb_MOVIE + json_IMDB_result).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert doc != null;
            if (doc.select("span[itemprop = ratingValue]").first() == null) {
                rating = "none";
            } else {
                Element rat = doc.select("span[itemprop = ratingValue]").first();
                rating = rat.ownText();
            }
        }

        //get picture bitmap
        String posterPath = jsonObject.get("poster_path").getAsString();
        Bitmap img = null;
        try {
            if (!Objects.equals(posterPath, "null")) {
                String urldisplay = IMAGE_PATH + IMAGE_SIZE[3] + posterPath;
                RequestOptions options = new RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);

                img = Glide
                        .with(cntxt)
                        .asBitmap()
                        .load(urldisplay)
                        .apply(options)
                        .submit()
                        .get();


            } else img = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.noposter);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        //parsing genres ids
        JsonArray ids = jsonObject.get("genres").getAsJsonArray();
        ArrayList<Integer> arrGenres = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            JsonObject jObject = ids.get(i).getAsJsonObject();
            arrGenres.add(jObject.get("id").getAsInt());
        }

        //parsing production companies
        JsonArray companies = jsonObject.get("production_companies").getAsJsonArray();
        ArrayList<String> arrCompanies = new ArrayList<>();
        for (int i = 0; i < companies.size(); i++) {
            JsonObject jObject = companies.get(i).getAsJsonObject();
            arrCompanies.add(jObject.get("name").getAsString());
        }

        //parsing production countries
        JsonArray countries = jsonObject.get("production_countries").getAsJsonArray();
        ArrayList<String> arrCountries = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            JsonObject jObject = countries.get(i).getAsJsonObject();
            arrCountries.add(jObject.get("iso_3166_1").getAsString());
        }

        return new MovieBuilder(
                id,
                imdbId,
                rating,
                title,
                originalTitle,
                originalLanguage,
                overview,
                posterPath,
                img,
                releaseDate,
                tagline,
                runtime,
                tmdb,
                voteCount,
                arrGenres,
                arrCompanies,
                arrCountries,
                savedLang
        );
    }
}
