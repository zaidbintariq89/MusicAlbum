package com.bell.youtubeplayer.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bell.youtubeplayer.adapters.PlayListAdapter
import com.bell.youtubeplayer.databinding.FragmentPlaylistBinding
import com.bell.youtubeplayer.manager.AuthManager
import com.bell.youtubeplayer.models.createYoutubeItem
import com.bell.youtubeplayer.utils.Constants
import com.bell.youtubeplayer.view.activities.PlayListDetailActivity
import com.bell.youtubeplayer.viewmodel.PlaylistViewModel
import com.google.api.services.youtube.model.Playlist

class PlayListFragment : AppBaseFragment<PlaylistViewModel>(PlaylistViewModel::class.java),
    SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun createBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun init(savedInstanceState: Bundle?) {
        if (getViewModel()!!.playlistItems.isEmpty()) {
            getPlayList()
        } else {
            bindAdapter(getViewModel()!!.playlistItems)
        }
        binding.swipeRefreshView.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        binding.swipeRefreshView.isRefreshing = false
        getPlayList()
    }

    private fun getPlayList() {
        showProgressDialog("PlayList", "Please wait while loading playlist info")
        val youtube = AuthManager.getYoutubeInstance(requireContext())
        getViewModel()?.getPlayList(youtube)
        // observe
        getViewModel()?.playlistObservable!!.observe(this, { playlist ->
            hideProgressDialog()
            if (!playlist.isNullOrEmpty()) {
                toggleEmptyView(false)
                bindAdapter(playlist)
            } else {
                toggleEmptyView(true)
            }
        })
    }

    private fun toggleEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyView.visibility = View.VISIBLE
            binding.itemsRecyclerView.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.itemsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun bindAdapter(playlistItems: List<Playlist>) {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.itemsRecyclerView.layoutManager = linearLayoutManager

        val adapter = PlayListAdapter(playlistItems, this::onItemClickListener)
        binding.itemsRecyclerView.adapter = adapter
    }

    private fun onItemClickListener(item: Playlist) {
        val youtubeItem = createYoutubeItem(item)
        val intent = Intent(context, PlayListDetailActivity::class.java)
        intent.putExtra(Constants.PLAY_LIST_ITEM, youtubeItem)
        requireActivity().startActivity(intent)
    }
}