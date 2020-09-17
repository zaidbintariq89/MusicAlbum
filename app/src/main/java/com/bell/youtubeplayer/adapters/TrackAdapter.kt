package com.bell.youtubeplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bell.youtubeplayer.databinding.TrackItemDetailBinding
import com.bell.youtubeplayer.extensions.loadImage
import com.bell.youtubeplayer.models.YouTubeItem

class TrackAdapter(
    private val tracks: ArrayList<YouTubeItem>,
    private val onItemClick: (YouTubeItem) -> Unit
) : RecyclerView.Adapter<TrackAdapter.PlayListViewHolder>() {

    fun setItems(items: ArrayList<YouTubeItem>) {
        tracks.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TrackItemDetailBinding.inflate(layoutInflater)
        return PlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        if (tracks.isNotEmpty()) {
            holder.setData(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return if (tracks.isNotEmpty())
            tracks.size
        else
            0
    }

    //////////////
    // View Holder
    //////////////
    inner class PlayListViewHolder(private val binding: TrackItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(track: YouTubeItem) {
            val snippet = track.snippet

            if (snippet.thumbnails?.defaultImage != null) {
                val url = snippet.thumbnails.defaultImage.url
                binding.image.loadImage(url)
            }

            val title = snippet.title
            binding.tvTitle.text = title

            if (!track.videoLength.isNullOrEmpty()) {
                binding.tvDuration.text = track.videoLength
            }
            //
            // Click Listener
            //
            binding.root.setOnClickListener {
                onItemClick.invoke(track)
            }
        }
    }
}