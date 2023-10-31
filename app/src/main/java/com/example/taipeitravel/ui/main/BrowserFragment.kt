package com.example.taipeitravel.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.taipeitravel.databinding.FragmentBrowserBinding

// for browse website
class BrowserFragment : Fragment() {

    private var _binding: FragmentBrowserBinding? = null
    val binding get() = _binding!!

    private val args: BrowserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowserBinding.inflate(inflater, container, false)

        // change action bar title
        (activity as MainActivity).setActionBarTitle(args.name)

        // setup webview
        val mWebSettings = binding.wvBrowser.getSettings()
        mWebSettings.javaScriptEnabled = true
        mWebSettings.domStorageEnabled = true
        mWebSettings.javaScriptCanOpenWindowsAutomatically = true
        binding.wvBrowser.webViewClient = WebViewClient()
        binding.wvBrowser.loadUrl(args.url)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}