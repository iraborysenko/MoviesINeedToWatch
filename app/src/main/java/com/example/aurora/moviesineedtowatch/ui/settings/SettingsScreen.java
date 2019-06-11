package com.example.aurora.moviesineedtowatch.ui.settings;

import android.app.Activity;

/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 10/06/19
 * Time: 20:37
 */
public interface SettingsScreen {

    interface View {

    }

    interface Presenter {

        void exportDb();

        void importDb(Activity activity);
    }

}
