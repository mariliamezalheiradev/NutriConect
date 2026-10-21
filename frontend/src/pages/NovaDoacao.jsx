import React, { useState } from 'react';

export default function NovaDoacao() {
  const [doacao, setDoacao] = useState({
    item: '',
    quantidade: '',
    unidade: 'kg',
    validade: ''
  });

  const handleChange = (e) => {
    setDoacao({ ...doacao, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Doação registada:', doacao);
    alert('Doação registada com sucesso!');
  };

  return (
    <div style={{ padding: '20px', maxWidth: '500px', margin: '0 auto' }}>
      <h2>Registrar Nova Doação</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '15px' }}>
          <label style={{ display: 'block', marginBottom: '5px' }}>Item / Alimento:</label>
          <input
            type="text"
            name="item"
            value={doacao.item}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </div>

        <div style={{ marginBottom: '15px' }}>
          <label style={{ display: 'block', marginBottom: '5px' }}>Quantidade:</label>
          <input
            type="number"
            name="quantidade"
            value={doacao.quantidade}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </div>

        <div style={{ marginBottom: '15px' }}>
          <label style={{ display: 'block', marginBottom: '5px' }}>Data de Validade:</label>
          <input
            type="date"
            name="validade"
            value={doacao.validade}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </div>

        <button type="submit" style={{ padding: '10px 20px', background: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
          Salvar Doação
        </button>
      </form>
    </div>
  );
}