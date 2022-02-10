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
import srccapp.databinding.FragmentNoticeBinding

class NoticeFragment:Fragment() {

    private var _binding: FragmentNoticeBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)


        getNotices()
        return binding.root
    }

    fun getNotices() {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, String> = HashMap()
        data["email"] = Preferences.instance!!.email!!
        data["userType"] = Preferences.instance!!.userType!!
        val apiManager: ApiManager? = ApiManager.init()
        apiManager!!.getNotices(data).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    val stringResponse = response.body()?.string()
                    val jsonobject = JSONObject(stringResponse)
                    if (jsonobject.getString("errorCode").equals("1"))
                        context!!.showSnackBar("Invalid Credentials! Please try again", binding.rlMain)
                    else {
                        var dataList = ArrayList<String>()
                        for (i in 0 until jsonobject.getJSONArray("responseObject").length()) {
                            dataList.add(
                                jsonobject.getJSONArray("responseObject").getJSONObject(i)
                                    .toString()
                            )
                        }
                        val mAdapter = NoticeListAdapter(context!!, dataList)
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

}