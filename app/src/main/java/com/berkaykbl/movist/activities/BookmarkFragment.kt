package com.berkaykbl.movist.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.berkaykbl.movist.R
import com.berkaykbl.movist.Utils
import com.berkaykbl.movist.database.MovieDao
import com.berkaykbl.movist.database.TvSerieDao
import com.berkaykbl.movist.databinding.FragmentBookmarkBinding
import com.berkaykbl.movist.databinding.FragmentMoviesBinding
import com.bumptech.glide.load.engine.Resource

class BookmarkFragment: Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private var movieDao: MovieDao? = null
    private var tvSerieDao: TvSerieDao? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val utils = Utils()
        movieDao = utils.movieDao()
        tvSerieDao = utils.tvserieDao()
        val allMovies = movieDao!!.getAllMovies()
        val allTvseries = tvSerieDao!!.getAllTvseries()

        binding.movieText.text = String.format("%s %s",allMovies.size, getString(R.string.movie))
        binding.tvserieText.text = String.format("%s %s",allTvseries.size, getString(R.string.tv_serie))

        binding.bookmarkMovies.setOnClickListener {
            val navController = utils.getNavController()
            navController!!.navigate(R.id.action_to_BookmarkMovies)
        }

        binding.bookmarkTvSeries.setOnClickListener {
            val navController = utils.getNavController()
            navController!!.navigate(R.id.action_to_BookmarkTvSeries)
        }
    }
}