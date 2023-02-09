package com.example.newsappex.db

import androidx.room.TypeConverter
import com.example.newsappex.mbos.Source

/**
 * Created by #kannanpvm007 on  08/02/23.
 */
class Converters {

    @TypeConverter
    fun fromSores(source: Source):String{
        return source.name
    }
    @TypeConverter
    fun toSores(source: String): Source {
        return Source(source,source)
    }
}