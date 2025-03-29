package com.example.recipeapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    data class FavoriteRecipesState(
        val recipes: List<Recipe> = listOf()
    )

    private val repository = RecipesRepository(application.applicationContext)
    private var mutableCurrentFavoriteRecipes = MutableLiveData(FavoriteRecipesState())
    val currentFavoriteRecipes: LiveData<FavoriteRecipesState> get() = mutableCurrentFavoriteRecipes

    fun loadFavoriteRecipes() {
        viewModelScope.launch {
            val favoriteRecipes = repository.getFavoriteRecipes()
            mutableCurrentFavoriteRecipes.value =
                currentFavoriteRecipes.value?.copy(recipes = favoriteRecipes)
        }
    }
}