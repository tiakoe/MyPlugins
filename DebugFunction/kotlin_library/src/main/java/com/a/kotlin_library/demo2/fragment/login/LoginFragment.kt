package com.a.kotlin_library.demo2.fragment.login

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentLoginBinding
import com.a.kotlin_library.demo2.fragment.base.BaseFragment
import com.a.kotlin_library.demo2.fragment.frag_utils.showMessage
import com.a.kotlin_library.demo2.retrofit.ApiService
import com.a.kotlin_library.demo2.utils.MmkvUtil
import com.a.kotlin_library.demo2.utils.nav
import com.a.kotlin_library.demo2.utils.navigateAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun layoutId(): Int = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.loginResult.observe(viewLifecycleOwner, {
            if (it.errorCode == 0) {
                MmkvUtil.saveUser(it.data)
                nav().navigateUp()
            }
        })
        binding.vm = viewModel
    }


    inner class Listener {

        fun login() {
            when {
                viewModel.username.get().isEmpty() -> {
                    showMessage("请填写账号")
                }
                viewModel.password.get().isEmpty() -> {
                    showMessage("请填写密码")
                }
                else -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        viewModel.loginResult.value = ApiService.create().loginAsync(viewModel.username.get(), viewModel.password.get())
                    }
                }
            }
        }

        fun clear() {
            viewModel.username.set("")
        }

        fun goRegister() {
            hideSoftKeyboard(activity)
            nav().navigateAction(R.id.action_loginFragment_to_registerFragment)
        }

        var onCheckedChangeListener =
                CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                    viewModel.isShowPwd.set(isChecked)
                }

        private fun hideSoftKeyboard(activity: Activity?) {
            activity?.let { act ->
                val view = act.currentFocus
                view?.let {
                    val inputMethodManager =
                            act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(
                            view.windowToken,
                            InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        }
    }
}
