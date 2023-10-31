package com.example.taipeitravel.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taipeitravel.R
import com.example.taipeitravel.databinding.FragmentHomeBinding
import com.example.taipeitravel.ui.main.adapters.AttractionAdapter
import com.example.taipeitravel.ui.main.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

// for list attraction
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

    private lateinit var adp: AttractionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adp = AttractionAdapter(
            homeViewModel.attractions.value!!
        ) { item, sharedElements ->
            val destination = HomeFragmentDirections.actionStartFragmentToDetailFragment(item)
            sharedElements.transitionName = "hero${item.id}" // set for hero image animation
            binding.executePendingBindings()
            val extras = FragmentNavigatorExtras(
                sharedElements to sharedElements.transitionName,
            )
            val builder = NavOptions.Builder()
                .setLaunchSingleTop(true)

            builder.setPopUpTo(R.id.startFragment, false, true)
            findNavController().navigate(destination, extras)
        }

        adp.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

        // fetch first page of data
        homeViewModel.fetch(reload = true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // reuse the view if it has not been destroyed
        if (_binding != null)
            return binding.root

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // set bottom sheet initial value
        BottomSheetBehavior.from(binding.btnSheet).apply {
            peekHeight = (200 * resources.displayMetrics.density + 0.5f).toInt()
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        // bind view adn model
        binding.homeViewModel = homeViewModel

        homeViewModel.attractions.observe(viewLifecycleOwner) {
            adp.notifyDataSetChanged()
        }

        binding.myAdapter = adp
        bindScrollListener()

        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
        binding.recyclerView.doOnPreDraw { startPostponedEnterTransition() }

        return binding.root
    }

    private var onFetching = false
    private fun bindScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val pos = linearLayoutManager?.findLastCompletelyVisibleItemPosition() ?: -1

                val adapter = binding.myAdapter!!
                if (adapter.showLoadMore && !onFetching && adapter.itemCount > 1 && pos == adapter.itemCount - 1) {
                    onFetching = true
                    homeViewModel.fetch(reload = false) {
                        onFetching = false
                        adapter.showLoadMore = false
                    }
                }
                if (!adapter.showLoadMore && pos <= adapter.itemCount - 2) {
                    adapter.showLoadMore = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerView.clearOnScrollListeners()
        _binding = null
    }

}