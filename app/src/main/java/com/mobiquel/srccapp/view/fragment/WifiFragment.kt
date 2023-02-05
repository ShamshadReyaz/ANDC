package com.mobiquel.srccapp.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mobiquel.srccapp.databinding.FragmentWifiBinding
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.utils.showSnackBar
import com.mobiquel.srccapp.utils.showToast
import com.mobiquel.srccapp.view.viewmodel.APIViewModel4
import org.json.JSONObject


class WifiFragment : Fragment() {

    private var _binding: FragmentWifiBinding? = null
    private val binding get() = _binding!!
    private val apiViewModel: APIViewModel4 by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWifiBinding.inflate(inflater, container, false)
        //apiViewModel = ViewModelProviders.of(this).get(APIViewModel2::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences!!.instance!!.loadPreferences(requireContext())

        binding.progressBar.visibility = View.VISIBLE

        apiViewModel?.getWifiData(Preferences.instance!!.collegeRollNo!!)?.observe(this, Observer {

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
                    binding.rollNumber.setText(
                        jsonobject.getJSONObject("responseObject")
                            .getString("rollNo")
                    )
                    binding.wifiId.setText(
                        jsonobject.getJSONObject("responseObject").getString("wifiId")
                    )
                    binding.password.setText(
                        jsonobject.getJSONObject("responseObject").getString("password")
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        Log.e("ON CREATE VIEW", "PROFILE FRAGMENT")

       /* binding.textIl1.setOnClickListener {

            val manager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clipData = ClipData.newPlainText("text", binding.rollNumber.getText())
            manager!!.setPrimaryClip(clipData)
            requireContext().showToast("Text Copied")

        }*/
        binding.copy1.setOnClickListener {
            val manager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clipData = ClipData.newPlainText("text", binding.rollNumber.getText())
            manager!!.setPrimaryClip(clipData)
            requireContext().showToast("Text Copied")

        }
        binding.copy2.setOnClickListener {
            val manager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clipData = ClipData.newPlainText("text", binding.wifiId.getText())
            manager!!.setPrimaryClip(clipData)
            requireContext().showToast("Text Copied")

        }
        binding.copy3.setOnClickListener {
            val manager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clipData = ClipData.newPlainText("text", binding.password.getText())
            manager!!.setPrimaryClip(clipData)
            requireContext().showToast("Text Copied")

        }
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




}