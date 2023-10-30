package com.example.taipeitravel.utilities

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

@BindingAdapter(
    "srcUrl",
    "placeholder",
    requireAll = true
)
fun ImageView.bindAttr(url: String?, placeHolder: Drawable) {
    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    Glide.with(this.context).load(url ?: "")
        .placeholder(placeHolder)
        .transition(DrawableTransitionOptions.withCrossFade(factory))
        .into(this)
}