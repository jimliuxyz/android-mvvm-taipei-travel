package com.example.taipeitravel.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.taipeitravel.R
import com.example.taipeitravel.databinding.FragmentDetailBinding
import com.example.taipeitravel.ui.main.adapters.ImageAdapter

// for show attraction detail
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set enter transition (hero image)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        enterTransition =
            TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.slide_bottom)
        returnTransition =
            TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.slide_bottom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.recycler.transitionName = "hero${args.item.id}"

        // setup image slider
        val arrayList = arrayListOf("empty")
        if (args.item.images.isNotEmpty()) {
            arrayList.clear()
            arrayList.addAll(args.item.images.map { it.src })
        }

        val adapter = ImageAdapter(requireContext(), arrayList)
        binding.recycler.adapter = adapter

        // bind formation
        binding.tvDetailTitle.text = args.item.name
        binding.tvDetailDesc.text =
            "${args.item.introduction.trim()}\n\n${args.item.address}\n${args.item.tel}"

        val url =
            arrayListOf<String?>(args.item.officialSite, args.item.url, args.item.facebook, "")
                .first { it?.isNotEmpty() == true }!!
        binding.tvDetailWerbsite.text = url

        // change action bar title
        (activity as MainActivity).setActionBarTitle(args.item.name)

        // set website click action
        binding.tvDetailWerbsite.setOnClickListener {
            val args_ = BrowserFragmentArgs(args.item.name, url)
            findNavController().navigate(R.id.browserFragment, args_.toBundle())
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}