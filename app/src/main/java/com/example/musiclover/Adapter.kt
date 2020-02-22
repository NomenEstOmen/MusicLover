package com.example.musiclover

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class Adapter(val searchResults: SearchResults) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recyclerview_item,
            parent, false
        )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAlbum = searchResults.results.get(position)

        Picasso.get().load(currentAlbum.thumb).into(holder.imageView)
        holder.textView1.text = currentAlbum.title
        holder.textView2.text = currentAlbum.year
        holder.album = currentAlbum
    }

    // kt single expression syntax
    override fun getItemCount() = searchResults.results.count()

    class ViewHolder(itemView: View, var album: Album? = null) : RecyclerView.ViewHolder(itemView) {

        companion object {
            val ALBUM_TITLE_KEY = "ALBUM_TITLE"
            val ALBUM_ID_KEY = "ALBUM_ID"
            val ALBUM_IMAGE_URL_KEY = "ALBUM_IMAGE_URL"
        }

        val imageView: ImageView = itemView.image_view      //like .findViewById(image_view)
        val textView1: TextView = itemView.text_view_1
        val textView2: TextView = itemView.text_view_2

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, AlbumDetailsActivity::class.java)
                intent.putExtra(ALBUM_TITLE_KEY, album?.title)
                intent.putExtra(ALBUM_ID_KEY, album?.id)
                intent.putExtra(ALBUM_IMAGE_URL_KEY, album?.thumb);
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    imageView, ViewCompat.getTransitionName(imageView)!!
                )
                itemView.context.startActivity(intent, options.toBundle())
            }
        }
    }
}