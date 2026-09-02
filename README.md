# NutriConect

Sistema de gestão desenvolvido para apoiar Organizações Não Governamentais (ONGs) no processo de captação, organização e distribuição de alimentos doados por comércios locais. O sistema busca reduzir o desperdício alimentar e fortalecer a capacidade operacional de instituições que atuam diretamente no combate à fome.

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Contextualização: por que o ODS 2](#contextualização-por-que-o-ods-2-fome-zero-e-agricultura-sustentável)
- [O Problema](#o-problema-desperdício-de-alimentos-no-comércio-local-e-vulnerabilidade-das-ongs)
- [Objetivo do Sistema](#objetivo-do-sistema)
- [Público-Alvo](#público-alvo)
- [Inteligência Artificial no Aplicativo](#inteligência-artificial-no-aplicativo)
- [Modelagem do Sistema (UML)](#modelagem-do-sistema-uml)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Instalação](#instalação)
- [Como Usar](#como-usar)
- [Contribuição](#contribuição)
- [Licença](#licença)

---

## Sobre o Projeto

Este repositório contém o código-fonte de um sistema de gestão desenvolvido para apoiar Organizações Não Governamentais (ONGs) no processo de captação, organização e distribuição de alimentos doados por comércios locais. O sistema busca reduzir o desperdício alimentar e fortalecer a capacidade operacional de instituições que atuam diretamente no combate à fome.

## Contextualização: por que o ODS 2 (Fome Zero e Agricultura Sustentável)?

A escolha do Objetivo de Desenvolvimento Sustentável (ODS) 2 — Fome Zero e Agricultura Sustentável, definido pela Agenda 2030 da ONU — se justifica pela conexão direta entre o problema identificado e as metas propostas por esse objetivo.

O ODS 2 não trata apenas da produção de alimentos, mas também da garantia de acesso a alimentação adequada e da redução de perdas ao longo de toda a cadeia produtiva e de distribuição. Entre suas metas, destaca-se o compromisso de reduzir pela metade o desperdício de alimentos per capita mundial nos níveis de varejo e consumo, além de diminuir as perdas na cadeia de produção e abastecimento.

No contexto local, observa-se um paradoxo recorrente: enquanto estabelecimentos comerciais descartam diariamente alimentos ainda próprios para consumo, famílias em situação de insegurança alimentar continuam sem acesso regular à comida. Esse projeto se propõe a atuar exatamente nessa lacuna, funcionando como uma ponte tecnológica entre a oferta (comércios com excedentes) e a demanda (ONGs que atendem populações vulneráveis), contribuindo diretamente para as metas do ODS 2.

## O Problema: Desperdício de Alimentos no Comércio Local e Vulnerabilidade das ONGs

### O desperdício nos comércios locais

Supermercados, feiras, padarias, restaurantes e outros estabelecimentos comerciais descartam, diariamente, grandes volumes de alimentos que ainda estão em condições adequadas para consumo. Esse desperdício ocorre por diversos motivos, entre eles:

- **Vencimento de prazos comerciais**, muitas vezes anteriores à data real de validade;
- **Excesso de produção ou compra**, gerando sobras não comercializadas;
- **Padrões estéticos de mercado**, que descartam alimentos com aparência fora do "padrão", mesmo estando próprios para consumo;
- **Ausência de processos estruturados de doação**, que fazem com que o descarte se torne o caminho mais simples e imediato para o comerciante.

Esse cenário resulta em perdas econômicas para os comércios, impacto ambiental significativo (como emissão de gases de efeito estufa por alimentos em decomposição em aterros) e, sobretudo, no desperdício de um recurso que poderia estar suprindo necessidades básicas da população.

### A vulnerabilidade das ONGs

Do outro lado dessa cadeia, muitas ONGs que atuam na distribuição de alimentos e no combate à fome enfrentam desafios estruturais significativos:

- **Dependência de doações irregulares**, sem previsibilidade de volume, tipo ou frequência;
- **Falta de recursos tecnológicos** para gerenciar cadastros de beneficiários, estoque de doações e logística de distribuição;
- **Processos manuais e descentralizados**, que dificultam o registro de informações, a comunicação com doadores e a tomada de decisão;
- **Limitação de equipe e infraestrutura**, o que reduz a capacidade de resposta rápida a excedentes disponíveis nos comércios.

Essa combinação de fatores — desperdício de um lado e escassez de recursos de gestão do outro — evidencia a necessidade de uma solução que organize, sistematize e facilite essa conexão, otimizando o aproveitamento de alimentos e fortalecendo a atuação das ONGs junto às comunidades que atendem.

## Objetivo do Sistema

Diante desse cenário, o sistema desenvolvido neste projeto tem como objetivo oferecer às ONGs uma ferramenta de gestão que permita:

- Registrar e organizar doações recebidas de comércios locais;
- Gerenciar estoque de alimentos de forma simples e acessível;
- Acompanhar a distribuição para beneficiários;
- Facilitar a comunicação e o histórico de parcerias com doadores.

Com isso, busca-se reduzir o desperdício de alimentos, fortalecer a capacidade operacional das ONGs e contribuir de forma concreta para as metas do ODS 2 no contexto local.

## Público-Alvo

Dentro do projeto NutriConect, existem dois principais públicos-alvo: os **Doadores** e os **Receptores**. Embora possuam necessidades e objetivos diferentes, ambos estão diretamente relacionados à proposta do aplicativo, que busca criar uma ponte entre alimentos que poderiam ser desperdiçados e pessoas ou instituições que necessitam desses recursos.

### Doadores

O primeiro público-alvo é formado pelos doadores, que podem ser comerciantes, feirantes, supermercados locais, pequenos estabelecimentos e também moradores que possuam alimentos próprios para consumo que não serão mais utilizados. Esses usuários terão um papel fundamental no funcionamento do NutriConect, pois serão responsáveis por disponibilizar os alimentos que poderão ser destinados a instituições e comunidades em situação de vulnerabilidade social.

Entre as principais características desse público está a necessidade de encontrar uma forma simples, rápida e segura de realizar doações, evitando que alimentos ainda próprios para consumo sejam descartados. No caso de comerciantes e estabelecimentos, por exemplo, podem existir produtos próximos da data de validade, alimentos que não atendem mais aos padrões comerciais de aparência ou itens que não foram vendidos dentro do período esperado, mas que ainda apresentam condições adequadas para consumo.

O aplicativo pretende facilitar esse processo, permitindo que o doador informe quais alimentos estão disponíveis, a quantidade, as condições para retirada e outras informações necessárias. Dessa forma, além de contribuir para a redução do desperdício, o doador poderá participar de uma ação social de maneira prática e organizada.

### Receptores

O segundo público-alvo é formado pelos receptores, que incluem gestores de Organizações Não Governamentais (ONGs), abrigos, cozinhas comunitárias, projetos sociais e outras instituições que atendam pessoas em situação de vulnerabilidade social.

Esse público enfrenta, em muitos casos, dificuldades relacionadas à falta de recursos financeiros para a aquisição regular de alimentos, além dos desafios para encontrar doações e organizar sua logística de recebimento. Por esse motivo, o NutriConect busca oferecer uma maneira mais direta de localizar alimentos disponíveis para doação e estabelecer contato com possíveis doadores.

Para os receptores, o aplicativo poderá facilitar a identificação de oportunidades de doação de acordo com suas necessidades, localização e capacidade de recebimento. Isso contribui para que os alimentos sejam destinados de maneira mais eficiente, reduzindo o tempo entre a oferta e o recebimento da doação.

### Relação entre os públicos

A principal característica do público-alvo do NutriConect é justamente a conexão entre esses dois grupos. De um lado, existem pessoas e estabelecimentos que possuem alimentos excedentes e que, sem uma alternativa adequada, poderiam acabar sendo descartados. Do outro, existem instituições que necessitam desses alimentos para atender pessoas em situação de vulnerabilidade.

Nesse contexto, o NutriConect atua como uma ponte entre quem pode doar e quem precisa receber, tornando o processo mais organizado, acessível e eficiente. A proposta não é apenas reduzir o desperdício de alimentos, mas também fortalecer a colaboração entre comerciantes, moradores e instituições sociais, transformando um problema cotidiano em uma oportunidade de gerar impacto positivo na comunidade.

## Inteligência Artificial no Aplicativo

O aplicativo utiliza duas abordagens complementares de Inteligência Artificial para combater o desperdício de alimentos e promover a segurança alimentar:

### 1. Algoritmo de Matching Geográfico por Raio (Localização de ONGs)

Mapeia e conecta pessoas que precisam de alimento às ONGs parceiras mais próximas para retirada imediata:

- **Mapeamento:** Cadastro geolocalizado de ONGs parceiras com pontos de coleta e distribuição.
- **Busca por Proximidade:** Identificação da localização do usuário e cálculo do raio de distância até os pontos de distribuição.
- **Recomendação Inteligente:** Priorização das ONGs mais próximas com itens disponíveis para facilitar o deslocamento.

### 2. IA Generativa de Receitas (Visão Computacional e Culinária)

Ajuda o usuário a aproveitar ingredientes em casa antes que vençam:

- **Reconhecimento por Foto:** Identificação automática dos alimentos a partir de uma foto tirada pelo usuário.
- **Geração de Receitas:** Sugestões personalizadas de preparo prático e rápido com base nos ingredientes detectados.
- **Aproveitamento Total:** Foco em consumo consciente e desperdício zero.

## Modelagem do Sistema (UML)

### 📐 Especificação das Entidades e Métodos (UML)

* **Entidade (Classe Base):** Centraliza os dados cadastrais gerais e geolocalização (`id: int`, `razaoSocial: String`, `cnpj: String`, `cep: String`, `telefone: String`, `endereco: String`, `latitude: double`, `longitude: double`).
* **Usuario:** Gerencia autenticação e papéis de acesso no sistema (`id: int`, `authId: String`, `nome: String`, `email: String`, `papel: String`) com o método `fazerLogin(): boolean`.
* **Doador (Especialização de Entidade):** Representa o doador da plataforma, contendo o atributo de negócio `tipoComercio: Enum`.
* **ONG (Especialização de Entidade):** Representa a instituição receptora com `registroSocial: String` e `limiteReservasAtivas: int`.
* **Serviços de Localização:** O **ServicoGeocodificacao** faz a conversão via `buscarCoordenadasPorCep(cep: String): Coordenadas`, enquanto o **ServicoGeolocalizacao** realiza o cálculo via `calcularDistanciaHaversine(...)` e a ordenação de feed por proximidade via `ordenarFeedPorProximidade(...)`.
  
### 🖼️ Diagrama de Classes UML

<img width="1370" height="836" alt="diagrama_UML2semestre" src="https://github.com/user-attachments/assets/199b9122-330a-4605-a348-d3095d034390" />


## Funcionalidades

_(Seção a ser detalhada)_

## Tecnologias Utilizadas

_(Seção a ser detalhada)_

## Instalação

_(Seção a ser detalhada)_

## Como Usar

_(Seção a ser detalhada)_

## Contribuição

_(Seção a ser detalhada)_

## Licença

_(Seção a ser detalhada)_
