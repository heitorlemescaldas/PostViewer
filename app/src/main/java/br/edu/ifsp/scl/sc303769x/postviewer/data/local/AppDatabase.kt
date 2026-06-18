package br.edu.ifsp.scl.sc303769x.postviewer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.ifsp.scl.sc303769x.postviewer.data.local.dao.LocalCommentDao
import br.edu.ifsp.scl.sc303769x.postviewer.data.local.entity.LocalCommentEntity

// Define o banco de dados Room e as entidades associadas
@Database(entities = [LocalCommentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localCommentDao(): LocalCommentDao

    companion object {
        // Volatile garante que as leituras/escritas sejam atômicas entre threads
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Retorna a instância única (Singleton) do banco de dados
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "postviewer_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
