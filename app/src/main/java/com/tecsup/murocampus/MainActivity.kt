package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.*
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
        setContent {
            val vm: PostsViewModel = viewModel(factory = factory)
            var seleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
            var creando by rememberSaveable { mutableStateOf(false) }
            BackHandler(seleccionado != null || creando) {
                if (creando) {
                    vm.cerrarPublicacion()
                    creando = false
                } else {
                    vm.cerrarDetalle()
                    seleccionado = null
                }
            }
            LaunchedEffect(seleccionado) { seleccionado?.let(vm::abrir) }
            MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF164682))) {
                Scaffold(
                    floatingActionButton = {
                        if (!creando && seleccionado == null) {
                            FloatingActionButton(onClick = { creando = true }) {
                                Text("+", style = MaterialTheme.typography.headlineSmall)
                            }
                        }
                    },
                    topBar = {
                    TopAppBar(
                        title = { Text("MuroCampus") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }) { padding ->
                    val id = seleccionado
                    val contenido = Modifier.padding(padding)
                    when {
                        creando -> PantallaCrear(vm, modifier = contenido)
                        id != null -> PantallaDetalle(vm, id, modifier = contenido)
                        else -> PantallaLista(vm, alAbrir = { seleccionado = it }, modifier = contenido)
                    }
                }
            }
        }
    }
}
