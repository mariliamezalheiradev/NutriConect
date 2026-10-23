import { useState } from "react";

function GerarReceita() {
  const [extras, setExtras] = useState("");
  const [carregando, setCarregando] = useState(false);
  const [receita, setReceita] = useState("");

  function gerar() {
    setReceita("");
    setCarregando(true);

    // Simulação: depois trocamos pela chamada de verdade à API
    setTimeout(() => {
      setReceita("Aqui vai aparecer a receita gerada pela IA.");
      setCarregando(false);
    }, 2000);
  }

  return (
    <section>
      <h2>Gerar receita com IA</h2>

      <label>
        Ingredientes (separados por vírgula):
        <input
          type="text"
          value={extras}
          onChange={(e) => setExtras(e.target.value)}
        />
      </label>

      <button onClick={gerar} disabled={carregando}>
        Gerar receita
      </button>

      {carregando && <p>Gerando receita...</p>}
      {receita && <p>{receita}</p>}
    </section>
  );
}

export default GerarReceita;