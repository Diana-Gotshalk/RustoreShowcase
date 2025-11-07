package com.example.rustore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rustore.data.AppRepository
import com.example.rustore.model.AppInfo
import com.example.rustore.storage.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RustoreViewModel(
    private val repository: AppRepository = AppRepository,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _apps = MutableStateFlow(repository.getApps())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    val onboardingCompleted: StateFlow<Boolean> =
        onboardingRepository.onboardingCompleted.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun findApp(appId: String): AppInfo? = repository.getAppById(appId)

    fun categoriesWithCounters() = repository.getCategoriesWithCounters()

    fun markOnboardingSeen() {
        viewModelScope.launch {
            onboardingRepository.markCompleted()
        }
    }
}

class RustoreViewModelFactory(
    private val onboardingRepository: OnboardingRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(RustoreViewModel::class.java)) {
            "Unknown ViewModel class"
        }
        return RustoreViewModel(onboardingRepository = onboardingRepository) as T
    }
}
