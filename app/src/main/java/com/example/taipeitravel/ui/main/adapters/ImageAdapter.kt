package com.example.taipeitravel.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taipeitravel.R
import com.example.taipeitravel.databinding.ItemImgBinding

// adapter for list landscape image
class ImageAdapter(val context: Context, val arrayList: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(arrayList[position])
            .placeholder(R.drawable.img_placeholder5)
            .into(holder.imageView)
    }

    class ViewHolder(val binding: ItemImgBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView get() = binding.listItemImage
    }
}