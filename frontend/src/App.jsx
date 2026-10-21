import { Routes, Route } from "react-router-dom";
import Acesso from "./pages/Acesso";
import Painel from "./pages/Painel";
import NovaDoacao from "./components/NovaDoacao";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Acesso />} />
      <Route path="/painel" element={<Painel />} />
      <Route path="/nova-doacao" element={<NovaDoacao />} />
    </Routes>
  );
}

export default App;