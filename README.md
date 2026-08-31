# NutriConect

### 📐 Especificação das Entidades e Métodos (UML)

* **Alimento & Estoque:** Mapeia os insumos (`id: int`, `nome: String`, `quantidade: double`, `unidadeMedida: String`, `dataVencimento: Date`, `status: String`) com o método `verificarValidade(): boolean`. A classe **Estoque** (`id: int`) gerencia o fluxo de itens através dos métodos `adicionarItem(alimento: Alimento): void`, `moverParaDoacao(alimento: Alimento): void` e `moverParaReservado(alimento: Alimento): void`.
* **PedidoDoacao:** Controla as solicitações de doação (`id: int`, `dataSolicitacao: Date`, `dataRetiradaPrevista: Date`, `tokenQRCode: String`, `status: String`) e valida a entrega via `validarQRCode(token: String): boolean`.
* **Avaliacao:** Registra o feedback das doações finalizadas (`id: int`, `nota: int`, `comentario: String`).
* **IAGenerativaReceitas:** Módulo utilitário (`apiKey: String`) responsável por sugerir receitas e evitar o desperdício de comida através de `gerarReceitaAproveitamentoTotal(ingredientes: List<String>): String`.
  
### 🖼️ Diagrama de Classes UML

<img width="1370" height="836" alt="diagrama_UML2semestre" src="https://github.com/user-attachments/assets/95108537-80f0-4c71-8c8b-7c4a93809f5f" />


