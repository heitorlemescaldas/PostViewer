package br.edu.ifsp.scl.sc303769x.postviewer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.ifsp.scl.sc303769x.postviewer.ui.postdetail.PostDetailScreen
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
            
            if (postId != null) {
                PostDetailScreen(
                    postId = postId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
