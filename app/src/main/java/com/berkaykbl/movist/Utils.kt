package com.berkaykbl.movist

import android.content.Context
import androidx.room.Room
import com.berkaykbl.movist.database.AppDatabase
import com.berkaykbl.movist.database.MovieDao
import com.berkaykbl.movist.database.SettingDao
import com.berkaykbl.movist.database.TvSerieDao

private var db: AppDatabase? = null
private var movieDao: MovieDao? = null
private var tvserieDao: TvSerieDao? = null
private var settingsDao: SettingDao? = null


class Utils {
    fun getMoviePopularURL(page: Int = 1): String =
        "https://api.themoviedb.org/3/movie/popular?language=en-US&page=$page"

    fun getMovieTopRatedURL(page: Int = 1): String =
        "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=$page"

    fun getTvSeriePopularURL(page: Int = 1): String =
        "https://api.themoviedb.org/3/tv/popular?language=en-US&page=$page"

    fun getTvSerieTopRatedURL(page: Int = 1): String =
        "https://api.themoviedb.org/3/tv/top_rated?language=en-US&page=$page"

    fun getMovieSearchURL(page: Int = 1, searchQuery: String) =
        "https://api.themoviedb.org/3/search/movie?query=$searchQuery&include_adult=false&language=en-US&page=$page"

    fun getTvSerieSearchURL(page: Int = 1, searchQuery: String) =
        "https://api.themoviedb.org/3/search/tv?query=$searchQuery&include_adult=false&language=en-US&page=$page"


    fun getAPIKey(): String =
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4M2ZkZmI2MTdiMGY5NDYxZDViMDNkNzE2YTg5MGUwMCIsInN1YiI6IjY2MTJlZmU0MjgzZWQ5MDE3YzFkMWE5ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._NSaWCVwOc5tmrBv9PIfTkqAkkdaYQGj3KeXo92-nUc"

    fun getDB(): AppDatabase? = db
    fun setDB(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "movist-database")
            .allowMainThreadQueries()
            .build()

    fun movieDao() : MovieDao = db!!.movieDao()
    fun tvserieDao() : TvSerieDao = db!!.tvserieDao()
    fun settingDao() : SettingDao = db!!.settingDao()
}