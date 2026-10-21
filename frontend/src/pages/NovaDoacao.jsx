import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function NovaDoacao() {
  const navigate = useNavigate();
  const [ingredientes, setIngredientes] = useState([]);
  const [itens, setItens] = useState([]);
  const [ingredienteId, setIngredienteId] = useState('');
  const [quantidade, setQuantidade] = useState('');
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState('');
  const [erro, setErro] = useState('');

  // Proteção da tela e carregamento de ingredientes
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/');
      return;
    }

    api.get('/api/ingredientes')
      .then(response => {
        setIngredientes(response.data);
      })
      .catch(err => {
        if (err.response?.status === 401) {
          navigate('/');
        } else {
          setErro(err.message || 'Erro ao carregar ingredientes.');
        }
      });
  }, [navigate]);

  const handleAdicionarItem = (e) => {
    e.preventDefault();
    if (!ingredienteId || !quantidade || Number(quantidade) <= 0) {
      setErro('Selecione um ingrediente e informe uma quantidade válida.');
      return;
    }

    const idNum = Number(ingredienteId);
    if (itens.some(item => item.ingredienteId === idNum)) {
      setErro('Este ingrediente já foi adicionado.');
      return;
    }

    const ingSelecionado = ingredientes.find(i => i.id === idNum);

    setItens([
      ...itens,
      {
        ingredienteId: idNum,
        nome: ingSelecionado?.nome,
        unidade: ingSelecionado?.unidade,
        quantidade: Number(quantidade)
      }
    ]);
    setIngredienteId('');
    setQuantidade('');
    setErro('');
  };

  const handleRemoverItem = (id) => {
    setItens(itens.filter(item => item.ingredienteId !== id));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (itens.length === 0) {
      setErro('Adicione pelo menos um item à doação.');
      return;
    }

    setLoading(true);
    setMensagem('');
    setErro('');

    try {
      const payload = {
        itens: itens.map(i => ({
          ingredienteId: i.ingredienteId,
          quantidade: i.quantidade
        }))
      };

      const response = await api.post('/api/doacoes', payload);

      if (response.status === 201 || response.status === 200) {
        setMensagem('Doação registrada com sucesso 😄');
        setItens([]);
      }
    } catch (err) {
      const status = err.response?.status;
      if (status === 401) {
        navigate('/');
      } else if (status === 403) {
        setErro('Acesso negado: só quem é DOADOR pode doar.');
      } else {
        setErro(err.message || 'Erro ao registrar doação.');
      }
    } finally {
      setLoading(false);
    }
  };

  const ingredientesDisponiveis = ingredientes.filter(
    ing => !itens.some(item => item.ingredienteId === ing.id)
  );

  return (
    <div className="container">
      <h2>Registrar Nova Doação</h2>

      {mensagem && <div className="success-message">{mensagem}</div>}
      {erro && <div className="error-message">{erro}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="ingrediente">Ingrediente:</label>
          <select
            id="ingrediente"
            value={ingredienteId}
            onChange={(e) => setIngredienteId(e.target.value)}
          >
            <option value="">Selecione um ingrediente</option>
            {ingredientesDisponiveis.map(ing => (
              <option key={ing.id} value={ing.id}>
                {ing.nome} ({ing.unidade})
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="quantidade">Quantidade:</label>
          <input
            id="quantidade"
            type="number"
            value={quantidade}
            onChange={(e) => setQuantidade(e.target.value)}
            placeholder="Digite a quantidade"
          />
        </div>

        <button type="button" onClick={handleAdicionarItem}>
          Adicionar Item
        </button>

        <h3>Itens Adicionados:</h3>
        {itens.length === 0 ? (
          <p>Nenhum item adicionado.</p>
        ) : (
          <ul>
            {itens.map(item => (
              <li key={item.ingredienteId}>
                {item.nome} - {item.quantidade} {item.unidade}
                <button type="button" onClick={() => handleRemoverItem(item.ingredienteId)}>
                  Remover
                </button>
              </li>
            ))}
          </ul>
        )}

        <button type="submit" disabled={loading}>
          {loading ? 'Registrando...' : 'Salvar Doação'}
        </button>
      </form>
    </div>
  );
}