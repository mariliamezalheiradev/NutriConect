# Revisão do repositório NutriConect

Revisão inicial feita em 07/10/2026, quando a `main` tinha apenas documentação e o código estava espalhado em várias branches.
Este documento registra o que foi encontrado e como cada ponto foi resolvido.

## 1. Problemas encontrados e status

| # | Problema encontrado | Status |
|---|---|---|
| 1 | Chave da API do Gemini escrita em arquivo de configuração | **Resolvido no código.** A chave é lida da variável de ambiente `GEMINI_API_KEY`; nenhum segredo fica versionado. |
| 2 | `.idea/`, `target/` e arquivos `.class` versionados | **Resolvido.** Removidos e cobertos pelo `.gitignore`. |
| 3 | Projeto do Guilherme dentro de pasta aninhada | **Resolvido.** `pom.xml` e `src/` na raiz. |
| 4 | Dois `pom.xml` (MySQL × PostgreSQL) e duas classes principais (`NutriconectApplication` × `NutriConectApplication`) | **Resolvido.** PostgreSQL padronizado (H2 só nos testes) e uma única classe principal. |
| 5 | Modelo `gemini-1.5-flash` possivelmente descontinuado, chave na URL e `printStackTrace()` | **Resolvido.** Modelo configurável (`GEMINI_MODEL`), chave enviada no cabeçalho, uso de logger, tempo limite e novas tentativas. |
| 6 | `DoacaoService` apenas validava e devolvia texto; DTOs duplicados | **Resolvido.** O serviço grava a doação; `DoacaoDTO` validado substitui o `DoacaoRequestDTO`. |
| 7 | Senha do usuário em texto puro | **Resolvido.** BCrypt no cadastro; respostas nunca devolvem a senha. |
| 8 | Branches antigas sem uso (`parte-*`) | **Resolvido.** Conteúdo integrado à `main` e branches removidas. |
| 9 | Entidades divergiam do diagrama conceitual do banco (sem `item_doacao`, `status_doacao` e `receita_ingrediente`) | **Resolvido.** Modelo alinhado às 10 tabelas do diagrama. |

## 2. Pendências em aberto

* **Autenticação e autorização:** os endpoints ainda são abertos.
* **Endpoints que faltam:** cadastro de receptor e de estoque, listagem e atualização de status das doações, persistência das receitas geradas.
* **Migrações de banco:** `spring.jpa.hibernate.ddl-auto=update` serve para desenvolvimento; para produção usar uma ferramenta de migração (Flyway ou Liquibase).
* **Integração contínua:** não há workflow no GitHub Actions; hoje os testes (`mvn clean test`) são executados manualmente.
* **Geolocalização:** o matching geográfico por raio, descrito na arquitetura, ainda não foi implementado.
