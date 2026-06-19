package br.edu.ifsp.scl.sc303769x.postviewer.ui.postlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.scl.sc303769x.postviewer.PostViewerApplication
import br.edu.ifsp.scl.sc303769x.postviewer.data.repository.PostRepository
import br.edu.ifsp.scl.sc303769x.postviewer.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class PostUiModel(
    val id: Int,
    val title: String,
    val body: String,
    val commentCount: Int
)

sealed class PostListState {
    object Loading : PostListState()
    data class Success(val posts: List<PostUiModel>) : PostListState()
    data class Error(val message: String) : PostListState()
}

class PostListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as PostViewerApplication).repository

    private val _state = MutableStateFlow<PostListState>(PostListState.Loading)
    val state: StateFlow<PostListState> = _state.asStateFlow()

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            _state.value = PostListState.Loading
            val postsDeferred = async { repository.getPosts() }
            val remoteCommentsDeferred = async { repository.getAllRemoteComments() }
                
            val posts = postsDeferred.await()
            val remoteComments = remoteCommentsDeferred.await()

            repository.getAllLocalComments()
                .catch { e ->
                    val commentCountByPost = remoteComments.groupingBy { it.postId }.eachCount()
                    val postUiModels = posts.map { post ->
                        PostUiModel(
                            id = post.id,
                            title = post.title,
                            body = post.body,
                            commentCount = commentCountByPost[post.id] ?: 0
                        )
                    }
                    _state.value = PostListState.Success(postUiModels)
                }
                .collect { localComments ->
                    val allComments = remoteComments + localComments
                    val commentCountByPost = allComments.groupingBy { it.postId }.eachCount()
                        
                    val postUiModels = posts.map { post ->
                        PostUiModel(
                            id = post.id,
                            title = post.title,
                            body = post.body,
                            commentCount = commentCountByPost[post.id] ?: 0
                        )
                    }
                    _state.value = PostListState.Success(postUiModels) }
        }
    }
}
