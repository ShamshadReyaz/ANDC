package com.mobiquel.dyalsinghapp.view.fragment

import android.annotation.SuppressLint
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquel.dyalsinghapp.databinding.FragmentAttendanceBinding
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener2
import com.mobiquel.dyalsinghapp.data.ApiManager
import com.mobiquel.dyalsinghapp.pojo.AttendanceStudentModel
import com.mobiquel.dyalsinghapp.pojo.SlotAttendanceStudentModel
import com.mobiquel.dyalsinghapp.room.database.AppDatabase
import com.mobiquel.dyalsinghapp.room.entity.AttendanceClassEntity
import com.mobiquel.dyalsinghapp.room.viewmodel.AttendanceViewModel
import com.mobiquel.dyalsinghapp.utils.*
import com.mobiquel.dyalsinghapp.view.FacultyHomeActivity
import com.mobiquel.dyalsinghapp.view.adapter.ListOfSlotsStudentsAttendanceListAdapter
import com.mobiquel.dyalsinghapp.view.adapter.StudentsAttendanceListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceFragment : Fragment() {
    @Inject
    lateinit var apiManager: ApiManager
    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!
    private var paperId = ""
    private var groupId = ""
    private var dateOfAttendance = ""
    private var currentPos = 0
    private var listOfSlot = ArrayList<SlotAttendanceStudentModel>()
    private var deletedListOfSlot = ArrayList<SlotAttendanceStudentModel>()
    private var studentsAttendanceListAdapter: StudentsAttendanceListAdapter? = null
    private var slotsStudentsAttendanceListAdapter: ListOfSlotsStudentsAttendanceListAdapter? = null
    var period = "0"
    var databaseId = -1
    var typeOfOperation = "ADD"
    private val appDatabase by lazy { AppDatabase.getDataBase(requireActivity()).attendanceDao() }
    private var slotId = "0"
    private lateinit var attendanceViewModel: AttendanceViewModel
    private var sessionType =""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        attendanceViewModel = ViewModelProvider(this).get(AttendanceViewModel::class.java)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences!!.instance!!.loadPreferences(requireContext())

        paperId = requireArguments().getString("PAPRID").toString()
        groupId = requireArguments().getString("GRPID").toString()
        when(requireArguments().getString("SESSIONTYPE").toString()){
            "Lecture"->sessionType="Lecture"
            "PRACTICAL"->sessionType="Practical"
            "TUTORIAL"->sessionType="Tutorial"
        }
        dateOfAttendance = requireArguments().getString("DATE").toString()
        binding.datetoday.text = requireArguments().getString("DATE").toString()
        binding.grppaper.text = requireArguments().getString("GRPNAME")
            .toString() + ", " + requireArguments().getString("PAPRNAME").toString()+" ("+sessionType+")"

        binding.attendanceNotMarked.setOnClickListener {
            requireActivity().showSingleButtonDialog("You haven't submitted/marked attendance yet for this date.")
        }
        binding.addmore.setOnClickListener {
            if(listOfSlot.size==4){
                requireActivity().showSingleButtonDialog("Sorry!You can add maximum of 4 slots.")
            }
            else{
                val builder = AlertDialog.Builder(requireActivity())
                builder.setMessage("Do you want to copy data from the last slot?")
                builder.setPositiveButton("Yes") { dialogInterface, which ->
                    dialogInterface.cancel()
                    var dataTemp = listOfSlot.get(currentPos).listOfStudent
                    var dataModel = listOfSlot.get(currentPos)
                    dataModel.listOfStudent =
                        studentsAttendanceListAdapter?.listOfAttendance?.map { it.copy() }
                    listOfSlot.set(currentPos, dataModel)
                    for (slot in listOfSlot)
                        slot.isSelected = "F"

                    listOfSlot.add(
                        SlotAttendanceStudentModel(
                            "Slot " + listOfSlot.size.plus(1),
                            "0",
                            "" + dataTemp!!.size,
                            "T",
                            dataTemp,
                            "" + Integer.parseInt(period).plus(1), slotId
                        )
                    )
                    period="" + Integer.parseInt(period).plus(1)
                    currentPos = listOfSlot.size - 1
                    slotsStudentsAttendanceListAdapter?.updateList(listOfSlot)
                    binding.listOfAttendance.scrollToPosition(listOfSlot.size - 1)
                    studentsAttendanceListAdapter?.updateList(dataTemp)
                    updateBottoomSheet()
                }
                builder.setNegativeButton("No") { dialogInterface, which ->
                    dialogInterface.cancel()
                    var dataTemp = listOfSlot.get(currentPos).listOfStudent
                    var dataModel = listOfSlot.get(currentPos)
                    dataModel.listOfStudent =
                        studentsAttendanceListAdapter?.listOfAttendance?.map { it.copy() }
                    listOfSlot.set(currentPos, dataModel)
                    for (slot in listOfSlot)
                        slot.isSelected = "F"

                    dataTemp?.map { it.isPresent = "A" }
                    listOfSlot.add(
                        SlotAttendanceStudentModel(
                            "Slot " + listOfSlot.size.plus(1),
                            "0",
                            "" + dataTemp!!.size,
                            "T",
                            dataTemp,
                            "" + Integer.parseInt(period).plus(1), slotId
                        )
                    )
                    period="" + Integer.parseInt(period).plus(1)
                    currentPos = listOfSlot.size - 1
                    slotsStudentsAttendanceListAdapter?.updateList(listOfSlot)
                    binding.listOfAttendance.scrollToPosition(listOfSlot.size - 1)
                    studentsAttendanceListAdapter?.updateList(dataTemp)
                    updateBottoomSheet()
                }

                val alertDialog = builder.create()


                // Set other dialog properties
                alertDialog.setCancelable(true)
                alertDialog.show()
            }
        }

        studentsAttendanceListAdapter = StudentsAttendanceListAdapter(requireActivity(), this)
        binding.listOfStudents.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.listOfStudents.adapter = studentsAttendanceListAdapter

        slotsStudentsAttendanceListAdapter = ListOfSlotsStudentsAttendanceListAdapter(
            requireActivity(),
            object : RecyclerItemClickListener2 {
                override fun onRecyclerItemClicked(pos: Int, oldpos: Int) {

                    var dataModel = listOfSlot.get(oldpos)
                    dataModel.listOfStudent =
                        studentsAttendanceListAdapter?.listOfAttendance?.map { it.copy() }
                    listOfSlot.set(oldpos, dataModel)
                    currentPos = pos

                    studentsAttendanceListAdapter?.updateList(listOfSlot.get(currentPos).listOfStudent!!)

                    for (model in listOfSlot)
                        model.isSelected = "F"

                    listOfSlot.get(pos).isSelected = "T"
                    slotsStudentsAttendanceListAdapter?.updateList(listOfSlot)
                    binding.listOfAttendance.scrollToPosition(pos)
                    updateBottoomSheet()
                }

            },
            object : RecyclerItemClickListener {
                override fun onRecyclerItemClicked(position: Int) {
                    val builder = AlertDialog.Builder(requireActivity())
                    if (position == 0 && listOfSlot.size == 1) {
                        builder.setMessage("It is mandatory to have atleast one slot. You can't delete this slot.")
                        builder.setPositiveButton("OK") { dialogInterface, which ->
                            dialogInterface.cancel()
                        }

                    } else {
                        builder.setMessage("Are you sure you want to delete this slot?")
                        builder.setPositiveButton("Yes") { dialogInterface, which ->
                            dialogInterface.cancel()
                            val oldpos = position
                            var pos = -1
                            if (oldpos == 0)
                                pos = 0
                            else
                                pos = position - 1

                            var dataModel = listOfSlot.get(oldpos)
                            dataModel.listOfStudent =
                                studentsAttendanceListAdapter?.listOfAttendance?.map { it.copy() }
                            listOfSlot.set(oldpos, dataModel)
                            currentPos = pos
                            if (oldpos == 0)
                                studentsAttendanceListAdapter?.updateList(listOfSlot.get(1).listOfStudent!!)
                            else
                                studentsAttendanceListAdapter?.updateList(listOfSlot.get(currentPos).listOfStudent!!)
                            for (model in listOfSlot)
                                model.isSelected = "F"

                            if (!dataModel.slotId.equals("0"))
                            {
                                deletedListOfSlot.add(dataModel)
                                deletedListOfSlot.map { it.type="DELETE" }
                            }
                            if (typeOfOperation.equals("UPDATE") && !dataModel.slotId.equals("0"))
                                deleteSlot(dataModel.slotId)

                            listOfSlot.removeAt(oldpos)
                            listOfSlot.get(pos).isSelected = "T"
                            slotsStudentsAttendanceListAdapter?.updateList(listOfSlot)
                            binding.listOfAttendance.scrollToPosition(pos)
                            updateBottoomSheet()

                        }
                        builder.setNegativeButton("No") { dialogInterface, which ->
                            dialogInterface.cancel()
                        }
                    }


                    val alertDialog = builder.create()
                    alertDialog.setCancelable(true)
                    alertDialog.show()


                }

            })
        binding.listOfAttendance.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.listOfAttendance.adapter = slotsStudentsAttendanceListAdapter


        binding.markall.setOnCheckedChangeListener { p0, p1 ->
            if (p0.isPressed) {
                if (p1)
                    markAll("P")
                else
                    markAll("A")
            }

        }

        binding.submit.setOnClickListener {
            //saveAttendanceInDatabase()
            if (requireActivity().isNetworkAvailable())
            {
                if(databaseId==-1){
                    markAttendanceForClassForDates()
                }
                else{
                    lifecycleScope.launch {
                        val attendanceClassEntity = attendanceViewModel.getAttendanceByCondition(
                            requireActivity(), Preferences.instance!!.userId!!,
                            groupId!!,
                            paperId!!,
                            dateOfAttendance!!
                        )
                        if (attendanceClassEntity != null) {
                            markAttendanceForClassForDates()
                        }
                        else{
                            val builder = AlertDialog.Builder(requireActivity())
                            builder.setMessage("Your attendance is already synced on server!")
                            builder.setPositiveButton("OK") { dialogInterface, which ->
                                dialogInterface.cancel()
                                (requireActivity() as FacultyHomeActivity).redirectToFragment("onoff")
                            }
                            val alertDialog = builder.create()
                            alertDialog.setCancelable(true)
                            alertDialog.show()
                        }
                    }
                }
            }
            else
                saveAttendanceInDatabase()

        }

        /*if (requireActivity().isNetworkAvailable())
            getVirtualClassForAttendanceForDates()
        else*/
        getAttendanceData()
        /*if (requireActivity().isNetworkAvailable())
            getVirtualClassForAttendanceForDates()
        else {
            binding.bottomlayout.visibility = View.GONE
            binding.addmore.visibility = View.GONE
            binding.labelLayout.visibility = View.GONE
            binding.noInternet.visibility = View.VISIBLE
            requireActivity().showSnackBar(
                "Internet not present! Please try again.",
                binding.submit
            )
        }*/


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


    private fun markAll(status: String) {
        for (studetModel in studentsAttendanceListAdapter?.listOfAttendance!!) {
            studetModel.isPresent = status
        }
        studentsAttendanceListAdapter?.notifyDataSetChanged()
        updateBottoomSheet()
    }

    fun updateBottoomSheet() {
        var present = 0
        var absent = 0
        for (data in studentsAttendanceListAdapter?.listOfAttendance!!) {
            if (data.isPresent.equals("P"))
                present++
            else
                absent++
        }
        binding.markall.isChecked = absent <= 0
        //Log.e("TEMP",""+listOfStudentTemp.size)
        val modelTemp = listOfSlot.get(currentPos)
        modelTemp.absent = "" + absent
        modelTemp.present = "" + present
        listOfSlot.set(currentPos, modelTemp)
        binding.totalAbsent.text = "Total Absent: " + absent
        binding.totalPresent.text = "Total Present: " + present
        slotsStudentsAttendanceListAdapter?.updateList(listOfSlot)
    }

    private fun getVirtualClassForAttendanceForDates() {
        binding.progressBar.visibility = View.VISIBLE
        Preferences.instance!!.loadPreferences(requireActivity())
        val data: MutableMap<String, String> = HashMap()
        data["facultyId"] = Preferences.instance!!.userId!!
        data["virtualClassId"] = groupId
        data["paperId"] = paperId
        data["dates"] = dateOfAttendance
        data["sortBy"] = "ROLLNO"

        apiManager!!.getVirtualClassForAttendanceForDates(data)
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

                            for (j in 0 until jsonobject.getJSONArray("responseObject").length()) {
                                //if(sessionType.equals(jsonobject.getJSONArray("responseObject").getJSONObject(j).getJSONObject("sessionRecord").getString("lectureType"))){
                                var attendaceMarkedStatus=false
                                if(jsonobject.getJSONArray("responseObject").getJSONObject(j).isNull("sessionRecord")){
                                    attendaceMarkedStatus=false
                                }
                                else if (!jsonobject.getJSONArray("responseObject").getJSONObject(j).isNull("sessionRecord") &&
                                    !sessionType.equals(jsonobject.getJSONArray("responseObject").getJSONObject(j).getJSONObject("sessionRecord").getString("lectureType"))) {
                                    continue
                                }
                                else if (!jsonobject.getJSONArray("responseObject").getJSONObject(j).isNull("sessionRecord") &&
                                    sessionType.equals(jsonobject.getJSONArray("responseObject").getJSONObject(j).getJSONObject("sessionRecord").getString("lectureType"))) {
                                    attendaceMarkedStatus=true
                                    typeOfOperation = "UPDATE"
                                    period = jsonobject.getJSONArray("responseObject").getJSONObject(j).getJSONObject("sessionRecord").getString("period")
                                }
                                var listOfStudent = ArrayList<AttendanceStudentModel>()
                                val studentJsonArray = jsonobject.getJSONArray("responseObject").getJSONObject(j)
                                        .getJSONArray("studentList")
                                var present = 0
                                var absent = 0

                                for (i in 0 until studentJsonArray.length()) {

                                    if (attendaceMarkedStatus) {
                                        slotId = studentJsonArray.getJSONObject(i).getString("slotId")
                                    }
                                    var attendanceStudentModel = AttendanceStudentModel(
                                        studentJsonArray.getJSONObject(i)
                                            .getString("studentId"),
                                        studentJsonArray.getJSONObject(i)
                                            .getString("studentName"),
                                        studentJsonArray.getJSONObject(i).getString("slotId"),
                                        "A",
                                        studentJsonArray.getJSONObject(i).getString("rollNo"),
                                        studentJsonArray.getJSONObject(i).getString("sem")
                                    )
                                    if(!attendaceMarkedStatus)
                                        absent++
                                    else if (studentJsonArray.getJSONObject(i).getString("isPresent").equals("")) {
                                        absent++
                                    } else {
                                        attendanceStudentModel.isPresent=studentJsonArray.getJSONObject(i).getString("isPresent")
                                        if (studentJsonArray.getJSONObject(i).getString("isPresent").equals("P"))
                                            present++
                                        else
                                            absent++
                                    }
                                    listOfStudent.add(attendanceStudentModel)
                                }

                                listOfSlot.add(SlotAttendanceStudentModel("Slot " + j.plus(1), "" + present, "" + absent, "F", listOfStudent, period, slotId))

                            }
                            if(listOfSlot.size==0){
                                var listOfStudent = ArrayList<AttendanceStudentModel>()
                                val studentJsonArray = jsonobject.getJSONArray("responseObject").getJSONObject(0)
                                    .getJSONArray("studentList")
                                var present = 0
                                var absent = 0

                                for (i in 0 until studentJsonArray.length()) {
                                    var attendanceStudentModel = AttendanceStudentModel(
                                        studentJsonArray.getJSONObject(i)
                                            .getString("studentId"),
                                        studentJsonArray.getJSONObject(i)
                                            .getString("studentName"),
                                        studentJsonArray.getJSONObject(i).getString("slotId"),
                                        "A",
                                        studentJsonArray.getJSONObject(i).getString("rollNo"),
                                        studentJsonArray.getJSONObject(i).getString("sem")
                                    )
                                    listOfStudent.add(attendanceStudentModel)
                                }

                                listOfSlot.add(SlotAttendanceStudentModel("Slot 1", "" + present, "" + absent, "F", listOfStudent, period, slotId))
                            }

                            listOfSlot.get(0).isSelected = "T"
                            studentsAttendanceListAdapter?.updateList(listOfSlot.get(0).listOfStudent!!)
                            updateBottoomSheet()
                            if(slotId.equals("0"))
                                binding.attendanceNotMarked.visibility=View.VISIBLE


                        } else if (jsonobject.getString("errorCode").equals("1"))
                            requireActivity().showSnackBar(
                                "Invalid Credentials! Please try again",
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

    private fun markAttendanceForClassForDates() {
        binding.progressBar.visibility = View.VISIBLE
        var attendanceJSONArray = JSONArray()
        //var deletedAttendanceJSONArray = JSONArray()
        //var period=0
        for (data in listOfSlot) {
            val attendanceJson = JSONObject()
            var absentIds = ""
            var presentIds = ""
            var tempListOfStud=data.listOfStudent!!.toSet()
            for (dataStudent in tempListOfStud) {
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
            attendanceJson.put("lectureType", sessionType)
            attendanceJson.put("absentStudentIds", absentIds)
            attendanceJson.put("presentStudentIds", presentIds)
            attendanceJson.put("ecaStudentIds", "")
            attendanceJson.put("date", dateOfAttendance)

            attendanceJSONArray.put(attendanceJson)


        }

        for (data in deletedListOfSlot) {
            if(!data.slotId.equals("0"))
                deleteSlot(data.slotId)
        }
        Preferences.instance!!.loadPreferences(requireActivity())
        val data: MutableMap<String, String> = HashMap()
        data["attendanceJSON"] = attendanceJSONArray.toString()
        //data["deletedSessionIds"] = deletedAttendanceJSONArray.toString()

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
                                if (databaseId != -1) {
                                    lifecycleScope.launch {
                                        appDatabase.deleteAllattendanceById(databaseId)
                                    }
                                }

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

    @SuppressLint("SuspiciousIndentation")
    private fun saveAttendanceInDatabase() {
        lifecycleScope.launch {
            val attendanceClassEntityInDB = attendanceViewModel.getAttendanceByCondition(
                requireActivity(),
                Preferences.instance!!.userId!!,
                groupId,
                paperId,
                dateOfAttendance
            )
            if (attendanceClassEntityInDB != null) {
                listOfSlot.addAll(deletedListOfSlot)
                attendanceViewModel.deleteAttendaceById(requireActivity(), databaseId)
                var attendanceClassEntity = AttendanceClassEntity()
                attendanceClassEntity.virtualGroupId = groupId
                attendanceClassEntity.paperId = paperId
                attendanceClassEntity.slotId = slotId
                attendanceClassEntity.virtualGroupName = requireArguments().getString("GRPNAME")
                attendanceClassEntity.paperName = requireArguments().getString("PAPRNAME")
                attendanceClassEntity.facultyId = Preferences.instance!!.userId!!
                attendanceClassEntity.sessionDate = dateOfAttendance
                attendanceClassEntity.listOfStudent = listOfSlot
                attendanceClassEntity.lectureType = sessionType
                attendanceClassEntity.period = period
                Log.e(
                    "STATUS",
                    "" + attendanceViewModel.insert(requireActivity(), attendanceClassEntity)
                )
                requireActivity().showToast("No internet present! Attendance saved successfully!")
                (requireActivity() as FacultyHomeActivity).redirectToFragment("onoff")

            } else {
                var attendanceClassEntity = AttendanceClassEntity()
                listOfSlot.addAll(deletedListOfSlot)
                attendanceClassEntity.virtualGroupId = groupId
                attendanceClassEntity.paperId = paperId
                attendanceClassEntity.slotId = slotId
                attendanceClassEntity.virtualGroupName = requireArguments().getString("GRPNAME")
                attendanceClassEntity.paperName = requireArguments().getString("PAPRNAME")
                attendanceClassEntity.facultyId = Preferences.instance!!.userId!!
                attendanceClassEntity.sessionDate = dateOfAttendance
                attendanceClassEntity.listOfStudent = listOfSlot
                attendanceClassEntity.lectureType = sessionType
                attendanceClassEntity.period = period
                Log.e(
                    "STATUS",
                    "" + attendanceViewModel.insert(requireActivity(), attendanceClassEntity)
                )
                requireActivity().showToast("No internet present! Attendance saved successfully!")
                (requireActivity() as FacultyHomeActivity).redirectToFragment("onoff")

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SuspiciousIndentation")
    private fun getAttendanceData() {
        lifecycleScope.launch {
            val attendanceClassEntity = attendanceViewModel.getAttendanceByCondition(
                requireActivity(), Preferences.instance!!.userId!!,
                groupId!!,
                paperId!!,
                dateOfAttendance!!
            )
            if (attendanceClassEntity != null) {
                period = attendanceClassEntity.period.toString()
                slotId = attendanceClassEntity.slotId.toString()
                var temp=(attendanceClassEntity.listOfStudent!!)
                listOfSlot = temp.filter { it.type.equals("ADD") } as ArrayList<SlotAttendanceStudentModel>
                try{
                    deletedListOfSlot = temp.filter { it.type.equals("DELETE") } as ArrayList<SlotAttendanceStudentModel>
                }catch (e:java.lang.Exception){

                }

                databaseId = attendanceClassEntity.id!!
                for(data in listOfSlot)
                    data.isSelected="F"
                listOfSlot.get(0).isSelected = "T"
                studentsAttendanceListAdapter?.updateList(listOfSlot.get(0).listOfStudent!!)
                updateBottoomSheet()
            } else if (requireActivity().isNetworkAvailable())
                getVirtualClassForAttendanceForDates()
            else {
                binding.bottomlayout.visibility = View.GONE
                binding.addmore.visibility = View.GONE
                binding.labelLayout.visibility = View.GONE
                binding.noInternet.visibility = View.VISIBLE
                requireActivity().showSnackBar(
                    "Internet not present! Please try again.",
                    binding.submit
                )
            }
        }


    }

    private fun getDataBaseId() {
        lifecycleScope.launch {
            val attendanceClassEntity = appDatabase.getAttendanceByCondition(
                Preferences.instance!!.userId!!,
                groupId,
                paperId,
                dateOfAttendance
            )
            if (attendanceClassEntity != null) {
                //Handler(Looper.getMainLooper()).post {
                databaseId = attendanceClassEntity.id!!
                //}


            }

        }
    }

    private fun deleteSlot(slotId: String) {

        Preferences.instance!!.loadPreferences(requireActivity())
        val data: MutableMap<String, String> = HashMap()
        data["facultyId"] = Preferences.instance!!.userId!!
        data["virtualClassId"] = groupId
        data["paperId"] = paperId
        data["sessionDate"] = dateOfAttendance
        data["sessionId"] = slotId

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