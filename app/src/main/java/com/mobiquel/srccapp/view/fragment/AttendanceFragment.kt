package com.mobiquel.srccapp.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener2
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.databinding.FragmentAttendanceBinding
import com.mobiquel.srccapp.pojo.AttendanceStudentModel
import com.mobiquel.srccapp.pojo.SlotAttendanceStudentModel
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.utils.showToast
import com.mobiquel.srccapp.view.LoginActivity
import com.mobiquel.srccapp.view.adapter.ListOfSlotsStudentsAttendanceListAdapter
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
    private var currentPos=0
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

            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage("Do you want to copy data from the last slot?")
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                dialogInterface.cancel()
                var dataTemp=listOfSlot.get(currentPos).listOfStudent
                var  dataModel=listOfSlot.get(currentPos)
                dataModel.listOfStudent=studentsAttendanceListAdapter?.listOfAttendance?.map { it.copy() }
                listOfSlot.set(currentPos,dataModel)
                for(slot in listOfSlot)
                    slot.isSelected="F"
                currentPos++
                listOfSlot.add(SlotAttendanceStudentModel("Slot "+listOfSlot.size.plus(1),"0",""+dataTemp!!.size,"T",dataTemp))
                slotsStudentsAttendanceListAdapter?.notifyDataSetChanged()
                binding.listOfAttendance.scrollToPosition(listOfSlot.size-1)
                studentsAttendanceListAdapter?.updateList(dataTemp)
                updateBottoomSheet()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                dialogInterface.cancel()
                var dataTemp=listOfSlot.get(currentPos).listOfStudent
                var  dataModel=listOfSlot.get(currentPos)
                dataModel.listOfStudent=studentsAttendanceListAdapter?.listOfAttendance?.map { it.copy() }
                listOfSlot.set(currentPos,dataModel)
                for(slot in listOfSlot)
                    slot.isSelected="F"
                currentPos++
                dataTemp?.map { it.isPresent="F" }
                listOfSlot.add(SlotAttendanceStudentModel("Slot "+listOfSlot.size.plus(1),"0",""+dataTemp!!.size,"T",dataTemp))
                slotsStudentsAttendanceListAdapter?.notifyDataSetChanged()
                binding.listOfAttendance.scrollToPosition(listOfSlot.size-1)
                studentsAttendanceListAdapter?.updateList(dataTemp)
                updateBottoomSheet()
            }

            val alertDialog = builder.create()


            // Set other dialog properties
            alertDialog.setCancelable(true)
            alertDialog.show()

        }
        binding.progressBar.visibility = View.VISIBLE
        studentsAttendanceListAdapter=StudentsAttendanceListAdapter(requireActivity(),this)
        binding.listOfStudents.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.listOfStudents.adapter = studentsAttendanceListAdapter

        slotsStudentsAttendanceListAdapter=ListOfSlotsStudentsAttendanceListAdapter(requireActivity(),listOfSlot,object : RecyclerItemClickListener2{
            override fun onRecyclerItemClicked(pos: Int,oldpos:Int) {

                var  dataModel=listOfSlot.get(oldpos)
                dataModel.listOfStudent=studentsAttendanceListAdapter?.listOfAttendance?.map { it.copy() }
                listOfSlot.set(oldpos,dataModel)
                currentPos=pos

                studentsAttendanceListAdapter?.updateList(listOfSlot.get(currentPos).listOfStudent!!)

                for (model in listOfSlot)
                    model.isSelected="F"

                listOfSlot.get(pos).isSelected="T"
                slotsStudentsAttendanceListAdapter?.notifyDataSetChanged()
                binding.listOfAttendance.scrollToPosition(pos)
                updateBottoomSheet()

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
                           var listOfStudent=ArrayList<AttendanceStudentModel>()

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
                                    absent++
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

                           // var dataTemp=listOfStudentTemp.map { it }


                            listOfSlot.add(SlotAttendanceStudentModel("Slot 1",""+present,""+absent,"F",
                                listOfStudent
                            ))
                            studentsAttendanceListAdapter?.updateList(listOfSlot.get(0).listOfStudent!!)
                            updateBottoomSheet()


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
        for(studetModel in studentsAttendanceListAdapter?.listOfAttendance!!){
            studetModel.isPresent=status
        }
        studentsAttendanceListAdapter?.notifyDataSetChanged()
        updateBottoomSheet()
    }

    fun updateBottoomSheet(){
        var present=0
        var absent=0
        for(data in studentsAttendanceListAdapter?.listOfAttendance!!){
            if(data.isPresent.equals("T"))
                present++
            else
                absent++
        }
        //Log.e("TEMP",""+listOfStudentTemp.size)
        val modelTemp=listOfSlot.get(currentPos)
        modelTemp.absent=""+absent
        modelTemp.present=""+present
        listOfSlot.set(currentPos,modelTemp)
        binding.totalAbsent.text="Total Absent: "+absent
        binding.totalPresent.text="Total Present: "+present
        slotsStudentsAttendanceListAdapter?.notifyDataSetChanged()
    }

}