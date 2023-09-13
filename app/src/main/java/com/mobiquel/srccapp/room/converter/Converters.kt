package com.mobiquel.srccapp.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import com.mobiquel.srccapp.pojo.SlotAttendanceStudentModel
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun listToJson(value: List<SlotAttendanceStudentModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<SlotAttendanceStudentModel>::class.java).toList()
}