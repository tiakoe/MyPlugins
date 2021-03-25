package com.a.kotlin_library.demo2.room

import androidx.room.*
import com.a.kotlin_library.demo2.bean.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "homeData")
@TypeConverters(TagTypeConverter::class)
data class HomeTable(
        @ColumnInfo(name = "apkLink") val apkLink: String,
        @ColumnInfo(name = "audit") val audit: Int,
        @ColumnInfo(name = "author") val author: String,
        @ColumnInfo(name = "canEdit") val canEdit: Boolean,
        @ColumnInfo(name = "chapterId") val chapterId: Int,
        @ColumnInfo(name = "chapterName") val chapterName: String,
        @ColumnInfo(name = "collect") val collect: Boolean,
        @ColumnInfo(name = "courseId") val courseId: Int,
        @ColumnInfo(name = "desc") val desc: String,
        @ColumnInfo(name = "descMd") val descMd: String,
        @ColumnInfo(name = "envelopePic") val envelopePic: String,
        @ColumnInfo(name = "fresh") val fresh: Boolean,
        @ColumnInfo(name = "host") val host: String,
        @PrimaryKey @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "link") val link: String,
        @ColumnInfo(name = "niceDate") val niceDate: String,
        @ColumnInfo(name = "niceShareDate") val niceShareDate: String,
        @ColumnInfo(name = "origin") val origin: String,
        @ColumnInfo(name = "prefix") val prefix: String,
        @ColumnInfo(name = "projectLink") val projectLink: String,
        @ColumnInfo(name = "publishTime") val publishTime: Long,
        @ColumnInfo(name = "realSuperChapterId") val realSuperChapterId: Int,
        @ColumnInfo(name = "selfVisible") val selfVisible: Int,
        @ColumnInfo(name = "shareDate") val shareDate: Long,
        @ColumnInfo(name = "shareUser") val shareUser: String,
        @ColumnInfo(name = "superChapterId") val superChapterId: Int,
        @ColumnInfo(name = "superChapterName") val superChapterName: String,
        @ColumnInfo(name = "tags") val tags: List<com.a.kotlin_library.demo2.bean.Tag>,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "type") val type: Int,
        @ColumnInfo(name = "userId") val userId: Int,
        @ColumnInfo(name = "visible") val visible: Int,
        @ColumnInfo(name = "zan") val zan: Int
)

//data class Tag(
//        @ColumnInfo(name = "name") val name: String,
//        @ColumnInfo(name = "url") val url: String
//)


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
