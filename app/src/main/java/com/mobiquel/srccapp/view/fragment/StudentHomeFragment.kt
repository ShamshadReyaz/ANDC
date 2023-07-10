package com.mobiquel.srccapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.srccapp.R
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.databinding.FragmentNoticeBinding
import com.mobiquel.srccapp.databinding.FragmentStudentHomeBinding
import com.mobiquel.srccapp.pojo.ButtonModel
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.view.HomeActivity
import com.mobiquel.srccapp.view.adapter.ButtonListAdapter
import com.mobiquel.srccapp.view.adapter.NoticeListAdapter
import com.mobiquel.srccapp.view.viewmodel.APIViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentHomeFragment : Fragment() {

    private var _binding: FragmentStudentHomeBinding? = null
    private val binding get() = _binding!!
    private var apiViewModel: APIViewModel?=null
    private var listOfBtns:ArrayList<ButtonModel>?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        apiViewModel = ViewModelProviders.of(this).get(APIViewModel::class.java)
        listOfBtns= ArrayList()
        listOfBtns!!.add(ButtonModel("Notice", R.drawable.ic_notice_faculty,"notice"))
        listOfBtns!!.add(ButtonModel("Profile", R.drawable.ic_profile_stu,"profile"))
        listOfBtns!!.add(ButtonModel("Maintenance", R.drawable.ic_maintenance_stu,"maintenance"))
        listOfBtns!!.add(ButtonModel("Wifi", R.drawable.ic_credentials,"wifi"))
        listOfBtns!!.add(ButtonModel("Academics", R.drawable.ic_academic_details,"academics"))
        listOfBtns!!.add(ButtonModel("Assessment", R.drawable.ic_assessment,"assessment"))
        listOfBtns!!.add(ButtonModel("Attendance", R.drawable.ic_attendance_stu,"attendance"))
        listOfBtns!!.add(ButtonModel("Student", R.drawable.ic_student,"student"))

        binding.title.text= HtmlCompat.fromHtml("Hi, <font  color=#FF4750>"+Preferences.instance!!.userName+"</font>\nWelcome to SRCC App.", HtmlCompat.FROM_HTML_MODE_LEGACY)
        val adapter=ButtonListAdapter(requireActivity(),listOfBtns!!,object : RecyclerItemClickListener{
            override fun onRecyclerItemClicked(position: Int) {
                (activity!! as HomeActivity).redirectToFragment(listOfBtns!!.get(position).type!!)
            }

        })
        binding.listOfBtns.layoutManager=GridLayoutManager(requireActivity(),2)
        binding.listOfBtns.adapter=adapter
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences!!.instance!!.loadPreferences(requireContext())

        Log.e("ON CREATE VIEW", "NOTICE FRAGMENT")


        // getNotices()
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