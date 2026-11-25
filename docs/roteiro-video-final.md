# Roteiro do vídeo final – Entrega 5 (NutriConect)

**Duração alvo:** cerca de 4 min 45 s (o limite é de 3 a 5 minutos).
**Formato:** cada integrante apresenta **a parte que fez**, conforme as issues da Entrega 5 no Kanban. Cada fala tem de 20 a 30 segundos (o teste ao vivo tem cerca de 55 segundos); a fala escrita é só uma base, podem dizer com as próprias palavras.

**O que o professor quer ver:** a **API hospedada** (com URL pública), um **teste mostrando que os dados são salvos no banco na nuvem** e a **explicação de SaaS**.

**Como gravar:** uma chamada no Google Meet com gravação ligada. Uma pessoa (sugestão: Marília) compartilha a tela e navega pelo app, pelo código e pelos painéis do Render e do Supabase; cada integrante liga o microfone na sua vez e fala. Se alguém não puder estar na chamada, essa pessoa grava o seu trecho no Loom e o vídeo é juntado depois.

| Ordem | Tempo | Quem | Parte (issue) | Na tela |
|---|---|---|---|---|
| 1 | 0:00 – 0:20 | Lucas | Abertura: problema, ODS 2 e o que há de novo | Página inicial do app |
| 2 | 0:20 – 0:45 | Marcelo | Dockerfile e porta para o deploy (#70) | `Dockerfile` + `application.properties` |
| 3 | 0:45 – 1:10 | Guilherme | Banco PostgreSQL no Supabase (#71) | Painel do Supabase |
| 4 | 1:10 – 1:35 | Pedro | Variáveis de ambiente e segredos (#73) | README (Deploy na nuvem) + painel do Render |
| 5 | 1:35 – 2:05 | Marília | Deploy da API no Render (#72) | Render: logs e URL pública |
| 6 | 2:05 – 2:30 | Grazielle | Front-end apontando para a nuvem (#74) | `frontend/.env` + DevTools (Network) |
| 7 | 2:30 – 3:25 | Matheus | Teste ao vivo na nuvem (#75) | App + Table Editor do Supabase |
| 8 | 3:25 – 3:55 | Diana | O NutriConect como SaaS (#76) | README (seção SaaS) |
| 9 | 3:55 – 4:20 | Heloiza | README com a URL da API e o vídeo (#78) | README no GitHub |
| 10 | 4:20 – 4:45 | Marília | Encerramento (#79) | Kanban com as issues concluídas |

---

## Antes de gravar (checklist)
- [ ] **Acordar a API:** no plano grátis do Render, a API dorme depois de 15 minutos sem uso e a primeira requisição demora cerca de 50 segundos. Abra `https://<nome>.onrender.com/api/ingredientes` alguns minutos antes de gravar (deve aparecer um erro 401 em JSON, o que prova que a API está no ar).
- [ ] **Supabase ativo:** o projeto grátis pausa depois de 7 dias sem uso. Confira no painel e, se estiver pausado, clique em **Restore**.
- [ ] Front-end rodando com `npm run dev` em `http://localhost:5173`, com o `frontend/.env` apontando para a URL do Render (`VITE_API_URL`). Reinicie o `npm run dev` depois de trocar o `.env`.
- [ ] No navegador, abrir o DevTools (F12) na aba **Network**, com o filtro **Fetch/XHR**, para mostrar que as chamadas vão para `onrender.com` e não para `localhost:8080`.
- [ ] Painel do **Supabase** aberto no **Table Editor**, já logado, com as tabelas `tb_...` visíveis.
- [ ] Painel do **Render** aberto na aba **Logs** (com a linha `Started NutriConectApplication`) e na aba **Environment**.
- [ ] **Nunca mostrar o valor de nenhum segredo** na gravação: na aba Environment do Render, mostre só os **nomes** das variáveis (`DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `GEMINI_API_KEY`) e não clique em revelar. Não abra o `.env` com senhas, a connection string do Supabase nem a chave do Gemini.
- [ ] Um **e-mail novo** para cadastrar ao vivo (Matheus) e um doador **já cadastrado** com e-mail e senha anotados.
- [ ] Alguns ingredientes cadastrados e, se a receita for mostrada, a chave do Gemini ativa. A receita pode demorar, então ela é opcional neste vídeo.
- [ ] No editor, abrir em abas na ordem das falas: `Dockerfile`, `application.properties`, `frontend/.env.example`.
- [ ] Fazer um ensaio rápido e cronometrar.

---

## 1. Lucas – Abertura: problema, ODS 2 e o que há de novo (0:00 – 0:20)
**Na tela:** página inicial do app.

> "Oi, nós somos o grupo do NutriConect. Todo dia, mercados e feiras jogam fora comida que ainda pode ser consumida, enquanto ONGs que combatem a fome não têm doações regulares. O NutriConect conecta esses dois lados e apoia o ODS 2, Fome Zero. Nesta entrega, a API e o banco de dados saíram dos nossos computadores e foram para a nuvem. Cada um vai mostrar a sua parte."

## 2. Marcelo – Dockerfile e porta para o deploy (0:20 – 0:45)
**Na tela:** `Dockerfile` e, no `application.properties`, a linha `server.port=${PORT:8080}`.

> "Eu preparei a API para rodar na nuvem. O Render roda aplicações Java por meio de Docker, então criei este Dockerfile em duas etapas: na primeira, o Maven compila o projeto; na segunda, uma imagem menor só roda o arquivo .jar. Também mudei a porta: o Render informa em qual porta a API deve escutar por uma variável chamada PORT; no nosso computador, sem essa variável, continua a 8080."

## 3. Guilherme – Banco PostgreSQL no Supabase (0:45 – 1:10)
**Na tela:** painel do Supabase (nome do projeto e região); depois o **Table Editor**, com as tabelas `tb_...` criadas pela aplicação.

> "Eu criei o banco de dados na nuvem, um PostgreSQL gratuito no Supabase, na região de São Paulo. Escolhemos o Supabase porque o banco grátis do Render é apagado depois de 30 dias. Um detalhe importante: usamos o endereço chamado **Session pooler**, e não a conexão direta, porque a direta só funciona por IPv6 e o Render grátis não tem IPv6. As tabelas aparecem aqui sozinhas, porque a API as cria quando conecta."

## 4. Pedro – Variáveis de ambiente e segredos (1:10 – 1:35)
**Na tela:** README, seção **Deploy na nuvem** (a tabela de variáveis). Depois, a aba **Environment** do Render, mostrando **só os nomes**.

> "Eu cuidei dos segredos. A API precisa de cinco variáveis: o endereço, o usuário e a senha do banco, o segredo que assina os tokens de login e a chave da IA do Google. Nenhum desses valores está no código nem no GitHub: tudo o que vai para o repositório fica visível no histórico. Os valores ficam só aqui, no painel do Render, e no README está a tabela dizendo para que serve cada variável e de onde vem."

## 5. Marília – Deploy da API no Render (1:35 – 2:05)
**Na tela:** Render, aba **Logs** (a linha `Started NutriConectApplication`) e a **URL pública**; no navegador, abrir `/api/ingredientes` e mostrar o erro 401 em JSON.

> "Eu fiz o deploy. No Render, conectei o repositório do GitHub, escolhi o Docker e o plano gratuito, e cadastrei as variáveis. Aqui nos logs aparece que a aplicação iniciou, e esta é a **URL pública** da API. Para provar que ela está no ar, abro uma rota protegida: sem token, ela responde 401, o erro que esperamos. No plano grátis, a API dorme depois de 15 minutos sem uso, por isso acordamos ela antes de gravar."

## 6. Grazielle – Front-end apontando para a nuvem (2:05 – 2:30)
**Na tela:** `frontend/.env.example` (mostrar a linha `VITE_API_URL`) e o navegador com o DevTools na aba **Network**, mostrando as requisições indo para `onrender.com`.

> "Eu fiz o front-end conversar com a API na nuvem. O endereço da API vem de uma variável do front, a `VITE_API_URL`, definida no arquivo `.env`. Trocamos de `localhost` para a URL do Render, reiniciamos o front, e aqui na aba Network dá para ver que as chamadas agora vão para `onrender.com`. O app continua rodando no computador; o que mudou é para onde ele fala."

## 7. Matheus – Teste ao vivo na nuvem (2:30 – 3:25)
**Na tela:** (1) o app: **Cadastrar** um doador com e-mail novo; **Entrar**; (2) Painel › **Doar**: adicionar itens e registrar uma doação; (3) Supabase › **Table Editor**: abrir `tb_usuario` e `tb_doacao` (e `tb_item_doacao`) e mostrar as linhas novas.

> "Agora o teste de ponta a ponta, tudo na nuvem. Eu cadastro um doador com um e-mail novo, entro com ele e registro uma doação com dois ingredientes. O app mandou os dados para a API no Render, e a API gravou no Supabase. Aqui no Table Editor do Supabase está a prova: o usuário que acabei de criar na tabela de usuários, e a doação nova com os seus itens. Os dados estão salvos no banco da nuvem."

## 8. Diana – O NutriConect como SaaS (3:25 – 3:55)
**Na tela:** README, seção **NutriConect como SaaS**, com o desenho do fluxo.

> "SaaS quer dizer Software como Serviço: a pessoa usa o sistema pela internet, sem instalar servidor nem banco de dados. No nosso caso, o front-end roda no aparelho do usuário, a API roda no Render, o banco no Supabase e a inteligência artificial no Google Gemini. Isso ajuda uma ONG pequena, que não precisa de computador servidor, de técnico nem de manutenção. E, quando atualizamos a API, a atualização vale para todos os usuários ao mesmo tempo."

## 9. Heloiza – README com a URL da API e o vídeo (3:55 – 4:20)
**Na tela:** topo do README no GitHub, com a URL pública da API e o link do vídeo; e as seções **Tecnologias Utilizadas** e **Funcionalidades** atualizadas.

> "Eu deixei o README pronto para a entrega. No topo, em destaque, estão a URL pública da API e o link deste vídeo. Atualizei a seção de tecnologias, que agora inclui Render, Supabase e Docker, e a de funcionalidades, que passa a dizer que a API e o banco estão hospedados na nuvem. Quem abrir o repositório já encontra tudo."

## 10. Marília – Encerramento (4:20 – 4:45)
**Na tela:** Kanban (GitHub Projects) com as issues da Entrega 5 concluídas e ligadas aos PRs.

> "Então, resumindo: um Dockerfile para rodar a API em qualquer lugar, um banco PostgreSQL no Supabase, a API no Render com URL pública, os segredos guardados fora do código, o front falando com a nuvem e um teste mostrando os dados salvos no banco. Tudo está no GitHub, cada parte ligada à sua issue no Kanban. Obrigada!"

---

## Perguntas que o professor quer ver respondidas
- **A API está hospedada?** Sim, no Render, com URL pública (partes 2, 5 e 6).
- **O banco está na nuvem e os dados são salvos nele?** Sim, no Supabase, e o teste ao vivo mostra a linha nova no Table Editor (partes 3 e 7).
- **O app conversa com a API na nuvem?** Sim, pela `VITE_API_URL`, e a aba Network mostra as chamadas para `onrender.com` (parte 6).
- **O que é SaaS e como o projeto se encaixa?** Parte 8.
- **Por que essas tecnologias?** Docker para a API rodar em qualquer lugar, Render e Supabase por terem plano gratuito, o Supabase por não apagar o banco depois de 30 dias, e os segredos no painel do Render para nunca irem para o GitHub.
- **O que acontece no plano grátis?** A API dorme depois de 15 minutos sem uso e a primeira requisição demora cerca de 50 segundos; o Supabase pausa depois de 7 dias sem uso e se reativa pelo painel.

## Depois de gravar
1. Conferir se o vídeo tem de 3 a 5 minutos e se nenhum segredo apareceu na tela (se apareceu, regrave o trecho e troque o segredo).
2. Publicar no Loom, no YouTube (não listado) ou no Google Drive com acesso "qualquer pessoa com o link".
3. Abrir o link em uma **aba anônima** do navegador para conferir que abre para quem não é do grupo.
4. Passar o link para quem cuida do README (issue #78) e trocar o `COLE_AQUI_O_LINK_DO_VIDEO` do README, se ainda estiver lá.
5. Atualizar o Kanban e marcar o checklist da issue #79.
