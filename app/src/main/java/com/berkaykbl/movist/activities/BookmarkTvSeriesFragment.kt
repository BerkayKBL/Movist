package com.berkaykbl.movist.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berkaykbl.movist.R
import com.berkaykbl.movist.Utils
import com.berkaykbl.movist.adapter.MovieAdapter
import com.berkaykbl.movist.adapter.TVSerieAdapter
import com.berkaykbl.movist.database.MovieDao
import com.berkaykbl.movist.database.MovieEntity
import com.berkaykbl.movist.database.TvSerieDao
import com.berkaykbl.movist.database.TvSerieEntity
import com.berkaykbl.movist.databinding.FragmentBookmarkDetailsBinding
import com.berkaykbl.movist.model.Movie
import com.berkaykbl.movist.model.TVSerie
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class BookmarkTvSeriesFragment: Fragment() {
    private lateinit var binding: FragmentBookmarkDetailsBinding
    private var tvSerieDao: TvSerieDao? = null
    private var adapter: TVSerieAdapter? = null
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
        tvSerieDao = utils.tvserieDao()
        val allTvSeries = tvSerieDao!!.getAllTvseries()

        binding.title.text = String.format("%s %s",getString(R.string.bookmark), getString(R.string.tv_series))

        getTvSeriesFromList(allTvSeries)

    }


    private fun getTvSeriesFromList(tvseries: List<TvSerieEntity>) {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()

                val tvSerieList = ArrayList<TVSerie>()
                while (tvSerieList.size < tvseries.size) {
                    val tvserie = tvseries.get(tvSerieList.size)
                    val request = Request.Builder()
                        .url(Utils().getTvSerieDetailURL(tvserie.tvserieId))
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
                    val tvserieJSON = gson.fromJson(
                        response,
                        TVSerie::class.java
                    )
                    tvSerieList.add(tvserieJSON)
                }

                withContext(Dispatchers.Main) {
                    adapter =
                        TVSerieAdapter(requireContext(), tvSerieList, tvSerieDao)
                    binding.items.adapter = adapter
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}