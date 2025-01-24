package com.example.recipeapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.ui.categories.CategoriesListFragment
import com.example.recipeapp.ui.recipes.favorites.FavoritesFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CategoriesListFragment>(R.id.mainContainer, CATEGORIES_TAG)
            }
        }
        setContentView(binding.root)

        binding.btnFavorites.setOnClickListener {
            val favoritesFragment = supportFragmentManager.findFragmentByTag(FAVORITES_TAG)
            if (favoritesFragment == null || !favoritesFragment.isVisible) {
                supportFragmentManager.commit {
                    replace<FavoritesFragment>(R.id.mainContainer, FAVORITES_TAG)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }

        binding.btnCategories.setOnClickListener {
            val categoriesFragment = supportFragmentManager.findFragmentByTag(CATEGORIES_TAG)
            if (categoriesFragment == null || !categoriesFragment.isVisible) {
                supportFragmentManager.commit {
                    replace<CategoriesListFragment>(R.id.mainContainer, CATEGORIES_TAG)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }

    companion object {
        private const val FAVORITES_TAG = "Favorites"
        private const val CATEGORIES_TAG = "Categories"
    }
}