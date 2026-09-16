# NutriConect

### 📐 Especificação das Entidades e Métodos (UML)

**Entidade (Classe Base):** Centraliza os dados cadastrais gerais e geolocalização (`id: int`, `razaoSocial: String`, `cnpj: String`, `cep: String`, `telefone: String`, `endereco: String`, `latitude: double`, `longitude: double`).

**Usuario:** Gerencia autenticação e papéis de acesso no sistema (`id: int`, `authId: String`, `nome: String`, `email: String`, `papel: String`) com o método `fazerLogin(): boolean`.

**Doador (Especialização de Entidade):** Representa o doador da plataforma, contendo o atributo de negócio `tipoComercio: Enum`.

**ONG (Especialização de Entidade):** Representa a instituição receptora com `registroSocial: String` e `limiteReservasAtivas: int`.

**Serviços de Localização:** O **ServicoGeocodificacao** faz a conversão via `buscarCoordenadasPorCep(cep: String): Coordenadas`, enquanto o **ServicoGeolocalizacao** realiza o cálculo via `calcularDistanciaHaversine(...)` e a ordenação de feed por proximidade via `ordenarFeedPorProximidade(...)`.
  
## Arquitetura do Sistema (Modelo C4)

Para garantir que todas as frentes de desenvolvimento (Front-end e Back-end) estejam alinhadas e que a integração com os serviços de Inteligência Artificial seja viável e segura, a arquitetura do NutriConect foi documentada utilizando o **Modelo C4**. 

### Diagrama de Contexto (Nível 1)
O diagrama de contexto ilustra a visão macro do NutriConect, mostrando nossos principais usuários (Doadores e Gestores de ONG) e como o nosso sistema interage com plataformas externas para entregar valor.

> **Nota:** O Nível 1 demonstra a relação de atores externos com o ecossistema NutriConect.
> 
> ![Diagrama de Contexto - Nível 1](docs/c4-nivel1.jpeg) 

### Diagrama de Contêineres (Nível 2)

> **Nota:** O Nível 2 deste diagrama faz um "zoom" no nosso sistema, detalhando os grandes blocos de execução, suas tecnologias e como os dados fluem entre eles.
>
> ![Diagrama de Contêineres - Nível 2](docs/c4-nivel2.jpeg) 

### Justificativas Técnicas

As escolhas arquiteturais foram feitas pensando no crescimento sustentável da plataforma e na segurança dos dados:

**Back-end em Java com Spring Boot:** A escolha deste ecossistema garante alta escalabilidade para lidar com um volume crescente de doações e acessos simultâneos. Além disso, oferece um módulo nativo rigoroso de segurança (Spring Security), essencial para proteger dados sensíveis de usuários e organizações.

**Consumo Isolado da Google Gemini API (Inteligência Artificial):** A comunicação com o Google AI Studio para a nossa geração de receitas focada no ODS 2 é feita **exclusivamente pelo nosso servidor Back-end (Java)**. 
Esse isolamento protege nossas chaves de API contra interceptação no lado do cliente e centraliza as regras de negócio. O aplicativo apenas interage com a nossa API, que por sua vez consome a IA e devolve o resultado seguro.
**Banco de Dados Relacional (PostgreSQL/MySQL):** Garante a integridade referencial complexa necessária para vincular de forma consistente os Doadores, ONGs, Estoques e Históricos de Transações.
**Integração com API de Geocodificação:** Vital para o nosso algoritmo de Matching Geográfico por Raio. 
O back-end converte os CEPs cadastrados em coordenadas (Latitude e Longitude), permitindo que o sistema calcule a distância e sugira as ONGs mais próximas de forma eficiente.
