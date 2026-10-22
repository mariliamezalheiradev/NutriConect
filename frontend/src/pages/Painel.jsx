import { Navigate } from "react-router-dom";
import { lerToken } from "../services/api";

function Painel() {
  if (!lerToken()) {
    return <Navigate to="/" replace />;
  }

  return (
    <main className="container">
      <h1>Painel</h1>
      <p>Painel do usuário. Será feito nas issues 5, 6 e 7.</p>
    </main>
  );
}

export default Painel;