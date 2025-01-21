package com.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.Ingredient

data class RecipeState(
    var isFavorite: Boolean? = null,
    val title: String? = null,
    var portionSize: Int = 1,
    val ingredients: List<Ingredient> = mutableListOf(),
    val method: List<String> = mutableListOf(),
)

class RecipeViewModel : ViewModel() {

}