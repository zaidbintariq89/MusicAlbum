package com.bell.youtubeplayer.extensions

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bell.youtubeplayer.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun Context.isConnectedToInternet(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

fun View.hideKeyBoard() {
    val context = this.context
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Activity.hideKeyBoard() {
    val view: View? = this.currentFocus
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun View.showKeyBoard() {
    val context = this.context
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.showKeyBoard() {
    val view: View? = this.currentFocus
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    //imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.SHOW_IMPLICIT)
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun ImageView.loadCircularImage(path: String) {
    Glide.with(this)
        .load(path)
        .override(100, 100)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(R.drawable.attachment_placeholder)
        .into(this)
}

fun ImageView.loadImage(resourceId: Int, requestOptions: RequestOptions) {
    Glide.with(this)
        .load(resourceId)
        .apply(requestOptions)
        .into(this)
}

fun ImageView.loadImage(resourceId: Int) {
    Glide.with(this)
        .load(resourceId)
        .placeholder(R.drawable.attachment_placeholder)
        .into(this)
}

@BindingAdapter("imageSrc")
fun ImageView.loadImage(path: String) {
    if (path.isNotEmpty()) {
        Glide.with(this)
            .load(path)
            .placeholder(R.drawable.attachment_placeholder)
            .into(this)
    } else {
        setImageResource(R.mipmap.ic_launcher_round)
    }
}

fun ImageView.loadImage(path: String, placeholder: Int) {
    if (path.isNotEmpty()) {
        Glide.with(this)
            .load(path)
            .placeholder(placeholder)
            .into(this)
    } else {
        setImageResource(placeholder)
    }
}

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.setColorDrawable(
    color: Int,
    shape: Int = RECTANGLE
) {
    val gd = if (this.background is GradientDrawable) {
        this.background as GradientDrawable
    } else {
        GradientDrawable()
    }

    gd.setColor(color)
    gd.gradientType = LINEAR_GRADIENT
    gd.shape = shape
    gd.cornerRadius = 0f
    this.background = gd
}

internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(this, drawable)

internal fun ImageView.getDrawableCompat(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(this.context, drawable)

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))