package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.murocampus.model.Post

@Composable
fun TarjetaPost(post: Post, alAbrir: () -> Unit, modifier: Modifier = Modifier) {
    Card(onClick = alAbrir, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Usuario ${post.userId}", color = MaterialTheme.colorScheme.primary)
            Text(post.title, style = MaterialTheme.typography.titleMedium)
            Text(post.body, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun Cargando(texto: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator()
        Text(texto)
    }
}

@Composable
fun ErrorConReintento(mensaje: String, reintentar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(mensaje)
            Button(onClick = reintentar) { Text("Reintentar") }
        }
    }
}
