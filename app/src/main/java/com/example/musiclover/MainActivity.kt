package com.example.musiclover

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import saschpe.discogs.Discogs
import saschpe.discogs.model.database.Result
import saschpe.discogs.model.database.Search
import saschpe.discogs.model.release.Release
import saschpe.discogs.service.DatabaseService.Companion.SEARCH_GENRE
import android.transition.Fade
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideActionBarFlashing()

        getDiscogs()

    }

    private fun hideActionBarFlashing() {
        val fade = Fade()
        fade.excludeTarget(R.id.action_bar_container, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = fade
        window.exitTransition = fade
    }

    private fun insertDataInRecyclerView(searchResults: List<Result>?) {

        val resultSize = searchResults?.size
        val list = generateList(searchResults, resultSize)

        recycler_view.adapter = Adapter(list)
        recycler_view.layoutManager = LinearLayoutManager(this)
        //recycler_view.setHasFixedSize(true) //performance optimization when size known
    }

    private fun generateList(searchResults: List<Result>?, resultSize: Int?): List<Item> {
        val list = ArrayList<Item>()

        for (i in 0 until resultSize!!) {
            val thumb = searchResults?.get(i)?.thumb
            val albumName = searchResults?.get(i)?.title
            val year =  if (searchResults?.get(i)?.year != null) searchResults.get(i).year else ""
            val id = searchResults?.get(i)?.id

            val item = Item(thumb!!, albumName!!, year!!, id!!)
            list += item
        }

        return list
    }

    private fun getDiscogs() {
        val discogs = Discogs("MusicLover", key = "XdhiupScYeQScOxuMQVj", secret = "nTqdLXuMTQbIjchjuoAVprTkTDpigTBA")

        // Search the Discogs database for album / artist...

        discogs.database
            .search(hashMapOf(
                SEARCH_GENRE to "Jazz"))
            .enqueue(object : Callback<Search> {
                override fun onFailure(call: Call<Search>, t: Throwable) {}

                override fun onResponse(call: Call<Search>, response: Response<Search>) {
                    val searchResults = response.body()?.results
                    insertDataInRecyclerView(searchResults)
                }})
    }

}