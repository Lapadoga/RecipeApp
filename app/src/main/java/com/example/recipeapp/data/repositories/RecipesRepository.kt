package com.example.recipeapp.data.repositories

import com.example.recipeapp.data.services.RecipeApiService
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

class RecipesRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(RECIPE_API_BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val service = retrofit.create(RecipeApiService::class.java)
    private val threadPool = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    fun getCategories(): List<Category>? {
        val task: Callable<retrofit2.Response<List<Category>>> = Callable {
            val categoriesCall = service.getCategories()
            categoriesCall.execute()
        }
        val future: Future<retrofit2.Response<List<Category>>> = threadPool.submit(task)

        val result = try {
            val response = future.get()
            response.body()
        } catch (e: Exception) {
            null
        }
        return result
    }

    fun getCategoryById(id: Int): Category? {
        val task: Callable<retrofit2.Response<Category>> = Callable {
            val categoryCall = service.getCategoryById(id)
            categoryCall.execute()
        }
        val future: Future<retrofit2.Response<Category>> = threadPool.submit(task)

        val result = try {
            val response = future.get()
            response.body()
        } catch (e: Exception) {
            null
        }
        return result
    }

    fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        val task: Callable<retrofit2.Response<List<Recipe>>> = Callable {
            val recipesCall = service.getRecipesByCategoryId(id)
            recipesCall.execute()
        }
        val future: Future<retrofit2.Response<List<Recipe>>> = threadPool.submit(task)

        val result = try {
            val response = future.get()
            response.body()
        } catch (e: Exception) {
            null
        }
        return result
    }

    fun getRecipeById(id: Int): Recipe? {
        val task: Callable<retrofit2.Response<Recipe>> = Callable {
            val recipeCall = service.getRecipeById(id)
            recipeCall.execute()
        }
        val future: Future<retrofit2.Response<Recipe>> = threadPool.submit(task)

        val result = try {
            val response = future.get()
            response.body()
        } catch (e: Exception) {
            null
        }
        return result
    }

    fun getRecipesByIds(ids: String): List<Recipe>? {
        val task: Callable<retrofit2.Response<List<Recipe>>> = Callable {
            val recipesCall = service.getRecipesByIds(ids)
            recipesCall.execute()
        }
        val future: Future<retrofit2.Response<List<Recipe>>> = threadPool.submit(task)

        val result = try {
            val response = future.get()
            response.body()
        } catch (e: Exception) {
            null
        }
        return result
    }

    companion object {
        const val RECIPE_API_BASE_URL = "https://recipes.androidsprint.ru/api/"
        const val NUMBER_OF_THREADS = 10
        const val ERROR_TEXT = "Ошибка получения данных"
    }
}