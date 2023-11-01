package com.mobiquel.srccapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.srccapp.R
import com.mobiquel.srccapp.databinding.FragmentStudentHomeBinding
import com.mobiquel.srccapp.pojo.ButtonModel
import com.mobiquel.srccapp.room.database.AppDatabase
import com.mobiquel.srccapp.room.viewmodel.AttendanceViewModel
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.autoscrollviewpager.BannerPagerAdapter
import com.mobiquel.srccapp.view.FacultyHomeActivity
import com.mobiquel.srccapp.view.adapter.ButtonListAdapter
import com.mobiquel.srccapp.view.adapter.ListOfOfflineAttendanceAdapter
import com.mobiquel.srccapp.view.viewmodel.APIViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FacultyHomeFragment : Fragment() {

    private var _binding: FragmentStudentHomeBinding? = null
    private val binding get() = _binding!!
    private var apiViewModel: APIViewModel? = null
    private var listOfBtns: ArrayList<ButtonModel>? = null
    private var offlineAdapter: ListOfOfflineAttendanceAdapter? = null
    private lateinit var attendanceViewModel: AttendanceViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        apiViewModel = ViewModelProviders.of(this).get(APIViewModel::class.java)
        attendanceViewModel= ViewModelProvider(this).get(AttendanceViewModel::class.java)

        listOfBtns = ArrayList()
        if (Preferences.instance?.userType.equals("faculty")) {
            listOfBtns!!.add(ButtonModel("Notice", R.drawable.notice_4,"notice"))
            //listOfBtns!!.add(ButtonModel("Attendance", R.drawable.attendance_3,"attendance"))
            listOfBtns!!.add(ButtonModel("Maintenance", R.drawable.maintence_5,"maintenance"))
            //listOfBtns!!.add(ButtonModel("Offline", R.drawable.baseline_wifi_off_home,"offline"))
        }
        else{
            listOfBtns!!.add(ButtonModel("Notice", R.drawable.notice_4,"notice"))
            // listOfBtns!!.add(ButtonModel("Profile", R.drawable.ic_profile_stu,"profile"))
            listOfBtns!!.add(ButtonModel("Maintenance", R.drawable.maintence_5,"maintenance"))

        }
        //listOfBtns!!.add(ButtonModel("Attendance", R.drawable.attendance_3,""))

        //binding.title.text= HtmlCompat.fromHtml("Hi, <font  color=#FF4750>"+Preferences.instance!!.userName+"</font>\nWelcome to SRCC App.", HtmlCompat.FROM_HTML_MODE_LEGACY)
        val adapter=ButtonListAdapter(requireActivity(),listOfBtns!!,object : RecyclerItemClickListener{
            override fun onRecyclerItemClicked(position: Int) {
                (activity!! as FacultyHomeActivity).redirectToFragment(listOfBtns!!.get(position).type!!)
            }

        })
        binding.myprofile.setOnClickListener {
            (requireActivity() as FacultyHomeActivity).redirectToFragment("profile")
        }
        binding.listOfBtns.layoutManager=GridLayoutManager(requireActivity(),3)
        binding.listOfBtns.adapter=adapter

        try{
            val nameSplit=Preferences.instance!!.userName!!.split(" ")
            Log.e("NAME SLPIT",nameSplit.toString())
            Log.e("NAME SLPIT SIZE",nameSplit.size.toString())
            Log.e("NAME SLPIT SIZE",nameSplit.get(0))
            Log.e("NAME SLPIT SIZE",nameSplit.get(1))
            if(nameSplit.size>1){
                binding.hilabel.text= "Hi ${nameSplit.get(0)} ${nameSplit.get(1)}, welcome"
            }
            else  if(nameSplit.size>0){
                binding.hilabel.text= "Hi ${nameSplit.get(0)}, Welcome"
            }


        }catch (e:Exception){}

        var bannerList=ArrayList<Int>()
        bannerList.add(R.drawable.banner_3_3)
        val pagerAdapter = BannerPagerAdapter(bannerList)

        binding.vpBanner!!.startAutoScroll()
        binding.vpBanner!!.setInterval(3000)
        binding.vpBanner!!.setCycle(true)
        binding.vpBanner!!.setStopScrollWhenTouch(true)

        binding.vpBanner!!.setAdapter(pagerAdapter)
        binding.dotsIndicator!!.setViewPager(binding.vpBanner)
        binding.dotsIndicator.visibility = View.GONE

        val sdf = SimpleDateFormat("dd MMM yyyy")
        val currentDate = sdf.format(Date())
        binding.datetoday.setText(currentDate)



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