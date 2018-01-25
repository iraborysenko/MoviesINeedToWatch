package com.example.aurora.moviesineedtowatch.tmdb;

import java.util.HashMap;

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

            //who knows
            put(105,new String[]{"Disaster","Катастрофа"});
            put(82,new String[]{"Eastern","Западный"});
            put(2916,new String[]{"Erotic","Эротика"});
            put(10750,new String[]{"Fan film","Fan film"});
            put(10753,new String[]{"Film noir","Фильм Нуар"});
            put(878,new String[]{"Fiction","Фэнтези"});
            put(10769,new String[]{"Foreign","Иностранный"});
            put(10595,new String[]{"Holiday","Праздничный"});
            put(10756,new String[]{"Indie","Инди"});
            put(22,new String[]{"Musical","Мюзикл"});
            put(10754,new String[]{"Neo-noir","Нео Нуар"});
            put(1115,new String[]{"Road movie","Дорожное кино"});
            put(10755,new String[]{"Short","Короткий"});
            put(9805,new String[]{"Sport","Спорт"});
            put(10758,new String[]{"Sporting event","Спортивное событие"});
            put(10757,new String[]{"Sport film","Спортивный фильм"});
            put(10748,new String[]{"Suspense","Неизвестный"});
        }
    };
    final static String TMDB_MOVIE = "http://api.themoviedb.org/3/movie/";
    final static String IMAGE_PATH = "https://image.tmdb.org/t/p/";
    final static String IMAGE_SIZE = "w640/"; //or array?
    final static String EN = "language=en-US";
    final static String RU = "language=ru-RU";
    final static String AMPERSAND = "&";
    final static HashMap<String, String[]> countries = new HashMap<String, String[]>() {
        {
            put("AU",new String[]{"Australia","Австралия"});
            put("CA",new String[]{"Canada","Канада"});
            put("DE",new String[]{"Denmark","Германия"});
            put("FR",new String[]{"France","Франция"});
            put("GB",new String[]{"England","Англия"});
            put("IN",new String[]{"India","Индия"});
            put("NZ",new String[]{"New Zealand","Новая Зеландия"});
            put("US",new String[]{"USA","США"});
        }
    };

}
