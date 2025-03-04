package com.example.recipeapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
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
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            val categoriesList: List<Category> = client.newCall(request).execute().use {
                val categoriesListType = object : TypeToken<List<Category>>() {}.type
                Gson().fromJson(it.body?.string(), categoriesListType)
            }

            val categoriesIdList: List<Int> = categoriesList.map { it.id }

            val recipesJsonData = mutableListOf<String>()
            val tasks = categoriesIdList.map {
                Callable {
                    val requestConnection = Request.Builder()
                        .url("https://recipes.androidsprint.ru/api/category/$it/recipes")
                        .build()

                    client.newCall(requestConnection).execute().body?.string() ?: ""
                }
            }
            val futures = threadPool.invokeAll(tasks)

            for (future in futures) {
                recipesJsonData.add(future.get())
            }
            threadPool.shutdown()
        }
        thread.start()
    }

    companion object {
        const val NUMBER_OF_THREADS = 10
    }
}