package com.example.rustore.model

import androidx.compose.ui.graphics.Color

enum class AppCategory(val russianLabel: String) {
    Finance("Финансы"),
    Tools("Инструменты"),
    Games("Игры"),
    Government("Государственные"),
    Transport("Транспорт")
}

data class AppIcon(
    val background: Color,
    val accent: Color,
    val symbol: String
)

data class AppScreenshot(
    val id: String,
    val label: String,
    val gradientStart: Color,
    val gradientEnd: Color
)

data class AppInfo(
    val id: String,
    val name: String,
    val developer: String,
    val category: AppCategory,
    val shortDescription: String,
    val ageRating: String,
    val rating: Double,
    val icon: AppIcon,
    val screenshots: List<AppScreenshot>,
    val descriptionAsset: String
)
