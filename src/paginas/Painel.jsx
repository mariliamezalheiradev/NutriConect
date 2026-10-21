import React, { useState } from 'react';
import BuscaOngs from '../componentes/BuscaOngs';

export function Painel() {
    const [ongSelecionada, setOngSelecionada] = useState(null);

    const handleSelecionarOng = (ong) => {
        setOngSelecionada(ong);
        console.log('ONG selecionada:', ong);
    };

    return (
        <div style={{ padding: '20px' }}>
            <h1>Painel de Doações</h1>

            {/* Componente de busca por CEP e raio */}
            <BuscaOngs onSelecionarOng={handleSelecionarOng} />

            {/* Exibição da ONG escolhida */}
            {ongSelecionada && (
                <div style={{ background: '#e6ffe6', padding: '15px', borderRadius: '5px', marginTop: '15px' }}>
                    <h3>ONG Selecionada para a Doação:</h3>
                    <p><strong>{ongSelecionada.nome}</strong></p>
                    <p>{ongSelecionada.endereco}</p>
                </div>
            )}
        </div>
    );
}

export default Painel;