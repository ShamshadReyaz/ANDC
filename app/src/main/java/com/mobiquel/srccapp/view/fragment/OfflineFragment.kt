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

        getOfflineData()


    }
    fun getOfflineData(){
        val dateTimeFormat = SimpleDateFormat("dd MMM, yyyy")
        lifecycleScope.launch {
            if (attendanceViewModel.getAllAttendanceData(requireActivity()).isNotEmpty()) {
                listOfOffline.clear()
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




}