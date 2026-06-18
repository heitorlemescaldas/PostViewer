package br.edu.ifsp.scl.sc303769x.postviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.edu.ifsp.scl.sc303769x.postviewer.data.local.entity.LocalCommentEntity
import kotlinx.coroutines.flow.Flow

// Interface de acesso aos dados (DAO) para a tabela de comentários locais
@Dao
interface LocalCommentDao {
    // Retorna um Flow reativo com os comentários de um post específico
    @Query("SELECT * FROM local_comments WHERE postId = :postId")
    fun getLocalCommentsByPostId(postId: Int): Flow<List<LocalCommentEntity>>

    // Insere um novo comentário de forma síncrona para evitar bugs de KSP
    @Insert
    fun insertLocalComment(comment: LocalCommentEntity)
}
