# Vitrina RuStore
Небольшая витрина приложений RuStore. Приложение демонстрирует онбординг, каталог приложений с поиском и фильтрацией по категориям, экран деталей с HTML-описанием и полноэкранный просмотр скриншотов. Код подходит как учебный пример архитектуры на базе одного `ViewModel`, Flow/DataStore и Navigation Compose.

## Основные возможности
- Экран онбординга (`OnboardingScreen`) с сохранением прогресса в `DataStore`, чтобы не показывать его повторно.
- Каталог приложений (`AppListScreen`) с поиском по названию/категории/описанию и быстрыми переходами к списку категорий.
- Экран категорий (`CategoriesScreen`) с подсчётом количества приложений в каждой группе.
- Экран деталей (`AppDetailScreen`) с карточкой приложения, кнопкой «установить», WebView-описанием из `assets/descriptions` и горизонтальным списком скриншотов.
- Полноэкранный просмотр скриншота (`FullscreenScreenshotScreen`) с плавным градиентом и собственным `TopAppBar`.
- Демонстрационный вызов установки: вместо настоящего `PackageInstaller` показывается `Snackbar` и `Toast`, чтобы не требовались реальные APK.

## Стек и библиотеки
- Kotlin 1.9 + Coroutines + Flow.
- Jetpack Compose Material 3, Navigation Compose, Lifecycle Compose.
- AndroidX DataStore (Preferences) для хранения признака прохождения онбординга.
- WebView для рендеринга HTML-описаний приложений.
- Gradle Kotlin DSL, `compileSdk` 34, `minSdk` 26, `jvmTarget` 17.

## Структура проекта
```
app/
├─ src/main/java/com/example/rustore/
│  ├─ MainActivity.kt
│  ├─ data/AppRepository.kt          // фиктивные данные и категории
│  ├─ model/                         // модели AppInfo/AppScreenshot
│  ├─ storage/OnboardingRepository.kt// DataStore-обёртка
│  ├─ ui/RustoreApp.kt               // граф навигации
│  ├─ ui/components/                 // AppCard, иконки
│  ├─ ui/screens/                    // экраны онбординга, списка, деталей и т.д.
│  ├─ ui/theme/                      // цвета/типографика
│  └─ viewmodel/RustoreViewModel.kt  // бизнес-логика и источники данных
├─ src/main/assets/descriptions/     // HTML для блока “Описание”
└─ src/main/res/                     // ресурсы Material, иконки, строки
```

## Быстрый старт
1. Установите Android Studio Ladybug или новее, JDK 17 и Android SDK 34.
2. Откройте папку `Vitrina_ruStore` в Android Studio или импортируйте как существующий Gradle-проект.
3. Дождитесь синхронизации Gradle.
4. Запустите конфигурацию *app* на эмуляторе/устройстве (Build Variant `debug`).

## Архитектура экранов
- `MainActivity` создаёт `RustoreViewModel` через `RustoreViewModelFactory`, подключённый к `OnboardingRepository`.
- `RustoreApp` содержит `NavHost` с маршрутами онбординга, списка приложений, категорий, деталей и полноэкранного просмотра.
- `AppRepository` выступает чистым источником данных и группирует приложения по `AppCategory`.
- Состояние UI берётся из `StateFlow` (`apps`, `onboardingCompleted`) и наблюдается через `collectAsStateWithLifecycle`.
- HTML-описания хранятся в `assets/descriptions/*.html` и загружаются в `WebView` через `AndroidView`.

## Просмотр работы приложения
https://vk.com/video207559717_456243088?list=ln-f3q8Wqapb28dimHDjp

