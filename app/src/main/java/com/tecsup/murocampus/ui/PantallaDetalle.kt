package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val eliminacion by vm.eliminacion.collectAsStateWithLifecycle()
    var confirmar by remember { mutableStateOf(false) }
    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Eliminar publicación?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmar = false
                    vm.eliminar(id)
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("Cancelar") }
            }
        )
    }
    Column(modifier) {
        when (val actual = estado) {
            UiState.Cargando -> Cargando("Cargando detalle…")
            is UiState.Error -> ErrorConReintento(actual.mensaje) { vm.abrir(id) }
            is UiState.Exito -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(actual.datos.post.title, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text(actual.datos.post.body)
                    OutlinedButton(
                        onClick = { confirmar = true },
                        enabled = eliminacion != UiState.Cargando && eliminacion !is UiState.Exito<*>
                    ) { Text("Eliminar") }
                    when (val resultado = eliminacion) {
                        null -> Unit
                        UiState.Cargando -> Text("Eliminando…")
                        is UiState.Exito -> Text("Eliminación simulada")
                        is UiState.Error -> Text(resultado.mensaje, color = MaterialTheme.colorScheme.error)
                    }
                }
                item {
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Text("Comentarios (${actual.datos.comentarios.size})", style = MaterialTheme.typography.titleLarge)
                }
                items(actual.datos.comentarios, key = { it.id }) { comentario ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(comentario.email, color = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(4.dp))
                            Text(comentario.body)
                        }
                    }
                }
            }
        }
    }
}
