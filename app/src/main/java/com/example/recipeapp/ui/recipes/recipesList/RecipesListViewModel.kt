package com.example.recipeapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Recipe
import java.io.IOException

class RecipesListViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val recipes: List<Recipe> = listOf(),
        val categoryTitle: String = "",
        val categoryImage: Drawable? = null,
    )

    private val mutableCurrentRecipes = MutableLiveData(RecipesListState())
    val currentRecipes: LiveData<RecipesListState> get() = mutableCurrentRecipes

    fun loadCategory(categoryId: Int) {
        // TODO: load from network

        val category = STUB.getCategoryById(categoryId)
        val recipes = STUB.getRecipesByCategoryId(categoryId)

        val categoryDrawable = if (category == null) null
        else try {
            val stream = application.assets.open(category.imageUrl)
            Drawable.createFromStream(stream, null)
        } catch (e: IOException) {
            Log.e("Drawable", e.stackTraceToString())
            null
        }

        mutableCurrentRecipes.value =
            RecipesListState(recipes, category?.title ?: "", categoryDrawable)
    }
}