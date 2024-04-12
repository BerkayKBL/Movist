package com.berkaykbl.movist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert
    fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): List<MovieEntity>

    @Update
    fun updateMovie(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE id = :id")
    fun deleteMovie(id: Int)
}


@Dao
interface TvSerieDao {

    @Insert
    fun insertTvserie(tvserie: TvSerieEntity)

    @Query("SELECT * FROM tvseries")
    fun getAllTvseries(): List<TvSerieEntity>

    @Query("SELECT * FROM tvseries WHERE id = :id")
    fun getTvserie(id: Int): List<TvSerieEntity>

    @Update
    fun updateTvserie(tvserie: TvSerieEntity)

    @Query("DELETE FROM tvseries WHERE id = :id")
    fun deleteTvSerie(id: Int)
}

@Dao
interface SettingDao {

    @Insert
    fun insertSetting(setting: SettingEntity)

    @Query("SELECT * FROM settings WHERE setting_key = :key")
    fun getSettingWithListener(key: String): Flow<List<SettingEntity>>

    @Query("SELECT * FROM settings WHERE setting_key = :key")
    fun getSetting(key: String): List<SettingEntity>

    @Update
    fun update(settingsEntity: SettingEntity)

    /*@Query("SELECT * FROM dates WHERE date IN (:month)")
    fun getMonthDate()*/

}