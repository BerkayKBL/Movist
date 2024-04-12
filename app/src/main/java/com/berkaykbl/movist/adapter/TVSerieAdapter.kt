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
import com.berkaykbl.movist.database.TvSerieDao
import com.berkaykbl.movist.database.TvSerieEntity
import com.berkaykbl.movist.model.TVSerie
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.DecimalFormat

class TVSerieAdapter(var context: Context, var arrayList: List<TVSerie>, var dao: TvSerieDao?) :
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
        val view: View = View.inflate(context, R.layout.element_tvserie, null)
        val tvSerieJSON = getItem(position)

        var poster = view.findViewById<ImageView>(R.id.tvserieImage)
        val title = view.findViewById<TextView>(R.id.tvserieName)
        val rating = view.findViewById<TextView>(R.id.tvserieRating)
        val bookmark = view.findViewById<ImageButton>(R.id.tvserieBookmark)

        val decimalFormat = DecimalFormat("#.#")
        title.text = tvSerieJSON.original_name
        rating.text = decimalFormat.format(tvSerieJSON.vote_average)

        if (rating.text.length == 1) {
            rating.text = String.format("%s,%s",rating.text.toString(), "0")
        }
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/original" + tvSerieJSON.poster_path)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(poster)


        if (dao != null) {
            var tvSerieEntityList : List<TvSerieEntity> = dao!!.getTvserie(tvSerieJSON.id)
            var tvSerieEntity : TvSerieEntity? = null
            var hasBookmark: Boolean = false
            if (tvSerieEntityList.isNotEmpty()) {
                bookmark.isActivated = true
                tvSerieEntity = tvSerieEntityList.first()
                hasBookmark = true
            }
            bookmark.setOnClickListener {
                if (hasBookmark) {
                    it.isActivated = false
                    dao!!.deleteTvSerie(tvSerieJSON.id)
                    hasBookmark = false
                } else {
                    it.isActivated = true
                    dao!!.insertTvserie(TvSerieEntity(tvserieId = tvSerieJSON.id))
                    hasBookmark = true
                }
            }
        }

        return view
    }

}