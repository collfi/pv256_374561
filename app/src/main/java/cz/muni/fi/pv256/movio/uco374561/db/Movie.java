package cz.muni.fi.pv256.movio.uco374561.db;

/**
 * Created by collfi on 18. 10. 2015.
 */
public class Movie {
    String coverPath;
    String title;
    long releaseDate;

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
}
