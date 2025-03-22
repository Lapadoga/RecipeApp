package com.example.recipeapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Category(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)
