package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        val portionSize: Int = 1,
        val recipe: Recipe? = null,
        val recipeImageUrl: String = "",
    )

    private val repository = RecipesRepository(application.applicationContext)
    private val mutableCurrentRecipe = MutableLiveData(RecipeState())
    val currentRecipe: LiveData<RecipeState> get() = mutableCurrentRecipe

    fun loadRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val imageUrl = RecipesRepository.RECIPE_API_BASE_URL +
                    RecipesRepository.RECIPE_API_IMAGES_CATALOG + recipe.imageUrl

            mutableCurrentRecipe.value = currentRecipe.value?.copy(
                portionSize = 1,
                recipe = recipe,
                recipeImageUrl = imageUrl
            )
        }
    }

    fun onFavoritesClicked() {
        val recipe = currentRecipe.value?.recipe
        if (recipe != null) {
            val newFavoriteState = !recipe.isFavorite
            val newRecipe = recipe.copy(isFavorite = newFavoriteState)
            mutableCurrentRecipe.value = currentRecipe.value?.copy(recipe = newRecipe)

            saveRecipe(newRecipe)
        }
    }

    fun onSeekBarChange(newPortionSize: Int) {
        mutableCurrentRecipe.value = currentRecipe.value?.copy(portionSize = newPortionSize)
    }

    private fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.cacheRecipes(listOf(recipe))
        }
    }
}