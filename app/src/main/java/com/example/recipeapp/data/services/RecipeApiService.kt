package com.example.recipeapp.data.services

import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RecipeApiService {
    @GET("category")
    suspend fun getCategories(): List<Category>

    @GET("recipe/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): Recipe

    @GET("recipes")
    suspend fun getRecipesByIds(@Query("ids") ids: String): List<Recipe>

    @GET("category/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Category

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(@Path("id") id: Int): List<Recipe>
}