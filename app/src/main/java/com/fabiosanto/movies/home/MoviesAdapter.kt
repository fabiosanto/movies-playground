package com.fabiosanto.movies.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabiosanto.movies.R
import com.fabiosanto.movies.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviesAdapter(private val model: HomeContract.Model,
                    private val onMovieClick: (index: Int) -> Unit,
                    private val onMoreDataNeeded: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = parent.inflate(viewType)
        return when (viewType) {
            R.layout.movie_item -> PhotoViewHolder(itemView)
            R.layout.loading_item -> LoadItemHolder(itemView)
            R.layout.header_item -> HeaderItemHolder(itemView)
            else -> throw IllegalStateException("Unknown viewType = $viewType")
        }
    }

    override fun getItemCount() = model.list.size

    override fun getItemViewType(position: Int): Int {
        return when (model.list[position]) {
            is Row.HeaderItem -> R.layout.header_item
            is Row.MovieItem -> R.layout.movie_item
            is Row.LoadingItem -> R.layout.loading_item
            else -> -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> holder.bindData((model.list[position] as Row.MovieItem).movieData)
            is LoadItemHolder -> holder.bind()
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

    inner class LoadItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() {
            onMoreDataNeeded()
        }
    }

    inner class HeaderItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}