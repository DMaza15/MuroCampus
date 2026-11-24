package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
            MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF164682))) {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text("MuroCampus") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }) { padding ->
                    PantallaLista(vm, alAbrir = {}, modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
