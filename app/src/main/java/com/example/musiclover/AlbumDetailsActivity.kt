package com.example.musiclover

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tracklist_part.view.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class AlbumDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        //image is low resolution maybe load it again through get discogs.release..
        val itemImage = intent.getStringExtra(Adapter.ViewHolder.ALBUM_IMAGE_URL_KEY)
        Picasso.get().load(itemImage).into(detail_image)

        val albumTitle = intent.getStringExtra(Adapter.ViewHolder.ALBUM_TITLE_KEY)
        supportActionBar?.title = albumTitle

        val id = intent.getIntExtra(Adapter.ViewHolder.ALBUM_ID_KEY, -1)
        println(id)

        getDiscogsRelease(id, albumTitle)
        hideActionBarFlashing()
    }

    private fun onArtistClick(artistId: Int?, artistName: String?) {
        artist_info.setOnClickListener {
            val intent = Intent(this, ArtistActivity::class.java)
            intent.putExtra(ARTIST_ID_KEY, artistId)
            intent.putExtra(ARTIST_NAME_KEY, artistName)
            startActivity(intent)
        }
    }

    private fun getDiscogsRelease(id: Int, albumTitle: String) {
        val key = "?key=XdhiupScYeQScOxuMQVj"
        val secret = "&secret=nTqdLXuMTQbIjchjuoAVprTkTDpigTBA"

        val url = "https://api.discogs.com/releases/" + id + key + secret
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                val searchResults = gson.fromJson(body, ReleaseDetails::class.java)
                println(searchResults)

                val artistName = searchResults.artists.get(0).name
                val artistId = searchResults.artists.get(0).id
                val highResImage = searchResults.images.get(0).resource_url
                val genreText = searchResults.genres.joinToString()
                val countryText = searchResults.country
                val yearText = searchResults.year
                val trackList = searchResults.tracklist



                onArtistClick(artistId, artistName)
                runOnUiThread {
                    Picasso.get().load(highResImage).placeholder(detail_image.drawable)
                        .into(detail_image)
                    getTracksAndDurations(trackList)
                    albumtitle.text = albumTitle
                    genre.text = genreText
                    country.text = countryText
                    year.text = yearText.toString()
                    artist_info.visibility = View.VISIBLE
                    artist_info.text = artistName
                    tracklistDescription.visibility = View.VISIBLE
                }


            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("failed to execute request")
            }
        })
    }

    private fun getTracksAndDurations(trackList: List<Track>?) {
        if (trackList != null) {
            tracklist.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            for (track in trackList) {
                addTrackAndDuration(track.title, track.duration)

                println(track.title)
                println(track.duration)
            }
        }
    }

    private fun addTrackAndDuration(trackName: String?, duration: String?) {
        val inflater = LayoutInflater.from(this)
        val layout =
            inflater.inflate(R.layout.tracklist_part, tracklist, false) as LinearLayout
        layout.tracklistName.text = trackName
        layout.tracklistDuration.text = duration
        tracklist.addView(layout)
    }

    //FIX: toolbar back button does not use transition animation
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition();
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideActionBarFlashing() {
        val fade = Fade()
        fade.excludeTarget(R.id.action_bar_container, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = fade
        window.exitTransition = fade
    }

    companion object {
        val ARTIST_ID_KEY = "ARTIST_ID"
        val ARTIST_NAME_KEY = "ARTIST_NAME"
    }

}

class ReleaseDetails(
    val artists: List<Artist>, val images: List<Image>, val genres: List<String>,
    val country: String, val year: Int, val tracklist: List<Track>
)

class Artist(val name: String, val id: Int)

class Image(val resource_url: String)

class Track(val title: String, val duration: String)