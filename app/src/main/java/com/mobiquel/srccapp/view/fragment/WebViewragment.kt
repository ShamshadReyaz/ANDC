package com.mobiquel.srccapp.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mobiquel.srccapp.databinding.FragmentWebviewBinding
import com.mobiquel.srccapp.utils.Preferences
import com.mobiquel.srccapp.view.viewmodel.APIViewModel4
import kotlinx.android.synthetic.main.fragment_webview.*


class WebViewragment : Fragment() {

    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!
    private val apiViewModel: APIViewModel4 by viewModels()
    private var url = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)

        url = "https://yourdost.com/login/sso?token="+requireArguments().getString("TOKEN")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences!!.instance!!.loadPreferences(requireContext())

        binding.progressBar.visibility = View.VISIBLE

        webview.getSettings().setDefaultTextEncodingName("UTF-8")
        val settings: WebSettings = webview.getSettings()
        settings.domStorageEnabled = true
        settings.defaultTextEncodingName = "UTF-8"
        settings.pluginState = WebSettings.PluginState.ON_DEMAND
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.javaScriptEnabled = true
        settings.loadsImagesAutomatically = true
        // settings.setAppCachePath("/data/data/com.your.package.appname/cache"‌​);
      //  settings.setAppCacheEnabled(true)
       // settings.cacheMode = WebSettings.LOAD_DEFAULT

        webview.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
             try{
                 if (requireActivity() != null && isAdded)
                     requireActivity().setProgress(progress * 1000)
             }catch (e:Exception){}

            }
        })
        webview.setWebViewClient(object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = View.GONE
            }

            @Throws(Exception::class)
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                try {
                    progressBar.visibility = View.GONE
                    Log.e("URL_DOST_FINI",url)
                }catch (e:java.lang.Exception){}

            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("URL_DOST_SART",url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                return true
            }
        }
        )


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
        webview.loadUrl(url)
        Log.e("RESUME", "PROFILE FRAGMENT")
    }


}