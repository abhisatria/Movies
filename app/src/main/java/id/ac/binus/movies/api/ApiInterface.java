package id.ac.binus.movies.api;

import id.ac.binus.movies.models.Search;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/")
    Call<Search> getMovies(
            @Query("s") String title,
            @Query("apikey") String apiKey
    );
}
