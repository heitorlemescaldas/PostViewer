# PostViewer

**Aluno:** Heitor Lemes Caldas
**Disciplina:** Programação para Dispositivos Móveis
**Professor:** Pedro Northon Nobile

## Descrição do Aplicativo
O **PostViewer** é uma aplicação Android desenvolvida como trabalho final da disciplina. O seu principal objetivo é consumir a API pública JSONPlaceholder para exibir uma lista de posts e os seus respetivos comentários. Adicionalmente, a aplicação suporta uma arquitetura *Offline-First* parcial, permitindo que o utilizador adicione os seus próprios comentários localmente, sendo estes persistidos no dispositivo e exibidos juntamente com os comentários provenientes da API.

## Funcionalidades e Requisitos Cumpridos
- ✅ **Lista de Posts:** Carregamento assíncrono dos posts da API.
- ✅ **Detalhes do Post:** Exibição da lista de comentários da API associados a um post específico.
- ✅ **Comentários Locais (Bónus):** Criação de comentários armazenados localmente via banco de dados que persistem entre sessões do aplicativo e surgem imediatamente no ecrã.
- ✅ **Qualidade de Código:** Arquitetura MVVM, separação rigorosa de responsabilidades e tratamento de estados da interface (Loading, Success, Error).
- ✅ **Conventional Commits:** Histórico de commits mantido sob as normas padrão exigidas.

## Tecnologias e Bibliotecas Utilizadas
- **Linguagem:** Kotlin (v2.0.21)
- **UI Toolkit:** Jetpack Compose (Material Design 3)
- **Navegação:** Navigation Compose
- **Consumo de API:** Retrofit 2 & Gson Converter
- **Persistência Local:** Room Database (com KSP)
- **Assincronismo & Estado:** Coroutines, ViewModel e StateFlow

## Decisões de Design e Arquitetura Relevantes
- **Arquitetura MVVM com Clean Architecture:** A lógica de acesso aos dados foi separada no `PostRepository`. Os ecrãs não sabem de onde vêm os dados (API ou Room), dependendo estritamente do `ViewModel` que expõe um `StateFlow` imutável (`PostListState` / `PostDetailState`).
- **Padrão Single Source of Truth (SSOT):** No `PostDetailViewModel`, os comentários da API e do Room são geridos de forma combinada. A persistência Room devolve um `Flow` reativo, o que significa que mal um comentário é inserido, o ecrã atualiza automaticamente (`collectLatest`) sem a necessidade de recarregar a API.
- **Resolução de Conflitos KSP/Kotlin 2.0:** Devido a problemas de compatibilidade da compilação de assinaturas *suspend/continuation* no KSP com a versão do Room exigida, optou-se por utilizar o `Dispatchers.IO` no Repositório envolvendo um método `DAO` síncrono. Isso permitiu garantir uma persistência sem bloqueio de UI de forma totalmente segura e estável, fugindo das dependências obsoletas (KAPT).
- **Interface Responsiva (Edge-to-Edge):** A caixa de *input* para comentários foi desenvolvida utilizando modificadores avançados de `WindowInsets` (`navigationBarsPadding` e `imePadding`) no Compose, assegurando uma experiência de utilizador moderna onde o teclado empurra a UI em vez de se sobrepor ao texto.

## Instruções para Execução Local
1. Clone o repositório na sua máquina local:
   ```bash
   git clone https://github.com/heitorlemescaldas/PostViewer.git
   ```
2. Abra o projeto no **Android Studio** (versão Hedgehog ou superior recomendada).
3. Aguarde o *Gradle Sync* finalizar automaticamente.
4. Selecione um emulador (API 26+) ou dispositivo físico conectado.
5. Clique em **Run** (Shift + F10).

## Capturas de Tela (Screenshots)

### Lista de Posts e Detalhes (API)
![Lista de Posts](docs/Tela%201.png)
![Detalhes do Post](docs/Tela%202.png)

### Comentários Locais (Room)
![Comentários Locais](docs/Tela%203%20-%20comentario%20local.png)
![Escrevendo](docs/Tela%204%20-%20escrevendo%20comentario.png)
![Último Comentário](docs/Tela%205%20-%20Ultimo%20comentario%20postado.png)
