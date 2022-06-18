package com.example.shopingofmine.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.databinding.SearchedProductItemBinding


class SearchRecyclerAdapter(private val onClick: (product: ProductItem) -> Unit) :
    ListAdapter<ProductItem, SearchRecyclerAdapter.SearchViewHolder>(
        SearchDiffCallback()
    ) {

    inner class SearchViewHolder(private val binding: SearchedProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var product: ProductItem

        fun fill(item: ProductItem) {
            product = item
            binding.apply {
                productName.text = product.name
                if (item.images.isNotEmpty()) {
                    Glide.with(root)
                        .load(item.images[0].src)
                        .error(R.drawable.ic_baseline_error_outline_24)
                        .placeholder(R.drawable.ic_baseline_shopping_basket_24)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(productImage)
                }
            }
            itemView.setOnClickListener {
                onClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            SearchedProductItemBinding.inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.fill(getItem(position))

    }
}


class SearchDiffCallback : DiffUtil.ItemCallback<ProductItem>() {
    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem == newItem
    }
}