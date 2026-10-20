import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, salvarToken } from "../services/api";
import "./LoginForm.css";

export default function LoginForm() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function enviar(evento) {
    evento.preventDefault();
    setErro("");
    setCarregando(true);

    try {
      const dados = await api("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, senha }),
      });
      salvarToken(dados.token);
      navigate("/painel");
    } catch (e) {
      if (e.status === 401) {
        setErro("E-mail ou senha incorretos.");
      } else if (e.status === undefined) {
        setErro("Não foi possível conectar à API. Verifique se o back-end está rodando.");
      } else {
        setErro(e.message);
      }
    } finally {
      setCarregando(false);
    }
  }

  return (
    <form className="login-form" onSubmit={enviar}>
      <label htmlFor="email">E-mail</label>
      <input
        id="email"
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        autoComplete="email"
        required
      />

      <label htmlFor="senha">Senha</label>
      <input
        id="senha"
        type="password"
        value={senha}
        onChange={(e) => setSenha(e.target.value)}
        autoComplete="current-password"
        required
      />

      {erro && (
        <p className="login-form__erro" role="alert">
          {erro}
        </p>
      )}

      <button type="submit" disabled={carregando}>
        {carregando ? "Entrando..." : "Entrar"}
      </button>
    </form>
  );
}