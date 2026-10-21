import React, { useState } from 'react';

export function BuscaOngs({ onSelecionarOng }) {
    const [cep, setCep] = useState('');
    const [raioKm, setRaioKm] = useState(10);
    const [ongs, setOngs] = useState([]);
    const [loading, setLoading] = useState(false);
    const [erro, setErro] = useState('');
    const [buscou, setBuscou] = useState(false);

    const handleBuscar = async (e) => {
        e.preventDefault();
        setErro('');
        setOngs([]);
        setLoading(true);
        setBuscou(true);

        const cepLimpo = cep.replace(/\D/g, '');

        try {
            const token = localStorage.getItem('token');

            const response = await fetch(
                `/api/receptores/proximos?cep=${cepLimpo}&raioKm=${raioKm}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                }
            );

            if (response.status === 401) {
                localStorage.removeItem('token');
                window.location.href = '/login';
                return;
            }

            if (response.status === 404) {
                throw new Error('CEP não encontrado. Verifique o número digitado.');
            }

            if (response.status === 400) {
                throw new Error('Raio inválido. Insira um valor numérico válido.');
            }

            if (response.status === 503) {
                throw new Error('Serviço temporariamente indisponível. Tente novamente mais tarde.');
            }

            if (!response.ok) {
                throw new Error('Erro ao buscar ONGs próximas.');
            }

            const data = await response.json();
            setOngs(data);
        } catch (err) {
            setErro(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px', marginBottom: '20px' }}>
            <h2>Buscar ONGs Próximas</h2>

            <form onSubmit={handleBuscar} style={{ display: 'flex', gap: '10px', marginBottom: '15px' }}>
                <div>
                    <label htmlFor="cep">CEP: </label>
                    <input
                        id="cep"
                        type="text"
                        placeholder="00000-000"
                        value={cep}
                        onChange={(e) => setCep(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="raio">Raio (km): </label>
                    <input
                        id="raio"
                        type="number"
                        min="1"
                        value={raioKm}
                        onChange={(e) => setRaioKm(e.target.value)}
                        required
                    />
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Buscando...' : 'Buscar'}
                </button>
            </form>

            {loading && <p>Carregando ONGs próximas...</p>}

            {erro && <p style={{ color: 'red' }}>{erro}</p>}

            {!loading && buscou && !erro && ongs.length === 0 && (
                <p>Nenhuma ONG nesse raio</p>
            )}

            {!loading && ongs.length > 0 && (
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {ongs.map((ong) => (
                        <li
                            key={ong.id}
                            style={{
                                border: '1px solid #eee',
                                padding: '10px',
                                marginBottom: '10px',
                                borderRadius: '4px',
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center',
                            }}
                        >
                            <div>
                                <strong>{ong.nome}</strong>
                                <p style={{ margin: '4px 0' }}>{ong.endereco}</p>
                                <small>Distância: {ong.distancia ? `${ong.distancia.toFixed(1)} km` : 'N/A'}</small>
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

export default BuscaOngs;