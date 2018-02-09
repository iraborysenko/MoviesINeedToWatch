package com.example.aurora.moviesineedtowatch.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.aurora.moviesineedtowatch.R;
import com.example.aurora.moviesineedtowatch.tmdb.Const;
import com.example.aurora.moviesineedtowatch.tmdb.DB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        DB db1 = new DB(this);

        // Inserting Contacts
        db1.addMovie("Ravi", "9100000000");
        db1.addMovie("Srinivas", "9199999999");

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");

        String selectQuery = "SELECT  * FROM " + "contacts";


        SQLiteDatabase db = db1.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.e(Const.SEE, cursor.getString(0));
                Log.e(Const.SEE, cursor.getString(1));

            } while (cursor.moveToNext());
        }


        ImageButton searchButton = findViewById(R.id.main_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                searchTMDB();
            }
        });

//        movieTMDB();
    }

    public void movieTMDB() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    public void searchTMDB() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}