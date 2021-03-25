package com.a.kotlin_library.demo2.fragment.register

import android.widget.CompoundButton
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentRegisterBinding
import com.a.kotlin_library.demo2.bean.Response
import com.a.kotlin_library.demo2.fragment.base.BaseFragment
import com.a.kotlin_library.demo2.fragment.frag_utils.showMessage
import com.a.kotlin_library.demo2.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {


    override fun layoutId(): Int = R.layout.fragment_register

    override fun initView() {
        binding.vm = viewModel
    }

    inner class Listener {

        fun clear() {
            viewModel.username.set("")
        }

        fun register() {
            when {
                viewModel.username.get().isEmpty() -> showMessage("请填写账号")
                viewModel.password.get().isEmpty() -> showMessage("请填写密码")
                viewModel.password2.get().isEmpty() -> showMessage("请填写确认密码")
                viewModel.password.get().length < 6 -> showMessage("密码最少6位")
                viewModel.password.get() != viewModel.password2.get() -> showMessage("密码不一致")
                else -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        var result: Response<Any>
                        withContext(Dispatchers.IO) {
                            result = ApiService.create().registerAsync(viewModel.username.get(), viewModel.password.get(), viewModel.password2.get())
                            if (result.errorCode == 0) {
                                ApiService.create().loginAsync(viewModel.username.get(), viewModel.password.get())
                            }
                        }
                    }
                }
            }
        }

        var onCheckedChangeListener1 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            viewModel.isShowPwd.set(isChecked)
        }
        var onCheckedChangeListener2 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            viewModel.isShowPwd2.set(isChecked)
        }
    }

}

