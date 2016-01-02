package cz.muni.fi.pv256.movio.uco374561.services;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio.uco374561.models.Movie;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by collfi on 17. 11. 2015.
 */
public interface Download {
    @GET("/movie/now_playing")
    ArrayList<Movie> getNowPlaying(@Query("api_key") String key);

    @GET("/discover/movie")
    ArrayList<Movie> getNextWeek(@Query("primary_release_date.gte") String rd,
                                 @Query("primary_release_date.lte") String rd2,
                                 @Query("sort_by") String sort,
                                 @Query("api_key") String key);

    @GET("/movie/{id}")
    Movie getMovie(@Path("id") String id, @Query("api_key") String key);
}
