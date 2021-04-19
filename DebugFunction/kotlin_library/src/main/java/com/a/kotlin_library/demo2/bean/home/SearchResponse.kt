package com.a.kotlin_library.demo2.bean.home

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class SearchResponse(var id: Int,
                          var link: String,
                          var name: String,
                          var order: Int,
                          var visible: Int) : Parcelable
