package com.a.kotlin_library.demo2.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class HomeData(
        val `data`: Data,
        val errorCode: Int,
        val errorMsg: String
)

data class Data(
        val curPage: Int,
        val datas: List<DataX>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
)

@Parcelize
data class DataX(
        val apkLink: String,
        val audit: Int,
        val author: String,
        val canEdit: Boolean,
        val chapterId: Int,
        val chapterName: String,
        val collect: Boolean,
        val courseId: Int,
        val desc: String,
        val descMd: String,
        val envelopePic: String,
        val fresh: Boolean,
        val host: String,
        val id: Int,
        val link: String,
        val niceDate: String,
        val niceShareDate: String,
        val origin: String,
        val prefix: String,
        val projectLink: String,
        val publishTime: Long,
        val realSuperChapterId: Int,
        val selfVisible: Int,
        val shareDate: Long,
        val shareUser: String,
        val superChapterId: Int,
        val superChapterName: String,
        val tags: List<Tag>,
        val title: String,
        val type: Int,
        val userId: Int,
        val visible: Int,
        val zan: Int
) : Parcelable

@Parcelize
data class Tag(
        val name: String,
        val url: String
) : Parcelable
