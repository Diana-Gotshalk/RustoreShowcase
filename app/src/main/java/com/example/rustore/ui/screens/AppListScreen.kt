package com.example.rustore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.rustore.model.AppInfo
import com.example.rustore.ui.components.AppCard
import com.example.rustore.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onCategoriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val filteredApps = remember(searchQuery, apps) {
        if (searchQuery.isBlank()) {
            apps
        } else {
            val query = searchQuery.lowercase()
            apps.filter {
                it.name.lowercase().contains(query) ||
                        it.category.russianLabel.lowercase().contains(query) ||
                        it.shortDescription.lowercase().contains(query)
            }
        }
    }
    val grouped = remember(apps) {
        apps.groupBy { it.category }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Витрина RuStore",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Доступно приложений: ${apps.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(id = R.string.search_hint)) }
                )
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Категории",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        grouped.entries.forEach { (category, appsInCategory) ->
                            AssistChip(
                                onClick = onCategoriesClick,
                                label = {
                                    Text(
                                        text = "${category.russianLabel} · ${appsInCategory.size}",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Category,
                                        contentDescription = null
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            )
                        }
                    }
                    Button(
                        onClick = onCategoriesClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = stringResource(id = R.string.view_categories))
                    }
                }
            }

            items(
                items = filteredApps,
                key = { it.id }
            ) { app ->
                AppCard(
                    app = app,
                    onClick = { onAppClick(app) }
                )
            }
        }
    }
}
