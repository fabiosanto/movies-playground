package com.fabiosanto.movies.home

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.fabiosanto.movies.MovieDBRouter
import kotlinx.coroutines.*
import org.json.JSONArray

class HomeModel(private var queue: RequestQueue) : HomeContract.Model {

    private var currentPage = 1
    override var list: ArrayList<Any> = arrayListOf(Row.HeaderItem())
    private val supervisorJob = Job()
    private val modelScope = CoroutineScope(Dispatchers.IO + supervisorJob)

    private suspend fun addData(newData: JSONArray, onDataReady: () -> Unit) {

        val def = modelScope.async { getAdapterData(newData) }

        list.addAll(def.await())
        withContext(Dispatchers.Main) {
            onDataReady()
        }
    }

    private fun getAdapterData(newData: JSONArray): ArrayList<Any> {
        return arrayListOf<Any>().apply {
            for (i in 0 until newData.length()) {
                with(newData.getJSONObject(i)) {
                    this@apply.add(Row.MovieItem(MovieViewState(this.getString("title"),
                            "${MovieDBRouter.IMAGE_BASE}${this.getString("poster_path")}")))
                }
            }
        }
    }

    override fun loadMovies(onDataReady: () -> Unit, onRequestFailed: (String) -> Unit) {
        queue.add(MovieDBRouter(Request.Method.GET, MovieDBRouter.DISCOVER, { modelScope.launch { addData(it, onDataReady) } }, Response.ErrorListener {
            onRequestFailed(it.message ?: "Sorry, something went wrong")
        }))
    }

    override fun loadNextPage(onDataReady: () -> Unit, onRequestFailed: (String) -> Unit) {
        currentPage++

        queue.add(MovieDBRouter(Request.Method.GET, MovieDBRouter.DISCOVER + MovieDBRouter.PAGE + currentPage, { modelScope.launch { addData(it, onDataReady) } }, Response.ErrorListener {
            onRequestFailed(it.message ?: "Sorry, something went wrong")
        }))
    }
}

class MovieViewState(val title: String, val posterUrl: String)

sealed class Row {
    class LoadingItem : Row()
    class MovieItem(val movieData: MovieViewState) : Row()
    class HeaderItem : Row()
}