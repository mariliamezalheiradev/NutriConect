import { Routes, Route } from "react-router-dom";
import Acesso from "./pages/Acesso";
import Painel from "./pages/Painel";
import NovaDoacao from "./pages/NovaDoacao"; // <--- Importa o teu componente

function App() {
  return (
    <Routes>
      <Route path="/" element={<Acesso />} />
      <Route path="/painel" element={<Painel />} />
      <Route path="/nova-doacao" element={<NovaDoacao />} /> {/* <--- Adiciona a nova rota */}
    </Routes>
  );
}

export default App;