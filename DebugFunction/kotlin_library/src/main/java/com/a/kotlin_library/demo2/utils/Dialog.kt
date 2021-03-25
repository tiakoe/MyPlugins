package com.a.kotlin_library.demo2.fragment.frag_utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.a.kotlin_library.demo2.utils.SettingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner

fun AppCompatActivity.showMessage(
        message: String,
        title: String = "温馨提示",
        positiveButtonText: String = "确定",
        positiveAction: () -> Unit = {},
        negativeButtonText: String = "",
        negativeAction: () -> Unit = {}
) {
    MaterialDialog(this)
            .cancelable(true)
            .lifecycleOwner(this)
            .show {
                title(text = title)
                message(text = message)
                positiveButton(text = positiveButtonText) {
                    positiveAction.invoke()
                }
                if (negativeButtonText.isNotEmpty()) {
                    negativeButton(text = negativeButtonText) {
                        negativeAction.invoke()
                    }
                }
                getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
                getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
            }
}

fun Fragment.showMessage(
        message: String,
        title: String = "温馨提示",
        positiveButtonText: String = "确定",
        positiveAction: () -> Unit = {},
        negativeButtonText: String = "",
        negativeAction: () -> Unit = {}
) {
    activity?.let {
        MaterialDialog(it)
                .cancelable(true)
                .lifecycleOwner(viewLifecycleOwner)
                .show {
                    title(text = title)
                    message(text = message)
                    positiveButton(text = positiveButtonText) {
                        positiveAction.invoke()
                    }
                    if (negativeButtonText.isNotEmpty()) {
                        negativeButton(text = negativeButtonText) {
                            negativeAction.invoke()
                        }
                    }
                    getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(it))
                    getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(it))
                }
    }
}
