package com.mobiquel.srccapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquel.srccapp.data.ApiManager
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.view.adapter.NoticeListAdapter
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import srccapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        getFacultyProfile()
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

}