package com.mobiquel.hansrajpp.view.fragment

import android.content.Context
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobiquel.hansrajapp.R
import com.mobiquel.hansrajapp.databinding.FragmentNoticeBinding
import com.mobiquel.hansrajpp.data.ApiManager
import com.mobiquel.hansrajpp.utils.Preferences
import com.mobiquel.hansrajpp.utils.showSnackBar
import com.mobiquel.hansrajpp.utils.showToast
import com.mobiquel.hansrajpp.view.adapter.MaintanceListAdapter
import com.mobiquel.hansrajpp.view.viewmodel.APIViewModel3
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MaintenanceFragment : Fragment() {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!
    private var apiViewModel: APIViewModel3?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
         apiViewModel = ViewModelProviders.of(this).get(APIViewModel3::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.visibility = View.VISIBLE
        binding.addRequest.visibility = View.VISIBLE
        binding.addRequest.setOnClickListener {
            showAddDialog()
        }

       getMainData()

        //getMaintenanceRequest()
    }
    fun getMaintenanceRequest() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["userId"] = Preferences.instance!!.userId!!
        data["userType"] = Preferences.instance!!.userType!!
        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getMyRequisitionsForMaintenance(data).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    val stringResponse = response.body()?.string()
                    val jsonobject = JSONObject(stringResponse)
                    if (jsonobject.getString("errorCode").equals("1"))
                        context!!.showSnackBar(
                            "Invalid Credentials! Please try again",
                            binding.rlMain
                        )
                    else {
                        var dataList = ArrayList<String>()
                        for (i in 0 until jsonobject.getJSONArray("responseObject").length()) {
                            dataList.add(
                                jsonobject.getJSONArray("responseObject").getJSONObject(i)
                                    .toString()
                            )
                        }
                        val mAdapter = MaintanceListAdapter(context!!, dataList)
                        binding.listView.layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.listView.adapter = mAdapter

                        if (dataList.size == 0)
                            binding.noResult.visibility = View.VISIBLE

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("ATTACH", "PROFILE FRAGMENT")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("CREATE", "PROFILE FRAGMENT")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("ON ACTIVITY CREATED", "PROFILE FRAGMENT")
    }

    override fun onStart() {
        super.onStart()
        Log.e("START", "PROFILE FRAGMENT")
    }

    override fun onResume() {
        super.onResume()
        Log.e("RESUME", "PROFILE FRAGMENT")
    }

    fun showAddDialog() {
        try {
            val dialogView: View = View.inflate(requireContext(), R.layout.dialog_add_request, null)
            val dialog = BottomSheetDialog(requireContext())
            val categoryDropdown = dialogView.findViewById<Spinner>(R.id.catspinner)
            val locDropdown = dialogView.findViewById<Spinner>(R.id.locspinner)
            val othersdetail = dialogView.findViewById<EditText>(R.id.others)
            val description = dialogView.findViewById<EditText>(R.id.description)
            val sosSwitch = dialogView.findViewById<Switch>(R.id.sos)
            val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)
            var isSos = "F"
            sosSwitch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    isSos = if (p1)
                        "T"
                    else
                        "F"
                }

            })

            locDropdown.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if(p2==5)
                        othersdetail.visibility=View.VISIBLE
                    else
                        othersdetail.visibility=View.GONE
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
            submitBtn.setOnClickListener {

                if (categoryDropdown.selectedItemPosition == 0) {
                    requireContext().showToast("Please select any one category")
                } else if (locDropdown.selectedItemPosition == 0) {
                    requireContext().showToast("Please select any one location")
                } else if (locDropdown.selectedItemPosition == 5 && othersdetail.text.toString()
                        .equals("")
                ) {
                    requireContext().showToast("Please enter other details")
                } else if (description.text.toString().equals("")) {
                    requireContext().showToast("Please enter description")
                } else {
                    dialog.cancel()
                    submitRequest(
                        categoryDropdown.selectedItem.toString(),
                        description.text.toString(),
                        locDropdown.selectedItem.toString(),
                        isSos,
                        othersdetail.text.toString()
                    )
                }

            }
            dialog!!.setContentView(dialogView)
            dialog!!.setCancelable(true)
            dialog!!.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
            dialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun submitRequest(
        requisitionCategory: String,
        description: String,
        location: String,
        isSOSMarked: String,
        otherDetails: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["userId"] = Preferences.instance!!.userId!!
        data["userType"] = Preferences.instance!!.userType!!
        data["requisitionCategory"] = requisitionCategory
        data["description"] = description
        data["location"] = location
        data["isSOSMarked"] = isSOSMarked
        data["otherDetails"] = otherDetails

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.addRequisitionForMaintenance(data).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    val stringResponse = response.body()?.string()
                    val jsonobject = JSONObject(stringResponse)
                    context!!.showSnackBar(
                        jsonobject.getString("errorMessage"),
                        binding.rlMain
                    )
                    if (jsonobject.getString("errorCode").equals("0"))
                        getMainData()
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

    fun getMainData(){
        apiViewModel?.getMaintenance(
            Preferences!!.instance!!.userId!!,
            Preferences!!.instance!!.userType!!
        )?.observe(this,
            Observer {

                binding.progressBar.visibility = View.GONE
                try {
                    val stringResponse = it!!.data!!.string()
                    val jsonobject = JSONObject(stringResponse)
                    if (jsonobject.getString("errorCode").equals("1"))
                        requireContext().showSnackBar(
                            "Invalid Credentials! Please try again",
                            binding.rlMain
                        )
                    else {
                        var dataList = ArrayList<String>()
                        for (i in 0 until jsonobject.getJSONArray("responseObject").length()) {
                            dataList.add(
                                jsonobject.getJSONArray("responseObject").getJSONObject(i)
                                    .toString()
                            )
                        }
                        val mAdapter = MaintanceListAdapter(requireContext(), dataList)
                        binding.listView.layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.listView.adapter = mAdapter

                        if (dataList.size == 0)
                            binding.noResult.visibility = View.VISIBLE
                        else
                            binding.noResult.visibility = View.GONE

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
    }
}