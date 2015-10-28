package cz.muni.fi.pv256.movio.uco374561.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by collfi on 18. 10. 2015.
 */

public class Movie implements Parcelable {
    String coverPath;
    String title;
    long releaseDate;
    String posterPath;
    String overview;

    public Movie() {
    }

    public Movie(String coverPath, String title, long releaseDate, String posterPath, String overview) {
        this.coverPath = coverPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coverPath);
        dest.writeString(this.title);
        dest.writeLong(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
    }

    private Movie(Parcel in) {
        this.coverPath = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readLong();
        this.posterPath = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

