package com.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemIngredientBinding
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1

    class ViewHolder(binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        val ingredientTitle = binding.ingredientTitle
        val ingredientAmount = binding.ingredientAmount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(ItemIngredientBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val positionData = dataSet[position]
        holder.ingredientTitle.text = positionData.description
        val quantity = BigDecimal(positionData.quantity) * BigDecimal(quantity)
        val displayQuantity =
            quantity.setScale(1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        holder.ingredientAmount.text =
            "$displayQuantity ${positionData.unitOfMeasure}"
    }

    fun updateIngredients(progress: Int) {
        quantity = progress
    }
}