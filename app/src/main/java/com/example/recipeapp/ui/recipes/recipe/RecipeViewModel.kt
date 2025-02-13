package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.STUB
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

    private val mutableCurrentRecipe = MutableLiveData(RecipeState())
    val currentRecipe: LiveData<RecipeState> get() = mutableCurrentRecipe

    init {
        mutableCurrentRecipe.value = RecipeState()
    }

    fun loadRecipe(recipeId: Int) {
        // TODO: load from network

        val recipe = STUB.getRecipeById(recipeId)
        val drawable = if (recipe == null) null
        else
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
        val sharedPrefs = application.getSharedPreferences(
            FAVORITES_FILE_KEY,
            Context.MODE_PRIVATE
        )

        val recipesId = sharedPrefs.getStringSet(RECIPES_ID_KEY, mutableSetOf())
        return HashSet(recipesId)
    }

    private fun saveFavorites(recipesId: Set<String>) {
        val sharedPrefs = application.getSharedPreferences(
            FAVORITES_FILE_KEY,
            Context.MODE_PRIVATE
        )

        with(sharedPrefs.edit()) {
            putStringSet(RECIPES_ID_KEY, recipesId)
            apply()
        }
    }
}