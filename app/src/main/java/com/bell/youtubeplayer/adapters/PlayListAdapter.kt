package com.bell.youtubeplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bell.youtubeplayer.databinding.PlaylistItemBinding
import com.bell.youtubeplayer.extensions.loadImage
import com.google.api.services.youtube.model.Playlist

class PlayListAdapter(
    private val playlistItems: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PlaylistItemBinding.inflate(layoutInflater)
        return PlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        if (playlistItems.isNotEmpty()) {
            holder.setData(playlistItems[position])
        }
    }

    override fun getItemCount(): Int {
        return if (playlistItems.isNotEmpty())
            playlistItems.size
        else
            0
    }

    //////////////
    // View Holder
    //////////////
    inner class PlayListViewHolder(private val binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(playListItem: Playlist) {
            val snippet = playListItem.snippet
            if (snippet.thumbnails != null && snippet.thumbnails.high != null) {
                val url = snippet.thumbnails.high.url
                binding.ivPreviewIcon.loadImage(url)
            }

            val title = snippet.title
            binding.tvTitle.text = title

            if (playListItem.contentDetails != null) {
                val count = playListItem.contentDetails.itemCount
                val description = "$count video(s)"
                binding.tvEventDetail.text = description
            }

            //
            // Click Listener
            //
            binding.root.setOnClickListener {
                onItemClick.invoke(playListItem)
            }
        }
    }
}