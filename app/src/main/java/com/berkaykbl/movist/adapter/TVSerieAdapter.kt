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
import com.berkaykbl.movist.model.TVSerie
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.DecimalFormat

class TVSerieAdapter(var context: Context, var arrayList: List<TVSerie>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): TVSerie {
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
        title.text = movieJSON.original_name
        rating.text = decimalFormat.format(movieJSON.vote_average)

        if (rating.text.length == 1) {
            rating.text = String.format("%s,%s",rating.text.toString(), "0")
        }
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/original" + movieJSON.poster_path)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(poster)


        bookmark.setOnClickListener {
            it.isActivated = !it.isActivated
        }

        return view
    }

}