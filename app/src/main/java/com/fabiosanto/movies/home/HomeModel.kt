package com.fabiosanto.movies.home

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.fabiosanto.movies.MovieDBRouter
import org.json.JSONArray

class HomeModel(private var queue: RequestQueue) : HomeContract.Model {

    private var movies: JSONArray = JSONArray()

    override val dataSize: Int
        get() = movies.length()


    override fun getMovieAt(index: Int): MovieViewState? {
        return movies.getJSONObject(index)?.let {
            MovieViewState(it.getString("title"),
                    "${MovieDBRouter.IMAGE_BASE}${it.getString("poster_path")}")
        }
    }

    override fun loadMovies(onDataReady: () -> Unit, onRequestFailed: (String) -> Unit) {
        queue.add(MovieDBRouter(Request.Method.GET, MovieDBRouter.DISCOVER, {
            movies = it
            onDataReady()
        }, Response.ErrorListener {
            onRequestFailed(it.message ?: "Sorry, something went wrong")
        }))
    }
}

class MovieViewState(val title: String, val posterUrl: String)