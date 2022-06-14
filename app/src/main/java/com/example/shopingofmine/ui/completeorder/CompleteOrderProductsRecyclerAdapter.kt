package com.example.shopingofmine.ui.completeorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.databinding.CompleteOrderProductItemBinding


class CompleteOrderProductsRecyclerAdapter(private val onClick: (ProductItem) -> Unit, private val countList: List<Int>) :
    ListAdapter<ProductItem, CompleteOrderProductsRecyclerAdapter.CompleteOrderViewHolder>(
        CompleteOrderDiffCallback()
    ) {

    inner class CompleteOrderViewHolder(private val binding: CompleteOrderProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun fill(item: ProductItem) {
            binding.apply {
                Glide.with(root)
                    .load(item.images[0].src)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .placeholder(R.drawable.ic_baseline_shopping_basket_24)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(productImage)
                (countList[adapterPosition].toString() + " عدد").also { binding.count.text = it }
            }
            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompleteOrderViewHolder = CompleteOrderViewHolder(
        CompleteOrderProductItemBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CompleteOrderViewHolder, position: Int) {
        holder.fill(getItem(position))
    }
}


class CompleteOrderDiffCallback : DiffUtil.ItemCallback<ProductItem>() {
    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem == newItem
    }

}
