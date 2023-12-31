package com.example.taipeitravel.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taipeitravel.databinding.FragmentHomeBinding
import com.example.taipeitravel.ui.main.adapters.AttractionAdapter
import com.example.taipeitravel.ui.main.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


// for list attraction
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })

    private lateinit var adp: AttractionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adp = AttractionAdapter(
            homeViewModel.attractions.value!!
        ) { idx, item, sharedElements ->
            (activity as MainActivity).navToDetail(item, sharedElements)
            homeViewModel.emitAnimateMapEvent(idx)
        }

        adp.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

        // fetch first page of data
        homeViewModel.fetch(reload = true)

        homeViewModel.attractions.observe(this) {
            adp.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this;

        // set bottom sheet initial value
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            BottomSheetBehavior.from(binding.btnSheet).apply {
                peekHeight = (200 * resources.displayMetrics.density + 0.5f).toInt()
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        // bind view and model
        binding.homeViewModel = homeViewModel

        binding.myAdapter = adp
        bindScrollListener()

        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
        binding.recyclerView.doOnPreDraw { startPostponedEnterTransition() }

        // scroll to last navigated attraction idx
        /*if (homeViewModel.lastNavDetailIdx >= 0) {
            binding.recyclerView.smoothScrollToPosition(homeViewModel.lastNavDetailIdx)
            homeViewModel.lastNavDetailIdx = -1
        }*/
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
}