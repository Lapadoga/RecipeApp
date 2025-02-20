package com.example.recipeapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemMethodBinding

class MethodAdapter(private var dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        val methodText = binding.tvMethod
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(ItemMethodBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val positionData = dataSet[position]
        holder.methodText.text = "${position + 1}. $positionData"
    }

    fun setDataSet(newDataSet: List<String>) {
        dataSet = newDataSet
    }
}