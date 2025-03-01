package com.example.recipeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnFavorites.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment)
            if (navController.currentDestination?.id != R.id.favoritesFragment)
                navController.navigate(R.id.action_global_to_favorites)
        }

        binding.btnCategories.setOnClickListener {
            val navController = findNavController(R.id.nav_host_fragment)
            if (navController.currentDestination?.id != R.id.categoriesListFragment)
                navController.navigate(R.id.action_global_to_categories)
        }

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            val body = connection.inputStream.bufferedReader().readText()
            Log.i("!!!", body)
            val categoriesListType = object : TypeToken<List<Category>>() {}.type
            val categoriesList: List<Category> = Gson().fromJson(body, categoriesListType)
            Log.i("!!!", categoriesList.joinToString("; "))
        }
        thread.start()
    }
}