package com.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipeapp.model.Recipe

data class RecipeState(
    val isFavorite: Boolean = false,
    val portionSize: Int = 1,
    val recipe: Recipe? = null,
)

class RecipeViewModel : ViewModel() {

}