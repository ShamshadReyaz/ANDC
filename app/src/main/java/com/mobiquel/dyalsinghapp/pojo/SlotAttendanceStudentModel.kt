package com.mobiquel.dyalsinghapp.pojo

data class SlotAttendanceStudentModel (
    var slotName: String? = "",
    var present: String? = "0",
    var absent: String? = "0",
    var isSelected: String? = "F",
    var listOfStudent:List<AttendanceStudentModel>?=ArrayList(),
    var period:String="0",
    var slotId:String="0",
    var type:String="ADD"
)