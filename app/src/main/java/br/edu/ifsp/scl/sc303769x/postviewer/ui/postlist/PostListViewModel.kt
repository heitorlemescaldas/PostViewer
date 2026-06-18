package br.edu.ifsp.scl.sc303769x.postviewer.ui.postlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.edu.ifsp.scl.sc303769x.postviewer.PostViewerApplication
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.scl.sc303769x.postviewer.data.repository.PostRepository
import br.edu.ifsp.scl.sc303769x.postviewer.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Define os possíveis estados da UI para a lista de posts (padrão StateFlow)
sealed class PostListState {
    object Loading : PostListState()
    data class Success(val posts: List<Post>) : PostListState()
    data class Error(val message: String) : PostListState()
}

class PostListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as PostViewerApplication).repository

    // Estado observável pela UI. O underline impede alterações diretas de fora do ViewModel.
    private val _state = MutableStateFlow<PostListState>(PostListState.Loading)
    val state: StateFlow<PostListState> = _state.asStateFlow()

    init {
        fetchPosts()
    }

    // Função responsável por buscar os dados do repositório em modo assíncrono (Coroutines)
    private fun fetchPosts() {
        viewModelScope.launch {
            _state.value = PostListState.Loading
            try {
                val posts = repository.getPosts()
                _state.value = PostListState.Success(posts)
            } catch (e: Exception) {
                _state.value = PostListState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}
