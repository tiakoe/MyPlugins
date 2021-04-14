package com.a.kotlin_library.room.table

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "bannerTable")
@Parcelize
data class BannerTable(
        @ColumnInfo(name = "desc") val desc: String,
        @PrimaryKey @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "imagePath") val imagePath: String,
        @ColumnInfo(name = "isVisible") val isVisible: Int,
        @ColumnInfo(name = "order") val order: Int,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "type") val type: Int,
        @ColumnInfo(name = "url") val url: String
) : Parcelable, Expr()


