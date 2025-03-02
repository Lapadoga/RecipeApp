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
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val threadPool = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

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

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val body = connection.inputStream.bufferedReader().readText()
            val categoriesListType = object : TypeToken<List<Category>>() {}.type
            val categoriesList: List<Category> = Gson().fromJson(body, categoriesListType)
            val categoriesIdList: List<Int> = categoriesList.map { it.id }

            val recipesJsonData = mutableListOf<String>()
            val tasks = categoriesIdList.map {
                Callable {
                    val connectionUrl =
                        URL("https://recipes.androidsprint.ru/api/category/$it/recipes")
                    val requestConnection = connectionUrl.openConnection() as HttpURLConnection

                    requestConnection.inputStream.bufferedReader().readText()
                }
            }
            val futures = threadPool.invokeAll(tasks)

            for (future in futures) {
                recipesJsonData.add(future.get())
            }
            threadPool.shutdown()
            Log.i("!!!", recipesJsonData.joinToString("\n"))
        }
        thread.start()
    }

    companion object {
        const val NUMBER_OF_THREADS = 10
    }
}