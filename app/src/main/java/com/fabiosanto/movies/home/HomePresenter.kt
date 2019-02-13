package com.fabiosanto.movies.home

class HomeContract {

    interface View {
        fun setUpView()
        fun refreshData()
        fun showToast(message: String)
        var onMovieClickListener: (Int) -> Unit
        var onMoreDataNeeded: () -> Unit
    }

    interface Model {
        val list: ArrayList<Any>
        fun loadMovies(onDataReady: () -> Unit, onRequestFailed: (String) -> Unit)
        fun loadNextPage(onDataReady: () -> Unit, onRequestFailed: (String) -> Unit)
    }
}

class HomePresenter(private val view: HomeContract.View, private val model: HomeContract.Model) {

    init {
        view.setUpView()
        view.onMovieClickListener = { onMovieClick(it) }
        view.onMoreDataNeeded = { model.loadNextPage({ view.refreshData() }, { }) }
        model.loadMovies({ view.refreshData() }, { })
    }

    private fun onMovieClick(index: Int) {
        view.showToast((model.list[index] as Row.MovieItem).movieData.title)
    }
}