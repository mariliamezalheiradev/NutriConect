# NutriConect

### 📐 Especificação das Entidades e Métodos (UML)

* **Usuario (Classe Abstrata):** Classe pai que centraliza os dados cadastrais básicos (`id: int`, `nome: String`, `email: String`, `telefone: String`, `endereco: String`) e os métodos de autenticação `fazerLogin(): boolean` e `atualizarCadastro(): void`.
* **Doador (Especialização de Usuario):** Representa o doador cadastrado na plataforma. Contém os atributos de negócio `cnpj: String` e `tipoComercio: Enum`, além dos métodos `cadastrarAlimento(): void` e `confirmarRetirada(): void`.
* **ONG (Especialização de Usuario):** Representa a instituição receptora. Contém os atributos `registroSocial: String`, `capArmazenamento: double` e `necessidadeUrgente: String`, além dos métodos `solicitarDoacao(): void` e `gerarReceitaComIA(): void`.

### 🖼️ Diagrama de Classes UML

<img width="859" height="836" alt="DiagramaAtualizado" src="https://github.com/user-attachments/assets/ef381f95-dd6f-42c6-8d3f-707ce8f5c590" />
