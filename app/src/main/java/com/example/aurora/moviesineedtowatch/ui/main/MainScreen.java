package com.example.aurora.moviesineedtowatch.ui.main;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 16/07/18
 * Time: 20:05
 */
public interface MainScreen {
    interface View {
        void showMovies();
    }

    interface Presenter {
        void loadMovies();
    }
}
