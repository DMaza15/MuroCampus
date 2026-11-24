package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()
    private var cargaLista: Job? = null

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()
    private var cargaDetalle: Job? = null
    private var idDetalle: Int? = null

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    fun filtrar(userId: Int?) {
        if (_usuarioFiltro.value == userId) return
        _usuarioFiltro.value = userId
        cargar()
    }

    init {
        cargar()
    }

    fun cargar() {
        val usuario = _usuarioFiltro.value
        cargaLista?.cancel()
        cargaLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = if (usuario == null) repo.listar() else repo.porUsuario(usuario)
            _lista.value = resultado.aEstado()
        }
    }

    fun abrir(id: Int) {
        if (idDetalle == id && _detalle.value !is UiState.Error) return
        cargaDetalle?.cancel()
        idDetalle = id
        _detalle.value = UiState.Cargando
        cargaDetalle = viewModelScope.launch {
            val resultado = coroutineScope {
                val post = async { repo.detalle(id) }
                val comentarios = async { repo.comentarios(id) }
                post.await().fold(
                    onSuccess = { dato ->
                        comentarios.await().map { DetalleUi(dato, it) }
                    },
                    onFailure = {
                        comentarios.cancel()
                        Result.failure(it)
                    }
                )
            }
            _detalle.value = resultado.aEstado()
        }
    }

    fun cerrarDetalle() {
        cargaDetalle?.cancel()
        idDetalle = null
        _detalle.value = UiState.Cargando
    }

    private fun <T> Result<T>.aEstado(): UiState<T> = fold(
        onSuccess = { UiState.Exito(it) },
        onFailure = { UiState.Error(mensajeDe(it)) }
    )
}
