package br.edu.ifsp.scl.sc303769x.postviewer.ui.postdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.scl.sc303769x.postviewer.model.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: Int,
    onNavigateBack: () -> Unit,
    viewModel: PostDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var commentText by remember { mutableStateOf("") }

    // Dispara a busca de comentários assim que o ecrã for composto pela primeira vez
    // (O bloco LaunchedEffect roda a sua rotina se o postId for diferente da renderização anterior)
    LaunchedEffect(postId) {
        viewModel.fetchComments(postId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comentários") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Adicionar comentário...") },
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                viewModel.addLocalComment(
                                    postId = postId,
                                    name = "Usuário Local",
                                    email = "usuario@local.com",
                                    body = commentText
                                )
                                commentText = ""
                            }
                        }
                    ) {
                        Text("Enviar")
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = state) {
                is PostDetailState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PostDetailState.Success -> {
                    if (currentState.comments.isEmpty()) {
                        Text(
                            text = "Nenhum comentário encontrado.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(currentState.comments) { comment ->
                                CommentItem(comment = comment)
                            }
                        }
                    }
                }
                is PostDetailState.Error -> {
                    Text(
                        text = "Erro: ${currentState.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

// Componente para desenhar o cartão de um comentário oriundo da API
@Composable
fun CommentItem(comment: Comment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = comment.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = comment.email,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comment.body,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
