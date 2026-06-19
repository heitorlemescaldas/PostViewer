package br.edu.ifsp.scl.sc303769x.postviewer.data.remote

import br.edu.ifsp.scl.sc303769x.postviewer.model.Comment
import br.edu.ifsp.scl.sc303769x.postviewer.model.Post
import retrofit2.http.GET
import retrofit2.http.Path

interface PostService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}/comments")
    suspend fun getCommentsByPostId(@Path("id") postId: Int): List<Comment>

    //Retorna todos os comentarios
    @GET("comments")
    suspend fun getAllComments(): List<Comment>
}
