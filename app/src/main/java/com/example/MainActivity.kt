package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.data.local.AppDatabase
import com.example.data.repository.MuslimRepository
import com.example.ui.screens.MainHostScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MuslimViewModel
import com.example.ui.viewmodel.MuslimViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize offline Room database instance using transaction safe coroutine scopes
        val database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = MuslimRepository(database.muslimDao())

        // 2. Build MuslimViewModel using custom providers to inject standard constructors
        val viewModel = ViewModelProvider(
            this,
            MuslimViewModelFactory(application, repository)
        )[MuslimViewModel::class.java]

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainHostScreen(viewModel = viewModel)
                }
            }
        }
    }
}
