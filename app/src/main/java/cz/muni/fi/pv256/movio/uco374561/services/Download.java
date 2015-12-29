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
    //    .url("http://api.themoviedb.org/3/movie/now_playing?api_key=" + "c331638cd30b7ab8a4b73dedbbb62193")
//    @GET("/movie?primary_release_date.gte=2015-11-09&primary_release_date.lte=2015-11-16&sort_by=avg_rating.desc&api_key=" + "c331638cd30b7ab8a4b73dedbbb62193")
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
