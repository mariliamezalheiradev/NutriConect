# NutriConect

### 📐 Especificação das Entidades e Métodos (UML)

* **Alimento:** Mapeia os insumos doados no sistema (`id: int`, `nome: String`, `quantidade: double`, `unidadeMedida: String`, `dataVencimento: Date`, `statusPerecivel: boolean`), contando com o método `verificarValidade(): boolean` para controle de segurança alimentar.
* **GerenciadorIA:** Módulo utilitário da plataforma (`modeloIA: String`) responsável pelas regras de **Matching Geográfico** via `calcularMelhorMatch(): void` e pela **IA Generativa de Receitas** via `sugerirReceitaNutritiva(): void` para evitar o desperdício de alimentos.

### 🖼️ Diagrama de Classes UML

<img alt="diagramaUML" src="https://github.com/user-attachments/assets/49d9a7d1-8a1d-4b0e-9db0-6394ea2d54f9" />
