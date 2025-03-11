package com.example.recipeapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import java.io.IOException

class RecipesListViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val recipes: List<Recipe> = listOf(),
        val categoryTitle: String = "",
        val categoryImage: Drawable? = null,
    )

    private val repository = RecipesRepository()
    private val mutableCurrentRecipes = MutableLiveData(RecipesListState())
    val currentRecipes: LiveData<RecipesListState> get() = mutableCurrentRecipes

    fun loadCategory(category: Category) {
        val categoryDrawable = try {
            val stream = application.assets.open(category.imageUrl)
            Drawable.createFromStream(stream, null)
        } catch (e: IOException) {
            Log.e("Drawable", e.stackTraceToString())
            null
        }

        val data = repository.getRecipesByCategoryId(category.id)
        if (data == null)
            Toast.makeText(
                application.baseContext,
                RecipesRepository.ERROR_TEXT,
                Toast.LENGTH_SHORT
            ).show()
        else
            mutableCurrentRecipes.value = RecipesListState(data, category.title, categoryDrawable)
    }
}