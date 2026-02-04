package com.mobiquel.dyalsinghapp.room.repo

import android.content.Context
import com.mobiquel.dyalsinghapp.room.database.AppDatabase
import com.mobiquel.dyalsinghapp.room.entity.AttendanceClassEntity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AttendanceRepository {
    companion object{
        var userDatabase: AppDatabase?=null

        private fun intialiseDB(context: Context): AppDatabase?
        {
            return AppDatabase.getDataBase(context)!!
        }

        fun insert(context: Context,attendance: AttendanceClassEntity)
        {
            userDatabase= intialiseDB(context)

            CoroutineScope(IO).launch {
                userDatabase!!.attendanceDao().insert(attendance)
            }
        }

        suspend fun getAllAttendanceData(context: Context): List<AttendanceClassEntity>
        {
            userDatabase= intialiseDB(context)

            return userDatabase!!.attendanceDao().getAllAttendance()
        }

        suspend fun getAttendanceByCondition(context: Context, facultyId:String?, virtualGroupId:String?, paperId:String?, sessionDate:String?): AttendanceClassEntity
        {
            userDatabase= intialiseDB(context)
            return userDatabase!!.attendanceDao().getAttendanceByCondition(facultyId,virtualGroupId,paperId,sessionDate)
        }
        suspend fun deleteAttendance(context: Context, id:Int?)
        {
            userDatabase= intialiseDB(context)
            return userDatabase!!.attendanceDao().deleteAllattendanceById(id!!)
        }
    }
}