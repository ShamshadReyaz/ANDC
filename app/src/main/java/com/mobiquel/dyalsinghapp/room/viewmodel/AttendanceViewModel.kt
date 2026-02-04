package com.mobiquel.dyalsinghapp.room.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiquel.dyalsinghapp.data.ApiManager
import com.mobiquel.dyalsinghapp.pojo.SlotAttendanceStudentModel
import com.mobiquel.dyalsinghapp.room.entity.AttendanceClassEntity
import com.mobiquel.dyalsinghapp.room.repo.AttendanceRepository

import com.mobiquel.dyalsinghapp.utils.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val apiManager: ApiManager):ViewModel() {

    fun insert(context: Context, user: AttendanceClassEntity)
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

    suspend fun syncData(context: Context){

        if (AttendanceRepository.getAllAttendanceData(context).isNotEmpty()) {
            val listOfOffline = ArrayList(AttendanceRepository.getAllAttendanceData(context).toList())
            if (listOfOffline.isNotEmpty()) {
                for(attendace in listOfOffline){

                    var temp=attendace.listOfStudent!!
                     var listOfSlot = ArrayList<SlotAttendanceStudentModel>()
                     var deletedListOfSlot = ArrayList<SlotAttendanceStudentModel>()
                    listOfSlot.clear()
                    deletedListOfSlot.clear()
                    listOfSlot = temp.filter { it.type.equals("ADD") } as ArrayList<SlotAttendanceStudentModel>
                    try{
                        deletedListOfSlot = temp.filter { it.type.equals("DELETE") } as ArrayList<SlotAttendanceStudentModel>
                    }catch (e:java.lang.Exception){

                    }
                    var attendanceJSONArray = JSONArray()

                    for (data in listOfSlot) {
                        val attendanceJson = JSONObject()
                        var absentIds = ""
                        var presentIds = ""
                        for (dataStudent in data.listOfStudent!!) {
                            if (dataStudent.isPresent.equals("P"))
                                presentIds = presentIds + dataStudent.studentId + ","
                            else
                                absentIds = absentIds + dataStudent.studentId + ","
                        }

                        attendanceJson.put("sessionId", "")
                        attendanceJson.put("virtualGroupId", attendace.virtualGroupId)
                        attendanceJson.put("paperId", attendace.paperId)
                        attendanceJson.put("facultyId", Preferences.instance!!.userId!!)
                        attendanceJson.put("sessionDate", attendace.sessionDate)
                        attendanceJson.put("period", data.period)
                        attendanceJson.put("lectureType", "Lecture")
                        attendanceJson.put("absentStudentIds", absentIds)
                        attendanceJson.put("presentStudentIds", presentIds)
                        attendanceJson.put("ecaStudentIds", "")
                        attendanceJson.put("date", attendace.sessionDate)

                        attendanceJSONArray.put(attendanceJson)


                    }

                    for (dataAtt in deletedListOfSlot) {
                        if(!dataAtt.slotId.equals("0"))
                        {
                            val data: MutableMap<String, String> = HashMap()
                            data["facultyId"] = Preferences.instance!!.userId!!
                            data["virtualClassId"] = attendace.virtualGroupId!!
                            data["paperId"] = attendace.paperId!!
                            data["sessionDate"] = attendace.sessionDate!!
                            data["sessionId"] = dataAtt.slotId
                            apiManager!!.deleteSessionWithAttendance(data)
                                .enqueue(object : Callback<ResponseBody> {
                                    override fun onResponse(
                                        call: Call<ResponseBody>,
                                        response: Response<ResponseBody>
                                    ) {
                                        Log.e("DELETED SLOT","SLOT DEL")
                                    }
                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
                                })

                        }
                    }
                    val data: MutableMap<String, String> = HashMap()
                    data["attendanceJSON"] = attendanceJSONArray.toString()
                    //data["deletedSessionIds"] = deletedAttendanceJSONArray.toString()
                    apiManager!!.markAttendanceForClassForDates(data)
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                Log.e("ATTENDANCE MARKED","ATTENDANCE MARKED")
                                //deleteAllattendanceById(databaseId)
                                viewModelScope.launch {
                                    AttendanceRepository.deleteAttendance(context,attendace.id)
                                }

                            }
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
                        })
                }
            }

        }
    }
}