package br.edu.ifsp.scl.sc303769x.postviewer.data.repository

import br.edu.ifsp.scl.sc303769x.postviewer.data.local.dao.LocalCommentDao
import br.edu.ifsp.scl.sc303769x.postviewer.data.local.entity.LocalCommentEntity
import br.edu.ifsp.scl.sc303769x.postviewer.data.remote.RetrofitInstance
import br.edu.ifsp.scl.sc303769x.postviewer.model.Comment
import br.edu.ifsp.scl.sc303769x.postviewer.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PostRepository(private val localCommentDao: LocalCommentDao) {
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }

    // Obtém os comentários da API
    suspend fun getRemoteComments(postId: Int): List<Comment> {
        return RetrofitInstance.api.getCommentsByPostId(postId)
    }

    // Retorna um fluxo contínuo dos comentários guardados no Room
    fun getLocalComments(postId: Int): Flow<List<Comment>> {
        return localCommentDao.getLocalCommentsByPostId(postId).map { entities ->
            entities.map { entity ->
                Comment(
                    postId = entity.postId,
                    id = entity.id + 10000, // Soma 10000 para evitar colisão com os IDs da API
                    name = entity.name,
                    email = entity.email,
                    body = entity.body
                )
            }
        }
    }

    // Adiciona um comentário local chamando o DAO numa thread de IO (background)
    suspend fun addLocalComment(postId: Int, name: String, email: String, body: String) {
        withContext(Dispatchers.IO) {
            localCommentDao.insertLocalComment(
                LocalCommentEntity(
                    postId = postId,
                    name = name,
                    email = email,
                    body = body
                )
            )
        }
    }
}
