package com.berkaykbl.movist.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berkaykbl.movist.R
import com.berkaykbl.movist.Utils
import com.berkaykbl.movist.database.MovieDao
import com.berkaykbl.movist.database.TvSerieDao
import com.berkaykbl.movist.databinding.FragmentBookmarkBinding
import com.berkaykbl.movist.databinding.FragmentMoviesBinding
import com.bumptech.glide.load.engine.Resource

class BookmarkFragment: Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var movieDao: MovieDao
    private lateinit var tvSerieDao: TvSerieDao
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(layoutInflater)
        val utils = Utils()
        movieDao = utils.movieDao()
        tvSerieDao = utils.tvserieDao()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allMovies = movieDao.getAllMovies()
        val allTvseries = tvSerieDao.getAllTvseries()

        binding.movieText.text = String.format("%s %s",allMovies.size, getString(R.string.movie))
        binding.tvserieText.text = String.format("%s %s",allTvseries.size, getString(R.string.tv_serie))


    }
}