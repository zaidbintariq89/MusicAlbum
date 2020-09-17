package com.bell.youtubeplayer.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bell.youtubeplayer.R
import com.bell.youtubeplayer.adapters.TrackAdapter
import com.bell.youtubeplayer.databinding.FragmentSearchBinding
import com.bell.youtubeplayer.extensions.hideKeyBoard
import com.bell.youtubeplayer.models.YouTubeItem
import com.bell.youtubeplayer.viewmodel.SearchViewModel
import java.util.*

class SearchFragment : AppBaseFragment<SearchViewModel>(SearchViewModel::class.java) {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var trackAdapter: TrackAdapter? = null
    private var searchQuery: String = ""

    private lateinit var searchView: SearchView
    private lateinit var mSearchItem: MenuItem

    //Declare these variables globally in class
    private var userScrolled = true
    private var pastVisibleItems = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        binding.root.hideKeyBoard()
    }

    override fun createBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun init(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        trackAdapter = null

        getViewModel()!!.searchObservable.observe(this, { items ->
            hideProgressDialog()
            if (!items.isNullOrEmpty()) {
                if (trackAdapter == null) {
                    bindAdapter(items)
                } else {
                    trackAdapter!!.setItems(items)
                }
            }
        })
    }

    private fun search(query: String, pageToken: String) {
        showProgressDialog("Search", "Loading items...")
        getViewModel()!!.searchTrack(query, getString(R.string.SEARCH_KEY), pageToken)
    }

    private fun bindAdapter(items: ArrayList<YouTubeItem>) {
        binding.itemsRecyclerView.layoutManager = linearLayoutManager

        trackAdapter = TrackAdapter(items, this::onItemClickListener)
        binding.itemsRecyclerView.adapter = trackAdapter

        listScrollListener()
    }

    private fun listScrollListener() {
        binding.itemsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // If scroll state is touch scroll then set userScrolled
                // true

                // If scroll state is touch scroll then set userScrolled
                // true
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Here get the child count, item count and visibleitems
                // from layout manager
                visibleItemCount = linearLayoutManager.childCount
                totalItemCount = linearLayoutManager.itemCount
                pastVisibleItems = linearLayoutManager
                    .findFirstVisibleItemPosition()

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false
                if (userScrolled
                    && visibleItemCount + pastVisibleItems == totalItemCount
                ) {
                    userScrolled = false
                    updateRecyclerView()
                }
            }
        })
    }

    private fun updateRecyclerView() {
        search(searchQuery, getViewModel()!!.nextPageToken)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        // search view
        mSearchItem = menu.findItem(R.id.action_search)
        searchView = mSearchItem.actionView as SearchView
        searchView.queryHint = "Search videos"
        searchView.setOnQueryTextListener(querySearchListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private val querySearchListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (!query.isNullOrEmpty()) {
                searchQuery = query
                search(searchQuery, "")
                binding.root.hideKeyBoard()
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return true
        }
    }
}