import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, salvarToken, removerToken } from "../services/api";
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
      // Um token antigo (vencido) faria o back-end recusar o login com 401.
      removerToken();
      const dados = await api("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, senha }),
      });
      if (!dados?.token) {
        throw new Error("Resposta inesperada do servidor. Tente novamente.");
      }
      salvarToken(dados.token);
      navigate("/painel");
    } catch (e) {
      // As mensagens já vêm prontas do back-end ou do módulo da API.
      setErro(e.message);
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
        <p className="mensagem-erro" role="alert">
          {erro}
        </p>
      )}

      <button type="submit" disabled={carregando}>
        {carregando ? "Entrando..." : "Entrar"}
      </button>
    </form>
  );
}
