package com.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemMethodBinding
import com.example.recipeapp.databinding.ItemMethodBinding.bind

class MethodAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        val methodText = binding.tvMethod
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_method, parent, false)
        val binding = bind(view)

        return ViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val positionData = dataSet[position]
        holder.methodText.text = "${position + 1}. $positionData"
    }

}