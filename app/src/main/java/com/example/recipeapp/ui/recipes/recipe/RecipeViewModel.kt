package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.FAVORITES_FILE_KEY
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.RECIPES_ID_KEY

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        val isFavorite: Boolean = false,
        val portionSize: Int = 1,
        val recipe: Recipe? = null,
    )

    private val mutableCurrentRecipe = MutableLiveData(RecipeState())
    val currentRecipe: LiveData<RecipeState> get() = mutableCurrentRecipe

    init {
        mutableCurrentRecipe.value = RecipeState()
    }

    fun loadRecipe(recipeId: Int) {
        // TODO: load from network

        val favoriteRecipes = getFavorites()
        mutableCurrentRecipe.value = RecipeState(
            favoriteRecipes.contains(recipeId.toString()),
            currentRecipe.value?.portionSize ?: 1,
            STUB.getRecipeById(recipeId)
        )
    }

    fun onFavoritesClicked() {
        val favoriteRecipes = getFavorites()
        val newIsFavoriteState = !(currentRecipe.value?.isFavorite ?: false)
        if (newIsFavoriteState) {
            favoriteRecipes.remove(currentRecipe.value?.recipe?.id.toString())
        } else {
            favoriteRecipes.add(currentRecipe.value?.recipe?.id.toString())
        }

        mutableCurrentRecipe.value =
            mutableCurrentRecipe.value?.copy(isFavorite = newIsFavoriteState)

        saveFavorites(favoriteRecipes)
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            FAVORITES_FILE_KEY,
            Context.MODE_PRIVATE
        )

        val recipesId = sharedPrefs.getStringSet(RECIPES_ID_KEY, mutableSetOf())
        return HashSet(recipesId)
    }

    private fun saveFavorites(recipesId: Set<String>) {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            FAVORITES_FILE_KEY,
            Context.MODE_PRIVATE
        )

        with(sharedPrefs.edit()) {
            putStringSet(RECIPES_ID_KEY, recipesId)
            apply()
        }
    }
}