package com.example.ehs.Feed

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.youtube.view.*

class YoutubeListAdapter(private val items: List<Youtube>) :
    RecyclerView.Adapter<YoutubeListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_youtube, parent, false)
        return ViewHolder(view as YouTubePlayerView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
        }

//        holder.youTubePlayerView.setEnableAutomaticInitialization(false)
        holder.youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(items!![position].videoId, 0f)
            }
        }, false)


        holder.apply {
            bind(listener, item)

        }
    }


    class ViewHolder(itemView: YouTubePlayerView) : RecyclerView.ViewHolder(itemView) {
        var youTubePlayerView: YouTubePlayerView = itemView

        fun bind(listener: View.OnClickListener, item: Youtube) {
            youTubePlayerView = itemView.card_content_player_view
            youTubePlayerView.setOnClickListener(listener)
        }
    }

}

