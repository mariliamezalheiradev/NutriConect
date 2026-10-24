import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

export default function NovaDoacao({ receptorId, onSucesso }) {
  const [ingredientes, setIngredientes] = useState([]);
  const [ingredienteId, setIngredienteId] = useState('');
  const [quantidade, setQuantidade] = useState('');
  const [itens, setItens] = useState([]);
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState('');
  const [erro, setErro] = useState('');

  // 1. Carregar lista de ingredientes
  useEffect(() => {
    async function carregarIngredientes() {
      try {
        const data = await api('/api/ingredientes');
        setIngredientes(data || []);
      } catch (e) {
        setErro('Erro ao carregar lista de ingredientes.');
      }
    }
    carregarIngredientes();
  }, []);

  // Ingrediente selecionado atualmente para mostrar a unidade de medida
  const ingredienteSelecionado = ingredientes.find(
    (i) => String(i.id) === String(ingredienteId)
  );

  // 2. Adicionar item à lista local (impedindo duplicados)
  const handleAdicionarItem = (e) => {
    e.preventDefault();
    setErro('');
    setMensagem('');

    if (!ingredienteId) {
      setErro('Selecione um ingrediente.');
      return;
    }

    const qtdNum = Number(quantidade);
    if (!quantidade || qtdNum <= 0) {
      setErro('Informe uma quantidade válida maior que zero.');
      return;
    }

    // Verificar se ingrediente já foi adicionado
    const jaExiste = itens.some((item) => String(item.ingredienteId) === String(ingredienteId));
    if (jaExiste) {
      setErro('Este ingrediente já foi adicionado à doação.');
      return;
    }

    const novoItem = {
      ingredienteId: Number(ingredienteId),
      nome: ingredienteSelecionado?.nome || 'Ingrediente',
      unidade: ingredienteSelecionado?.unidadeMedida || ingredienteSelecionado?.unidade || '',
      quantidade: qtdNum
    };

    setItens([...itens, novoItem]);
    setIngredienteId('');
    setQuantidade('');
  };

  // 3. Remover item da lista
  const handleRemoverItem = (idParaRemover) => {
    setItens(itens.filter((item) => item.ingredienteId !== idParaRemover));
  };

  // 4. Submeter a doação para a API
  const handleRegistrarDoacao = async () => {
    setLoading(true);
    setMensagem('');
    setErro('');

    if (itens.length === 0) {
      setErro('Adicione pelo menos um item para registrar a doação.');
      setLoading(false);
      return;
    }

    // Estrutura exigida pela Issue: { itens: [{ ingredienteId, quantidade }], receptorId }
    const payload = {
      itens: itens.map((item) => ({
        ingredienteId: item.ingredienteId,
        quantidade: item.quantidade
      })),
      receptorId: receptorId ? Number(receptorId) : null
    };

    try {
      // Formato exigido pelo projeto: api("/api/doacoes", { method: "POST", body: JSON.stringify(payload) })
      await api('/api/doacoes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      });

      setMensagem('Doação registrada');
      setItens([]);
      if (onSucesso) onSucesso();
    } catch (e) {
      const status = e.status;

      if (status === 400) {
        setErro('Erro na requisição: certifique-se de adicionar itens com quantidade maior que zero.');
      } else if (status === 403) {
        setErro('Apenas usuários doadores podem registrar doações.');
      } else if (status === 401) {
        setErro('Sessão expirada. Faça login novamente.');
      } else {
        setErro('Erro ao registrar doação. Tente novamente.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px', border: '1px solid #ddd', borderRadius: '8px', marginTop: '20px' }}>
      <h2>Registrar Doação</h2>

      {mensagem && <p style={{ color: 'green', fontWeight: 'bold' }}>{mensagem}</p>}
      {erro && <p style={{ color: 'red' }}>{erro}</p>}

      {/* Formulário de Seleção */}
      <form onSubmit={handleAdicionarItem} style={{ display: 'flex', gap: '10px', alignItems: 'center', marginBottom: '15px' }}>
        <select
          value={ingredienteId}
          onChange={(e) => setIngredienteId(e.target.value)}
        >
          <option value="">Selecione um ingrediente</option>
          {ingredientes.map((ing) => (
            <option key={ing.id} value={ing.id}>
              {ing.nome} {ing.unidadeMedida ? `(${ing.unidadeMedida})` : ing.unidade ? `(${ing.unidade})` : ''}
            </option>
          ))}
        </select>

        <input
          type="number"
          placeholder="Quantidade"
          value={quantidade}
          onChange={(e) => setQuantidade(e.target.value)}
          min="1"
          step="any"
        />

        {ingredienteSelecionado && (
          <span>{ingredienteSelecionado.unidadeMedida || ingredienteSelecionado.unidade || ''}</span>
        )}

        <button type="submit">Adicionar Item</button>
      </form>

      {/* Lista de Itens Adicionados */}
      {itens.length > 0 && (
        <div style={{ marginBottom: '15px' }}>
          <h3>Itens da Doação:</h3>
          <ul>
            {itens.map((item) => (
              <li key={item.ingredienteId} style={{ marginBottom: '5px' }}>
                {item.nome}: {item.quantidade} {item.unidade} {' '}
                <button type="button" onClick={() => handleRemoverItem(item.ingredienteId)}>
                  Remover
                </button>
              </li>
            ))}
          </ul>

          <button
            onClick={handleRegistrarDoacao}
            disabled={loading}
            style={{ padding: '10px 20px', backgroundColor: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
          >
            {loading ? 'Enviando...' : 'Concluir e Registrar Doação'}
          </button>
        </div>
      )}
    </div>
  );
}