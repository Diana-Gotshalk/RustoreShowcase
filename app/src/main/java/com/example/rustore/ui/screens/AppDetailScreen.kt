package com.example.rustore.ui.screens

import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.rustore.R
import com.example.rustore.model.AppInfo
import com.example.rustore.model.AppScreenshot
import com.example.rustore.ui.components.AppIconBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(
    app: AppInfo,
    snackbarHost: @Composable () -> Unit,
    onBack: () -> Unit,
    onInstall: (AppInfo) -> Unit,
    onScreenshotClick: (AppScreenshot) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = app.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = snackbarHost
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                AppSummaryBlock(app = app)
            }
            item {
                Button(
                    onClick = { onInstall(app) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = stringResource(id = R.string.install))
                }
            }
            item {
                ScreenshotsRow(
                    screenshots = app.screenshots,
                    onScreenshotClick = onScreenshotClick
                )
            }
            item {
                HtmlDescription(assetName = app.descriptionAsset)
            }
        }
    }
}

@Composable
private fun AppSummaryBlock(app: AppInfo) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AppIconBadge(icon = app.icon)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = app.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )
            Text(
                text = app.developer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Категория · ${app.category.russianLabel}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Возраст ${app.ageRating} • Оценка ${app.rating}★",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = app.shortDescription,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun ScreenshotsRow(
    screenshots: List<AppScreenshot>,
    onScreenshotClick: (AppScreenshot) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(id = R.string.screenshots_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(screenshots) { screenshot ->
                Column(
                    modifier = Modifier
                        .height(220.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    screenshot.gradientStart,
                                    screenshot.gradientEnd
                                )
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .width(240.dp)
                        .padding(20.dp)
                        .clickable { onScreenshotClick(screenshot) },
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = screenshot.label,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Тапните, чтобы раскрыть",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun HtmlDescription(assetName: String) {
    val context = LocalContext.current
    val assetPath = remember(assetName) { "file:///android_asset/descriptions/$assetName" }
    AndroidView(
        factory = {
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                settings.apply {
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    domStorageEnabled = false
                    defaultTextEncodingName = "utf-8"
                }
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        },
        update = { webView ->
            webView.loadUrl(assetPath)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    )
}
