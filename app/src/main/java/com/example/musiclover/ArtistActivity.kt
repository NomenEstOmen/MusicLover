package com.example.musiclover

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        val artistId = intent.getIntExtra(AlbumDetailsActivity.ARTIST_ID_KEY, -1)

        val artistTitle = intent.getStringExtra(AlbumDetailsActivity.ARTIST_NAME_KEY)
        supportActionBar?.title = artistTitle

        getDiscogsArtistInfo(artistId)

    }

    private fun getDiscogsArtistInfo(artistId: Int) {
/*        val discogs = Discogs(
            "MusicLover",
            key = "XdhiupScYeQScOxuMQVj",
            secret = "nTqdLXuMTQbIjchjuoAVprTkTDpigTBA"
        )

        // Query a particular release...
        discogs.release
            .release(id.toString())
            .enqueue(object : Callback<Release> {
                override fun onFailure(call: Call<Release>, t: Throwable) {}

                override fun onResponse(call: Call<Release>, response: Response<Release>) {

                }
            })*/
    }
}
