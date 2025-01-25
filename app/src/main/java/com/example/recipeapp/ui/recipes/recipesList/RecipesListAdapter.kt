package com.example.recipeapp.ui.recipes.recipesList

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemRecipeBinding
import com.example.recipeapp.model.Recipe
import java.io.IOException

class RecipesListAdapter(private val dataset: List<Recipe>) :
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
        val drawable = try {
            val stream = context?.assets?.open(positionData.imageUrl)
            Drawable.createFromStream(stream, null)
        } catch (e: IOException) {
            Log.e("error", e.stackTraceToString())
            null
        }
        holder.itemTitle.text = positionData.title
        holder.itemImage.setImageDrawable(drawable)
        holder.itemImage.contentDescription =
            "${context.getString(R.string.text_item_recipe_description)} ${positionData.title.lowercase()}"
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(positionData.id)
        }
    }

    override fun getItemCount() = dataset.size

}