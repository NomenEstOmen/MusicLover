package com.example.musiclover

import android.os.Bundle
import android.transition.Fade
import androidx.appcompat.app.AppCompatActivity
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

        //getDiscogs()
        recycler_view.layoutManager = LinearLayoutManager(this)
        fetchDiscogsJson()
    }

    private fun hideActionBarFlashing() {
        val fade = Fade()
        fade.excludeTarget(R.id.action_bar_container, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = fade
        window.exitTransition = fade
    }

    private fun fetchDiscogsJson() {
        val url =
            "https://api.discogs.com/database/search?key=XdhiupScYeQScOxuMQVj" +
                    "&secret=nTqdLXuMTQbIjchjuoAVprTkTDpigTBA&genre=Jazz"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                val searchResults = gson.fromJson(body, SearchResults::class.java)

                runOnUiThread {
                    recycler_view.adapter = Adapter(searchResults)
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("failed to execute request")
            }
        })

    }

}

class SearchResults(val results: List<Album>)

class Album(val thumb: String, val title: String, val year: String, val id: Int)