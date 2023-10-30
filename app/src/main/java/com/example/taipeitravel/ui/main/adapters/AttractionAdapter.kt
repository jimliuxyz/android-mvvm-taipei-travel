package com.example.taipeitravel.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taipeitravel.databinding.ItemLoadingBinding
import com.example.taipeitravel.databinding.ItemRowBinding
import com.example.taipeitravel.models.Attraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// adapter for list attraction
class AttractionAdapter(
    var dataModelList: ArrayList<Attraction>,
    val clickListener: ((item: Attraction, sharedElements: View) -> Unit)? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    private var _showLoadMore = false
    var showLoadMore: Boolean
        get() {
            return _showLoadMore
        }
        set(value) {
            _showLoadMore = value
            GlobalScope.launch(Dispatchers.Main) {
                notifyItemChanged(itemCount - 1)
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        } else {
            val binding =
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LoadingViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= dataModelList.count()) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ItemViewHolder) {
            val dataModel: Attraction = dataModelList[position]
            holder.bind(position, dataModel)
        } else if (holder is LoadingViewHolder) {
            holder.bind(showLoadMore && dataModelList.isNotEmpty())
        }
    }

    override fun getItemCount(): Int {
        return dataModelList.size + 1
    }

    inner class ItemViewHolder(val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(idx: Int, data: Attraction) {
            binding.img = if (data.images.isNotEmpty()) data.images[0].src else ""
            binding.itemIdx = idx
            binding.item = data
            binding.executePendingBindings()
            binding.itemClickListener = View.OnClickListener {
                clickListener?.invoke(data, binding.itemImage)
            }
        }
    }

    inner class LoadingViewHolder(val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show: Boolean) {
            binding.show = show
        }
    }
}