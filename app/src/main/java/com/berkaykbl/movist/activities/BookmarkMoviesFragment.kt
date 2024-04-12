package com.berkaykbl.movist.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berkaykbl.movist.R
import com.berkaykbl.movist.Utils
import com.berkaykbl.movist.adapter.MovieAdapter
import com.berkaykbl.movist.database.MovieDao
import com.berkaykbl.movist.database.MovieEntity
import com.berkaykbl.movist.database.TvSerieDao
import com.berkaykbl.movist.databinding.FragmentBookmarkBinding
import com.berkaykbl.movist.databinding.FragmentBookmarkDetailsBinding
import com.berkaykbl.movist.model.Movie
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class BookmarkMoviesFragment: Fragment() {
    private lateinit var binding: FragmentBookmarkDetailsBinding
    private var movieDao: MovieDao? = null
    private var adapter: MovieAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkDetailsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val utils = Utils()
        movieDao = utils.movieDao()
        val allMovies = movieDao!!.getAllMovies()

        binding.title.text = String.format("%s %s",getString(R.string.bookmark), getString(R.string.movies))

        getMoviesFromList(allMovies)

    }


    private fun getMoviesFromList(movies: List<MovieEntity>) {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()

                val movieList = ArrayList<Movie>()
                while (movieList.size < movies.size) {
                    val movie = movies.get(movieList.size)
                    val request = Request.Builder()
                        .url(Utils().getMovieDetailURL(movie.movieId))
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader(
                            "Authorization",
                            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4M2ZkZmI2MTdiMGY5NDYxZDViMDNkNzE2YTg5MGUwMCIsInN1YiI6IjY2MTJlZmU0MjgzZWQ5MDE3YzFkMWE5ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._NSaWCVwOc5tmrBv9PIfTkqAkkdaYQGj3KeXo92-nUc"
                        )
                        .build()
                    val res = client.newCall(request).execute()
                    val response = res.body!!.string()
                    val gson = Gson()
                    val movieJSON = gson.fromJson(
                        response,
                        Movie::class.java
                    )
                    movieList.add(movieJSON)
                }

                withContext(Dispatchers.Main) {
                    adapter =
                        MovieAdapter(requireContext(), movieList, movieDao)
                    binding.items.adapter = adapter
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}