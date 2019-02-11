package com.fabiosanto.movies.home

class HomeContract {

    interface View {
        fun setUpView()
        fun refreshData()
        var onMovieClickListener: (Int) -> Unit
        fun showToast(message: String)
    }

    interface Model {
        val dataSize: Int
        fun getMovieAt(index: Int): MovieViewState?
        fun loadMovies(onDataReady: () -> Unit, onRequestFailed: (String) -> Unit)
    }
}

class HomePresenter(private val view: HomeContract.View, private val model: HomeContract.Model) {

    init {
        view.setUpView()
        view.onMovieClickListener = { onMovieClick(it) }
        model.loadMovies({ view.refreshData() }, { })
    }

    fun onMovieClick(index: Int) {
        model.getMovieAt(index)?.let {
            view.showToast(it.title)
        }
    }
}