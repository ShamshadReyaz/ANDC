package com.mobiquel.srccapp.view.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.databinding.FragmentNoticeBinding
import com.mobiquel.srccapp.pojo.SlotAttendanceStudentModel
import com.mobiquel.srccapp.room.entity.AttendanceClassEntity
import com.mobiquel.srccapp.room.viewmodel.AttendanceViewModel
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.utils.showToast
import com.mobiquel.srccapp.view.FacultyHomeActivity
import com.mobiquel.srccapp.view.adapter.ListOfOfflineAttendanceAdapter
import com.mobiquel.srccapp.view.viewmodel.APIViewModel
import kotlinx.android.synthetic.main.fragment_student_home.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class OfflineFragment : Fragment() {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!
    private var apiViewModel: APIViewModel? = null
    private lateinit var attendanceViewModel: AttendanceViewModel
    private var offlineAdapter: ListOfOfflineAttendanceAdapter? = null
    private var listOfSlot = ArrayList<SlotAttendanceStudentModel>()
    private var deletedListOfSlot = ArrayList<SlotAttendanceStudentModel>()
    private var listOfOffline=ArrayList<AttendanceClassEntity>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
        apiViewModel = ViewModelProviders.of(this).get(APIViewModel::class.java)
        attendanceViewModel = ViewModelProvider(this).get(AttendanceViewModel::class.java)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences!!.instance!!.loadPreferences(requireContext())

        Log.e("ON CREATE VIEW", "NOTICE FRAGMENT")
        binding.noResult.setText("No Offline record found!")
        val dateTimeFormat = SimpleDateFormat("dd MMM, yyyy")
        lifecycleScope.launch {
            if (attendanceViewModel.getAllAttendanceData(requireActivity()).isNotEmpty()) {

                val listOfOfflineTemp = attendanceViewModel.getAllAttendanceData(requireActivity())
                // listOfOffline.sortedBy { LocalDate.parse(it.sessionDate,dateTimeFormat)}
                listOfOffline=ArrayList(listOfOfflineTemp.sortedWith<AttendanceClassEntity>(object : Comparator<AttendanceClassEntity> {
                    override fun compare(p0: AttendanceClassEntity, pi: AttendanceClassEntity): Int {

                        if (dateTimeFormat.parse(p0.sessionDate).after(dateTimeFormat.parse(pi.sessionDate))) {
                            return 1
                        }

                        return -1
                    }
                }).toList())

                if (listOfOffline.isNotEmpty()) {
                    offlineAdapter = ListOfOfflineAttendanceAdapter(
                        requireActivity(),
                        listOfOffline!!,
                        object : RecyclerItemClickListener {
                            override fun onRecyclerItemClicked(position: Int) {
                                var bundle = Bundle()
                                bundle.putString(
                                    "GRPID",
                                    listOfOffline.get(position).virtualGroupId
                                )
                                bundle.putString("PAPRID", listOfOffline.get(position).paperId)
                                bundle.putString(
                                    "GRPNAME",
                                    listOfOffline.get(position).virtualGroupName
                                )
                                bundle.putString("PAPRNAME", listOfOffline.get(position).paperName)
                                bundle.putString("DATE", listOfOffline.get(position).sessionDate)
                                (activity!! as FacultyHomeActivity).redirectToAttendacePage(bundle)

                            }

                        },object : RecyclerItemClickListener{
                            override fun onRecyclerItemClicked(position: Int) {
                                val builder = AlertDialog.Builder(requireActivity())
                                builder.setMessage("Are you sure you want to submit attendance?")
                                builder.setPositiveButton("Yes") { dialogInterface, which ->
                                    dialogInterface.cancel()
                                    var temp=listOfOffline.get(position).listOfStudent!!
                                    listOfSlot.clear()
                                    deletedListOfSlot.clear()
                                    listOfSlot = temp.filter { it.type.equals("ADD") } as ArrayList<SlotAttendanceStudentModel>
                                    try{
                                        deletedListOfSlot = temp.filter { it.type.equals("DELETE") } as ArrayList<SlotAttendanceStudentModel>
                                    }catch (e:java.lang.Exception){

                                    }
                                    markAttendanceForClassForDates(
                                        listOfOffline.get(position).virtualGroupId!!,
                                        listOfOffline.get(position).paperId!!,
                                        listOfOffline.get(position).sessionDate!!,
                                        ""+listOfOffline.get(position).id,position)
                                }
                                builder.setNegativeButton("No") { dialogInterface, which ->
                                    dialogInterface.cancel()

                                }
                                val alertDialog = builder.create()
                                alertDialog.setCancelable(true)
                                alertDialog.show()

                            }
                        })
                    binding.listView.layoutManager = LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    binding.listView.adapter = offlineAdapter
                } else
                    binding.noResult.visibility = View.VISIBLE

            } else
                binding.noResult.visibility = View.VISIBLE

        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("ATTACH", "NOTICE FRAGMENT")
        //getNotices()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("CREATE", "NOTICE FRAGMENT")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("ON ACTIVITY CREATED", "NOTICE FRAGMENT")
    }

    override fun onStart() {
        super.onStart()
        Log.e("START", "NOTICE FRAGMENT")
    }

    override fun onResume() {
        super.onResume()
        Log.e("RESUME", "NOTICE FRAGMENT")
    }


    private fun markAttendanceForClassForDates(groupId:String,paperId:String,dateOfAttendance:String,attendanceId:String,listItemPos:Int) {
        binding.progressBar.visibility = View.VISIBLE
        var attendanceJSONArray = JSONArray()
        //var deletedAttendanceJSONArray = JSONArray()
        //var period=0
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
            attendanceJson.put("virtualGroupId", groupId)
            attendanceJson.put("paperId", paperId)
            attendanceJson.put("facultyId", Preferences.instance!!.userId!!)
            attendanceJson.put("sessionDate", dateOfAttendance)
            attendanceJson.put("period", data.period)
            attendanceJson.put("lectureType", "Lecture")
            attendanceJson.put("absentStudentIds", absentIds)
            attendanceJson.put("presentStudentIds", presentIds)
            attendanceJson.put("ecaStudentIds", "")
            attendanceJson.put("date", dateOfAttendance)

            attendanceJSONArray.put(attendanceJson)


        }

        for (data in deletedListOfSlot) {
            if(!data.slotId.equals("0"))
                deleteSlot(data.slotId,groupId,paperId, dateOfAttendance)
        }
        Preferences.instance!!.loadPreferences(requireActivity())
        val data: MutableMap<String, String> = HashMap()
        data["attendanceJSON"] = attendanceJSONArray.toString()
        //data["deletedSessionIds"] = deletedAttendanceJSONArray.toString()

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.markAttendanceForClassForDates(data)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    binding.progressBar.visibility = View.GONE
                    try {
                        val stringResponse = response.body()?.string()
                        val jsonobject = JSONObject(stringResponse)

                        if (jsonobject.getString("errorCode").equals("0")) {
                            if (jsonobject.getString("responseObject").equals("true")) {
                                /*
                                lifecycleScope.launch {
                                    appDatabase.deleteAllattendanceById(databaseId)
                                }
                                */
                                lifecycleScope.launch {
                                    attendanceViewModel.deleteAttendaceById(requireActivity(),Integer.parseInt(attendanceId))
                                }
                                listOfOffline.removeAt(listItemPos)
                                offlineAdapter!!.notifyDataSetChanged()
                                if(listOfOffline.size==0)
                                    binding.noResult.visibility=View.VISIBLE

                                requireActivity().showToast("Attendance marked successfully!")
                                (requireActivity() as FacultyHomeActivity).redirectToFragment("onoff")
                            }

                        } else if (jsonobject.getString("errorCode").equals("1"))
                            requireActivity().showSnackBar(
                                "Error marking attendance! Please try again.",
                                binding.rlMain
                            )
                        else {

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE")
                    binding.progressBar.visibility = View.GONE
                }

            })

    }

    private fun deleteSlot(slotId: String,groupId:String,paperId:String,dateOfAttendance:String) {

        Preferences.instance!!.loadPreferences(requireActivity())
        val data: MutableMap<String, String> = HashMap()
        data["facultyId"] = Preferences.instance!!.userId!!
        data["virtualClassId"] = groupId
        data["paperId"] = paperId
        data["sessionDate"] = dateOfAttendance
        data["sessionId"] = slotId

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.deleteSessionWithAttendance(data)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    binding.progressBar.visibility = View.GONE


                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DATA", "FAILURE")
                    binding.progressBar.visibility = View.GONE
                }

            })

    }


}