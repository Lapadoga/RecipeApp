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
    private val dispatcher = Dispatchers.IO

    suspend fun getCategories(): List<Category>? {
        val result = try {
            withContext(dispatcher) {
                service.getCategories()
            }
        } catch (e: Exception) {
            null
        }

        return result
    }

    suspend fun getCategoryById(id: Int): Category? {
        val result = try {
            withContext(dispatcher) {
                service.getCategoryById(id)
            }
        } catch (e: Exception) {
            null
        }

        return result
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        val result = try {
            withContext(dispatcher) {
                service.getRecipesByCategoryId(id)
            }
        } catch (e: Exception) {
            null
        }

        return result
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        val result = try {
            withContext(dispatcher) {
                service.getRecipeById(id)
            }
        } catch (e: Exception) {
            null
        }

        return result
    }

    suspend fun getRecipesByIds(ids: String): List<Recipe>? {
        val result = try {
            withContext(dispatcher) {
                service.getRecipesByIds(ids)
            }
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