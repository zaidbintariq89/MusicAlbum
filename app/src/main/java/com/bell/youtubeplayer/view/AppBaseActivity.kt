package com.bell.youtubeplayer.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.bell.youtubeplayer.utils.DialogUtils
import com.bell.youtubeplayer.viewmodel.BaseViewModel

abstract class AppBaseActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    private val viewModelClass: Class<VM>? = null
) : AppCompatActivity() {

    private lateinit var binding: VB
    private var viewModel: VM? = null

    private var progressDialog: AlertDialog? = null

    protected fun showProgressDialog(title: String, message: String, cancelable: Boolean) {
        progressDialog = DialogUtils.showProgressDialog(this, title, message , cancelable)
    }

    protected fun showProgressDialog(title: String, message: String) {
        progressDialog = DialogUtils.showProgressDialog(this, title, message)
    }

    protected fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog?.dismiss()
        }
    }

    /**
     * Bind XML Layout to View
     */
    @LayoutRes
    abstract fun getLayoutResId(): Int

    /**
     * @param savedInstanceState
     * Calls after on create
     */
    abstract fun init(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //create data binding view
        binding = DataBindingUtil.setContentView(this, getLayoutResId())
        // initialize viewModel
        if (viewModelClass != null) {
            viewModel = ViewModelProvider(this).get(viewModelClass)
        }

        init(savedInstanceState)
    }

    fun getBinding(): VB = binding

    fun getViewModel(): VM? = viewModel
}