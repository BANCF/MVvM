package com.example.mvvm.api

import com.example.mvvm.response.DetailesMovieResponse
import com.example.mvvm.response.MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page")page: Int): Call<MovieListResponse>

    @GET("movie/{movie_id}")
    fun getMoviedetailes(@Path("movie_id")id : Int) : Call<DetailesMovieResponse>
}