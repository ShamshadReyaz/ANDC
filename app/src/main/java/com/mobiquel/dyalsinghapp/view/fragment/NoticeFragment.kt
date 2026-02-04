package com.mobiquel.dyalsinghapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquel.dyalsinghapp.databinding.FragmentNoticeBinding
import com.mobiquel.dyalsinghapp.data.ApiManager
import com.mobiquel.dyalsinghapp.utils.Preferences
import com.mobiquel.dyalsinghapp.utils.showSnackBar
import com.mobiquel.dyalsinghapp.view.adapter.NoticeListAdapter
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class NoticeFragment : Fragment() {
    @Inject
    lateinit var apiManager: ApiManager
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
        Preferences!!.instance!!.loadPreferences(requireContext())

        val data: MutableMap<String, String> = HashMap()
        data["email"] = Preferences!!.instance!!.email!!
        data["userType"] = Preferences!!.instance!!.userType!!

        binding.progressBar.visibility = View.VISIBLE
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