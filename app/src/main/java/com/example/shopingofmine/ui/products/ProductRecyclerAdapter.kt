package com.example.shopingofmine.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.LargeProductLayoutBinding
import com.example.shopingofmine.databinding.ProductLayoutBinding
import com.example.shopingofmine.model.serverdataclass.ProductItem


class ProductsRecyclerAdapter(private val onClick: (product: ProductItem) -> Unit) :
    ListAdapter<ProductItem, ProductsRecyclerAdapter.LargeProductViewHolder>(
        ProductDiffCallback()
    ) {

    inner class LargeProductViewHolder(private val binding: LargeProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var product: ProductItem

        fun fill(item: ProductItem) {
            product = item
            binding.apply {
                productName.text = product.name
                if (product.price.isNotBlank()) price.text = "%,d".format(product.price.toInt()) + " ریال"
                averageRating.text = "امتیاز ${item.average_rating} از 5"
                Glide.with(root)
                    .load(item.images[0].src)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .placeholder(R.drawable.ic_baseline_shopping_basket_24)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(productImage)
            }
            itemView.setOnClickListener {
                onClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LargeProductViewHolder = LargeProductViewHolder(
        LargeProductLayoutBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: LargeProductViewHolder, position: Int) {
        holder.fill(getItem(position))

    }

}


class ProductDiffCallback : DiffUtil.ItemCallback<ProductItem>() {
    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem == newItem
    }
}