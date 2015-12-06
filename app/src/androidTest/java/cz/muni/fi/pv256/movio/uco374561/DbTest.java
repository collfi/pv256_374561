package cz.muni.fi.pv256.movio.uco374561;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio.uco374561.db.MovieContract;
import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import cz.muni.fi.pv256.movio.uco374561.providers.MovieManager;

/**
 * Created by collfi on 22. 11. 2015.
 */
public class DbTest extends AndroidTestCase {

    private static final String TAG = "Movie";


    private MovieManager mManager;

    @Override
    protected void setUp() throws Exception {
       mManager = new MovieManager(mContext);
    }

    @Override
    public void tearDown() throws Exception {
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI, null,null);
        super.tearDown();
    }

    public void testAll() throws Exception {
        List<Movie> movies = new ArrayList<>(2);
        Movie m1 = createMovie("Movie1", "poster", "cover", "date", "overview");
        Movie m2 = createMovie("Movie2", "poster", "cover", "date", "overview");
        movies.add(m1);
        movies.add(m2);
        mManager.createMovie(m1);
        mManager.createMovie(m2);
        List<Movie> result = mManager.getAll();
        assertTrue(result.size() == 2);
        assertEquals(movies, result);
    }

    public void testGet() throws Exception {
        Movie m = createMovie("Movie", "poster", "cover", "date", "overview");
        mManager.createMovie(m);
        Movie result = mManager.getMovie(m.getTitle());
        assertEquals(m, result);
    }

    public void testDelete() throws Exception {
        Movie m = createMovie("Movie", "poster", "cover", "date", "overview");
        mManager.createMovie(m);
        mManager.deleteMovie(m);
        Movie result = mManager.getMovie(m.getTitle());
        assertNull(result);
    }

    private Movie createMovie(String title, String poster, String cover, String date, String overview) {
        Movie m = new Movie();
        m.setCoverPath(cover);
        m.setOverview(overview);
        m.setPosterPath(poster);
        m.setTitle(title);
        m.setReleaseDate(date);
        return m;
    }
}