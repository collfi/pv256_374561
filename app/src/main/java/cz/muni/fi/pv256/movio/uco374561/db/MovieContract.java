package cz.muni.fi.pv256.movio.uco374561.db;

import android.provider.BaseColumns;

/**
 * Created by collfi on 22. 11. 2015.
 */
public class MovieContract {
    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_COVER = "cover";
        public static final String COLUMN_NAME_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_NAME_POSTER = "poster";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
    }
}
