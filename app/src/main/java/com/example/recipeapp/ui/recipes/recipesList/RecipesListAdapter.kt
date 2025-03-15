package com.example.recipeapp.ui.recipes.recipesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.databinding.ItemRecipeBinding
import com.example.recipeapp.model.Recipe

class RecipesListAdapter(private var dataset: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemImage = binding.imageRecipe
        val itemTitle = binding.tvRecipeTitle
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)

        return ViewHolder(ItemRecipeBinding.inflate(inflater, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val positionData = dataset[position]
        val context = holder.itemView.context
        val imageUri = RecipesRepository.RECIPE_API_BASE_URL +
                RecipesRepository.RECIPE_API_IMAGES_CATALOG + positionData.imageUrl
        holder.itemTitle.text = positionData.title
        Glide.with(context)
            .load(imageUri)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(holder.itemImage)
        holder.itemImage.contentDescription =
            "${context.getString(R.string.text_item_recipe_description)} ${positionData.title.lowercase()}"
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(positionData.id)
        }
    }

    override fun getItemCount() = dataset.size

    fun setDataSet(newDataSet: List<Recipe>) {
        dataset = newDataSet
    }

}