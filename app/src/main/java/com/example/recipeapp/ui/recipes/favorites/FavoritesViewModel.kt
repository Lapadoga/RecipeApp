package com.example.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.FAVORITES_FILE_KEY
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.RECIPES_ID_KEY
import kotlinx.coroutines.launch

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {

    data class FavoriteRecipesState(
        val recipes: List<Recipe> = listOf()
    )

    private val repository = RecipesRepository()
    private var mutableCurrentFavoriteRecipes = MutableLiveData(FavoriteRecipesState())
    val currentFavoriteRecipes: LiveData<FavoriteRecipesState> get() = mutableCurrentFavoriteRecipes

    fun loadFavoriteRecipes() {
        val favoriteRecipesIds = getFavorites()
        viewModelScope.launch {
            val favoriteRecipes = if (favoriteRecipesIds.isEmpty()) listOf() else
                repository.getRecipesByIds(favoriteRecipesIds)

            if (favoriteRecipes == null)
                Toast.makeText(
                    application.baseContext,
                    RecipesRepository.ERROR_TEXT,
                    Toast.LENGTH_SHORT
                ).show()
            else
                mutableCurrentFavoriteRecipes.value =
                    currentFavoriteRecipes.value?.copy(recipes = favoriteRecipes)
        }
    }

    private fun getFavorites(): String {
        val recipesId = application.getSharedPreferences(
            FAVORITES_FILE_KEY,
            Context.MODE_PRIVATE
        ).getStringSet(RECIPES_ID_KEY, setOf())
        val data = recipesId?.joinToString(",") ?: ""
        return data
    }
}