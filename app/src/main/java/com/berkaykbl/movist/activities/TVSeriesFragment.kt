package com.berkaykbl.movist.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.berkaykbl.movist.Utils
import com.berkaykbl.movist.adapter.TVSerieAdapter
import com.berkaykbl.movist.database.MovieDao
import com.berkaykbl.movist.database.TvSerieDao
import com.berkaykbl.movist.databinding.FragmentTvseriesBinding
import com.berkaykbl.movist.model.TVSerie
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class TVSeriesFragment : Fragment() {
    private lateinit var binding: FragmentTvseriesBinding
    private var lastPage = 0
    private var tvSeriesList = ArrayList<TVSerie>()
    private var tvSerieDao : TvSerieDao? = null
    private var adapter: TVSerieAdapter? = null
    private var isSearch = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTvseriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val utils = Utils()
        tvSerieDao = utils.tvserieDao()
        getDataFromApi(utils.getTvSeriePopularURL())


        binding.topRatedButton.setOnClickListener {
            if (lastPage == 0) {
                getDataFromApi(utils.getTvSerieTopRatedURL())
                lastPage = 1
            }
        }
        binding.popularButton.setOnClickListener {
            if (lastPage == 1) {
                getDataFromApi(utils.getTvSeriePopularURL())
                lastPage = 0
            }
        }

        binding.searchTvSerie.setOnSearchClickListener {
            if (!isSearch) {
                isSearch = true
                binding.topToolbar.layoutParams.height = 0
                binding.tvseries.adapter = null
            }
        }

        binding.searchTvSerie.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                var url = utils.getTvSeriePopularURL()
                if (lastPage == 1) url =
                    utils.getTvSerieTopRatedURL()
                getDataFromApi(url)
                binding.topToolbar.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                return false
            }
        })

        binding.searchTvSerie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val searchQuery = query.split(" ").joinToString("+")
                    getDataFromApi(utils.getTvSerieSearchURL(searchQuery = searchQuery))
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
                        Utils().getAPIKey()
                    )
                    .build()


                val res = client.newCall(request).execute()
                val response = JSONObject(res.body!!.string())
                withContext(Dispatchers.Main) {
                    if (response.length() > 0) {
                        val tvseries = response.getJSONArray("results")
                        val gson = Gson()
                        tvSeriesList.clear()
                        while (tvSeriesList.size < tvseries.length()) {
                            val tvserie = gson.fromJson(
                                tvseries.get(tvSeriesList.size).toString(),
                                TVSerie::class.java
                            )
                            tvSeriesList.add(tvserie)
                        }
                        adapter =
                            TVSerieAdapter(requireContext(), tvSeriesList, tvSerieDao)
                        binding.tvseries.adapter = adapter
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}
