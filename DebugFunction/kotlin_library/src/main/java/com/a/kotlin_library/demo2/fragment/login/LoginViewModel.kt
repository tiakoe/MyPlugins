package com.a.kotlin_library.demo2.fragment.login

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.a.kotlin_library.demo2.bean.Response
import com.a.kotlin_library.demo2.bean.User
import com.a.kotlin_library.demo2.utils.BooleanObservableField
import com.a.kotlin_library.demo2.utils.StringObservableField
import com.a.kotlin_library.demo2.view_model.base.BaseViewModel

class LoginViewModel : BaseViewModel() {

    var loginResult = MutableLiveData<Response<User>>()


    var username = StringObservableField()

    var password = StringObservableField()

    var isShowPwd = BooleanObservableField()


    //用户名清除按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var clearVisible = object : ObservableInt(username) {
        override fun get(): Int {
            return if (username.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    //密码显示按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var passwordVisible = object : ObservableInt(password) {
        override fun get(): Int {
            return if (password.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
}
