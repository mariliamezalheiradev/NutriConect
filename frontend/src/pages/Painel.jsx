import { useState } from "react";
import { Navigate } from "react-router-dom";
import { lerToken } from "../services/api";
import GerarReceita from "../components/GerarReceita";
import BuscaOngs from "../components/BuscaOngs";

function Painel() {
  const [ongSelecionada, setOngSelecionada] = useState(null);

  if (!lerToken()) {
    return <Navigate to="/" replace />;
  }

  return (
    <main className="container">
      <h1>Painel</h1>
      <p>Painel do usuário. Será feito nas issues 5, 6 e 7.</p>
    <GerarReceita />

      <BuscaOngs onSelecionarOng={setOngSelecionada} />

      {ongSelecionada && (
        <div className="mensagem-sucesso" role="status">
          <h3>ONG selecionada para a doação</h3>
          <p>
            <strong>{ongSelecionada.nome}</strong>
          </p>
          <p>{ongSelecionada.endereco}</p>
        </div>
      )}
    </main>
  );
}

export default Painel;
