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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDiscogs()

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
            val country = searchResults?.get(i)?.country

            val item = Item(thumb!!, albumName!!, country!!)
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
                    println(searchResults)
                    insertDataInRecyclerView(searchResults)

                }})

        // Query a particular release...
        discogs.release
        .release("123141231")
        .enqueue(object : Callback<Release> {
            override fun onFailure(call: Call<Release>, t: Throwable) {}

            override fun onResponse(call: Call<Release>, response: Response<Release>) {
                val release = response.body()
            }})
    }

}