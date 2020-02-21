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
import kotlinx.android.synthetic.main.example_item.view.*

class Adapter(private val list: List<Item>) : RecyclerView.Adapter<Adapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item,
            parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        Picasso.get().load(currentItem.imageURL).into(holder.imageView)
        holder.textView1.text = currentItem.text1
        holder.item = currentItem
    }

    // kt single expression syntax
    override fun getItemCount() = list.size

    class ViewHolder(itemView: View, var item: Item? = null) : RecyclerView.ViewHolder(itemView) {

        companion object {
            val ALBUM_TITLE_KEY = "ALBUM_TITLE"
            val ALBUM_ID_KEY = "ALBUM_ID"
        }

        val imageView: ImageView = itemView.image_view      //like .findViewById(image_view)
        val textView1: TextView = itemView.text_view_1

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, AlbumDetailsActivity::class.java)
                intent.putExtra(ALBUM_TITLE_KEY, item?.text1)
                intent.putExtra(ALBUM_ID_KEY, item?.id)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity,
                    imageView, ViewCompat.getTransitionName(imageView)!!)
                itemView.context.startActivity(intent, options.toBundle())
            }
        }
    }
}