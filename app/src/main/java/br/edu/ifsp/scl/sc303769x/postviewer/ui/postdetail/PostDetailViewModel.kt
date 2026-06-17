package br.edu.ifsp.scl.sc303769x.postviewer.ui.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.scl.sc303769x.postviewer.data.repository.PostRepository
import br.edu.ifsp.scl.sc303769x.postviewer.model.Comment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estados possíveis para a tela de detalhes do post
sealed class PostDetailState {
    object Loading : PostDetailState()
    data class Success(val comments: List<Comment>) : PostDetailState()
    data class Error(val message: String) : PostDetailState()
}

class PostDetailViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _state = MutableStateFlow<PostDetailState>(PostDetailState.Loading)
    val state: StateFlow<PostDetailState> = _state.asStateFlow()

    // Faz a chamada à API filtrando os comentários pelo ID do post de forma assíncrona
    fun fetchComments(postId: Int) {
        viewModelScope.launch {
            _state.value = PostDetailState.Loading
            try {
                val comments = repository.getComments(postId)
                _state.value = PostDetailState.Success(comments)
            } catch (e: Exception) {
                _state.value = PostDetailState.Error(e.message ?: "Erro desconhecido ao carregar comentários")
            }
        }
    }
}
