package com.example.recipeapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val isFavorite: Boolean = false,
        val portionSize: Int = 1,
        val recipe: Recipe? = null,
    )

    private val mutableCurrentRecipe = MutableLiveData<RecipeState>()
    val currentRecipe: LiveData<RecipeState> get() = mutableCurrentRecipe

    init {
        Log.i("!!", "Initialization")
        mutableCurrentRecipe.postValue(RecipeState())
    }
}