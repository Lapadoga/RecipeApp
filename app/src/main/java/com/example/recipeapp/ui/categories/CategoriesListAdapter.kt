package com.example.recipeapp.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.databinding.ItemCategoryBinding
import com.example.recipeapp.model.Category


class CategoriesListAdapter(private var dataSet: List<Category>) :
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
        val imageUri = RecipesRepository.RECIPE_API_BASE_URL +
                RecipesRepository.RECIPE_API_IMAGES_CATALOG + positionData.imageUrl
        viewHolder.itemTitle.text = positionData.title
        viewHolder.itemDescription.text = positionData.description
        Glide.with(context)
            .load(imageUri)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.itemImage)
        viewHolder.itemImage.contentDescription =
            "${context.getString(R.string.text_item_category_description)} ${positionData.title.lowercase()}"

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount() = dataSet.size

    fun setDataSet(newDataSet: List<Category>) {
        dataSet = newDataSet
    }
}