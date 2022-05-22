package com.example.shopingofmine.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.databinding.CategoryLayoutBinding
import com.example.shopingofmine.model.serverdataclass.CategoryItem


class CategoriesRecyclerAdapter : ListAdapter<CategoryItem, CategoriesRecyclerAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    inner class CategoryViewHolder(private val binding: CategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var category: CategoryItem


        fun fill(item: CategoryItem) {
            category = item
            binding.apply {
                categoryTitle.apply {
                    text = item.name
                    isSelected = true
                }
                productCount.text = item.count.toString()
                Glide.with(root)
                    .load(item.image.src)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(categoryImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder = CategoryViewHolder(
        CategoryLayoutBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )


    override fun onBindViewHolder(holder: CategoriesRecyclerAdapter.CategoryViewHolder, position: Int) {
        holder.fill(getItem(position))

    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem.name == newItem.name
        //todo
    }
}