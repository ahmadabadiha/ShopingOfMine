package com.example.shopingofmine.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.SliderImageLayoutBinding

class HomeSliderViewPagerAdapter(private val imageUrlList: List<String>) :
    RecyclerView.Adapter<HomeSliderViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(private val binding: SliderImageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_shopping_basket_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.sliderImage)
        }

    }

    override fun getItemCount(): Int = imageUrlList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val binding = SliderImageLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.setData(imageUrlList[position])
    }
}