package com.example.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.adapter.MovieAdapter
import com.example.mvvm.api.ApiClient
import com.example.mvvm.api.ApiService
import com.example.mvvm.databinding.ActivityMainBinding
import com.example.mvvm.response.MovieListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieAdapter by lazy {MovieAdapter()}
    private val api: ApiService by lazy{
        ApiClient().getClient().create(ApiService::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            prgBarMovie.visibility = View.VISIBLE

            val callMovieApi = api.getPopularMovie(1)
            callMovieApi.enqueue(object : Callback<MovieListResponse>{
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    prgBarMovie.visibility = View.GONE
                    when(response.code()){
                        //successful response
                        in 200..299 ->{
                            response.body().let { itBody ->
                                itBody?.results.let { itData ->
                                    if(itData!!.isNotEmpty()){
                                        movieAdapter.differ.submitList(itData)
                                        rvMovie.apply {
                                            layoutManager = LinearLayoutManager(this@MainActivity)
                                            adapter = movieAdapter
                                        }
                                    }
                                }
                            }
                        }

                        //redirection message
                        in 300 .. 399 -> {
                            Log.d("Response Code", "Redirection message : ${response.code()}")
                        }

                        //client error response
                        in 400..499 ->  {
                            Log.d("Response Code", "Client error message : ${response.code()}")
                        }

                        //server error response
                        in  500..599 -> {
                            Log.d("Response Code", "Server error message : ${response.code()}")
                        }

                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                   binding.prgBarMovie.visibility = View.GONE
                    Log.d("onFailure", "Err : ${t.message}")

                }


            })

        }
    }
}

