package com.example.laboratorio8.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.laboratorio8.data.PhotoEntity
import com.example.laboratorio8.data.PhotoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: PhotoRepository,
    isNetworkAvailable: () -> Boolean,
    onPhotoClick: (String) -> Unit,
    isDarkMode: Boolean,
    toggleTheme: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("nature") }
    var photos by remember { mutableStateOf<List<PhotoEntity>>(emptyList()) }
    var recentSearches by remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            recentSearches = repository.getRecentQueries()
        }
    }

    LaunchedEffect(searchQuery) {
        delay(500) // Debounce
        if (searchQuery.isNotBlank()) {
            scope.launch {
                repository.addRecentQuery(searchQuery.trim())
                photos = repository.getPhotos(searchQuery.trim(), isNetworkAvailable())
                recentSearches = repository.getRecentQueries()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fotos") },
                actions = {
                    IconButton(onClick = toggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.WbSunny else Icons.Default.Brightness4,
                            contentDescription = "Toggle Theme"
                        )
                    }
                    IconButton(onClick = { /* TODO: Navigate to Profile */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar fotos...") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                singleLine = true
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentSearches) { search ->
                    Text(
                        text = search,
                        modifier = Modifier.clickable { searchQuery = search }.padding(8.dp)
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(photos, key = { it.id }) { photo ->
                    PhotoCard(
                        photo = photo,
                        onClick = { onPhotoClick(photo.id) },
                        onToggleFavorite = {
                            scope.launch {
                                repository.toggleFavorite(photo.id, !photo.isFavorite)
                                val updatedPhotos = photos.map {
                                    if (it.id == photo.id) it.copy(isFavorite = !it.isFavorite) else it
                                }
                                photos = updatedPhotos
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoCard(photo: PhotoEntity, onClick: () -> Unit, onToggleFavorite: () -> Unit) {
    Card(
        modifier = Modifier.aspectRatio(1f).clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = photo.imageUrlSmall,
                contentDescription = "Foto por ${photo.photographer}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconToggleButton(
                checked = photo.isFavorite,
                onCheckedChange = { onToggleFavorite() },
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Icon(
                    imageVector = if (photo.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (photo.isFavorite) Color.Red else Color.White
                )
            }
        }
    }
}
