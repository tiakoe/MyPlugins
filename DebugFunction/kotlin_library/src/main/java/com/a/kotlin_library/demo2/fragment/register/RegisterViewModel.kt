package com.a.kotlin_library.demo2.fragment.register

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableInt
import com.a.kotlin_library.demo2.utils.BooleanObservableField
import com.a.kotlin_library.demo2.utils.StringObservableField
import com.a.kotlin_library.demo2.view_model.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {
    @Bindable
    var username = StringObservableField()

    @Bindable
    var password = StringObservableField()

    @Bindable
    var password2 = StringObservableField()

    @Bindable
    var isShowPwd = BooleanObservableField()

    @Bindable
    var isShowPwd2 = BooleanObservableField()

    @Bindable
    var clearVisible = object : ObservableInt(username) {
        override fun get(): Int {
            return if (username.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    @Bindable
    var passwordVisible = object : ObservableInt(password) {
        override fun get(): Int {
            return if (password.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    @Bindable
    var passwordVisible2 = object : ObservableInt(password2) {
        override fun get(): Int {
            return if (password2.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
}
