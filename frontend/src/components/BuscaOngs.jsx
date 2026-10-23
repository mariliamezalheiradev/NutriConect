import React, { useState } from 'react';
import { api } from '../services/api';

export default function BuscaOngs({ onSelecionarOng }) {
    const [cep, setCep] = useState('');
    const [raioKm, setRaioKm] = useState(10);
    const [ongs, setOngs] = useState([]);
    const [loading, setLoading] = useState(false);
    const [mensagem, setMensagem] = useState('');

    const handleBuscar = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMensagem('');
        setOngs([]);

        try {
            const response = await api.get('/api/receptores/proximos', {
                params: { cep, raioKm }
            });

            if (response.data.length === 0) {
                setMensagem('Nenhuma ONG nesse raio');
            } else {
                setOngs(response.data);
            }
        } catch (error) {
            const status = error.response?.status;
            if (status === 400) {
                setMensagem('Raio inválido.');
            } else if (status === 404) {
                setMensagem('CEP não encontrado.');
            } else if (status === 401) {
                setMensagem('Sessão expirada. Redirecionando para o login...');
                window.location.href = '/';
            } else if (status === 503) {
                setMensagem('Serviço indisponível no momento. Tente novamente mais tarde.');
            } else {
                setMensagem('Erro ao buscar ONGs. Tente novamente.');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px', marginTop: '20px' }}>
            <h2>Buscar ONGs Próximas</h2>
            <form onSubmit={handleBuscar} style={{ display: 'flex', gap: '10px', marginBottom: '15px' }}>
                <input
                    type="text"
                    placeholder="Digite o CEP"
                    value={cep}
                    onChange={(e) => setCep(e.target.value)}
                    required
                />
                <input
                    type="number"
                    placeholder="Raio (km)"
                    value={raioKm}
                    onChange={(e) => setRaioKm(e.target.value)}
                    min="1"
                    required
                />
                <button type="submit" disabled={loading}>
                    {loading ? 'Buscando...' : 'Buscar'}
                </button>
            </form>

            {mensagem && <p style={{ color: 'red' }}>{mensagem}</p>}

            {ongs.length > 0 && (
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {ongs.map((ong) => (
                        <li key={ong.id || ong.cnpj} style={{ borderBottom: '1px solid #eee', padding: '10px 0', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <div>
                                <strong>{ong.nome || ong.razaoSocial}</strong>
                                <p style={{ margin: '4px 0', fontSize: '14px', color: '#555' }}>
                                    {ong.endereco || `${ong.logradouro || ''}, ${ong.numero || ''}`}
                                </p>
                                <small>{ong.distanciaKm ? `${ong.distanciaKm} km de distância` : ''}</small>
                            </div>
                            <button onClick={() => onSelecionarOng && onSelecionarOng(ong)}>
                                Escolher
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}