package com.example.shopingofmine.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.databinding.CartItemBinding

class CartRecyclerAdapter(
    private val countList: List<Int>,
    private val onImageClick: (product: ProductItem) -> Unit,
    private val onAddClick: (product: ProductItem) -> Unit,
    private val onSubtractClick: (product: ProductItem) -> Unit
) :
    ListAdapter<ProductItem, CartRecyclerAdapter.CartViewHolder>(CartDiffCallback()) {

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: ProductItem

        fun fill(item: ProductItem) {
            this.item = item
            binding.apply {
                Glide.with(root)
                    .load(item.images[0].src)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .placeholder(R.drawable.ic_baseline_shopping_basket_24)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(productImage)
                productName.text = item.name
                val computedPrice = item.regular_price.toInt() * countList[adapterPosition]
                ("%,d".format(computedPrice) + " ریال").also { price.text = it }
                if (item.regular_price != item.price) {
                    val discountAmount = (item.regular_price.toDouble() - item.price.toDouble()) * countList[adapterPosition]
                    ("%,d".format(discountAmount.toInt()) + " ریال" + " تخفیف").also { discount.text = it }
                }
                count.text = countList[adapterPosition].toString()
            }
            setOnClickListeners()

        }

        private fun setOnClickListeners() {

            binding.productImage.setOnClickListener {
                onImageClick(item)
            }

            binding.add.setOnClickListener {
                onAddClick(item)
                fill(item)
            }

            binding.subtract.setOnClickListener {
                onSubtractClick(item)
                if (countList.isNotEmpty()) fill(item)
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
        holder.fill(getItem(position))

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