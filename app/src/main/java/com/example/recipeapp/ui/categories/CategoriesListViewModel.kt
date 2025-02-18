package com.example.recipeapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Category

class CategoriesListViewModel(private val application: Application) : AndroidViewModel(application) {

    data class CategoriesListState(
        val categories: List<Category> = listOf(),
    )

    private val mutableCurrentCategories = MutableLiveData(CategoriesListState())
    val currentCategories: LiveData<CategoriesListState> get() = mutableCurrentCategories

    init {
        mutableCurrentCategories.value = CategoriesListState()
    }

    fun loadCategories() {
        // TODO: load from network

        val categories = STUB.getCategories()
        mutableCurrentCategories.value = CategoriesListState(categories)
    }
}