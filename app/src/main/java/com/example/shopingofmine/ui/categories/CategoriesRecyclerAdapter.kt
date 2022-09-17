package com.example.shopingofmine.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.CategoryItem
import com.example.shopingofmine.databinding.SubCategoryLayoutBinding


class CategoriesRecyclerAdapter(private val onClick: (String) -> Unit, private val groupedCategories: Map<Int, List<CategoryItem>>) :
    ListAdapter<CategoryItem, CategoriesRecyclerAdapter.CategoryViewHolder>(CategoryDiffCallback2()) {

    inner class CategoryViewHolder(private val binding: SubCategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var parentCategory: CategoryItem

        fun fill(item: CategoryItem) {

            parentCategory = item

            binding.apply {
                Glide.with(root)
                    .load(item.image.src)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(categoryIcon)
                categoryTitle.text = item.name
                seeAll.setOnClickListener {
                    onClick(item.id.toString())
                }
                val categoriesRecyclerAdapter = SubCategoriesRecyclerAdapter(onClick)
                recyclerView.adapter = categoriesRecyclerAdapter
                categoriesRecyclerAdapter.submitList(groupedCategories[item.id])
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder = CategoryViewHolder(
        SubCategoryLayoutBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.fill(getItem(position))

    }
}

class CategoryDiffCallback2 : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem == newItem
    }
}