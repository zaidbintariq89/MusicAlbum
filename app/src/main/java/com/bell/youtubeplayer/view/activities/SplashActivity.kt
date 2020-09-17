package com.bell.youtubeplayer.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.bell.youtubeplayer.R
import com.bell.youtubeplayer.databinding.ActivitySplashBinding
import com.bell.youtubeplayer.manager.AuthManager
import com.bell.youtubeplayer.view.AppBaseActivity
import com.bell.youtubeplayer.viewmodel.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SplashActivity : AppBaseActivity<ActivitySplashBinding, BaseViewModel>() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun init(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            openNextView()
        }, 1500)
    }

    private fun openNextView() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null && !account.isExpired) {
            // user is already logged in
            AuthManager.googleSignInAccount = account

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            val activityOptionCompact = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                getBinding().logo,
                "logoTransition"
            )
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent, activityOptionCompact.toBundle())
            ActivityCompat.finishAfterTransition(this)
            window.exitTransition = null
        }
    }
}