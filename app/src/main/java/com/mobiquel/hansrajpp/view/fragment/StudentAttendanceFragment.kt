package com.mobiquel.hansrajpp.view.fragment

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.hansrajapp.R
import com.mobiquel.hansrajapp.databinding.FragmentNoticeBinding
import com.mobiquel.hansrajpp.utils.*
import com.mobiquel.hansrajpp.view.adapter.ListOfStudentAttendanceAdapter
import com.mobiquel.hansrajpp.view.adapter.ListOfStudentAttendanceDetailAdapter
import com.mobiquel.hansrajpp.view.viewmodel.APIViewModel
import kotlinx.android.synthetic.main.list_item_attendance_detail.view.*
import kotlinx.android.synthetic.main.list_item_notices.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StudentAttendanceFragment : Fragment() {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!
    private var apiViewModel: APIViewModel?=null
    var attendanceHashMap : HashMap<String, JSONArray>?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
        apiViewModel = ViewModelProviders.of(this).get(APIViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences!!.instance!!.loadPreferences(requireContext())

        Log.e("ON CREATE VIEW", "ATTENDANCE  FRAGMENT")

        binding.progressBar.visibility = View.VISIBLE
        apiViewModel?.getStudentAttendance(Preferences!!.instance!!.userId!!)?.observe(this,
            Observer {
                binding.progressBar.visibility = View.GONE
                try {
                    val stringResponse = it!!.data!!.string()
                    val jsonobject = JSONObject(stringResponse)
                    if (jsonobject.getString("errorCode").equals("1"))
                        requireContext().showSnackBar("Invalid Credentials! Please try again", binding.rlMain)
                    else {

                        val jsonArray=jsonobject.getJSONArray("responseObject")
                        attendanceHashMap= HashMap()
                        for(i in 0 until jsonArray.length()){
                            val jsonObject=jsonArray.getJSONObject(i)
                            val key=jsonObject.getString("paperName")+"separator"+jsonObject.getString("section")
                            if(attendanceHashMap!!.containsKey(key)){
                                var dataArray= attendanceHashMap!!.get(key)
                                dataArray?.put(jsonObject)
                                attendanceHashMap!!.set(key,dataArray!!)
                            }else{
                                var dataArray=JSONArray()
                                dataArray?.put(jsonObject)
                                attendanceHashMap!!.set(key,dataArray!!)
                            }
                        }
                        var listOfKey=ArrayList<String>()
                        attendanceHashMap!!.forEach { listOfKey.add(it.key) }
                        if(listOfKey.size>0){
                            listOfKey.sortBy { it }
                            var adapter=ListOfStudentAttendanceAdapter(requireContext(),listOfKey,object :RecyclerItemClickListener{
                                override fun onRecyclerItemClicked(position: Int) {
                                        val jsonArray= attendanceHashMap!!.get(listOfKey.get(position))
                                    showAttendanceDetailDialog(listOfKey.get(position),jsonArray!!)
                                }
                            })
                            binding.listView.layoutManager = LinearLayoutManager(
                                requireActivity(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            binding.listView.adapter = adapter
                            binding.footerLayout.visibility=View.VISIBLE
                        }
                        else
                            binding.noResult.visibility=View.VISIBLE


                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
        binding.ins2.paintFlags =
            binding.ins2.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG
        binding.ins3.paintFlags =
            binding.ins3.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

        binding.ins2.setOnClickListener {
            requireActivity().redirectToWeb2("https://online.srcc.edu/smartprof/file_uploads/MarksSlab.pdf")
        }
        binding.ins3.setOnClickListener {
            requireActivity().redirectToWeb2("https://online.srcc.edu/smartprof/file_uploads/SRCC%20HANDBOOK%202021-22%20(1)-200-201.pdf")
        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    fun showAttendanceDetailDialog(key:String,jsonArray: JSONArray) {
        try {
            val dialogView: View = View.inflate(requireActivity(), R.layout.dialog_show_attendance_detail, null)
            val dialog = BottomSheetDialog(requireActivity())
            var monthName =
                dialogView.findViewById<TextView>(R.id.monthName)
            var classHeldTotal =
                dialogView.findViewById<TextView>(R.id.classHeldTotal)
            var classAttendedTotal =
                dialogView.findViewById<TextView>(R.id.classAttendedTotal)
            var benefitsTotal =
                dialogView.findViewById<TextView>(R.id.benefitsTotal)
            var attendancePerc =
                dialogView.findViewById<TextView>(R.id.attendacePerce)
            var marksOfAttendance =
                dialogView.findViewById<TextView>(R.id.marksOfAttendance)

            var listView =
                dialogView.findViewById<RecyclerView>(R.id.listview)
            val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
            val section=key.split("separator")[1]
            var typeOfClass=""
            var ugcType=""
            var creditsLecture=""
            if(jsonArray.length()>0)
                creditsLecture =jsonArray.getJSONObject(0).getJSONObject("paperDetails").getString("creditsLecture")
            if(section.substring(section.length-2,section.length-1).matches(regex))
                typeOfClass="CA"  //Tutorial
            else
                typeOfClass="IA"

            if(Preferences!!.instance!!.collegeRollNo!!.substring(0,2).toInt()>=22)
                ugcType="UGCF"  //Tutorial
            else
                ugcType="LOCF"//Lecture

            monthName.setText(key.split("separator")[0]+"\n("+key.split("separator")[1]+")")
            val mAdapter=ListOfStudentAttendanceDetailAdapter(requireActivity(),jsonArray,typeOfClass)
            listView.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
            listView.adapter = mAdapter


            var classHeld=0
            var classAttended=0
            var benefits=0
            for(i in 0 until jsonArray.length()){
                classHeld=classHeld.plus(Integer.parseInt(jsonArray.getJSONObject(i).getString("classesHeld")))
                classAttended=classAttended.plus(Integer.parseInt(jsonArray.getJSONObject(i).getString("classesAttended")))
                benefits=benefits.plus(Integer.parseInt(jsonArray.getJSONObject(i).getString("benefits")))
            }
            val oneBy3ofheld=(classHeld/3).toDouble()
            classHeldTotal.setText("Classes Held: "+classHeld)
            classAttendedTotal.setText("Classes Attended: "+classAttended)
            benefitsTotal.setText("Benefits: "+benefits)
            var attendancePercent:Double=((classAttended.toDouble()/classHeld.minus(minOf(oneBy3ofheld.toInt(),benefits)))*100)
            var attendanceMarks=0
            var multiplyingFactor=0.0
            when(attendancePercent){
                in 0.00 .. 66.90->attendanceMarks=0
                in 67.00 .. 69.90->attendanceMarks=1
                in 70.00 .. 74.90->attendanceMarks=2
                in 75.00 .. 79.90->attendanceMarks=3
                in 80.00 .. 84.90->attendanceMarks=4
                else->attendanceMarks=5
            }
            if(typeOfClass.equals("IA") && ugcType.equals("UGCF") && creditsLecture.equals("1"))
                multiplyingFactor= 0.4
            else if(typeOfClass.equals("IA") && ugcType.equals("UGCF") && creditsLecture.equals("2"))
                multiplyingFactor= 0.8
            else if(typeOfClass.equals("IA") && ugcType.equals("UGCF") && creditsLecture.equals("3"))
                multiplyingFactor= 1.2
            else if(typeOfClass.equals("CA") && ugcType.equals("UGCF"))
                multiplyingFactor= 1.0


            val attendanceMarksValue=(attendanceMarks*multiplyingFactor)

            attendancePerc.setText("Attendance Percentage\n"+String.format("%.2f", attendancePercent).toDouble())
            marksOfAttendance.setText("Marks of Attendance\n"+ String.format("%.2f", attendanceMarksValue).toDouble())
            dialog!!.setContentView(dialogView)
            dialog!!.setCancelable(true)
            dialog!!.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
            dialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}