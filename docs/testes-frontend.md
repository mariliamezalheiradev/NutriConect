# Testes do front-end

Registro dos testes do aplicativo (front-end, API e banco). Cobre os fluxos de **login, cadastro, busca de ONGs, doação e receita**, e os erros pedidos na issue #53: senha errada, sem token e API desligada. O erro de **CEP inexistente** não pôde ser testado (veja "O que não foi testado").

## Como foi testado

| Item | Valor |
|---|---|
| Data | 25/11/2026 |
| Código testado | `main` no commit `1c41cb9`. O back-end foi compilado desse mesmo código, com a linha `server.port=${PORT:8080}` da issue #70 |
| Banco | PostgreSQL 16, vazio no início de cada rodada (as tabelas são criadas pela aplicação) |
| Navegador | Chromium 153.0.8010.12 (Playwright, modo headless) |
| Tela | 1280 × 900 (computador) |
| Execução | Automatizada, com o Playwright, simulando um usuário (cliques e digitação). O script não está versionado no repositório |
| Resultado | **19 passaram, 3 parciais, 0 falharam** (de 22 verificações) |

> **Importante:** este roteiro foi executado por um script, e não à mão por uma pessoa do grupo. Veja "O que não foi testado" e repita à mão ao menos os fluxos principais antes de gravar o vídeo.

## Resultados

Mensagens entre aspas são o texto exato mostrado na tela. Situação "⚠️ Parcial" quer dizer que o erro foi tratado corretamente, mas o caminho de sucesso **não pôde ser executado** neste ambiente (veja os motivos abaixo da tabela).

| ID | Fluxo | O que fazer | Resultado esperado | Resultado obtido | Situação |
|---|---|---|---|---|---|
| T01 | Cadastro | Cadastrar como doador com dados válidos | Mensagem de conta criada | "Conta criada com sucesso! Agora entre com seu e-mail e senha." | ✅ Passou |
| T02 | Cadastro | Cadastrar de novo com o mesmo e-mail | Erro de e-mail já cadastrado | "Já existe um usuário com este e-mail." | ✅ Passou |
| T03 | Cadastro | Cadastrar como ONG com CEP válido | Conta criada (ou erro do serviço de CEP, se ele estiver fora do ar) | "O serviço de CEP está indisponível no momento. Tente novamente em instantes." | ⚠️ Parcial |
| T04 | Login | Entrar com e-mail e senha corretos | Abre o painel | Abriu o painel (`/painel`) | ✅ Passou |
| T05 | Login | Clicar em Sair | Volta para a tela inicial e apaga o token | Voltou para a tela inicial e o token foi apagado | ✅ Passou |
| T06 | Login | Entrar com senha errada | Mensagem de e-mail ou senha incorretos | "E-mail ou senha inválidos." | ✅ Passou |
| T07 | Login | Entrar com e-mail que não existe | A mesma mensagem da senha errada (não revela qual campo errou) | "E-mail ou senha inválidos." | ✅ Passou |
| T08 | Sem token | Abrir /painel sem estar logado | Redireciona para a tela inicial | Redirecionou para a tela inicial (`/`) | ✅ Passou |
| T09 | Sem token | Usar o painel com um token inválido (busca de ONGs) | O back-end responde 401, o token é apagado e volta ao login | Voltou para a tela inicial e o token foi apagado | ✅ Passou |
| T10 | Busca de ONGs | Buscar ONGs por um CEP válido (chamada real) | Lista de ONGs ordenada da mais perto para a mais longe (ou erro do serviço de CEP) | "O serviço de CEP está indisponível no momento. Tente novamente em instantes." | ⚠️ Parcial |
| T11 | Busca de ONGs | Buscar e clicar em Escolher (resposta da busca SIMULADA; a ONG é a do banco) | A ONG aparece com a distância e vira o destino da doação | A ONG apareceu com "1,2 km" e foi escolhida como destino da doação | ✅ Passou |
| T12 | Doação | Abrir Nova doação com uma ONG escolhida | Select lista os ingredientes e não aparece o aviso de 'sem ONG' | 3 ingredientes no select; sem o aviso de "nenhuma ONG selecionada" | ✅ Passou |
| T13 | Doação | Tentar concluir sem nenhum item | Botão de concluir não aparece enquanto não há itens | O botão de concluir não aparece enquanto não há itens | ✅ Passou |
| T14 | Doação | Adicionar o mesmo ingrediente duas vezes | Mensagem de ingrediente já adicionado | "Este ingrediente já foi adicionado à doação." | ✅ Passou |
| T15 | Doação | Registrar doação com 2 itens decimais (0,5 e 1,25) e ONG escolhida | Mensagem de doação registrada; no banco, 1 doação com receptor_id e 2 itens | "Doação registrada com sucesso!" | ✅ Passou |
| T16 | Doação | Registrar doação com 1 item e sem ONG escolhida | Aviso na tela e doação registrada sem destino (receptor_id nulo) | "Doação registrada com sucesso!" | ✅ Passou |
| T17 | Doação | Registrar doação com o token apagado/inválido | O back-end responde 401 e volta para o login | Voltou para a tela inicial | ✅ Passou |
| T18 | Doação | Entrar como ONG e tentar registrar uma doação | Mensagem de que só doadores podem doar (403) | "Apenas usuários doadores podem registrar doações." | ✅ Passou |
| T19 | Receita | Pedir receita sem escolher nenhum ingrediente | Mensagem pedindo para escolher ou digitar um ingrediente | "Escolha ou digite pelo menos um ingrediente." | ✅ Passou |
| T20 | Receita | Gerar receita com um ingrediente marcado (sem chave do Gemini no ambiente de teste) | Receita exibida (com chave ativa) ou mensagem amigável de IA indisponível | "A IA está ocupada, tente de novo em instantes." | ⚠️ Parcial |
| T21 | API desligada | Tentar entrar com o back-end parado | Mensagem de que não foi possível conectar à API | "Não foi possível conectar à API. Verifique se o back-end está rodando." | ✅ Passou |
| T22 | Geral | Erros de JavaScript no console durante todos os testes | Nenhum | Nenhum erro | ✅ Passou |

**Observações sobre os resultados parciais e simulados**
* **T03 e T10:** a API externa que converte CEP em coordenadas não é alcançável do ambiente de teste, então o cadastro de ONG e a busca por CEP devolveram o erro de serviço de CEP indisponível (tratado com a mensagem acima). O cadastro de ONG com sucesso e a lista real de ONGs próximas **não foram exibidos**.
* **T11:** como a busca real não funcionou, a resposta de `/api/receptores/proximos` foi **simulada no navegador** com a ONG que existe no banco de teste. O restante do fluxo (escolher a ONG e usá-la na doação) é real.
* **T20:** não havia chave do Gemini no ambiente, então só o tratamento do erro ("A IA está ocupada") foi verificado. A receita real precisa ser testada com a chave ativa.

## Conferência no banco

Depois das doações, as consultas abaixo confirmaram o que foi gravado (T15 e T16):

```sql
SELECT d.id, d.doador_id, d.receptor_id, s.descricao AS status
FROM tb_doacao d JOIN tb_status_doacao s ON s.id = d.status_id ORDER BY d.id;

SELECT i.doacao_id, g.nome AS ingrediente, i.quantidade
FROM tb_item_doacao i JOIN tb_ingrediente g ON g.id = i.ingrediente_id ORDER BY i.doacao_id, i.id;
```

| Doação | Doador | ONG (`receptor_id`) | Status | Itens |
|---|---|---|---|---|
| 1 | 1 | 2 | PENDENTE | Feijão 0,50 e Leite 1,25 |
| 2 | 1 | vazio (sem ONG) | PENDENTE | Arroz 2,00 |

Só essas duas doações existem: as tentativas com token inválido (T17) e como ONG (T18) **não gravaram nada**. O e-mail repetido (T02) também não criou um segundo usuário, e o cadastro de ONG que falhou por causa do CEP (T03) não deixou registro.

## Problemas encontrados

1. **Texto desatualizado na tela de doação.** Quando nenhuma ONG foi escolhida, a tela diz "Para escolher uma, use a busca acima", mas desde o novo layout a busca de ONGs fica em outra seção do painel ("ONGs perto"), e não acima. Sugestão: trocar por "use a seção ONGs perto".

Nenhum erro de JavaScript apareceu no console durante os testes (T22).

## O que não foi testado

* Cadastro de ONG com sucesso e busca por CEP real (a API de CEP precisa estar acessível).
* **CEP inexistente** (resposta 404): como a API de CEP não estava acessível, só foi visto o erro de serviço de CEP indisponível (T03 e T10). Teste com um CEP que não existe quando a API estiver acessível.
* Geração de receita real (precisa da `GEMINI_API_KEY` ativa).
* Outros navegadores (Firefox, Safari, Edge) e celular de verdade. Só o Chromium, em tela de computador.
* Cadastro com campos inválidos que o back-end recusa (erro 400 por campo).
* Teste manual por uma pessoa do grupo, como pede a issue.

## Como repetir

1. Suba o banco, o back-end e o front-end seguindo a seção **Instalação** do README.
2. Crie um doador, uma ONG e alguns ingredientes (pela tela de cadastro e pela API, conforme a seção **Como Usar**).
3. Percorra os fluxos da tabela na ordem dos IDs.
4. Para o T21, pare o back-end e tente entrar.
5. Confira as doações no banco com as consultas acima.
