package com.a.kotlin_library.demo2.utils.adapter

import android.os.SystemClock
import android.text.InputType
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.a.kotlin_library.demo2.utils.textString

object CustomBindAdapter {
    @BindingAdapter(value = ["noRepeatClick"])
    @JvmStatic
    fun noRepeatClick(view: View, clickListener: () -> Unit) {
        val mHits = LongArray(2)
        view.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()
            if (mHits[0] < SystemClock.uptimeMillis() - 500) {
                clickListener.invoke()
            }
        }
    }

    @BindingAdapter(value = ["showPwd"])
    @JvmStatic
    fun showPwd(view: EditText, boolean: Boolean) {
        if (boolean) {
            view.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            view.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        view.setSelection(view.textString().length)
    }

    @BindingAdapter(value = ["checkChange"])
    @JvmStatic
    fun checkChange(checkbox: CheckBox, listener: CompoundButton.OnCheckedChangeListener) {
        checkbox.setOnCheckedChangeListener(listener)
    }
}


