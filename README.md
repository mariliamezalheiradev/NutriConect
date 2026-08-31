# NutriConect

### 📐 Especificação das Entidades e Métodos (UML)

* **Alimento & Estoque:** Mapeia os insumos (`id: int`, `nome: String`, `quantidade: double`, `unidadeMedida: String`, `dataVencimento: Date`, `status: String`) com o método `verificarValidade(): boolean`. A classe **Estoque** (`id: int`) gerencia o fluxo de itens através dos métodos `adicionarItem(alimento: Alimento): void`, `moverParaDoacao(alimento: Alimento): void` e `moverParaReservado(alimento: Alimento): void`.
* **PedidoDoacao:** Controla as solicitações de doação (`id: int`, `dataSolicitacao: Date`, `dataRetiradaPrevista: Date`, `tokenQRCode: String`, `status: String`) e valida a entrega via `validarQRCode(token: String): boolean`.
* **Avaliacao:** Registra o feedback das doações finalizadas (`id: int`, `nota: int`, `comentario: String`).
* **IAGenerativaReceitas:** Módulo utilitário (`apiKey: String`) responsável por sugerir receitas e evitar o desperdício de comida através de `gerarReceitaAproveitamentoTotal(ingredientes: List<String>): String`.
  
### 🖼️ Diagrama de Classes UML

<img width="859" height="836" alt="DiagramaAtualizado" src="https://github.com/user-attachments/assets/4567fde9-d02f-402b-b828-0170973a18ae" />

