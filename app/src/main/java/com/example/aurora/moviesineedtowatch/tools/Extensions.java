package com.example.aurora.moviesineedtowatch.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.aurora.moviesineedtowatch.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by Android Studio.
 * User: Iryna
 * Date: 06/05/19
 * Time: 21:39
 */
public class Extensions {

    public static String returnTotalResultString(String totalSearchResult) {
        return String.format("Total amount: %s", totalSearchResult);
    }

    public static String returnAddMovieString(String movieTitle) {
        return "Movie \""+ movieTitle + "\" added to the wish list";
    }

    public static String bitmapToString(Bitmap in){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(),Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static int chooseColor(String imdbRating) {
        int color = R.color.OutOfBound;
        if (imdbRating.equals("none")) {
            color = R.color.NoMovie;
        } else {
            float rating = Float.parseFloat(imdbRating);
            if (rating<5.0f) {
                color = R.color.VeryBad;
            } else if (5.0f<= rating && rating<=5.9f) {
                color = R.color.Bad;
            } else if (6.0f<= rating && rating<=6.5f) {
                color = R.color.Avarage;
            } else if (6.6f<= rating && rating<=6.8f) {
                color = R.color.AboveAvarage;
            } else if (6.9f<= rating && rating<=7.2f) {
                color = R.color.Intermediate;
            } else if (7.3f<= rating && rating<=7.7f) {
                color = R.color.Good;
            } else if (7.8f<= rating && rating<=8.1f) {
                color = R.color.VeryGood;
            } else if (8.2f<= rating && rating<=8.5f) {
                color = R.color.Great;
            } else if (8.6f<= rating && rating<=8.9f) {
                color = R.color.Adept;
            } else if ( rating >= 9.0f) {
                color = R.color.Unicum;
            }
        }
        return color;
    }

    public static boolean isOffline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue != 0);
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) { e.printStackTrace(); }

        return true;
    }
}