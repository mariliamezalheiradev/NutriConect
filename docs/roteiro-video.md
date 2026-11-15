# Roteiro do vídeo – Entrega 4 (NutriConect)

**Duração alvo:** cerca de 4 min 40 s (o limite é de 3 a 5 minutos).
**Formato:** cada integrante apresenta **a parte que fez**, conforme as issues da Entrega 4 no Kanban. Cada fala tem de 20 a 35 segundos; a fala escrita é só uma base, podem dizer com as próprias palavras.

**Como gravar:** uma chamada no Google Meet com gravação ligada. Uma pessoa (sugestão: Marília) compartilha a tela e navega pelo app e pelo código; cada integrante liga o microfone na sua vez e fala. Se alguém não puder estar na chamada, essa pessoa grava o seu trecho no Loom e o vídeo é juntado depois.

| Ordem | Tempo | Quem | Parte (issue) | Na tela |
|---|---|---|---|---|
| 1 | 0:00 – 0:25 | Marcelo | Abertura: problema e ODS 2 | Página inicial do app |
| 2 | 0:25 – 0:55 | Marília | Base do front-end e módulo da API (#42) | `services/api.js` |
| 3 | 0:55 – 1:20 | Guilherme | Liberar o CORS no back-end (#43) | `SecurityConfig.java` |
| 4 | 1:20 – 1:45 | Lucas | Tela de cadastro (#45) | Cadastro + `CadastroForm.jsx` |
| 5 | 1:45 – 2:10 | Pedro | Tela de login (#44) | Login + `LoginForm.jsx` |
| 6 | 2:10 – 2:40 | Heloiza | Buscar ONGs próximas (#46) | Painel › ONGs + `BuscaOngs.jsx` |
| 7 | 2:40 – 3:15 | Matheus | Registrar doação (#47) | Painel › Doar + `NovaDoacao.jsx` |
| 8 | 3:15 – 3:45 | Diana | Gerar receita com IA (#48) | Painel › Receita + `GerarReceita.jsx` |
| 9 | 3:45 – 4:15 | Grazielle | Estilo e responsividade (#49) | App no modo celular e tema escuro |
| 10 | 4:15 – 4:40 | Marília | Encerramento | README com o link do vídeo |

---

## Antes de gravar (checklist)
- [ ] PostgreSQL no ar e back-end rodando com `JWT_SECRET`, `DB_PASSWORD` e `GEMINI_API_KEY` (o console termina com `Started NutriConectApplication`).
- [ ] Front-end rodando com `npm run dev` em `http://localhost:5173`.
- [ ] Dados de exemplo: um doador **já cadastrado** (e-mail e senha anotados), pelo menos uma ONG com CEP e alguns ingredientes.
- [ ] Um e-mail **novo** para o Lucas cadastrar ao vivo.
- [ ] No editor, abrir em abas na ordem das falas: `api.js`, `SecurityConfig.java`, `CadastroForm.jsx`, `LoginForm.jsx`, `BuscaOngs.jsx`, `NovaDoacao.jsx`, `DoacaoController.java`, `GerarReceita.jsx`.
- [ ] No navegador, abrir o DevTools (F12) na aba **Network**, com o filtro **Fetch/XHR**: é ali que aparecem os GET e POST de cada tela.
- [ ] Fazer um ensaio rápido e cronometrar.

---

## 1. Marcelo – Abertura: problema e ODS 2 (0:00 – 0:25)
**Na tela:** página inicial do app.

> "Oi, nós somos o grupo do NutriConect. Todo dia, mercados e feiras jogam fora comida que ainda pode ser consumida, enquanto ONGs que combatem a fome não têm doações regulares. O NutriConect conecta esses dois lados e apoia o ODS 2, Fome Zero. Nesta entrega fizemos o front-end em React, com duas telas, e cada um vai mostrar a parte que fez."

## 2. Marília – Base do front-end e módulo da API (0:25 – 0:55)
**Na tela:** `frontend/src/services/api.js`.

> "Eu montei a base do front-end em React com Vite e criei este arquivo, o `api.js`, por onde passam **todas** as chamadas para o back-end. Ele usa o `fetch` do navegador: monta a URL da API, que vem do `.env`, manda os dados em JSON e, se a pessoa já fez login, coloca o token no cabeçalho `Authorization`. Se a API devolve erro, ele pega a mensagem do back-end para mostrar na tela. Assim, cada tela só chama `api('/rota', ...)`."

## 3. Guilherme – Liberar o CORS no back-end (0:55 – 1:20)
**Na tela:** `src/main/java/com/nutriconect/security/SecurityConfig.java`, no método `corsConfigurationSource`.

> "O front roda na porta 5173 e a API na 8080. Para o navegador, são endereços diferentes, então ele bloquearia as requisições. Eu configurei o CORS no Spring Security: aqui liberamos o endereço do front, os métodos GET e POST, e os cabeçalhos `Authorization` e `Content-Type`. Também liberei as requisições `OPTIONS`, que o navegador manda antes para perguntar se pode."

## 4. Lucas – Tela de cadastro (1:20 – 1:45)
**Na tela:** clicar em **Cadastrar**, escolher "Sou doador", preencher e enviar. Depois, `CadastroForm.jsx` na função `cadastrar`.

> "Eu fiz o cadastro de doador e de ONG. Quando a pessoa clica em criar conta, o formulário manda um **POST** para `/api/doadores` ou para `/api/receptores`, dependendo do tipo. Para ONG, também vai o CEP, que o back-end transforma em coordenadas. Se algum campo estiver errado, a API devolve erro 400 e mostramos a mensagem embaixo do campo."

## 5. Pedro – Tela de login (1:45 – 2:10)
**Na tela:** clicar em **Entrar** com o doador já cadastrado, mostrar na aba Network a resposta com o token. Depois, `LoginForm.jsx` na função `enviar`.

> "Eu fiz a tela de login. Ela manda um **POST** para `/api/auth/login` com e-mail e senha. A API confere a senha e devolve um **token JWT**, que aparece aqui na resposta. Nós guardamos esse token no navegador e levamos a pessoa para o Painel. Se a senha estiver errada, mostramos o erro que vem da API."

## 6. Heloiza – Buscar ONGs próximas (2:10 – 2:40)
**Na tela:** Painel › **ONGs perto**: digitar um CEP, ajustar o raio, buscar e clicar em "Escolher" numa ONG. Depois, `BuscaOngs.jsx` na função `buscar`.

> "Minha parte foi a busca de ONGs próximas. A pessoa informa o CEP e o raio em quilômetros, e o app faz um **GET** em `/api/receptores/proximos`, passando o CEP e o raio na URL. A API calcula a distância e devolve as ONGs da mais perto para a mais longe. Aqui eu escolho uma ONG, e ela vira o destino da doação. Se o token vencer, o app volta para o login."

## 7. Matheus – Registrar doação (2:40 – 3:15)
**Na tela:** Painel › **Doar**: adicionar dois ingredientes com quantidade e clicar em registrar; mostrar o POST na aba Network. Depois, `NovaDoacao.jsx` na função `registrarDoacao` e, rapidamente, `DoacaoController.java`.

> "Essa é a funcionalidade principal. Primeiro, a tela faz um **GET** em `/api/ingredientes` para preencher a lista. O doador adiciona os itens e, quando clica em registrar, montamos um JSON com os ingredientes, as quantidades e a ONG escolhida, e mandamos um **POST** para `/api/doacoes`. No back-end, este controller recebe o JSON, valida e salva no banco. Só usuário doador pode doar: se não for, a API devolve 403 e mostramos o aviso."

## 8. Diana – Gerar receita com IA (3:15 – 3:45)
**Na tela:** Painel › **Receita**: marcar ingredientes, gerar e mostrar a receita. Depois, `GerarReceita.jsx` na função `gerar`.

> "Eu fiz a geração de receita com IA. A pessoa marca ou digita os ingredientes que tem, e o app manda um **POST** para `/api/receitas/gerar`. Quem chama a IA do Google Gemini é o nosso back-end, não o navegador: assim a chave da IA fica protegida. A receita volta como texto e aparece aqui. Se a IA estiver ocupada, a API responde 503 e pedimos para tentar de novo."

## 9. Grazielle – Estilo e responsividade (3:45 – 4:15)
**Na tela:** página inicial e Painel; no DevTools, ligar o modo celular (Ctrl+Shift+M) e mostrar a barra de navegação de baixo; trocar para o tema escuro.

> "Minha parte foi o visual. Apliquei o layout do protótipo: cores, fontes, botões e cartões, e o login e o cadastro abrindo numa janela com abas. Também deixei o app responsivo: no celular, o Painel ganha esta barra de navegação embaixo. E tem tema claro e escuro, que segue a configuração do aparelho."

## 10. Marília – Encerramento (4:15 – 4:40)
**Na tela:** README no GitHub.

> "Então, resumindo: duas telas em React, um único módulo que faz as requisições GET e POST com `fetch`, o token JWT para identificar quem está logado e o CORS liberado no Spring Boot. O código está no GitHub, cada parte ligada à sua issue no Kanban, e o link deste vídeo está no README. Obrigada!"

---

## Perguntas que o professor quer ver respondidas
- **Como o botão envia os dados?** Pelo `fetch`, dentro da função `api()` do `api.js` (partes 2, 4, 5 e 7).
- **Qual rota é chamada?** Cada pessoa fala a rota da sua tela (partes 4 a 8).
- **O que volta?** Token no login, lista de ONGs na busca, confirmação na doação e texto da receita.
- **Por que essas tecnologias?** React + Vite para prototipar rápido, `fetch` nativo centralizado, JWT para a API não guardar sessão, CORS por causa das portas diferentes e a chave da IA só no back-end.

## Depois de gravar
1. Conferir se o vídeo tem de 3 a 5 minutos.
2. Publicar no Loom, no YouTube (não listado) ou no Google Drive com acesso "qualquer pessoa com o link".
3. No `README.md`, trocar `COLE_AQUI_O_LINK_DO_VIDEO` pelo link (issue #52).
4. Mover as issues #50 e #51 para **Done** no Kanban.
