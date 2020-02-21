package com.example.musiclover

import android.os.Bundle
import android.transition.Fade
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AlbumDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        hideActionBarFlashing()

        val navBarTitle = intent.getStringExtra(Adapter.ViewHolder.ALBUM_TITLE_KEY)
        supportActionBar?.title = navBarTitle

        val id = intent.getIntExtra(Adapter.ViewHolder.ALBUM_ID_KEY, -1)
        println(id)

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