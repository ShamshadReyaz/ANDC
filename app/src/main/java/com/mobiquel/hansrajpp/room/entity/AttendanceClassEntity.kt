package com.mobiquel.hansrajpp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobiquel.hansrajpp.pojo.SlotAttendanceStudentModel

@Entity(tableName = "AttendanceClassEntity")
data class AttendanceClassEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var virtualGroupId: String? = "",
    var paperId: String? = "",
    var slotId: String? = "",
    var virtualGroupName: String? = "",
    var paperName: String? = "",
    var facultyId: String? = "",
    var sessionDate: String? = "",
    var period: String? = "",
    var listOfStudent: List<SlotAttendanceStudentModel>? = ArrayList(),
    var lectureType: String = "0"
)
