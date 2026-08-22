# NutriConect

### 📐 Especificação das Entidades e Métodos (UML)

* **Alimento:** Mapeia os insumos doados no sistema (`id: int`, `nome: String`, `quantidade: double`, `unidadeMedida: String`, `dataVencimento: Date`, `statusPerecivel: boolean`), contando com o método `verificarValidade(): boolean` para controle de segurança alimentar.
* **GerenciadorIA:** Módulo utilitário da plataforma (`modeloIA: String`) responsável pelas regras de **Matching Geográfico** via `calcularMelhorMatch(): void` e pela **IA Generativa de Receitas** via `sugerirReceitaNutritiva(): void` para evitar o desperdício de alimentos.

### 🖼️ Diagrama de Classes UML

<img width="859" height="836" alt="DiagramaAtualizado" src="https://github.com/user-attachments/assets/4567fde9-d02f-402b-b828-0170973a18ae" />

