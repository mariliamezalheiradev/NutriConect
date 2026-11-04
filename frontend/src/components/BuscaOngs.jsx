import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, removerToken } from "../services/api";
import "./BuscaOngs.css";

export default function BuscaOngs({ onSelecionarOng, ongSelecionada }) {
  const navigate = useNavigate();
  const [cep, setCep] = useState("");
  const [raioKm, setRaioKm] = useState(10);
  const [ongs, setOngs] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");
  const [buscou, setBuscou] = useState(false);

  async function buscar(evento) {
    evento.preventDefault();
    setErro("");
    setOngs([]);
    setCarregando(true);
    setBuscou(true);

    const parametros = new URLSearchParams({
      cep: cep.replace(/\D/g, ""),
      raioKm: String(raioKm),
    });

    try {
      const dados = await api(`/api/receptores/proximos?${parametros}`);
      setOngs(dados ?? []);
    } catch (e) {
      if (e.status === 401) {
        // Token vencido ou inválido: volta para o login.
        removerToken();
        navigate("/", { replace: true });
        return;
      }
      setErro(e.message);
    } finally {
      setCarregando(false);
    }
  }

  const textoVazio =
    buscou && !erro
      ? "Nenhuma ONG encontrada nesse raio."
      : "As ONGs próximas aparecem aqui, depois da busca.";

  return (
    <div className="cartao">
      <div className="col-form">
        <h2>ONGs perto de você</h2>

        <form onSubmit={buscar}>
          <label htmlFor="cep">CEP</label>
          <input
            id="cep"
            type="text"
            inputMode="numeric"
            placeholder="00000-000"
            maxLength={9}
            value={cep}
            onChange={(e) => setCep(e.target.value)}
            pattern="\d{5}-?\d{3}"
            title="Digite os 8 números do CEP"
            required
          />

          <label htmlFor="raio">
            Raio de busca: <span className="saida">{raioKm} km</span>
          </label>
          <input
            id="raio"
            type="range"
            min="1"
            max="100"
            value={raioKm}
            onChange={(e) => setRaioKm(e.target.value)}
          />

          <button className="btn full" type="submit" disabled={carregando} style={{ marginTop: 14 }}>
            {carregando ? "Buscando..." : "Buscar ONGs"}
          </button>
        </form>
      </div>

      <div className="col-saida">
        <h3 className="col-titulo">Resultado</h3>

        {carregando && (
          <p className="busca-ongs__status">
            <span className="carregando" aria-hidden="true" /> Carregando ONGs próximas...
          </p>
        )}

        {erro && (
          <p className="mensagem-erro" role="alert">
            {erro}
          </p>
        )}

        {!carregando && (
          <ul className="lista" aria-live="polite" data-vazio={textoVazio}>
            {ongs.map((ong, i) => {
              const escolhida = ongSelecionada?.id === ong.id;

              return (
                <li key={ong.id} className="item" style={{ animationDelay: `${i * 60}ms` }}>
                  <div>
                    <b>
                      {i + 1} · {ong.nome}
                    </b>
                    <small>
                      {ong.distanciaKm.toFixed(1).replace(".", ",")} km · {ong.endereco}
                    </small>
                  </div>

                  <button
                    type="button"
                    className={"chip" + (escolhida ? " on" : "")}
                    onClick={() => onSelecionarOng?.(ong)}
                  >
                    {escolhida ? "Escolhida" : "Escolher"}
                  </button>
                </li>
              );
            })}
          </ul>
        )}
      </div>
    </div>
  );
}