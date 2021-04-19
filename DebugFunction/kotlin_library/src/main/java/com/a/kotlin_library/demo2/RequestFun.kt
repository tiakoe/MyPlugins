//package com.a.kotlin_library.demo2
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.a.kotlin_library.demo2.exception.AppException
//import com.a.kotlin_library.demo2.fragment.search.ResultState
//import com.a.kotlin_library.demo2.view_model.base.BaseViewModel
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.launch
//
//class RequestFun {
//}
//
//fun <T> BaseViewModel.request(
//        block: suspend () -> BaseResponse<T>,
//        resultState: MutableLiveData<ResultState<T>>,
//        isShowDialog: Boolean = false,
//        loadingMessage: String = "请求网络中..."
//): Job {
//    return viewModelScope.launch {
//        runCatching {
//            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
//            //请求体
//            block()
//        }.onSuccess {
//            resultState.paresResult(it)
//        }.onFailure {
//            it.message?.loge()
//            resultState.paresException(it)
//        }
//    }
//}
//
///**
// * 处理返回值
// * @param result 请求结果
// */
//fun <T> MutableLiveData<ResultState<T>>.paresResult(result: BaseResponse<T>) {
//    value = when {
//        result.isSucces() -> {
//            ResultState.onAppSuccess(result.getResponseData())
//        }
//        else -> {
//            ResultState.onAppError(AppException(result.getResponseCode(), result.getResponseMsg()))
//        }
//    }
//}
