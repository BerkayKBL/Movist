package com.berkaykbl.movist.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.berkaykbl.movist.Utils
import com.berkaykbl.movist.adapter.MovieAdapter
import com.berkaykbl.movist.database.AppDatabase
import com.berkaykbl.movist.database.MovieDao
import com.berkaykbl.movist.database.MovieEntity
import com.berkaykbl.movist.databinding.FragmentMoviesBinding
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


class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding
    private var lastPage = 0
    private var moviesList = ArrayList<Movie>()
    private var adapter: MovieAdapter? = null
    private var isSearch = false
    private var movieDao : MovieDao? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val utils = Utils()
        getDataFromApi(utils.getMoviePopularURL())

        movieDao = utils.movieDao()

        binding.topRatedButton.setOnClickListener {
            if (lastPage == 0) {
                getDataFromApi(utils.getMovieTopRatedURL())
                lastPage = 1
            }
        }
        binding.popularButton.setOnClickListener {
            if (lastPage == 1) {
                getDataFromApi(utils.getMoviePopularURL())
                lastPage = 0
            }
        }

        binding.searchMovie.setOnSearchClickListener {
            if (!isSearch) {
                isSearch = true
                binding.topToolbar.layoutParams.height = 0
                binding.movies.adapter = null
            }
        }

        binding.searchMovie.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                var url = utils.getMoviePopularURL()
                if (lastPage == 1) url = utils.getMovieTopRatedURL()
                getDataFromApi(url)
                binding.topToolbar.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                return false
            }
        })

        binding.searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val searchQuery = query.split(" ").joinToString("+")
                    getDataFromApi(utils.getMovieSearchURL(searchQuery = searchQuery))
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }


    private fun getDataFromApi(apiURL: String) {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(apiURL)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4M2ZkZmI2MTdiMGY5NDYxZDViMDNkNzE2YTg5MGUwMCIsInN1YiI6IjY2MTJlZmU0MjgzZWQ5MDE3YzFkMWE5ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._NSaWCVwOc5tmrBv9PIfTkqAkkdaYQGj3KeXo92-nUc"
                    )
                    .build()

                val res = client.newCall(request).execute()
                val response = JSONObject(res.body!!.string())
                withContext(Dispatchers.Main) {
                    if (response.length() > 0) {
                        val movies = response.getJSONArray("results")
                        val gson = Gson()
                        moviesList.clear()
                        while (moviesList.size < movies.length()) {
                            val person = gson.fromJson(
                                movies.get(moviesList.size).toString(),
                                Movie::class.java
                            )
                            moviesList.add(person)
                        }
                        adapter =
                            MovieAdapter(requireContext(), moviesList, movieDao)
                        binding.movies.adapter = adapter
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}