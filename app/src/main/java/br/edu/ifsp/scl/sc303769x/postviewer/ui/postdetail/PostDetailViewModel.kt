package br.edu.ifsp.scl.sc303769x.postviewer.ui.postdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.scl.sc303769x.postviewer.PostViewerApplication
import br.edu.ifsp.scl.sc303769x.postviewer.model.Comment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Estados possíveis para a tela de detalhes do post
sealed class PostDetailState {
    object Loading : PostDetailState()
    data class Success(val comments: List<Comment>) : PostDetailState()
    data class Error(val message: String) : PostDetailState()
}

class PostDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as PostViewerApplication).repository

    private val _state = MutableStateFlow<PostDetailState>(PostDetailState.Loading)
    val state: StateFlow<PostDetailState> = _state.asStateFlow()

    private var remoteComments: List<Comment> = emptyList()

    fun fetchComments(postId: Int) {
        viewModelScope.launch {
            _state.value = PostDetailState.Loading
            try {
                remoteComments = repository.getRemoteComments(postId)
                repository.getLocalComments(postId).collectLatest { localComments ->
                    val allComments = remoteComments + localComments
                    _state.value = PostDetailState.Success(allComments)
                }
            } catch (e: Exception) {
                _state.value = PostDetailState.Error(e.message ?: "Erro desconhecido ao carregar comentários")
            }
        }
    }

    fun addLocalComment(postId: Int, name: String, email: String, body: String) {
        viewModelScope.launch {
            repository.addLocalComment(postId, name, email, body)
        }
    }
}
