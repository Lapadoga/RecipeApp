package com.example.recipeapp.data.services

import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(@Query("ids") ids: String): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: Int): Call<List<Recipe>>
}