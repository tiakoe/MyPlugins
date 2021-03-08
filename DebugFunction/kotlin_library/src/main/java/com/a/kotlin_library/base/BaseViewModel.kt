package com.a.kotlin_library.base

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        val showDialog by lazy { EventLiveData<String>() }

        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }

}
