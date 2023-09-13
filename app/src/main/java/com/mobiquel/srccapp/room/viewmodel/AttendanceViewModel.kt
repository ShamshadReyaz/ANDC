package com.mobiquel.srccapp.room.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mobiquel.srccapp.room.entity.AttendanceClassEntity
import com.mobiquel.srccapp.room.repo.AttendanceRepository

class AttendanceViewModel:ViewModel() {

    fun insert(context: Context, user:AttendanceClassEntity)
    {
        AttendanceRepository.insert(context,user)
    }

    suspend fun getAllAttendanceData(context: Context): List<AttendanceClassEntity>
    {
        return AttendanceRepository.getAllAttendanceData(context)
    }

    suspend fun getAttendanceByCondition(context: Context, facultyId:String?, virtualGroupId:String?, paperId:String?, sessionDate:String?): AttendanceClassEntity
    {
        return AttendanceRepository.getAttendanceByCondition(context,facultyId,virtualGroupId,paperId,sessionDate)
    }

    suspend fun deleteAttendaceById(context: Context, id:Int)
    {
        return AttendanceRepository.deleteAttendance(context,id)
    }
}