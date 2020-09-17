package com.bell.youtubeplayer.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bell.youtubeplayer.R
import com.bell.youtubeplayer.databinding.ActivityMainBinding
import com.bell.youtubeplayer.databinding.TabViewBinding
import com.bell.youtubeplayer.manager.AuthManager
import com.bell.youtubeplayer.utils.NetworkConstants
import com.bell.youtubeplayer.view.AppBaseActivity
import com.bell.youtubeplayer.view.fragments.PlayListFragment
import com.bell.youtubeplayer.view.fragments.SearchFragment
import com.bell.youtubeplayer.viewmodel.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.material.tabs.TabLayout


class MainActivity :
    AppBaseActivity<ActivityMainBinding, BaseViewModel>() {

    companion object {
        const val PLAYLIST = 0
        const val SEARCH = 1
    }

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout

    private var playListFragment = PlayListFragment()
    private var searchFragment = SearchFragment()

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        toolbar = getBinding().toolbar.toolbar
        tabLayout = getBinding().tabsLayout
        initToolbar()

        setupTabs()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        if (AuthManager.googleSignInAccount != null) {
            val name = AuthManager.googleSignInAccount!!.displayName
            getBinding().toolbar.toolbarTitle.text = "Music Player ($name)"
        } else {
            getBinding().toolbar.toolbarTitle.text = "Music Player"
        }

        getBinding().toolbar.rightButton.setOnClickListener {
            signOut()
        }
    }

    private fun setupTabs() {
        val tabsArray = arrayOf("PlayList", "Search")
        val layoutInflater = LayoutInflater.from(this)
        for (i in tabsArray.indices) {
            val view = TabViewBinding.inflate(layoutInflater)
            view.titleTxt.text = tabsArray[i]

            val tab = tabLayout.newTab()
            tab.customView = view.root
            if (i == PLAYLIST) {
                tab.customView!!.alpha = 1f
            } else {
                tab.customView!!.alpha = 0.5f
            }
            tabLayout.addTab(tab)
        }

        // default fragment
        replaceFragment(playListFragment)
        handleTabChange()
    }

    private fun handleTabChange() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.customView?.alpha = 0.5f
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.alpha = 1.0f
                when (tab.position) {
                    PLAYLIST -> {
                        getBinding().toolbar.rightButton.visibility = View.VISIBLE
                        replaceFragment(playListFragment)
                    }
                    SEARCH -> {
                        getBinding().toolbar.rightButton.visibility = View.GONE
                        replaceFragment(searchFragment)
                    }
                }
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val tag = fragment.javaClass.name
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.tabs_container_fragment, fragment, tag)
        transaction.commit()
    }

    private fun signOut() {
        if (AuthManager.mGoogleSignInClient != null) {
            doSignOut()
        } else {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(NetworkConstants.YOUTUBE_SCOPE))
                .requestEmail()
                .build()
            AuthManager.mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            if (AuthManager.mGoogleSignInClient != null) {
                doSignOut()
            } else {
                openLoginActivity(this)
            }
        }
    }

    private fun doSignOut() {
        AuthManager.mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                // logout
                AuthManager.googleSignInAccount = null
                AuthManager.mGoogleSignInClient = null
                openLoginActivity(this)
            }
    }
}