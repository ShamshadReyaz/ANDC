package com.mobiquel.srccapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobiquel.srccapp.pojo.SlotAttendanceStudentModel
import com.mobiquel.srccapp.room.entity.AttendanceClassEntity

@Dao
interface AttendanceDao {

    @Insert
    suspend fun insert(attendance: AttendanceClassEntity):Long

   /* @Query("UPDATE AttendanceClassEntity SET listOfStudent = :listOfStudent AND deletedListOfStudent = :deletedListOfStudent where id = :id")
    suspend fun updateAttendanceById(listOfStudent: List<SlotAttendanceStudentModel>,deletedListOfStudent: List<SlotAttendanceStudentModel>,id:Int)
*/
    /*@Delete
    suspend fun delete(attendance: AttendanceClassEntity)
*/
    @Query("delete from AttendanceClassEntity where id=:id")
    suspend fun deleteAllattendanceById(id:Int)

    @Query("select * from AttendanceClassEntity where facultyId = :facultyId AND virtualGroupId = :virtualGroupId AND paperId = :paperId AND sessionDate = :sessionDate")
    suspend fun getAttendanceByCondition(facultyId:String?,virtualGroupId:String?,paperId:String?,sessionDate:String?): AttendanceClassEntity

    @Query("select * from AttendanceClassEntity")
    suspend fun getAllAttendance(): List<AttendanceClassEntity>

}