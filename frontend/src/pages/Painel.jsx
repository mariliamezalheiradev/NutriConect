import { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { lerToken, removerToken } from "../services/api";
import Topo from "../components/Topo";
import Icone from "../components/Icone";
import BuscaOngs from "../components/BuscaOngs";

const SECOES = [
  { id: "ongs", titulo: "ONGs perto", rotulo: "ONGs", icone: "pino", texto: "Encontre quem recebe doações na sua região" },
  { id: "doar", titulo: "Nova doação", rotulo: "Doar", icone: "mais", texto: "Registre os ingredientes que vai doar" },
  { id: "receita", titulo: "Gerar receita", rotulo: "Receita", icone: "panela", texto: "A IA sugere um prato com o que você tem" },
];

function Painel() {
  const navigate = useNavigate();
  const [secao, setSecao] = useState("ongs");
  const [ongSelecionada, setOngSelecionada] = useState(null);

  if (!lerToken()) {
    return <Navigate to="/" replace />;
  }

  function sair() {
    removerToken();
    navigate("/", { replace: true });
  }

  return (
    <>
      <Topo logado>
        <span className="acoes">
          <button className="btn sm line" type="button" onClick={sair}>
            Sair
          </button>
        </span>
      </Topo>

      <main>
        <section className="tela painel">
          <div className="wrap">
            <h1>Painel</h1>
            <p className="sub">O que vamos fazer hoje?</p>

            <div className="cartoes">
              {SECOES.map((s) => (
                <button
                  key={s.id}
                  className="cartao-op"
                  type="button"
                  aria-current={secao === s.id}
                  onClick={() => setSecao(s.id)}
                >
                  <span className="ic"><Icone nome={s.icone} /></span>
                  <b>{s.titulo}</b>
                  <span className="t">{s.texto}</span>
                </button>
              ))}
            </div>

            {ongSelecionada && (
              <div className="dest" role="status" style={{ marginBottom: 16 }}>
                <span>
                  Destino: <b>{ongSelecionada.nome}</b> · {ongSelecionada.endereco}
                </span>
                <button className="chip sol" type="button" onClick={() => setOngSelecionada(null)}>
                  Trocar
                </button>
              </div>
            )}

            {secao === "ongs" && (
              <div className="secao">
                <BuscaOngs onSelecionarOng={setOngSelecionada} ongSelecionada={ongSelecionada} />
              </div>
            )}

            {secao === "doar" && (
              <div className="secao">
                <div className="cartao">
                  <h2>Nova doação</h2>
                  <div className="vazio-card">Esta tela ainda está sendo feita pelo grupo.</div>
                </div>
              </div>
            )}

            {secao === "receita" && (
              <div className="secao">
                <div className="cartao">
                  <h2>Gerar receita</h2>
                  <div className="vazio-card">Esta tela ainda está sendo feita pelo grupo.</div>
                </div>
              </div>
            )}
          </div>
        </section>
      </main>

      <nav className="nav" aria-label="Seções do painel">
        {SECOES.map((s) => (
          <button
            key={s.id}
            className="nav-item"
            type="button"
            aria-current={secao === s.id}
            onClick={() => setSecao(s.id)}
          >
            <Icone nome={s.icone} />
            {s.rotulo}
          </button>
        ))}
      </nav>
    </>
  );
}

export default Painel;