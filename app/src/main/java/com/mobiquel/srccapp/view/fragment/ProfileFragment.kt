package com.mobiquel.srccapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.databinding.FragmentProfileBinding
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.isValidEmail
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.utils.validatePhoneNumber
import com.mobiquel.srccapp.view.viewmodel.APIViewModel2
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiViewModel: APIViewModel2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        //  apiViewModel = APIViewModel2()

        Preferences!!.instance!!.loadPreferences(requireContext())
        //  var model = ProfileRequestModel()

        if (Preferences!!.instance!!.userType.equals("student")) {
            //model.studentId = Preferences.instance!!.userId!!
            //model.collegeRollNo = Preferences.instance!!.collegeRollNo!!
            getStudentProfile()
        } else if (Preferences!!.instance!!.userType.equals("faculty")) {
            getFacultyProfile()
            //model.facultyId=Preferences.instance!!.userId!!
        } else {
            getNonTeachingStaff()
            //model.staffId=Preferences.instance!!.userId!!
        }


        /*  binding.progressBar.visibility = View.VISIBLE

          apiViewModel?.getProfile(model)?.observe(requireActivity(), Observer {

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

                      if (Preferences!!.instance!!.userType.equals("student")) {
                          binding.facultyName.setText(
                              jsonobject.getJSONObject("responseObject")
                                  .getString("name")
                          )
                          binding.employeeCode.setText(
                              jsonobject.getJSONObject("responseObject").getString("enrollmentNo")
                          )
                          binding.empCodeTil.hint = "College Roll No"
                          binding.designationTil.hint = "Category"
                          binding.deptTil.hint = "Programme"

                          binding.designation.setText(
                              jsonobject.getJSONObject("responseObject").getString("category")
                          )

                          binding.department.setText(
                              jsonobject.getJSONObject("responseObject").getString("programName")
                          )
                          binding.mobile.setText(
                              jsonobject.getJSONObject("responseObject").getString("mobile")
                          )
                          binding.email.setText(
                              jsonobject.getJSONObject("responseObject").getString("email")
                          )
                          binding.personalEmail.setText(
                              jsonobject.getJSONObject("responseObject").getString("personalEmail")
                          )
                          binding.facultyTypeTil.visibility = View.GONE
                          binding.facultyType.setText(
                              jsonobject.getJSONObject("responseObject").getString("admissionType")
                          )

                      }
                      else if (Preferences!!.instance!!.userType.equals("faculty")) {
                          binding.facultyName.setText(
                              jsonobject.getJSONObject("responseObject")
                                  .getString("title") + " " + jsonobject.getJSONObject("responseObject")
                                  .getString("name")
                          )
                          binding.employeeCode.setText(
                              jsonobject.getJSONObject("responseObject").getString("employeeId")
                          )
                          binding.designation.setText(
                              jsonobject.getJSONObject("responseObject").getString("designation")
                          )
                          binding.department.setText(
                              jsonobject.getJSONObject("responseObject").getString("department")
                          )
                          binding.mobile.setText(
                              jsonobject.getJSONObject("responseObject").getString("mobile")
                          )
                          binding.email.setText(
                              jsonobject.getJSONObject("responseObject").getString("email")
                          )
                          binding.personalEmail.setText(
                              jsonobject.getJSONObject("responseObject").getString("personalEmail")
                          )
                          binding.facultyType.setText(
                              jsonobject.getJSONObject("responseObject").getString("type")
                          )
                      }
                      else {
                          binding.facultyName.setText(
                              jsonobject.getJSONObject("responseObject")
                                  .getString("name")
                          )
                          binding.employeeCode.setText(
                              jsonobject.getJSONObject("responseObject").getString("employeeId")
                          )
                          binding.designation.setText(
                              jsonobject.getJSONObject("responseObject").getString("designation")
                          )
                          binding.department.setText(
                              jsonobject.getJSONObject("responseObject").getString("department")
                          )

                          binding.mobile.setText(
                              jsonobject.getJSONObject("responseObject").getString("mobile")
                          )
                          if (jsonobject.getJSONObject("responseObject").getString("mobile")
                                  .equals("")
                          )
                              binding.mobile.setText("N/A")
                          else
                              binding.mobile.setText(
                                  jsonobject.getJSONObject("responseObject").getString("mobile")
                              )

                          if (jsonobject.getJSONObject("responseObject").getString("personalEmail")
                                  .equals("")
                          )
                              binding.personalEmail.setText("N/A")
                          else
                              binding.personalEmail.setText(
                                  jsonobject.getJSONObject("responseObject")
                                      .getString("personalEmail")
                              )

                          binding.email.setText(
                              jsonobject.getJSONObject("responseObject").getString("email")
                          )

                          binding.facultyTypeTil.visibility = View.GONE
                          binding.deptTil.visibility = View.GONE

                      }

                  }
              } catch (e: Exception) {
                  e.printStackTrace()
              }
          })

        */  Log.e("ON CREATE VIEW", "PROFILE FRAGMENT")


        return binding.root
    }

    private fun getFacultyProfile() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["facultyId"] = Preferences.instance!!.userId!!
        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getFacultyProfile(data).enqueue(object : Callback<ResponseBody> {
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
                        binding.facultyName.setText(jsonobject.getJSONObject("responseObject").getString("title")+" "+jsonobject.getJSONObject("responseObject").getString("name"))
                        binding.employeeCode.setText(jsonobject.getJSONObject("responseObject").getString("employeeId"))
                        binding.designation.setText(jsonobject.getJSONObject("responseObject").getString("designation"))
                        binding.department.setText(jsonobject.getJSONObject("responseObject").getString("department"))
                        binding.mobile.setText(jsonobject.getJSONObject("responseObject").getString("mobile"))
                        binding.email.setText(jsonobject.getJSONObject("responseObject").getString("email"))
                        binding.personalEmail.setText(jsonobject.getJSONObject("responseObject").getString("personalEmail"))
                        binding.facultyType.setText(jsonobject.getJSONObject("responseObject").getString("type"))
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

    private fun getStudentProfile() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["studentId"] = Preferences.instance!!.userId!!
        data["collegeRollNo"] = Preferences.instance!!.collegeRollNo!!

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getStudentProfile(data).enqueue(object : Callback<ResponseBody> {
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
                        binding.facultyName.setText(
                            jsonobject.getJSONObject("responseObject")
                                .getString("name")
                        )
                        binding.employeeCode.setText(
                            jsonobject.getJSONObject("responseObject").getString("enrollmentNo")
                        )
                        binding.empCodeTil.hint = "College Roll No"
                        binding.designationTil.hint = "Category"
                        binding.deptTil.hint = "Programme"

                        binding.designation.setText(
                            jsonobject.getJSONObject("responseObject").getString("category")
                        )

                        binding.department.setText(
                            jsonobject.getJSONObject("responseObject").getString("programName")
                        )
                        binding.mobile.setText(
                            jsonobject.getJSONObject("responseObject").getString("mobile")
                        )
                        binding.email.setText(
                            jsonobject.getJSONObject("responseObject").getString("email")
                        )
                        binding.personalEmail.setText(
                            jsonobject.getJSONObject("responseObject").getString("personalEmail")
                        )
                        binding.facultyTypeTil.visibility = View.GONE
                        binding.facultyType.setText(
                            jsonobject.getJSONObject("responseObject").getString("admissionType")
                        )
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


    private fun getNonTeachingStaff() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["staffId"] = Preferences.instance!!.userId!!

        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getNonTeachingStaffById(data).enqueue(object : Callback<ResponseBody> {
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

                        binding.facultyName.setText(
                            jsonobject.getJSONObject("responseObject")
                                .getString("name")
                        )
                        binding.employeeCode.setText(
                            jsonobject.getJSONObject("responseObject").getString("employeeId")
                        )
                        binding.designation.setText(
                            jsonobject.getJSONObject("responseObject").getString("designation")
                        )
                        binding.department.setText(
                            jsonobject.getJSONObject("responseObject").getString("department")
                        )

                        binding.mobile.setText(
                            jsonobject.getJSONObject("responseObject").getString("mobile")
                        )
                        if (jsonobject.getJSONObject("responseObject").getString("mobile")
                                .equals("")
                        )
                            binding.mobile.setText("N/A")
                        else
                            binding.mobile.setText(
                                jsonobject.getJSONObject("responseObject").getString("mobile")
                            )

                        if (jsonobject.getJSONObject("responseObject").getString("personalEmail")
                                .equals("")
                        )
                            binding.personalEmail.setText("N/A")
                        else
                            binding.personalEmail.setText(
                                jsonobject.getJSONObject("responseObject")
                                    .getString("personalEmail")
                            )

                        binding.email.setText(
                            jsonobject.getJSONObject("responseObject").getString("email")
                        )

                        binding.facultyTypeTil.visibility = View.GONE
                        binding.deptTil.visibility = View.GONE
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

    fun updateProfileCheck() {
        if (!requireContext().validatePhoneNumber(binding.mobile.text.toString()))
            requireContext().showSnackBar("Please enter mobile number", binding.rlMain)
        else if (!requireContext().isValidEmail(binding.personalEmail.text.toString()))
            requireContext().showSnackBar("Please enter email address", binding.rlMain)
        else {
            updateProfile()
        }

    }

    fun updateProfile() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["userId"] = Preferences.instance!!.userId!!
        data["userType"] = Preferences.instance!!.userType!!
        data["mobile"] = binding.mobile.text.toString()
        data["personalEmail"] = binding.personalEmail.text.toString()
        //userId,userType,mobile,personalEmail
        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.updateUserMobilePersonalEmail(data).enqueue(object : Callback<ResponseBody> {
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
                        binding.mobile.isEnabled = false
                        binding.personalEmail.isEnabled = false
                        binding.mobileTil.isErrorEnabled=false
                        binding.personalEmailTil.isErrorEnabled=false
                        context!!.showSnackBar(jsonobject.getString("errorMessage"), binding.rlMain)
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

    fun openEditMode() {
        binding.mobile.isEnabled = true
        binding.personalEmail.isEnabled = true

        binding.mobileTil.error = "Update Mobile Number"
        binding.personalEmailTil.error = "Update Personal Email Address"
    }


}