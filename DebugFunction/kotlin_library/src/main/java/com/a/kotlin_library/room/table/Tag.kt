package com.a.kotlin_library.room.table

import android.os.Parcelable
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

class TagTypeConverter {
    @TypeConverter
    fun stringToTag(value: String?): List<Tag>? {
        val type = object : TypeToken<List<Tag>>() {
        }.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun userToString(list: List<Tag>): String {
        return Gson().toJson(list)
    }
}

@Parcelize
data class Tag(
        val name: String,
        val url: String
) : Parcelable

//data class Tag(
//        @ColumnInfo(name = "name") val name: String,
//        @ColumnInfo(name = "url") val url: String
//)
