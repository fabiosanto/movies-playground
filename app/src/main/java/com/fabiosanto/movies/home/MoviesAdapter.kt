package com.fabiosanto.movies.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabiosanto.movies.R
import com.fabiosanto.movies.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo_item.view.*

class MoviesAdapter(private val model: HomeContract.Model, private val onMovieClick: (index: Int)-> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = PhotoViewHolder(parent.inflate(R.layout.photo_item))

    override fun getItemCount() = model.dataSize

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        model.getMovieAt(position)?.let {
            (holder as PhotoViewHolder).bindData(it)
        }
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { onMovieClick(adapterPosition) }
        }

        fun bindData(movieState: MovieViewState) {
            Picasso.get().load(movieState.posterUrl).into(itemView.image)
        }
    }
}