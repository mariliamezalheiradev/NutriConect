import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, removerToken } from "../services/api";
import "./NovaDoacao.css";

export default function NovaDoacao({ receptorId, onSucesso }) {
  const navigate = useNavigate();
  const [ingredientes, setIngredientes] = useState([]);
  const [ingredienteId, setIngredienteId] = useState("");
  const [quantidade, setQuantidade] = useState("");
  const [itens, setItens] = useState([]);
  const [carregando, setCarregando] = useState(false);
  const [mensagem, setMensagem] = useState("");
  const [erro, setErro] = useState("");

  // Token vencido ou inválido: volta para o login.
  const sessaoExpirada = useCallback(() => {
    removerToken();
    navigate("/", { replace: true });
  }, [navigate]);

  useEffect(() => {
    async function carregarIngredientes() {
      try {
        const dados = await api("/api/ingredientes");
        setIngredientes(dados || []);
      } catch (e) {
        if (e.status === 401) {
          sessaoExpirada();
          return;
        }
        setErro("Erro ao carregar a lista de ingredientes.");
      }
    }
    carregarIngredientes();
  }, [sessaoExpirada]);

  const ingredienteSelecionado = ingredientes.find(
    (i) => String(i.id) === String(ingredienteId)
  );

  function adicionarItem(evento) {
    evento.preventDefault();
    setErro("");
    setMensagem("");

    if (!ingredienteId) {
      setErro("Selecione um ingrediente.");
      return;
    }

    const qtdNum = Number(quantidade);
    if (!quantidade || qtdNum <= 0) {
      setErro("Informe uma quantidade válida maior que zero.");
      return;
    }

    const jaExiste = itens.some(
      (item) => String(item.ingredienteId) === String(ingredienteId)
    );
    if (jaExiste) {
      setErro("Este ingrediente já foi adicionado à doação.");
      return;
    }

    setItens([
      ...itens,
      {
        ingredienteId: Number(ingredienteId),
        nome: ingredienteSelecionado?.nome || "Ingrediente",
        unidade: ingredienteSelecionado?.unidade || "",
        quantidade: qtdNum,
      },
    ]);
    setIngredienteId("");
    setQuantidade("");
  }

  function removerItem(idParaRemover) {
    setItens(itens.filter((item) => item.ingredienteId !== idParaRemover));
  }

  async function registrarDoacao() {
    setErro("");
    setMensagem("");

    if (itens.length === 0) {
      setErro("Adicione pelo menos um item para registrar a doação.");
      return;
    }

    setCarregando(true);

    const payload = {
      itens: itens.map((item) => ({
        ingredienteId: item.ingredienteId,
        quantidade: item.quantidade,
      })),
      receptorId: receptorId ? Number(receptorId) : null,
    };

    try {
      await api("/api/doacoes", {
        method: "POST",
        body: JSON.stringify(payload),
      });

      setMensagem("Doação registrada com sucesso!");
      setItens([]);
      if (onSucesso) onSucesso();
    } catch (e) {
      if (e.status === 401) {
        sessaoExpirada();
        return;
      }
      if (e.status === 403) {
        setErro("Apenas usuários doadores podem registrar doações.");
      } else if (!e.status) {
        setErro(e.message);
      } else if (e.status === 400 || e.status === 422 || e.status === 404) {
        // Mensagem do back-end (validação, regra de negócio, item inexistente).
        setErro(e.message);
      } else {
        setErro("Erro ao registrar a doação. Tente novamente.");
      }
    } finally {
      setCarregando(false);
    }
  }

  return (
    <section className="nova-doacao">
      <h2>Registrar doação</h2>

      {mensagem && (
        <p className="mensagem-sucesso" role="status">
          {mensagem}
        </p>
      )}
      {erro && (
        <p className="mensagem-erro" role="alert">
          {erro}
        </p>
      )}

      {!receptorId && (
        <p role="status">
          Nenhuma ONG selecionada: a doação será registrada sem ONG de destino.
          Para escolher uma, use a busca acima.
        </p>
      )}

      <form onSubmit={adicionarItem}>
        <label htmlFor="doacao-ingrediente">Ingrediente</label>
        <select
          id="doacao-ingrediente"
          value={ingredienteId}
          onChange={(e) => setIngredienteId(e.target.value)}
        >
          <option value="">Selecione um ingrediente</option>
          {ingredientes.map((ing) => (
            <option key={ing.id} value={ing.id}>
              {ing.nome}
              {ing.unidade ? ` (${ing.unidade})` : ""}
            </option>
          ))}
        </select>

        <label htmlFor="doacao-quantidade">
          Quantidade{ingredienteSelecionado?.unidade
            ? ` (${ingredienteSelecionado.unidade})`
            : ""}
        </label>
        <input
          id="doacao-quantidade"
          type="number"
          inputMode="decimal"
          min="0.01"
          step="any"
          value={quantidade}
          onChange={(e) => setQuantidade(e.target.value)}
        />

        <button type="submit">Adicionar item</button>
      </form>

      {itens.length > 0 && (
        <div>
          <h3>Itens da doação</h3>
          <ul className="lista">
            {itens.map((item) => (
              <li key={item.ingredienteId} className="nova-doacao__item">
                <span>
                  {item.nome}: {item.quantidade} {item.unidade}
                </span>
                <button
                  type="button"
                  onClick={() => removerItem(item.ingredienteId)}
                >
                  Remover
                </button>
              </li>
            ))}
          </ul>

          <button onClick={registrarDoacao} disabled={carregando}>
            {carregando ? "Enviando..." : "Concluir e registrar doação"}
          </button>
        </div>
      )}
    </section>
  );
}
