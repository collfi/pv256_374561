package cz.muni.fi.pv256.movio.uco374561;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cz.muni.fi.pv256.movio.uco374561.db.Movie;

/**
 * Created by collfi on 18. 10. 2015.
 */
public class DetailActivity extends AppCompatActivity {
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        mMovie = new Movie();
        mMovie.setCoverPath(i.getStringExtra("cover"));
        mMovie.setReleaseDate(i.getLongExtra("release", -1));
        mMovie.setTitle(i.getStringExtra("title"));
    }
}
