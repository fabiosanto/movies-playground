package com.fabiosanto.movies.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.fabiosanto.movies.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope

class MainActivity : AppCompatActivity(), HomeContract.View {
    private var moviesAdapter: MoviesAdapter? = null
    override lateinit var onMovieClickListener: (Int) -> Unit
    override lateinit var onMoreDataNeeded: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val model = HomeModel(Volley.newRequestQueue(this))
        val presenter = HomePresenter(this, model)
        moviesAdapter = MoviesAdapter(model, onMovieClickListener, onMoreDataNeeded)
        recyclerView.adapter = moviesAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
    }

    override fun setUpView() {
    }

    override fun refreshData() {
        moviesAdapter?.notifyDataSetChanged()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}