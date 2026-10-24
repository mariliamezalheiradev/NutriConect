import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, removerToken } from "../services/api";
import "./BuscaOngs.css";

// Baseado no componente da PR #59 (Heloiza), adaptado ao módulo da API e ao tema.
export default function BuscaOngs({ onSelecionarOng }) {
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

  return (
    <section className="busca-ongs">
      <h2>Buscar ONGs próximas</h2>

      <form className="busca-ongs__form" onSubmit={buscar}>
        <div>
          <label htmlFor="cep">CEP</label>
          <input
            id="cep"
            type="text"
            inputMode="numeric"
            placeholder="00000-000"
            value={cep}
            onChange={(e) => setCep(e.target.value)}
            pattern="\d{5}-?\d{3}"
            title="Digite os 8 números do CEP"
            required
          />
        </div>

        <div>
          <label htmlFor="raio">Raio (km)</label>
          <input
            id="raio"
            type="number"
            min="1"
            max="100"
            value={raioKm}
            onChange={(e) => setRaioKm(e.target.value)}
            required
          />
        </div>

        <button type="submit" disabled={carregando}>
          {carregando ? "Buscando..." : "Buscar"}
        </button>
      </form>

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

      {!carregando && buscou && !erro && ongs.length === 0 && (
        <p>Nenhuma ONG encontrada nesse raio.</p>
      )}

      {ongs.length > 0 && (
        <ul className="lista">
          {ongs.map((ong) => (
            <li key={ong.id} className="busca-ongs__item">
              <div>
                <strong>{ong.nome}</strong>
                <p className="busca-ongs__endereco">{ong.endereco}</p>
                <small>Distância: {ong.distanciaKm.toFixed(1)} km</small>
              </div>

              <button type="button" onClick={() => onSelecionarOng?.(ong)}>
                Escolher
              </button>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
