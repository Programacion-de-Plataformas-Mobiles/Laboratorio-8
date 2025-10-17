package com.example.laboratorio8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.example.laboratorio8.data.AppDatabase
import com.example.laboratorio8.data.PhotoRepository
import com.example.laboratorio8.data.ThemeManager
import com.example.laboratorio8.screens.AppNavigation
import com.example.laboratorio8.ui.theme.Laboratorio8Theme
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { PhotoRepository(database.photoDao(), database.recentQueryDao()) }
    private val themeManager by lazy { ThemeManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)
            Laboratorio8Theme(darkTheme = isDarkMode) {
                AppNavigation(
                    repository = repository,
                    isNetworkAvailable = { isNetworkConnected() },
                    isDarkMode = isDarkMode,
                    toggleTheme = {
                        lifecycleScope.launch {
                            themeManager.setDarkMode(!isDarkMode)
                        }
                    }
                )
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
