package com.example.musiclover

import android.os.Bundle
import android.transition.Fade
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import saschpe.discogs.Discogs
import saschpe.discogs.model.release.Release
import saschpe.discogs.model.release.Tracklist

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

    private fun getDiscogsRelease(id: Int, albumTitle: String) {
        val discogs = Discogs("MusicLover", key = "XdhiupScYeQScOxuMQVj", secret = "nTqdLXuMTQbIjchjuoAVprTkTDpigTBA")

        // Query a particular release...
        discogs.release
            .release(id.toString())
            .enqueue(object : Callback<Release> {
                override fun onFailure(call: Call<Release>, t: Throwable) {}

                override fun onResponse(call: Call<Release>, response: Response<Release>) {
                    val artistName = response.body()?.artists?.get(0)?.name
                    val highResImage = response.body()?.images?.get(0)?.resourceUrl
                    val genreText = response.body()?.genres?.joinToString()
                    val countryText = response.body()?.country
                    val yearText = response.body()?.year
                    val trackList = response.body()?.tracklist
                    val (tracks, durations) = getTitlesAndDurations(trackList)

                    Picasso.get().load(highResImage).placeholder(detail_image.drawable).into(detail_image)
                    albumtitle.text = albumTitle
                    genre.text = genreText
                    country.text = countryText
                    year.text = yearText.toString()
                    tracklist.text = tracks
                    trackDurationList.text = durations
                    artist_info.visibility = View.VISIBLE
                    artist_info.text = artistName
                    tracklistDescription.visibility = View.VISIBLE

                }})
    }

    private fun getTitlesAndDurations(trackList: List<Tracklist>?): Pair<String,String> {
        if (trackList != null) {
            var multiLineTracks = ""
            var multiLineDurations = ""
            for (item in trackList) {
                multiLineTracks = multiLineTracks + item.title + "\n";
                multiLineDurations = multiLineDurations + item.duration + "\n";
                println(item.title)
                println(item.duration)
            }
            return Pair(multiLineTracks,multiLineDurations)
        }
        return Pair("No trackList available","")
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

}