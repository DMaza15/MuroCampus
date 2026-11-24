package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, alAbrir: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    val usuario by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf(null) + (1..10).toList()) { candidato ->
                FilterChip(
                    selected = usuario == candidato,
                    onClick = { vm.filtrar(candidato) },
                    label = { Text(candidato?.let { "Usuario $it" } ?: "Todos") }
                )
            }
        }
        when (val actual = estado) {
            UiState.Cargando -> Cargando("Cargando publicaciones…")
            is UiState.Error -> ErrorConReintento(actual.mensaje, vm::cargar)
            is UiState.Exito -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (actual.datos.isEmpty()) item { Text("Sin publicaciones") }
                items(actual.datos, key = { it.id }) { post ->
                    TarjetaPost(post, alAbrir = { alAbrir(post.id) })
                }
            }
        }
    }
}
