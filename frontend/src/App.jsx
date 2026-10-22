import { Routes, Route } from "react-router-dom";
import Acesso from "./pages/Acesso";
import Painel from "./pages/Painel";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Acesso />} />
      <Route path="/painel" element={<Painel />} />
    </Routes>
  );
}

export default App;