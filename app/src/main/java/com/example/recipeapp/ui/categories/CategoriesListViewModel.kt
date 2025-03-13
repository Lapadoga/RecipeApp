package com.example.recipeapp.ui.categories

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.model.Category

class CategoriesListViewModel(private val application: Application) :
    AndroidViewModel(application) {

    data class CategoriesListState(
        val categories: List<Category> = listOf(),
    )

    val repository = RecipesRepository()
    private val mutableCurrentCategories = MutableLiveData(CategoriesListState())
    val currentCategories: LiveData<CategoriesListState> get() = mutableCurrentCategories

    fun loadCategories() {
        val data = repository.getCategories()
        if (data == null)
            Toast.makeText(
                application.baseContext,
                RecipesRepository.ERROR_TEXT,
                Toast.LENGTH_SHORT
            ).show()
        else {
            mutableCurrentCategories.value = CategoriesListState(data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.shutdownPull()
    }
}