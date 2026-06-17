package br.edu.ifsp.scl.sc303769x.postviewer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.ifsp.scl.sc303769x.postviewer.ui.postlist.PostListScreen

// Componente responsável por gerir o grafo de navegação (Single Activity Architecture)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "post_list") {
        composable("post_list") {
            PostListScreen(
                onPostClick = { postId ->
                    // Navega para os detalhes passando o ID do post via rota
                    navController.navigate("post_detail/$postId")
                }
            )
        }
        composable("post_detail/{postId}") { backStackEntry ->
            // Recupera o ID passado como argumento na rota
            val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
            
            // Placeholder temporário. Faremos esta tela na Fase 4.
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Detalhes do Post ID: $postId")
            }
        }
    }
}
