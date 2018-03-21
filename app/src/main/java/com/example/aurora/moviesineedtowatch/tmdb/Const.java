package com.example.aurora.moviesineedtowatch.tmdb;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 25/01/18
 * Time:
 */

public class Const {
    public final static String DEBUG = "TMDBQM";
    public final static String TAG = "TAG";
    public final static String ERR = "VALUES";
    public final static String SEE = "LOOK HERE";

    public final static String TMDB_BASE = "http://api.themoviedb.org/3/";
    public final static String IMDb_MOVIE = "http://www.imdb.com/title/";
    public final static String IMAGE_PATH = "https://image.tmdb.org/t/p/";
    public final static String[] IMAGE_SIZE = {"w92", "w154", "w185", "w342", "w500", "w780", "original"}; //https://api.themoviedb.org/3/movie/1948/images?api_key=###
    public final static String EN = "en-US";
    public final static String RU = "ru-RU";
    public final static String SHARED_REFERENCES = "com.moviestowatch.PREFERENCE_FILE_KEY";

    public final static HashMap<String, Locale> lang = new HashMap<String, Locale>() {
        {
            put("false", new Locale("ru"));
            put("true", Locale.ENGLISH);
        }

    };

    public final static HashMap<Integer, String[]> genres = new HashMap<Integer, String[]>() {
        {
            put(28,new String[]{"Action","Боевик"});
            put(12,new String[]{"Adventure","Приключения"});
            put(16,new String[]{"Animation","Мультфильм"});
            put(35,new String[]{"Comedy","Комедия"});
            put(80,new String[]{"Crime","Криминал"});
            put(99,new String[]{"Documentary","Документальный"});
            put(18,new String[]{"Drama","Драма"});
            put(10751,new String[]{"Family","Семейный"});
            put(14,new String[]{"Fantasy","Фэнтези"});
            put(36,new String[]{"History","История"});
            put(27,new String[]{"Horror","Ужасы"});
            put(10402,new String[]{"Music","Музыка"});
            put(9648,new String[]{"Mystery","Детектив"});
            put(10749,new String[]{"Romance","Мелодрама"});
            put(878,new String[]{"Science fiction","Фантастика"});
            put(10770,new String[]{"TV movie","Телевизионный фильм"});
            put(53,new String[]{"Thriller","Триллер"});
            put(10752,new String[]{"War","Военный"});
            put(37,new String[]{"Western","Вестерн"});
        }
    };
}
