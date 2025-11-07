package com.example.rustore.data

import androidx.compose.ui.graphics.Color
import com.example.rustore.model.AppCategory
import com.example.rustore.model.AppIcon
import com.example.rustore.model.AppInfo
import com.example.rustore.model.AppScreenshot

object AppRepository {

    private val apps = listOf(
        AppInfo(
            id = "aurora-pay",
            name = "Aurora Pay",
            developer = "Nebula Fintech",
            category = AppCategory.Finance,
            shortDescription = "Бесконтактные платежи и единый кошелек для подписок.",
            ageRating = "12+",
            rating = 4.8,
            icon = AppIcon(
                background = Color(0xFF102043),
                accent = Color(0xFF4AD4FF),
                symbol = "₽"
            ),
            screenshots = listOf(
                screenshot("aurora-pay-analytics", "Аналитика", Color(0xFF1C74FF), Color(0xFF4AD4FF)),
                screenshot("aurora-pay-cards", "Карты", Color(0xFF102043), Color(0xFF1F3DA0)),
                screenshot("aurora-pay-subscriptions", "Подписки", Color(0xFF35B6FF), Color(0xFF68FFD9))
            ),
            descriptionAsset = "aurora_pay.html"
        ),
        AppInfo(
            id = "city-metro",
            name = "City Metro",
            developer = "Urban Mobility",
            category = AppCategory.Transport,
            shortDescription = "Маршруты метро и наземного транспорта с оплатой поездок.",
            ageRating = "0+",
            rating = 4.4,
            icon = AppIcon(
                background = Color(0xFF0C3C60),
                accent = Color(0xFFFFC857),
                symbol = "M"
            ),
            screenshots = listOf(
                screenshot("city-metro-map", "Карта", Color(0xFF0C3C60), Color(0xFF0FA3B1)),
                screenshot("city-metro-wallet", "Кошелек", Color(0xFFFFC857), Color(0xFFFF5E5B)),
                screenshot("city-metro-alerts", "Оповещения", Color(0xFF0FA3B1), Color(0xFF4AD4FF))
            ),
            descriptionAsset = "city_metro.html"
        ),
        AppInfo(
            id = "state-services",
            name = "ГосСервисы Light",
            developer = "Digital State",
            category = AppCategory.Government,
            shortDescription = "Документы, штрафы и важные уведомления в одном месте.",
            ageRating = "6+",
            rating = 4.6,
            icon = AppIcon(
                background = Color(0xFF112E51),
                accent = Color(0xFF50E3C2),
                symbol = "Г"
            ),
            screenshots = listOf(
                screenshot("gos-main", "Главный экран", Color(0xFF112E51), Color(0xFF3269FF)),
                screenshot("gos-docs", "Документы", Color(0xFF1D976C), Color(0xFF93F9B9)),
                screenshot("gos-services", "Сервисы", Color(0xFF50E3C2), Color(0xFF4AD4FF))
            ),
            descriptionAsset = "gos_services.html"
        ),
        AppInfo(
            id = "stellar-labs",
            name = "Stellar Labs",
            developer = "Cosmo Games",
            category = AppCategory.Games,
            shortDescription = "Ролевая игра с кооперативом и редактором уровней.",
            ageRating = "16+",
            rating = 4.9,
            icon = AppIcon(
                background = Color(0xFF1B1338),
                accent = Color(0xFFFF6AC1),
                symbol = "★"
            ),
            screenshots = listOf(
                screenshot("stellar-battle", "Сражения", Color(0xFF482880), Color(0xFFFF6AC1)),
                screenshot("stellar-build", "Создание", Color(0xFF1B1338), Color(0xFF4AD4FF)),
                screenshot("stellar-coop", "Кооператив", Color(0xFF32174D), Color(0xFF9B5DE5))
            ),
            descriptionAsset = "stellar_labs.html"
        ),
        AppInfo(
            id = "craft-tools",
            name = "Craft Tools",
            developer = "Nordic Systems",
            category = AppCategory.Tools,
            shortDescription = "Менеджер устройств, автоматизация и быстрые сценарии.",
            ageRating = "8+",
            rating = 4.5,
            icon = AppIcon(
                background = Color(0xFF14213D),
                accent = Color(0xFFFCA311),
                symbol = "⚙"
            ),
            screenshots = listOf(
                screenshot("craft-dashboard", "Панель", Color(0xFF14213D), Color(0xFF1F5472)),
                screenshot("craft-scenes", "Сцены", Color(0xFFFCA311), Color(0xFFFFC857)),
                screenshot("craft-automation", "Автоматизация", Color(0xFF1F5472), Color(0xFF4AD4FF))
            ),
            descriptionAsset = "craft_tools.html"
        )
    )

    fun getApps(): List<AppInfo> = apps

    fun getAppById(id: String): AppInfo? = apps.find { it.id == id }

    fun getCategoriesWithCounters(): Map<AppCategory, Int> =
        apps.groupingBy { it.category }.eachCount()

    private fun screenshot(
        id: String,
        label: String,
        startColor: Color,
        endColor: Color
    ) = AppScreenshot(
        id = id,
        label = label,
        gradientStart = startColor,
        gradientEnd = endColor
    )
}
