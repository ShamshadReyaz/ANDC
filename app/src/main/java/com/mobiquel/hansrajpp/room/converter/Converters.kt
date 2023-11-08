package com.mobiquel.hansrajpp.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.mobiquel.hansrajpp.pojo.SlotAttendanceStudentModel


class Converters {
    @TypeConverter
    fun listToJson(value: List<SlotAttendanceStudentModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<SlotAttendanceStudentModel>::class.java).toList()
}