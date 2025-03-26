package com.example.recipeapp.model.typeConverters

import androidx.room.TypeConverter
import com.example.recipeapp.model.Ingredient
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromIngredientsList(value: List<Ingredient>) = Json.encodeToString(value)

    @TypeConverter
    fun toIngredientsList(value: String) = Json.decodeFromString<List<Ingredient>>(value)

    @TypeConverter
    fun fromMethodsList(value: List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toMethodsList(value: String) = Json.decodeFromString<List<String>>(value)
}