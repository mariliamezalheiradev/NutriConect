import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, removerToken } from "../services/api";

function GerarReceita() {
  const navigate = useNavigate();
  const [lista, setLista] = useState([]);
  const [selecionados, setSelecionados] = useState([]);
  const [extras, setExtras] = useState("");
  const [carregando, setCarregando] = useState(false);
  const [receita, setReceita] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    api("/api/ingredientes")
      .then((dados) => setLista(Array.isArray(dados) ? dados : []))
      .catch(() => setLista([]));
  }, []);

  function alternar(nome) {
    setSelecionados((atual) =>
      atual.includes(nome)
        ? atual.filter((i) => i !== nome)
        : [...atual, nome]
    );
  }

  async function gerar() {
    const digitados = extras
      .split(",")
      .map((i) => i.trim())
      .filter(Boolean);
    const ingredientes = [...selecionados, ...digitados];

    if (ingredientes.length === 0) {
      setErro("Escolha ou digite pelo menos um ingrediente.");
      return;
    }

    setErro("");
    setReceita("");
    setCarregando(true);

    try {
      const resposta = await api("/api/receitas/gerar", {
        method: "POST",
        body: JSON.stringify({ ingredientes }),
      });
      const texto =
        typeof resposta === "string" ? resposta : resposta?.receita || "";
      if (texto) {
        setReceita(texto);
      } else {
        setErro("A IA não devolveu uma receita. Tente novamente.");
      }
    } catch (e) {
      if (e.status === 401) {
        // Token vencido ou inválido: volta para o login.
        removerToken();
        navigate("/", { replace: true });
        return;
      }
      if (e.status === 503) {
        setErro("A IA está ocupada, tente de novo em instantes.");
      } else if (e.status === 400) {
        setErro(e.message);
      } else if (!e.status) {
        setErro("Não foi possível conectar à API. Verifique se o back-end está rodando.");
      } else {
        setErro("Não foi possível gerar a receita agora.");
      }
    } finally {
      setCarregando(false);
    }
  }

  return (
    <section>
      <h2>Gerar receita com IA</h2>

      {lista.length > 0 && (
        <div>
          <p>Escolha os ingredientes:</p>
          {lista.map((item) => {
            const nome = typeof item === "string" ? item : item.nome;
            const chave = typeof item === "string" ? item : item.id ?? item.nome;
            return (
              <label key={chave} style={{ display: "block" }}>
                <input
                  type="checkbox"
                  checked={selecionados.includes(nome)}
                  onChange={() => alternar(nome)}
                />{" "}
                {nome}
              </label>
            );
          })}
        </div>
      )}

      <label>
        Outros ingredientes (separados por vírgula):
        <input
          type="text"
          value={extras}
          onChange={(e) => setExtras(e.target.value)}
        />
      </label>

      <button onClick={gerar} disabled={carregando}>
        Gerar receita
      </button>

      {carregando && <p role="status">Gerando receita...</p>}
      {erro && (
        <p className="mensagem-erro" role="alert">
          {erro}
        </p>
      )}
      {receita && <p style={{ whiteSpace: "pre-wrap" }}>{receita}</p>}
    </section>
  );
}

export default GerarReceita;