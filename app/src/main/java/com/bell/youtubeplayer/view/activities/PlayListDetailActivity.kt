package com.bell.youtubeplayer.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bell.youtubeplayer.R
import com.bell.youtubeplayer.adapters.TrackAdapter
import com.bell.youtubeplayer.databinding.ActivityPlaylistDetailBinding
import com.bell.youtubeplayer.extensions.loadImage
import com.bell.youtubeplayer.manager.AuthManager
import com.bell.youtubeplayer.models.YouTubeItem
import com.bell.youtubeplayer.utils.Constants
import com.bell.youtubeplayer.view.AppBaseActivity
import com.bell.youtubeplayer.viewmodel.PlayListDetailViewModel


class PlayListDetailActivity :
    AppBaseActivity<ActivityPlaylistDetailBinding, PlayListDetailViewModel>(PlayListDetailViewModel::class.java) {

    private var youtubeItem: YouTubeItem? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_playlist_detail
    }

    override fun init(savedInstanceState: Bundle?) {
        initToolbar()

        if (intent != null && intent.hasExtra(Constants.PLAY_LIST_ITEM)) {
            youtubeItem = intent.getParcelableExtra(Constants.PLAY_LIST_ITEM) as YouTubeItem
            bindData()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(getBinding().toolbar.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getBinding().toolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        getBinding().toolbar.rightButton.visibility = View.GONE
    }

    private fun bindData() {
        if (youtubeItem != null && youtubeItem?.id != null) {
            // bind image
            if (youtubeItem?.snippet != null && youtubeItem!!.snippet.thumbnails != null) {
                getBinding().toolbar.toolbarTitle.text = youtubeItem!!.snippet.title

                val url = youtubeItem!!.snippet.thumbnails!!.defaultImage!!.url
                getBinding().icThumb.loadImage(url)
            }

            // fetch from server
            showProgressDialog("PlayList Details", "Please wait while loading playlist info")
            val youtube = AuthManager.getYoutubeInstance(this)
            getViewModel()!!.getPlayListDetails(youtube, youtubeItem!!.id.videoId)
            getViewModel()!!.playlistObservable.observe(this, { items ->
                hideProgressDialog()

                if (!items.isNullOrEmpty()) {
                    bindAdapter(items)
                }
            })
        }
    }

    private fun bindAdapter(items: ArrayList<YouTubeItem>) {
        val linearLayoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        getBinding().itemsRecyclerView.layoutManager = linearLayoutManager

        val adapter = TrackAdapter(items, this::onItemClickListener)
        getBinding().itemsRecyclerView.adapter = adapter
    }

    private fun onItemClickListener(item: YouTubeItem) {
        val videoId = item.id.videoId
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$videoId")
            )
        )
        Log.i("Video", "Video Playing....")

    }
}