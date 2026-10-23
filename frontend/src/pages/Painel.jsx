import React, { useState } from 'react';
import BuscaOngs from '../components/BuscaOngs';

export default function Painel() {
    const [ongSelecionada, setOngSelecionada] = useState(null);

    const handleSelecionarOng = (ong) => {
        setOngSelecionada(ong);
        console.log('ONG selecionada:', ong);
    };

    return (
        <div style={{ padding: '20px' }}>
            <h1>Painel de Doações</h1>
            {ongSelecionada && (
                <p style={{ color: 'green' }}>
                    <strong>ONG Selecionada:</strong> {ongSelecionada.nome || ongSelecionada.razaoSocial}
                </p>
            )}
            <BuscaOngs onSelecionarOng={handleSelecionarOng} />
        </div>
    );
}