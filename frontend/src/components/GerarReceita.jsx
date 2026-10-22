import { useState } from "react";

function GerarReceita() {
  const [extras, setExtras] = useState("");

  function gerar() {
    console.log("Ingredientes digitados:", extras);
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

      <button onClick={gerar}>Gerar receita</button>
    </section>
  );
}

export default GerarReceita;