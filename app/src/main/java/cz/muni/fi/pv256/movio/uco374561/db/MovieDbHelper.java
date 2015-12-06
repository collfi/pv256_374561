package cz.muni.fi.pv256.movio.uco374561.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cz.muni.fi.pv256.movio.uco374561.db.MovieContract.MovieEntry;

/**
 * Created by collfi on 22. 11. 2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies";

    
    private static final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME +  " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieEntry.COLUMN_NAME_TITLE + " TEXT , " +
            MovieEntry.COLUMN_NAME_POSTER + " TEXT, " +
            MovieEntry.COLUMN_NAME_COVER + " TEXT, " +
            MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
            MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT);";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_MOVIE_TABLE);
        onCreate(db);
    }
}
