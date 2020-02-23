package com.example.musiclover

import android.os.Bundle
import android.transition.Fade
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideActionBarFlashing()

        recycler_view.layoutManager = LinearLayoutManager(this)
        searchDiscogs("genre=Jazz")
    }

    private fun hideActionBarFlashing() {
        val fade = Fade()
        fade.excludeTarget(R.id.action_bar_container, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = fade
        window.exitTransition = fade
    }

    private fun searchDiscogs(s: String) {
        val key = "XdhiupScYeQScOxuMQVj"
        val secret = "nTqdLXuMTQbIjchjuoAVprTkTDpigTBA"
        val url =
            "https://api.discogs.com/database/search?key=$key&secret=$secret&$s"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                val searchResults = gson.fromJson(body, SearchResults::class.java)
                println(body)

                runOnUiThread {
                    recycler_view.adapter = Adapter(searchResults)
                    //Adapter(searchResults).notifyDataSetChanged()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("failed to execute request")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search for an Album..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchDiscogs("release_title=$query&type=release")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }
}

class SearchResults(val results: List<Album>)

class Album(val thumb: String, val title: String?, val year: String?, val id: Int?)