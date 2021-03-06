package com.example.aurora.moviesineedtowatch.gson;

import com.example.aurora.moviesineedtowatch.tmdb.FoundMovie;
import com.example.aurora.moviesineedtowatch.tmdb.SearchResult;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 15/03/18
 * Time: 20:05
 */
public class SearchResultDeserializer implements JsonDeserializer{
    @Override
    public SearchResult deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String totalResults = jsonObject.get("total_results").getAsString();

        FoundMovie[] results = context.deserialize(jsonObject.get("results"),
                FoundMovie[].class);

        return new SearchResult(
                totalResults,
                results
        );
    }
}
