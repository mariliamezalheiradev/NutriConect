# NutriConect

### 📐 Especificação das Entidades e Métodos (UML)

* **Entidade (Classe Base):** Centraliza os dados cadastrais gerais e geolocalização (`id: int`, `razaoSocial: String`, `cnpj: String`, `cep: String`, `telefone: String`, `endereco: String`, `latitude: double`, `longitude: double`).
* **Usuario:** Gerencia autenticação e papéis de acesso no sistema (`id: int`, `authId: String`, `nome: String`, `email: String`, `papel: String`) com o método `fazerLogin(): boolean`.
* **Doador (Especialização de Entidade):** Representa o doador da plataforma, contendo o atributo de negócio `tipoComercio: Enum`.
* **ONG (Especialização de Entidade):** Representa a instituição receptora com `registroSocial: String` e `limiteReservasAtivas: int`.
* **Serviços de Localização:** O **ServicoGeocodificacao** faz a conversão via `buscarCoordenadasPorCep(cep: String): Coordenadas`, enquanto o **ServicoGeolocalizacao** realiza o cálculo via `calcularDistanciaHaversine(...)` e a ordenação de feed por proximidade via `ordenarFeedPorProximidade(...)`.
  
### 🖼️ Diagrama de Classes UML

<img width="859" height="836" alt="DiagramaAtualizado" src="https://github.com/user-attachments/assets/ef381f95-dd6f-42c6-8d3f-707ce8f5c590" />
