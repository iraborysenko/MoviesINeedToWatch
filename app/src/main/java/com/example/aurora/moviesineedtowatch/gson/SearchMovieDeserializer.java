package com.example.aurora.moviesineedtowatch.gson;

import com.example.aurora.moviesineedtowatch.tmdb.SearchMovieBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 15/03/18
 * Time: 20:28
 */
public class SearchMovieDeserializer implements JsonDeserializer{
    @Override
    public SearchMovieBuilder deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String id = jsonObject.get("id").getAsString();
        String title = jsonObject.get("title").getAsString();
        String originalTitle = jsonObject.get("original_title").getAsString();

        //get poster path
        String posterPath;
        if (jsonObject.get("poster_path").isJsonNull())
            posterPath = "";
        else posterPath = jsonObject.get("poster_path").getAsString();

        //parsing yearParams
        String releaseDate;
        if (Objects.equals(jsonObject.get("release_date").getAsString(), "") || jsonObject.get("release_date").isJsonNull())
            releaseDate = "----";
        else
            releaseDate = (String) jsonObject.get("release_date").getAsString().subSequence(0, 4);

        //parsing tmdb rating
        String tmdb;
        float tmdb_rating = Float.parseFloat(jsonObject.get("vote_average").getAsString());
        if (tmdb_rating == 0.0f) tmdb = "none";
        else tmdb = String.valueOf(tmdb_rating);

        //parsing genres ids
        JsonArray ids = jsonObject.get("genre_ids").getAsJsonArray();
        ArrayList<Integer> arrGenres = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            arrGenres.add(ids.get(i).getAsInt());
        }

        return new SearchMovieBuilder(
                id,
                title,
                originalTitle,
                posterPath,
                releaseDate,
                tmdb,
                arrGenres
        );
    }
}
