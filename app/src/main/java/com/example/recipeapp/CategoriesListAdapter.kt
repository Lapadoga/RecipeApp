package com.example.recipeapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemCategoryBinding
import com.example.recipeapp.data.Category
import java.io.IOException


class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemTitle: TextView = binding.tvCategoryTitle
        val itemDescription: TextView = binding.tvCategoryDescription
        val itemImage: ImageView = binding.imageCategory
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)

        return ViewHolder(ItemCategoryBinding.inflate(inflater, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val positionData = dataSet[position]
        val context = viewHolder.itemView.context
        val drawable = try {
            val stream = context?.assets?.open(positionData.imageUrl)
            Drawable.createFromStream(stream, null)
        } catch (e: IOException) {
            Log.e("error", e.stackTraceToString())
            null
        }
        viewHolder.itemTitle.text = positionData.title
        viewHolder.itemDescription.text = positionData.description
        viewHolder.itemImage.setImageDrawable(drawable)
        viewHolder.itemImage.contentDescription =
            "${context.getString(R.string.text_item_category_description)} ${positionData.title.lowercase()}"

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount() = dataSet.size
}