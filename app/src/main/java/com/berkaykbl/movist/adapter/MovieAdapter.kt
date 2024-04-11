package com.berkaykbl.movist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.berkaykbl.movist.R
import com.berkaykbl.movist.database.MovieDao
import com.berkaykbl.movist.database.MovieEntity
import com.berkaykbl.movist.model.Movie
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class MovieAdapter(var context: Context, var arrayList: List<Movie>, var dao: MovieDao?) :
    BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): Movie {
        return arrayList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.element_movie, null)
        val movieJSON = getItem(position)

        var poster = view.findViewById<ImageView>(R.id.movieImage)
        val title = view.findViewById<TextView>(R.id.movieName)
        val rating = view.findViewById<TextView>(R.id.movieRating)
        val bookmark = view.findViewById<ImageButton>(R.id.movieBookmark)


        val decimalFormat = DecimalFormat("#.#")
        title.text = movieJSON.title
        rating.text = decimalFormat.format(movieJSON.vote_average)

        if (rating.text.length == 1) {
            rating.text = String.format("%s,%s",rating.text.toString(), "0")
        }
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/original" + movieJSON.poster_path)
            .into(poster)

        if (dao != null) {
            var movieEntityList : List<MovieEntity> = dao!!.getMovie(movieJSON.id)
            var movieEntity : MovieEntity? = null
            var hasBookmark: Boolean = false
            if (!movieEntityList.isEmpty()) {
                bookmark.isActivated = true
                movieEntity = movieEntityList.first()
                hasBookmark = true
            }
            bookmark.setOnClickListener {
                if (hasBookmark) {
                    it.isActivated = false
                    dao!!.deleteMovie(movieEntity!!)
                    hasBookmark = false
                } else {
                    it.isActivated = true
                    dao!!.insertMovie(MovieEntity(movieId = movieJSON.id))
                    hasBookmark = true
                }
            }
        }

        return view
    }

}