package com.example.rustore.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rustore.model.AppInfo
import com.example.rustore.model.AppScreenshot
import com.example.rustore.ui.screens.AppDetailScreen
import com.example.rustore.ui.screens.AppListScreen
import com.example.rustore.ui.screens.CategoriesScreen
import com.example.rustore.ui.screens.FullscreenScreenshotScreen
import com.example.rustore.ui.screens.OnboardingScreen
import com.example.rustore.viewmodel.RustoreViewModel
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Store : Screen("store")
    data object Categories : Screen("categories")
    data object Detail : Screen("detail/{appId}") {
        fun createRoute(appId: String) = "detail/$appId"
    }

    data object Screenshot : Screen("screenshot/{appId}/{shotId}") {
        fun createRoute(appId: String, screenshotId: String) = "screenshot/$appId/$screenshotId"
    }
}

@Composable
fun RustoreApp(
    viewModel: RustoreViewModel
) {
    val navController = rememberNavController()
    val apps by viewModel.apps.collectAsStateWithLifecycle()
    val onboardingCompleted by viewModel.onboardingCompleted.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val installApp: (AppInfo) -> Unit = { app ->
        val packageInstaller = context.packageManager.packageInstaller
        val sessionCount = runCatching { packageInstaller.mySessions.size }.getOrDefault(0)
        Log.d("RustoreApp", "PackageInstaller доступен. Активные сессии: $sessionCount")
        scope.launch {
            snackbarHostState.showSnackbar("PackageInstaller подготовлен: $sessionCount сессий")
        }
        Toast.makeText(
            context,
            "Имитируем установку ${app.name}",
            Toast.LENGTH_SHORT
        ).show()
    }

    LaunchedEffect(onboardingCompleted) {
        if (onboardingCompleted) {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute == Screen.Onboarding.route) {
                navController.navigate(Screen.Store.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Onboarding.route
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onContinue = {
                        viewModel.markOnboardingSeen()
                        navController.navigate(Screen.Store.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Store.route) {
                AppListScreen(
                    apps = apps,
                    onAppClick = { app ->
                        navController.navigate(Screen.Detail.createRoute(app.id))
                    },
                    onCategoriesClick = {
                        navController.navigate(Screen.Categories.route)
                    }
                )
            }
            composable(Screen.Categories.route) {
                CategoriesScreen(
                    categories = viewModel.categoriesWithCounters(),
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("appId") { type = NavType.StringType })
            ) { entry ->
                val appId = entry.arguments?.getString("appId")
                val app = appId?.let(viewModel::findApp)
                if (app != null) {
                    AppDetailScreen(
                        app = app,
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        onBack = { navController.popBackStack() },
                        onInstall = installApp,
                        onScreenshotClick = { screenshot ->
                            navController.navigate(Screen.Screenshot.createRoute(app.id, screenshot.id))
                        }
                    )
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Приложение недоступно")
                    }
                    navController.popBackStack()
                }
            }
            composable(
                route = Screen.Screenshot.route,
                arguments = listOf(
                    navArgument("appId") { type = NavType.StringType },
                    navArgument("shotId") { type = NavType.StringType }
                )
            ) { entry ->
                val appId = entry.arguments?.getString("appId")
                val screenshotId = entry.arguments?.getString("shotId")
                val app: AppInfo? = appId?.let(viewModel::findApp)
                val screenshot: AppScreenshot? =
                    app?.screenshots?.find { it.id == screenshotId }
                if (app != null && screenshot != null) {
                    FullscreenScreenshotScreen(
                        appName = app.name,
                        screenshot = screenshot,
                        onBack = { navController.popBackStack() }
                    )
                } else {
                    navController.popBackStack()
                }
            }
        }
    }
}
