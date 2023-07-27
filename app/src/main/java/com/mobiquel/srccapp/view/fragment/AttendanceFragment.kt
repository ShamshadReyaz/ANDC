package com.mobiquel.srccapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener2
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.data.NameIdPojo
import com.mobiquel.srccapp.databinding.FragmentAttendanceBinding
import com.mobiquel.srccapp.databinding.FragmentNoticeBinding
import com.mobiquel.srccapp.pojo.AttendanceStudentModel
import com.mobiquel.srccapp.pojo.SlotAttendanceStudentModel
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.view.adapter.ListOfSlotsStudentsAttendanceListAdapter
import com.mobiquel.srccapp.view.adapter.NoticeListAdapter
import com.mobiquel.srccapp.view.adapter.StudentsAttendanceListAdapter
import com.mobiquel.srccapp.view.viewmodel.APIViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!
    private var apiViewModel: APIViewModel?=null
    private var paperId=""
    private var groupId=""
    private var dateOfAttendance=""
    private var listOfAttendance=ArrayList<java.util.ArrayList<AttendanceStudentModel>>()
    private var listOfStudent=ArrayList<AttendanceStudentModel>()
    private var listOfSlot=ArrayList<SlotAttendanceStudentModel>()
    private var studentsAttendanceListAdapter:StudentsAttendanceListAdapter?=null
    private var slotsStudentsAttendanceListAdapter:ListOfSlotsStudentsAttendanceListAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        apiViewModel = ViewModelProviders.of(this).get(APIViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences!!.instance!!.loadPreferences(requireContext())


        paperId=requireArguments().getString("PAPRID").toString()
        groupId=requireArguments().getString("GRPID").toString()
        dateOfAttendance=requireArguments().getString("DATE").toString()
        binding.datetoday.text = requireArguments().getString("DATE").toString()
        binding.grppaper.text = requireArguments().getString("GRPNAME").toString()+", "+requireArguments().getString("PAPRNAME").toString()

        binding.addmore.setOnClickListener {
            val listOfStudentTemp=ArrayList<AttendanceStudentModel>()
            listOfStudentTemp.addAll(listOfStudent)
            listOfAttendance.add(listOfStudentTemp)
            for(slot in listOfSlot)
                slot.isSelected="F"

            listOfSlot.add(SlotAttendanceStudentModel("Slot "+listOfSlot.size.plus(1),"0","0","T"))

            slotsStudentsAttendanceListAdapter?.notifyDataSetChanged()
            Log.e("LIST OF ATTENDACE","== "+listOfAttendance.size)
            Log.e("LIST OF ATTENDACE 2","== "+listOfAttendance.get(0).size+" == "+listOfAttendance.get(1).size)
            binding.listOfAttendance.scrollToPosition(listOfSlot.size-1)
        }
        binding.progressBar.visibility = View.VISIBLE
        studentsAttendanceListAdapter=StudentsAttendanceListAdapter(requireActivity(),listOfStudent,this)
        binding.listOfStudents.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.listOfStudents.adapter = studentsAttendanceListAdapter

        slotsStudentsAttendanceListAdapter=ListOfSlotsStudentsAttendanceListAdapter(requireActivity(),listOfSlot,object : RecyclerItemClickListener2{
            override fun onRecyclerItemClicked(pos: Int,oldpos:Int) {

               // listOfAttendance.get(oldpos).clear()

                val listOfStudentTemp=ArrayList<AttendanceStudentModel>()
                listOfStudentTemp.addAll(listOfStudent)
                listOfAttendance.set(oldpos,listOfStudentTemp)
                listOfStudent=ArrayList()
                listOfStudent.addAll(listOfAttendance.get(pos))
                studentsAttendanceListAdapter?.notifyDataSetChanged()

                for (model in listOfSlot)
                    model.isSelected="F"

                listOfSlot.get(pos).isSelected="T"
                slotsStudentsAttendanceListAdapter?.notifyDataSetChanged()
                binding.listOfAttendance.scrollToPosition(pos)

            }

        })
        binding.listOfAttendance.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.listOfAttendance.adapter = slotsStudentsAttendanceListAdapter


        binding.markall.setOnCheckedChangeListener { p0, p1 ->
            if (p1)
                markAll("T")
            else
                markAll("F")
        }

        getVirtualClassForAttendanceForDates()

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
    private fun getVirtualClassForAttendanceForDates() {
        Preferences.instance!!.loadPreferences(requireActivity())
        val data: MutableMap<String, String> = HashMap()
        data["facultyId"] = Preferences.instance!!.userId!!
        data["virtualClassId"] = groupId
        data["paperId"] = paperId
        data["dates"] = dateOfAttendance

        val apiManager: ApiManager? = ApiManager.init()
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
                          //  var listOfStudent=ArrayList<AttendanceStudentModel>()
                                val studentJsonArray=jsonobject.getJSONArray("responseObject").getJSONObject(0).getJSONArray("studentList")
                            var present=0
                            var absent=0
                            for (i in 0 until studentJsonArray.length()) {
                                if(studentJsonArray.getJSONObject(i).isNull("isPresent")){
                                    val attendanceStudentModel=AttendanceStudentModel(
                                        studentJsonArray.getJSONObject(i).getString("studentId"),
                                        studentJsonArray.getJSONObject(i).getString("studentName"),
                                        studentJsonArray.getJSONObject(i).getString("slotId"),
                                        "F",
                                        studentJsonArray.getJSONObject(i).getString("rollNo"),
                                        studentJsonArray.getJSONObject(i).getString("batch")
                                        )
                                    listOfStudent.add(attendanceStudentModel)
                                }
                                else{
                                    val attendanceStudentModel=AttendanceStudentModel(
                                        studentJsonArray.getJSONObject(i).getString("studentId"),
                                        studentJsonArray.getJSONObject(i).getString("studentName"),
                                        studentJsonArray.getJSONObject(i).getString("slotId"),
                                        studentJsonArray.getJSONObject(i).getString("isPresent"),
                                        studentJsonArray.getJSONObject(i).getString("rollNo"),
                                        studentJsonArray.getJSONObject(i).getString("batch")
                                    )
                                    if(studentJsonArray.getJSONObject(i).getString("isPresent").equals("T"))
                                        present++
                                    else
                                        absent++
                                    listOfStudent.add(attendanceStudentModel)
                                }

                            }

                            studentsAttendanceListAdapter?.notifyDataSetChanged()
                            updateBottoomSheet()
                            listOfAttendance.add(listOfStudent)
                            listOfSlot.add(SlotAttendanceStudentModel("Slot 1",""+present,""+absent,"F"))

                        } else if (jsonobject.getString("errorCode").equals("1"))
                            requireActivity().showSnackBar("Invalid Credentials! Please try again", binding.rlMain)
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

    private fun markAll(status:String){
        for(studetModel in listOfStudent){
            studetModel.isPresent=status
        }
        studentsAttendanceListAdapter?.notifyDataSetChanged()
        updateBottoomSheet()
    }

    fun updateBottoomSheet(){
        var present=0
        var absent=0
        for(data in listOfStudent){
            if(data.isPresent.equals("T"))
                present++
            else
                absent++
        }
        binding.totalAbsent.text="Total Absent: "+absent
        binding.totalPresent.text="Total Present: "+present
    }

}