package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.FAVORITES_FILE_KEY
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.RECIPES_ID_KEY
import java.io.IOException

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        val isFavorite: Boolean = false,
        val portionSize: Int = 1,
        val recipe: Recipe? = null,
        val recipeImage: Drawable? = null,
    )

    private val repository = RecipesRepository()
    private val sharedPreferences by lazy {
        application.getSharedPreferences(
            FAVORITES_FILE_KEY,
            Context.MODE_PRIVATE
        )
    }
    private val mutableCurrentRecipe = MutableLiveData(RecipeState())
    val currentRecipe: LiveData<RecipeState> get() = mutableCurrentRecipe

    fun loadRecipe(recipeId: Int) {
        val recipe = repository.getRecipeById(recipeId)
        if (recipe == null)
            Toast.makeText(
                application.baseContext,
                RecipesRepository.ERROR_TEXT,
                Toast.LENGTH_SHORT
            ).show()
        else {
            val drawable =
                try {
                    val stream = application.assets.open(recipe.imageUrl)
                    Drawable.createFromStream(stream, null)
                } catch (e: IOException) {
                    Log.e("Drawable", e.stackTraceToString())
                    null
                }
            val favoriteRecipes = getFavorites()

            mutableCurrentRecipe.value = RecipeState(
                favoriteRecipes.contains(recipeId.toString()),
                currentRecipe.value?.portionSize ?: 1,
                recipe,
                drawable
            )
        }
    }

    fun onFavoritesClicked() {
        val favoriteRecipes = getFavorites()
        val newIsFavoriteState = !(currentRecipe.value?.isFavorite ?: false)
        if (newIsFavoriteState) {
            favoriteRecipes.add(currentRecipe.value?.recipe?.id.toString())
        } else {
            favoriteRecipes.remove(currentRecipe.value?.recipe?.id.toString())
        }

        mutableCurrentRecipe.value =
            mutableCurrentRecipe.value?.copy(isFavorite = newIsFavoriteState)

        saveFavorites(favoriteRecipes)
    }

    fun onSeekBarChange(newPortionSize: Int) {
        mutableCurrentRecipe.value = mutableCurrentRecipe.value?.copy(portionSize = newPortionSize)
    }

    private fun getFavorites(): MutableSet<String> {
        val recipesId = sharedPreferences.getStringSet(RECIPES_ID_KEY, mutableSetOf())
        return HashSet(recipesId)
    }

    private fun saveFavorites(recipesId: Set<String>) {
        with(sharedPreferences.edit()) {
            putStringSet(RECIPES_ID_KEY, recipesId)
            apply()
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.shutdownPull()
    }
}