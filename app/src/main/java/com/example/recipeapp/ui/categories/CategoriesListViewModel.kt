package com.example.recipeapp.ui.categories

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(private val application: Application) :
    AndroidViewModel(application) {

    data class CategoriesListState(
        val categories: List<Category> = listOf(),
    )

    private val repository = RecipesRepository()
    private val mutableCurrentCategories = MutableLiveData(CategoriesListState())
    val currentCategories: LiveData<CategoriesListState> get() = mutableCurrentCategories

    fun loadCategories() {
        viewModelScope.launch {
            val data = repository.getCategories()
            if (data == null)
                Toast.makeText(
                    application.baseContext,
                    RecipesRepository.ERROR_TEXT,
                    Toast.LENGTH_SHORT
                ).show()
            else {
                mutableCurrentCategories.value = currentCategories.value?.copy(categories = data)
            }
        }
    }
}