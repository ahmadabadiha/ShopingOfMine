package com.example.shopingofmine.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.shopingofmine.databinding.ProductLayoutBinding
import com.example.shopingofmine.model.serverdataclass.ProductItem


class ProductsRecyclerAdapter : ListAdapter<ProductItem, ProductsRecyclerAdapter.ProductViewHolder>(DiffCallback()) {
    // private lateinit var viewClick: ViewClick

    inner class ProductViewHolder(private val binding: ProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)/*, View.OnClickListener*/ {
        //private var id = ""
        lateinit var user: ProductItem

        /*  init {
              binding.root.setOnClickListener(this)
          }

          override fun onClick(p0: View?) {
              viewClick.onClick(id)
          }*/

        fun fill(item: ProductItem) {
            user = item
            binding.apply {
                productName.apply {
                    text = item.name
                    isSelected = true
                }
                averageRating.text = item.average_rating
                Glide.with(root)
                    .load(item.images[0].src)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(productImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder = ProductViewHolder(
        ProductLayoutBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )


    override fun onBindViewHolder(holder: ProductsRecyclerAdapter.ProductViewHolder, position: Int) {
        holder.fill(getItem(position))

    }

    /* interface ViewClick {
         fun onClick(id: String)
     }

     fun onViewClicked(viewClick: ViewClick) {
         this.viewClick = viewClick
     }*/
}


class DiffCallback : DiffUtil.ItemCallback<ProductItem>() {
    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.name == newItem.name
        //todo
    }
}