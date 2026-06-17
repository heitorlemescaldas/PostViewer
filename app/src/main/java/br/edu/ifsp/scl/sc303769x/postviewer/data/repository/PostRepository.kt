package br.edu.ifsp.scl.sc303769x.postviewer.data.repository

import br.edu.ifsp.scl.sc303769x.postviewer.data.remote.RetrofitInstance
import br.edu.ifsp.scl.sc303769x.postviewer.model.Comment
import br.edu.ifsp.scl.sc303769x.postviewer.model.Post

// Repositório que abstrai a origem dos dados (API ou Base de Dados) para o ViewModel
class PostRepository {
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }

    suspend fun getComments(postId: Int): List<Comment> {
        return RetrofitInstance.api.getCommentsByPostId(postId)
    }
}
