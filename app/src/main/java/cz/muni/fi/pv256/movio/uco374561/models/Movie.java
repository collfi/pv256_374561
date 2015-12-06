package cz.muni.fi.pv256.movio.uco374561.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by collfi on 18. 10. 2015.
 */

public class Movie implements Parcelable {


    @SerializedName("poster_path")
    String coverPath;
    @SerializedName("original_title")
    String title;
    @SerializedName("release_date")
    String releaseDate;
    @SerializedName("backdrop_path")
    String posterPath;
    @SerializedName("overview")
    String overview;
    Long _id;

    public String getCoverPath() {
        return coverPath;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public Long getId() {
        return _id;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setId(Long id) {
        this._id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (coverPath != null ? !coverPath.equals(movie.coverPath) : movie.coverPath != null)
            return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (releaseDate != null ? !releaseDate.equals(movie.releaseDate) : movie.releaseDate != null)
            return false;
        if (posterPath != null ? !posterPath.equals(movie.posterPath) : movie.posterPath != null)
            return false;
        return !(overview != null ? !overview.equals(movie.overview) : movie.overview != null);

    }

    @Override
    public int hashCode() {
        int result = coverPath != null ? coverPath.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coverPath);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeValue(this._id);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.coverPath = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this._id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

