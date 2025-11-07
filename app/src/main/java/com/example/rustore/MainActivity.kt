package com.example.rustore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.rustore.storage.OnboardingRepository
import com.example.rustore.ui.RustoreApp
import com.example.rustore.ui.theme.RustoreShowcaseTheme
import com.example.rustore.viewmodel.RustoreViewModel
import com.example.rustore.viewmodel.RustoreViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: RustoreViewModel by viewModels {
        RustoreViewModelFactory(OnboardingRepository.from(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RustoreShowcaseTheme {
                RustoreApp(viewModel = viewModel)
            }
        }
    }
}
