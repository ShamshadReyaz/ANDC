package com.mobiquel.dyalsinghapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobiquel.dyalsinghapp.R
import com.mobiquel.dyalsinghapp.databinding.FragmentNoticeBinding
import com.mobiquel.dyalsinghapp.utils.Preferences
import com.mobiquel.dyalsinghapp.utils.showToast

class MaintenanceFragment : Fragment() {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)


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
                override fun onCheckedChanged(p0: CompoundButton, p1: Boolean) {
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

      /*  val apiManager: ApiManager? = ApiManager.init()
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
*/
    }

    fun getMainData(){

    }
}