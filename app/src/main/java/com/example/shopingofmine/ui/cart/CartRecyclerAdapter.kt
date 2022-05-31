package com.example.shopingofmine.ui.cart

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.CartItemBinding
import com.example.shopingofmine.model.serverdataclass.CategoryItem
import com.example.shopingofmine.model.serverdataclass.ProductItem
import java.util.*

class CartRecyclerAdapter(private val countList: List<Int>, private val onClick: (product: ProductItem) -> Unit) :
    ListAdapter<ProductItem, CartRecyclerAdapter.CartViewHolder>(CartDiffCallback()) {

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun fill(item: ProductItem, position: Int) {
            binding.apply {
                Glide.with(root)
                    .load(item.images[0].src)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .placeholder(R.drawable.ic_baseline_shopping_basket_24)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(productImage)
                productName.text = item.name
                price.text = "%,d".format(item.regular_price.toInt()) + " ریال"
                if (item.regular_price != item.price) {
                    val discountAmount = item.regular_price.toDouble() - item.price.toDouble()
                    val discountPercent = ((discountAmount / item.regular_price.toInt()) * 100).toInt()
                    discount.text = "%,d".format(discountAmount.toInt()) + " ریال" + discountPercent + "%"
                }
                count.text = countList[position].toString()
            }
            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder = CartViewHolder(
        CartItemBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.fill(getItem(position), position)

    }
}

class CartDiffCallback : DiffUtil.ItemCallback<ProductItem>() {
    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem == newItem
    }
}