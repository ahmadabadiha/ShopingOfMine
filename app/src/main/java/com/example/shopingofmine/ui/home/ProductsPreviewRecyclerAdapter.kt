package com.example.shopingofmine.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.R
import com.example.shopingofmine.data.model.apimodels.ProductItem
import com.example.shopingofmine.databinding.EndItemBinding
import com.example.shopingofmine.databinding.ProductLayoutBinding
import com.example.shopingofmine.databinding.StartItemBinding
import com.example.shopingofmine.ui.home.ListType

private const val ITEM_VIEW_TYPE_START = -1
private const val ITEM_VIEW_TYPE_ITEM = 0
private const val ITEM_VIEW_TYPE_END = 1

class ProductsPreviewRecyclerAdapter(private val onClick: (product: ProductItem) -> Unit, private val listType: ListType) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(
        ProductPreviewDiffCallback()
    ) {

    inner class ProductViewHolder(private val binding: ProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), CustomViewHolder {

        lateinit var product: ProductItem

        override fun fill(dateItem: DataItem) {
            product = (dateItem as DataItem.ProductPreviewItem).product
            binding.apply {
                productName.text = product.name
                "امتیاز ${product.average_rating} از 5".also { averageRating.text = it }
                if (product.images.isNotEmpty()) {
                    Glide.with(root)
                        .load(product.images[0].src)
                        .error(R.drawable.ic_baseline_error_outline_24)
                        .placeholder(R.drawable.ic_baseline_shopping_basket_24)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(productImage)
                }
                layout.setOnClickListener { onClick(product) }
            }
        }

    }

    inner class EndViewHolder(private val binding: EndItemBinding) :
        RecyclerView.ViewHolder(binding.root), CustomViewHolder {
        override fun fill(dateItem: DataItem) {

        }

    }

    inner class StartViewHolder(private val binding: StartItemBinding) :
        RecyclerView.ViewHolder(binding.root), CustomViewHolder {
        override fun fill(dateItem: DataItem) {
            when (listType) {
                ListType.POPULAR -> binding.icon.setImageResource(R.drawable.ic_outline_remove_red_eye_24)
                ListType.NEWEST -> binding.icon.setImageResource(R.drawable.ic_round_whatshot_24)
                ListType.TOP_RATED -> binding.icon.setImageResource(R.drawable.ic_round_star_24)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> ProductViewHolder(ProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_VIEW_TYPE_START -> StartViewHolder(StartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_VIEW_TYPE_END -> EndViewHolder(EndItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType ${viewType}.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomViewHolder) holder.fill(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.ProductPreviewItem -> ITEM_VIEW_TYPE_ITEM
            DataItem.Start -> ITEM_VIEW_TYPE_START
            DataItem.End -> ITEM_VIEW_TYPE_END
            else -> throw ClassCastException("Unknown viewType.")
        }
    }

    fun addStartItemAndSubmitList(list: List<ProductItem>) {
        val items = listOf(DataItem.Start) + list.map { DataItem.ProductPreviewItem(it) } + listOf(DataItem.End)
        submitList(items)

    }

}


sealed class DataItem {
    abstract val id: Int

    data class ProductPreviewItem(val product: ProductItem) : DataItem() {
        override val id = product.id
    }

    object Start : DataItem() {
        override val id = Int.MIN_VALUE
    }

    object End : DataItem() {
        override val id = Int.MAX_VALUE
    }
}


class ProductPreviewDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

interface CustomViewHolder {
    fun fill(dateItem: DataItem)
}