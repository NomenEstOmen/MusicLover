package com.example.musiclover

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_artist.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        val artistId = intent.getIntExtra(AlbumDetailsActivity.ARTIST_ID_KEY, -1)
        val artistTitle = intent.getStringExtra(AlbumDetailsActivity.ARTIST_NAME_KEY)
        supportActionBar?.title = artistTitle
        getDiscogsArtistInfo(artistId, artistTitle)
    }

    private fun getDiscogsArtistInfo(artistId: Int, artistTitle: String) {
        val key = "XdhiupScYeQScOxuMQVj"
        val secret = "nTqdLXuMTQbIjchjuoAVprTkTDpigTBA"
        val url = "https://api.discogs.com/artists/$artistId?key=$key&secret=$secret"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val searchResults = gson.fromJson(body, ArtistDetail::class.java)
                println(searchResults)

                val artistProfile = searchResults.profile
                runOnUiThread {
                    val artistImage = searchResults.images?.get(0)?.resource_url
                    Picasso.get().load(artistImage).into(detail_image)
                    artist_name.text = artistTitle
                    artist_profile.text = artistProfile
                }
            }
            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }
}

class ArtistDetail(val profile: String?, val images: List<Image>?)


