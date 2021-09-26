package com.challenge.app.flow.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.challenge.app.R
import com.challenge.app.databinding.AdapterItemBinding
import com.challenge.app.extensions.makeGone
import com.challenge.app.extensions.makeVisible
import com.challenge.app.models.Beer
import com.challenge.app.utils.setSafeOnClickListener
import java.util.*

class RVListAdapter(
    private val dataList: ArrayList<Beer>?,
    private val onItemClicked: (item: Beer) -> Unit
) : RecyclerView.Adapter<RVListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: kotlin.run { 0 }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { item ->
            holder.loadData(item)

            with(holder.binding) {
                this.root.setSafeOnClickListener { onItemClicked(item) }
            }
        }
    }

    class ViewHolder(
        val binding: AdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun loadData(item: Beer?) = with(binding) {
            tvTitle.text = item?.name
            tvABV.text = root.resources.getString(R.string.abv, item?.abv ?: "")

            Glide.with(imageView)
                .load(item?.imageUrl)
                .centerInside()
                .placeholder(R.drawable.bg_image_place_holder)
                .into(imageView)

            if(item?.isFavorite == true){
                ivFavorite.makeVisible()
            } else {
                ivFavorite.makeGone()
            }
        }
    }

    fun setData(records: List<Beer>) {
        dataList?.clear()
        dataList?.addAll(records)
        notifyDataSetChanged()
    }
}
