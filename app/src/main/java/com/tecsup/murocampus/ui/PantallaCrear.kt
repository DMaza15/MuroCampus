package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }
    val estado by vm.publicacion.collectAsStateWithLifecycle()
    val enviando = estado == UiState.Cargando

    LaunchedEffect(estado) {
        if (estado is UiState.Exito<*>) {
            titulo = ""
            contenido = ""
        }
    }

    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            singleLine = true,
            enabled = !enviando,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            minLines = 4,
            enabled = !enviando,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { vm.publicar(titulo, contenido) },
            enabled = !enviando && titulo.isNotBlank() && contenido.isNotBlank()
        ) { Text("Publicar") }

        when (val actual = estado) {
            null -> Unit
            UiState.Cargando -> Cargando("Publicando…")
            is UiState.Error -> Text(actual.mensaje, color = MaterialTheme.colorScheme.error)
            is UiState.Exito -> Text("Publicado con id ${actual.datos.id}")
        }
    }
}
