package cz.muni.fi.pv256.movio.uco374561.providers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio.uco374561.db.MovieContract;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;

/**
 * Created by collfi on 23. 11. 2015.
 */
public class MovieManager {
    //    public static final int COL_WORK_TIME_ID = 0;
//    public static final int COL_WORK_TIME_START_DATE = 1;
//    public static final int COL_WORK_TIME_END_DATE = 2;
    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_NAME_COVER,
            MovieContract.MovieEntry.COLUMN_NAME_POSTER,
            MovieContract.MovieEntry.COLUMN_NAME_TITLE,

    };

    private static final String LOCAL_DATE_FORMAT = "yyyyMMdd";

    private static final String WHERE_ID = MovieContract.MovieEntry._ID + " = ?";
    private static final String WHERE_TITLE = MovieContract.MovieEntry.COLUMN_NAME_TITLE + " = ?";


    private Context mContext;

    public MovieManager(Context context) {
        mContext = context.getApplicationContext();
    }


    public void createMovie(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("movie == null");
        }
        movie.setId(ContentUris.parseId(mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, prepareMovieValues(movie))));
        Log.i("gemovie", movie.getId() + " movie id after insert");
    }

    public ArrayList<Movie> getAll() {
        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, MOVIE_COLUMNS,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<Movie> movies = new ArrayList<>(cursor.getCount());
            while (!cursor.isAfterLast()) {
                movies.add(getMovieFromCursor(cursor));
                Log.i("all", getMovieFromCursor(cursor).getId() + " - " + getMovieFromCursor(cursor).getTitle());
                cursor.moveToNext();
            }
            cursor.close();
            return movies;
        }
        if (cursor != null) {
            cursor.close();
        }
        return new ArrayList<>();
    }

    public Movie getMovie(String id) {
        if (id == null) {
            throw new NullPointerException("movie == null");
        }
        Log.i("getmovie", id + " id v getmovie");
//        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, MOVIE_COLUMNS,
//                WHERE_ID, new String[]{id}, null);
        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, MOVIE_COLUMNS,
                WHERE_TITLE, new String[]{id}, null);
        Log.i("getmovie", cursor.getCount() + " count");

        if (cursor.moveToFirst()) {
            Movie m = null;
            while (!cursor.isAfterLast()) {
                m = getMovieFromCursor(cursor);
                cursor.moveToNext();
            }
            Log.i("getmovie", m.toString());
            cursor.close();
            return m;
        }
        cursor.close();
        return null;
    }

//    public void updateMovie(Movie movie) {
//        if (movie == null) {
//            throw new NullPointerException("movie == null");
//        }
//        if (movie.getId() == null) {
//            throw new IllegalStateException("movie id cannot be null");
//        }
//        if (movie.getTitle() == null) {
//            throw new IllegalStateException("movie title cannot be null");
//        }
//
//        mContext.getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, prepareMovieValues(movie), WHERE_ID, new String[]{String.valueOf(movie.getId())});
//    }


    public void deleteMovie(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("movie == null");
        }
        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, WHERE_TITLE, new String[]{String.valueOf(movie.getTitle())});
//        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, WHERE_ID, new String[]{String.valueOf(movie.getId())});
        movie.setId(null);

    }

    private ContentValues prepareMovieValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_COVER, movie.getCoverPath());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        return values;
    }

    private Movie getMovieFromCursor(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry._ID)));
        movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_TITLE)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE)));
        movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_POSTER)));
        movie.setCoverPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_COVER)));
        return movie;
    }

}