package com.bell.youtubeplayer.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.bell.youtubeplayer.R
import kotlinx.android.synthetic.main.custom_dialog_view.view.*
import kotlinx.android.synthetic.main.progress_dialog.view.*


interface DialogListener {
    fun onDialogAction(action: DialogAction)
}

interface SingleChoiceDialogListener {
    fun onItemSelected(item: String, position: Int)
}

object DialogUtils {

    fun showProgressDialog(context: Context, title: String, message: String, cancelable: Boolean): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null)
        view.msgTV.text = message
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setView(view)
        builder.setCancelable(cancelable)
        val alertDialog = builder.create()
        // For rounded Background
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.activity_rounded_bg)
        alertDialog.show()

        alertDialog.window?.attributes?.width =
            (context.resources.displayMetrics.widthPixels - (context.resources.displayMetrics.widthPixels / 6))
        alertDialog.window?.attributes = alertDialog.window?.attributes

        return alertDialog
    }

    fun showProgressDialog(context: Context, title: String, message: String): AlertDialog {
        return showProgressDialog(context, title, message, false)
    }

    fun showErrorDialog(
        context: Context,
        message: String
    ) {
        showDialog(context, "Error", message, "Ok", null, null)
    }

    fun showErrorDialog(
        context: Context,
        title: String,
        message: String,
        listener: DialogListener?
    ) {
        showDialog(context, title, message, "Ok", null, listener)
    }

    fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveBtnText: String?,
        cancelBtnText: String?,
        listener: DialogListener?
    ) {

        val view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_view, null)
        view.titleTV.text = title
        view.contentTV.text = message

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(false)

        if (!positiveBtnText.isNullOrBlank()) {
            builder.setPositiveButton(positiveBtnText) { dialog, _ ->
                dialog.dismiss()
                listener?.onDialogAction(DialogAction.SUCCESS)
            }
        }

        if (!cancelBtnText.isNullOrBlank()) {
            builder.setNegativeButton(cancelBtnText) { dialog, _ ->
                dialog.dismiss()
                listener?.onDialogAction(DialogAction.CANCEL)
            }
        }

        val alertDialog = builder.create()
        // For rounded Background
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.activity_rounded_bg)
        alertDialog.show()

        alertDialog.window?.attributes?.width =
            (context.resources.displayMetrics.widthPixels - (context.resources.displayMetrics.widthPixels / 7))
        alertDialog.window?.attributes = alertDialog.window?.attributes
    }

    fun showSingleChoiceDialog(
        context: Context,
        items: Array<String>,
        checkedItemPosition: Int,
        listener: SingleChoiceDialogListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setSingleChoiceItems(items, checkedItemPosition) { dialog, i ->
            val selectedItem: String = items[i]
            listener.onItemSelected(selectedItem, i)
            dialog.dismiss()
        }
        builder.setPositiveButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            listener.onItemSelected("Cancel", -1)
        }
        val alertDialog = builder.create()
        // For rounded Background
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.activity_rounded_bg)
        alertDialog.show()
        // for dynamic width
        alertDialog.window?.attributes?.width =
            (context.resources.displayMetrics.widthPixels - (context.resources.displayMetrics.widthPixels / 7))
        alertDialog.window?.attributes = alertDialog.window?.attributes
    }
}