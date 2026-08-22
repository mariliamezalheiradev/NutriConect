# NutriConect
  
  Usuário
--------------------------------
- id: int
- nome: String
- email: String
- telefone: String
- endereco: String
--------------------------------
+ fazerLogin(): boolean
+ atualizarCadastro(): void


Doador extends Usuário
--------------------------------
- cnpj: String
- tipoComercio: String
--------------------------------
+ cadastrarAlimento(): void
+ confirmarRetirada(): void


ONG extends Usuário
--------------------------------
- registroSocial: String
- capArmazenamento: double
- necessidadeUrgente: String
--------------------------------
+ solicitarDoacao(): void
+ gerarReceitaComIA(): void


Alimento
--------------------------------
- id: int
- nome: String
- quantidade: double
- unidadeMedida: String
- dataVencimento: Date
- statusPerecivel: boolean
--------------------------------
+ verificarValidade(): boolean


Doação
--------------------------------
- id: int
- dataDoacao: Date
- status: String
--------------------------------
+ registrarDoacao(): void
+ confirmarRetirada(): void


GerenciadorIA
--------------------------------
- modeloIA: String
--------------------------------
+ calcularMelhorMatch(): void
+ sugerirReceitaNutritiva(): void
