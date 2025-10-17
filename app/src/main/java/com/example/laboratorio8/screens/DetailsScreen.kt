package com.example.laboratorio8.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    repository: PhotoRepository,
    photoId: String,
    onNavigateBack: () -> Unit,
    isNetworkAvailable: () -> Boolean
) {
    var photo by remember { mutableStateOf<PhotoEntity?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(photoId) {
        scope.launch {
            photo = repository.getPhotoById(photoId, isNetworkAvailable())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(photo?.photographer ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        photo?.let {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = it.imageUrlLarge,
                    contentDescription = "Foto por ${it.photographer}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            val currentPhoto = photo!!
                            val newFavStatus = !currentPhoto.isFavorite
                            repository.toggleFavorite(currentPhoto.id, newFavStatus)
                            photo = currentPhoto.copy(isFavorite = newFavStatus)
                        }
                    }) {
                        Icon(
                            imageVector = if (it.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (it.isFavorite) Color.Red else LocalContentColor.current
                        )
                    }
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                }
            }
        }
    }
}
