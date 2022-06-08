package com.example.shopingofmine.ui.adapters

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopingofmine.data.model.apimodels.Review
import com.example.shopingofmine.databinding.CompleteReviewItemBinding
import com.example.shopingofmine.databinding.ShortReviewItemBinding


class CompleteReviewsRecyclerAdapter() :
    ListAdapter<Review, CompleteReviewsRecyclerAdapter.CompleteReviewViewHolder>(
        CompleteReviewDiffCallback()
    ) {

    inner class CompleteReviewViewHolder(private val binding: CompleteReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        @RequiresApi(Build.VERSION_CODES.N)
        fun fill(item: Review) {
            binding.apply {
                val content = SpannableString(item.reviewer)
                content.setSpan(UnderlineSpan(), 0, content.length, 0)
                (content).also { reviewer.text = it }

                reviewText.text = HtmlCompat.fromHtml(
                    HtmlCompat.fromHtml(item.review, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompleteReviewViewHolder = CompleteReviewViewHolder(
        CompleteReviewItemBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: CompleteReviewViewHolder, position: Int) {
        holder.fill(getItem(position))

    }

}


class CompleteReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}
