package br.edu.ifsp.scl.sc303769x.postviewer

import android.app.Application
import br.edu.ifsp.scl.sc303769x.postviewer.data.local.AppDatabase
import br.edu.ifsp.scl.sc303769x.postviewer.data.repository.PostRepository

// Classe Application customizada para gerir injeção de dependência manual (Singletons)
class PostViewerApplication : Application() {
    // Instancia a Base de Dados apenas quando for necessária pela primeira vez (lazy)
    val database by lazy { AppDatabase.getDatabase(this) }
    // Instancia o repositório passando o DAO da Base de Dados
    val repository by lazy { PostRepository(database.localCommentDao()) }
}
