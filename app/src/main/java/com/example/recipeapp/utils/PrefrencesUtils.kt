package com.example.recipeapp.utils

import android.content.Context
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.FAVORITES_FILE_KEY
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment.Companion.RECIPES_ID_KEY

object PreferencesUtils {
    fun getFavorites(context: Context?): MutableSet<String> {
        context?.let {
            val sharedPrefs = context.getSharedPreferences(FAVORITES_FILE_KEY, Context.MODE_PRIVATE)

            val recipesId = sharedPrefs.getStringSet(RECIPES_ID_KEY, mutableSetOf())
            return HashSet(recipesId)
        }
        return mutableSetOf()
    }
}