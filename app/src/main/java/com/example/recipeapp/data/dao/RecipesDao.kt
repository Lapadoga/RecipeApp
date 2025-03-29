package com.example.recipeapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.recipeapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM Recipe WHERE category_id = :categoryId")
    suspend fun getAllByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE id IN (:ids)")
    suspend fun getAllByIds(ids: List<Int>): List<Recipe>

    @Update
    suspend fun updateAll(recipe: List<Recipe>)

    @Insert
    suspend fun insertAll(recipe: List<Recipe>)

    @Query("SELECT * FROM Recipe WHERE is_favorite")
    suspend fun getFavoriteList(): List<Recipe>
}