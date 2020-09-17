package com.bell.youtubeplayer.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bell.youtubeplayer.view.activities.MainActivity
import com.bell.youtubeplayer.utils.DialogUtils
import com.bell.youtubeplayer.viewmodel.BaseViewModel

abstract class AppBaseFragment<VM : BaseViewModel>(
    private val viewModelClass: Class<VM>? = null
) : Fragment() {

    private var viewModel: VM? = null

    private var progressDialog: AlertDialog? = null
    private var mainActivity: MainActivity? = null

    protected fun showProgressDialog(title: String, message: String) {
        hideProgressDialog()
        progressDialog = DialogUtils.showProgressDialog(requireActivity(), title, message)
    }

    protected fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog?.dismiss()
        }
    }

    /**
     * @param inflater
     * @param container
     * @return View
     */
    abstract fun createBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    /**
     * @param savedInstanceState
     * Calls after on create
     */
    abstract fun init(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is MainActivity) {
            mainActivity = activity as MainActivity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return createBindingView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModelClass != null) {
            viewModel = ViewModelProvider(requireActivity()).get(viewModelClass)
        }
        init(savedInstanceState)
    }

    fun getViewModel(): VM? = viewModel

    fun getMainActivity() = mainActivity
}