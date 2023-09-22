package com.example.sparktech.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.sparktech.R
import com.example.sparktech.data.model.DashboardData
import com.example.sparktech.databinding.DashboardLayoutItemBinding

class DashBoardListAdapter : RecyclerView.Adapter<DashBoardListAdapter.DashBoardViewHolder>() {

    inner class DashBoardViewHolder(private val binding: DashboardLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DashboardData) {
            binding.apply {
                itemImageView.load(
                    item.image_link
                ){
                    crossfade(true)
                    placeholder(R.drawable.placeholder_image)
                    error(R.drawable.placeholder_image)
                }
                itemText.text = item.id.toString()

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DashBoardListAdapter.DashBoardViewHolder {
        val binding = DashboardLayoutItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DashBoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashBoardListAdapter.DashBoardViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size ?: 0

    private val diffCallBack = object : DiffUtil.ItemCallback<DashboardData>() {
        override fun areItemsTheSame(oldItem: DashboardData, newItem: DashboardData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DashboardData, newItem: DashboardData): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)
}