package com.berkaykbl.movist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "movies",
    indices = [Index(value = ["id"], unique = true)])
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "id")
    val movieId: Int,
)


@Entity(tableName = "tvseries",
    indices = [Index(value = ["id"], unique = true)])
data class TvSerieEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "id")
    val tvserieId: Int,
)

@Entity(
    tableName = "settings",
    indices = [Index(value = ["setting_key"], unique = true)]
)
data class SettingEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "setting_key")
    val key: String,
    @ColumnInfo(name = "setting_value")
    var value: String,
    @ColumnInfo(name = "setting_extra")
    val extra: String = "",
)