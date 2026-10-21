import { Link } from 'react-router-dom';

export default function Painel() {
  return (
    <div className="container">
      <h1>Painel Principal</h1>
      <div style={{ marginTop: '20px' }}>
        <Link to="/nova-doacao" className="btn">
          Registrar Nova Doação
        </Link>
      </div>
    </div>
  );
}