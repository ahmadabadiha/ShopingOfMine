package com.example.shopingofmine.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.CategoryLayoutBinding
import com.example.shopingofmine.model.serverdataclass.CategoryItem
import java.util.*


class CategoriesRecyclerAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<CategoryItem, CategoriesRecyclerAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    inner class CategoryViewHolder(private val binding: CategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var category: CategoryItem


        fun fill(item: CategoryItem) {

            category = item
            binding.apply {
                categoryImage.setBackgroundColor(Color.rgb(Random().nextInt(256), Random().nextInt(256), Random().nextInt(256)))
                categoryTitle.apply {
                    text = item.name
                    isSelected = true
                }
                productCount.text = "تعداد کالا: " + item.count.toString()
                Glide.with(root)
                    .load(item.image.src)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(categoryImage)
            }
            binding.layout.setOnClickListener {
                onClick(item.id.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder = CategoryViewHolder(
        CategoryLayoutBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.fill(getItem(position))

    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem == newItem
    }
}