package com.example.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.FAVORITES_FILE_KEY
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.RECIPES_ID_KEY

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {

    data class FavoriteRecipesState(
        val recipes: List<Recipe> = listOf()
    )

    private var mutableCurrentFavoriteRecipes = MutableLiveData(FavoriteRecipesState())
    val currentFavoriteRecipes: LiveData<FavoriteRecipesState> get() = mutableCurrentFavoriteRecipes

    fun loadFavoriteRecipes() {
        // TODO: load from network

        val favoriteRecipesIds = getFavorites()
        val favoriteRecipes = STUB.getRecipesByIds(favoriteRecipesIds)

        mutableCurrentFavoriteRecipes.value = FavoriteRecipesState(favoriteRecipes)
    }

    private fun getFavorites(): Set<Int> {
        val recipesId = application.getSharedPreferences(
            FAVORITES_FILE_KEY,
            Context.MODE_PRIVATE
        ).getStringSet(RECIPES_ID_KEY, setOf())
        return if (recipesId == null) hashSetOf() else HashSet(recipesId.map { it.toInt() })
    }
}