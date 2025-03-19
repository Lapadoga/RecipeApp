package com.example.recipeapp.ui.recipes.recipesList

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val recipes: List<Recipe> = listOf(),
        val categoryTitle: String = "",
        val categoryImageUrl: String = "",
    )

    private val repository = RecipesRepository()
    private val mutableCurrentRecipes = MutableLiveData(RecipesListState())
    val currentRecipes: LiveData<RecipesListState> get() = mutableCurrentRecipes

    fun loadCategory(category: Category) {
        viewModelScope.launch {
            val data = repository.getRecipesByCategoryId(category.id)
            if (data == null)
                Toast.makeText(
                    application.baseContext,
                    RecipesRepository.ERROR_TEXT,
                    Toast.LENGTH_SHORT
                ).show()
            else
                mutableCurrentRecipes.value = currentRecipes.value?.copy(
                    recipes = data,
                    categoryTitle = category.title,
                    categoryImageUrl = RecipesRepository.RECIPE_API_BASE_URL +
                            RecipesRepository.RECIPE_API_IMAGES_CATALOG + category.imageUrl
                )
        }
    }
}