package id.ac.binus.movies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("Year")
    @Expose
    private String year;

    @SerializedName("imdbID")
    @Expose
    private String imdb_id;

    @SerializedName("Poster")
    @Expose
    private String urlToImage;

    public Movie(){

    }

    public Movie(String title, String year, String imdb_id, String urlToImage) {
        this.title = title;
        this.year = year;
        this.imdb_id = imdb_id;
        this.urlToImage = urlToImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}
