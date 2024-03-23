package com.mobiquel.hansrajpp.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.mobiquel.mdt112.`interface`.RecyclerItemClickListener
import com.mobiquel.hansrajapp.R
import com.mobiquel.hansrajapp.databinding.FragmentStudentHomeBinding
import com.mobiquel.hansrajpp.pojo.ButtonModel
import com.mobiquel.hansrajpp.utils.Preferences
import com.mobiquel.hansrajpp.utils.autoscrollviewpager.BannerPagerAdapter
import com.mobiquel.hansrajpp.view.HomeActivity
import com.mobiquel.hansrajpp.view.adapter.ButtonListAdapter
import com.mobiquel.hansrajpp.view.viewmodel.APIViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
        //listOfBtns!!.add(ButtonModel("Academic Details", R.drawable.academic_1,"academics"))
        //listOfBtns!!.add(ButtonModel("Attendance", R.drawable.attendance_2,""))
        //listOfBtns!!.add(ButtonModel("IA Record", R.drawable.record_3,""))

        listOfBtns!!.add(ButtonModel("Attendance", R.drawable.attendance_3,"attendance"))
        listOfBtns!!.add(ButtonModel("Notices", R.drawable.notice_4,"notice"))

        // listOfBtns!!.add(ButtonModel("Maintenance", R.drawable.maintence_5,"maintenance"))
       // listOfBtns!!.add(ButtonModel("Credentials", R.drawable.credentials_6,"wifi"))
       // listOfBtns!!.add(ButtonModel("Academic details", R.drawable.academic_details,"academic"))
       // listOfBtns!!.add(ButtonModel("Assignments\nMessages", R.drawable.assignments_messages,"assignments"))
       // listOfBtns!!.add(ButtonModel("IA Record", R.drawable.ia_record,"iarecord"))

        binding.myprofile.setOnClickListener {
            (requireActivity() as HomeActivity).redirectToFragment("profile")
        }
        /*listOfBtns!!.add(ButtonModel("Profile", R.drawable.ic_profile_stu,"profile"))
        listOfBtns!!.add(ButtonModel("Assessment", R.drawable.ic_assessment,"assessment"))
        listOfBtns!!.add(ButtonModel("Student", R.drawable.ic_student,"student"))
*/
        try{
            val nameSplit=Preferences.instance!!.userName!!.split(" ")
            if(nameSplit.size>0){
                binding.hilabel.text= "Hi ${nameSplit.get(0)}, welcome"
            }
        }catch (e:Exception){}

        val adapter=ButtonListAdapter(requireActivity(),listOfBtns!!,object : RecyclerItemClickListener{
            override fun onRecyclerItemClicked(position: Int) {
                (activity!! as HomeActivity).redirectToFragment(listOfBtns!!.get(position).type!!)
            }

        })
        val sdf = SimpleDateFormat("dd MMM yyyy")
        val currentDate = sdf.format(Date())
        binding.datetoday.setText(currentDate)
        binding.listOfBtns.layoutManager=GridLayoutManager(requireActivity(),3)
        binding.listOfBtns.adapter=adapter

        var bannerList=ArrayList<Int>()
        bannerList.add(R.drawable.banner_hansraj_2)
        //bannerList.add(R.drawable.banner_2_2)
        val pagerAdapter = BannerPagerAdapter(bannerList)

        binding.vpBanner!!.startAutoScroll()
        binding.vpBanner!!.setInterval(3000)
        binding.vpBanner!!.setCycle(true)
        binding.vpBanner!!.setStopScrollWhenTouch(true)

        binding.vpBanner!!.setAdapter(pagerAdapter)
        binding.dotsIndicator!!.setViewPager(binding.vpBanner)
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