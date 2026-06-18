package br.edu.ifsp.scl.sc303769x.postviewer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa a tabela de comentários locais no banco de dados Room
@Entity(tableName = "local_comments")
data class LocalCommentEntity(
    // ID gerado automaticamente para cada comentário inserido
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String
)
