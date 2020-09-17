package com.bell.youtubeplayer.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.ChangeBounds
import android.util.Log
import android.view.View
import com.bell.youtubeplayer.R
import com.bell.youtubeplayer.databinding.ActivityLoginBinding
import com.bell.youtubeplayer.manager.AuthManager
import com.bell.youtubeplayer.utils.Constants
import com.bell.youtubeplayer.utils.NetworkConstants
import com.bell.youtubeplayer.view.AppBaseActivity
import com.bell.youtubeplayer.viewmodel.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task

fun openLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

class LoginActivity :
    AppBaseActivity<ActivityLoginBinding, BaseViewModel>() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun init(savedInstanceState: Bundle?) {
        // these made null to avoid flickering while transition
        window.enterTransition = null
        window.exitTransition = null
        // slow down the transition speed
        window.sharedElementEnterTransition = ChangeBounds().setDuration(800)

        getBinding().signInButton.setSize(SignInButton.SIZE_WIDE)
        observeLogin()
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()

        getBinding().signInButton.visibility = View.VISIBLE
        getBinding().lbl.visibility = View.VISIBLE
    }

    private fun observeLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(NetworkConstants.YOUTUBE_SCOPE))
            .requestEmail()
            .build()
        AuthManager.mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        getBinding().signInButton.setOnClickListener {
            signIn(AuthManager.mGoogleSignInClient!!)
        }
    }

    private fun signIn(client: GoogleSignInClient) {
        val signInIntent: Intent = client.signInIntent
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }

    private fun onLoginSuccess(account: GoogleSignInAccount?) {
        AuthManager.googleSignInAccount = account
        if (account != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully
            onLoginSuccess(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In", "signInResult:failed code=" + e.statusCode)
            onLoginSuccess(null)
        }
    }
}