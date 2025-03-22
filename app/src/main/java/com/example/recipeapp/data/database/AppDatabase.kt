package com.example.recipeapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.data.dao.CategoriesDao
import com.example.recipeapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}