<div align="center">

# Open5e App
Cliente móvel para exploração rápida de conteúdo SRD (Open5e): criaturas, magias e itens — com interface moderna em Jetpack Compose e autenticação Firebase.

[Protótipo no Figma](https://www.figma.com/proto/vT8cJnQVCxHsdKcP3O9WPw/Open5e-App?node-id=0-1&t=EiLH7RtaylrS5k5B-1)

</div>

## ✨ Visão Geral
O Open5e App reduz fricção na mesa de RPG permitindo pesquisa, leitura e comparação de criaturas, magias e itens com fluidez. Foco em desempenho, clareza visual e evolução contínua (favoritos, busca avançada, cache offline).

## 📱 Funcionalidades Principais
- Listagem de magias, monstros e itens com detalhes ricos
- Telas de detalhes com atributos estruturados (CR, componentes, raridade etc.)
- Autenticação (login/cadastro) para recursos personalizados futuros (favoritos, histórico)
- Renderização Markdown simplificada (negrito, itálico, cabeçalhos, listas)
- Navegação intuitiva por categorias (Home → Creatures/Spells/Items/Account)
- Base pronta para filtros avançados e paginação

## 🏗 Arquitetura (Resumo)
Camadas simples e evolutivas:
- UI: Jetpack Compose + ViewModels (estado e lógica)
- Network: Retrofit + Gson (`ApiClient`, `Open5eService`)
- Modelos: `Monster`, `Spell`, `MagicItem`
- Autenticação: Firebase Auth (expansão: favoritos em Realtime Database)
- Planejado: Repositórios, casos de uso e DI com Hilt

Fluxo: Compose UI → ViewModel → (Service/Repository) → API/Firebase/Cache

## 🗃 Modelos Principais
| Entidade | Campos Essenciais (exemplos) | Objetivo |
|----------|-----------------------------|----------|
| Monster | CR, AC, HP, ações, habilidades | Referência rápida de estatísticas |
| Spell | nível, escola, componentes, duração | Consulta durante preparação/conjuração |
| MagicItem | raridade, tipo, descrição, efeitos | Avaliar impacto narrativo/mecânico |

## 🧭 Navegação e Fluxos
1. Home → seleção de categoria
2. Lista → detalhe → retornar / (favoritar futuro)
3. Login/SignUp → sessão ativa → Home
4. Account → preferências (tema/acessibilidade futuramente)

## 🔌 Componentes Internos
- Parser Markdown (`MarkdownParser` / `TextMarkdown`)
- Tema centralizado (`Theme.kt`, `Color.kt`, `Type.kt`)
- ViewModels: orquestram estado imutável para Compose
- Serviços de rede: Retrofit interface + client configurado

## 🔄 Estratégia de Dados (Atual vs Futuro)
- Atual: busca on-demand direto da API
- Futuro: cache em memória + Room (offline), TTL para atualização, estado padronizado `UiState`

## 🔐 Autenticação
Firebase Auth para login/cadastro inicial. Planejado: abstração (`AuthRepository`) e favoritos / histórico em caminho isolado por usuário.

## 📝 Markdown
Suporte básico a formatação. Evoluções: tabelas, links internos, parsing incremental para textos maiores.

## 🎨 Tema & UX
Material3 preparado para expansão: tema dinâmico, paletas por categoria, ajuste de tipografia e acessibilidade.

## ✅ Requisitos Funcionais
| Código | Descrição | Status |
|--------|-----------|--------|
| RF01 | Cadastro de usuários (nome, e-mail, telefone, senha) | Parcial (telefone opcional) |
| RF02 | Login com e-mail e senha | Implementado |
| RF03 | Tela inicial com acesso a seções principais | Implementado |
| RF04 | Visualizar/editar dados da conta | Básico (edição limitada) |
| RF05 | Listar criaturas com filtros (CR, tipo, HP, tamanho) | Parcial (filtros em evolução) |
| RF06 | Consultar magias com filtros (nome, nível, classe, escola, componentes) | Parcial (expansão planejada) |
| RF07 | Detalhar itens mágicos (raridade, efeitos, descrição) | Implementado |

## 📐 Requisitos Não Funcionais
| Código | Descrição | Status |
|--------|-----------|--------|
| RNF01 | Kotlin + Android Studio | Ok |
| RNF02 | Gerenciamento de estado (ViewModel + Compose State) | Ok (LiveData substituído) |
| RNF03 | Integração API Open5e via Retrofit | Ok |
| RNF04 | Cache offline com Room | Planejado |
| RNF05 | Seguir protótipo Figma | Em andamento |

## 🚀 Roadmap (Próximos 3 Ciclos)
| Ciclo | Foco | Entregas |
|-------|------|----------|
| 1 | Fundamentos UX | Favoritos, UiState padronizado, melhorias tema |
| 2 | Performance & Offline | Cache Room, TTL, busca avançada |
| 3 | Expansão de Conteúdo | Novas categorias, filtros combinados, links internos |

## 🧪 Testes (Planejado)
- Unit: parsing Markdown, paginação, transformação de modelos
- Integração: Retrofit + MockWebServer
- UI: estados (loading/error/success) e navegação

## 🔒 Segurança
- HTTPS por padrão
- Evitar logs sensíveis (UID/token)
- Futuro: Firebase App Check

## 💪 Evolução Contínua
O desenvolvimento não irá parar: cada ciclo prioriza utilidade real na mesa de jogo (acesso rápido, contexto claro, personalização). A base atual suporta crescimento seguro sem refazer camadas críticas.

## 🤝 Contribuição
Sugestões e PRs são bem-vindos: foco em acessibilidade, internacionalização, performance e novas categorias. Mantenha consistência de estilo Kotlin/Compose.

## 🧾 Licença
Definir (ex: MIT). Conteúdo SRD via Open5e — incluir crédito conforme termos.

---
Documentação mais detalhada em `docs/DOCUMENTACAO_TECNICA.md`.
