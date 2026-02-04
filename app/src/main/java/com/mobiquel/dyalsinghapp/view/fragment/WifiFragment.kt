package com.mobiquel.dyalsinghapp.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobiquel.dyalsinghapp.databinding.FragmentWifiBinding
import com.mobiquel.dyalsinghapp.utils.Preferences
import com.mobiquel.dyalsinghapp.utils.showToast


class WifiFragment : Fragment() {

    private var _binding: FragmentWifiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWifiBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences!!.instance!!.loadPreferences(requireContext())

        binding.progressBar.visibility = View.VISIBLE
       Log.e("ON CREATE VIEW", "PROFILE FRAGMENT")


        binding.copy1.setOnClickListener {
                copyText(binding.rollNumber.text.toString())
        }
        binding.copy2.setOnClickListener {
            copyText(binding.wifiId.text.toString())
        }
        binding.copy3.setOnClickListener {
            copyText(binding.password.text.toString())
        }
        binding.copy4.setOnClickListener {
            copyText(binding.url.text.toString())
        }
        binding.copy5.setOnClickListener {
            copyText(binding.email.text.toString())
        }
        binding.copy6.setOnClickListener {
            copyText(binding.passwordMs.text.toString())
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

    fun copyText(data:String){
        val manager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        val clipData = ClipData.newPlainText("text", data)
        manager!!.setPrimaryClip(clipData)
        requireContext().showToast("Text Copied")
    }




}