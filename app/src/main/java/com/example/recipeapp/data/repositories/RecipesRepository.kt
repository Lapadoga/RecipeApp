package com.example.recipeapp.data.repositories

import com.example.recipeapp.data.services.RecipeApiService
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit

class RecipesRepository {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(RECIPE_API_BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()
    private val service = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategories(): List<Category>? {
        val response: Response<List<Category>>?
        withContext(Dispatchers.IO) {
            val categoriesCall = service.getCategories()
            response = categoriesCall.execute()
        }

        val result = try {
            response?.body()
        } catch (e: Exception) {
            null
        }
        return result
    }

    suspend fun getCategoryById(id: Int): Category? {
        var response: Response<Category>?
        withContext(Dispatchers.IO) {
            val categoryCall = service.getCategoryById(id)
            response = categoryCall.execute()
        }

        val result = try {
            response?.body()
        } catch (e: Exception) {
            null
        }

        return result
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        var response: Response<List<Recipe>>?
        withContext(Dispatchers.IO) {
            val recipesCall = service.getRecipesByCategoryId(id)
            response = recipesCall.execute()
        }

        val result = try {
            response?.body()
        } catch (e: Exception) {
            null
        }

        return result
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        var response: Response<Recipe>?
        withContext(Dispatchers.IO) {
            val recipeCall = service.getRecipeById(id)
            response = recipeCall.execute()
        }

        val result = try {
            response?.body()
        } catch (e: Exception) {
            null
        }

        return result
    }

    suspend fun getRecipesByIds(ids: String): List<Recipe>? {
        var response: Response<List<Recipe>>?
        withContext(Dispatchers.IO) {
            val recipesCall = service.getRecipesByIds(ids)
            response = recipesCall.execute()
        }

        val result = try {
            response?.body()
        } catch (e: Exception) {
            null
        }

        return result
    }

    companion object {
        const val RECIPE_API_BASE_URL = "https://recipes.androidsprint.ru/api/"
        const val ERROR_TEXT = "Ошибка получения данных"
        const val RECIPE_API_IMAGES_CATALOG = "images/"
    }
}