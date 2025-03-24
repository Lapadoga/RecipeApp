package com.example.recipeapp.data.repositories

import android.content.Context
import androidx.room.Room
import com.example.recipeapp.data.database.AppDatabase
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

class RecipesRepository(context: Context) {
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
    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database-recipes"
    ).fallbackToDestructiveMigration().build()
    private val categoriesDao = database.categoriesDao()
    private val recipesDao = database.recipesDao()

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

    suspend fun getCategoriesFromCache(): List<Category> = categoriesDao.getAll()

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

    suspend fun cacheCategories(categories: List<Category>) {
        categoriesDao.insertAll(categories)
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        val responce = try {
            withContext(dispatcher) {
                service.getRecipesByCategoryId(id)
            }
        } catch (e: Exception) {
            null
        }

        val result = responce?.map { it.copy(categoryId = id) }

        return result
    }

    suspend fun getRecipesByCategoryIdFromCache(categoryId: Int): List<Recipe> =
        recipesDao.getAllByCategoryId(categoryId)

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

    suspend fun cacheRecipes(recipes: List<Recipe>) {
        recipesDao.insertAll(recipes)
    }

    companion object {
        const val RECIPE_API_BASE_URL = "https://recipes.androidsprint.ru/api/"
        const val ERROR_TEXT = "Ошибка получения данных"
        const val RECIPE_API_IMAGES_CATALOG = "images/"
    }
}