package com.lesnyg.movieinfoapp;

import com.lesnyg.movieinfoapp.models.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("3/search/movie")
    Call<Search> getSearch(@Query("api_key") String key, @Query("query") String query, @Query("language") String language);

    @GET("3/movie/upcoming")
    Call<Search> getUpComing(@Query("api_key") String key, @Query("language") String language,@Query("page") int page);

    @GET("3/movie/popular")
    Call<Search> getPopular(@Query("api_key") String key, @Query("language") String language);

    @GET("3/movie/popular")
    Call<Search> getPopular(@Query("api_key") String key, @Query("language") String language, @Query("page") int page);


}



