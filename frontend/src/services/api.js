const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

export function salvarToken(token) {
  localStorage.setItem("token", token);
}

export function lerToken() {
  return localStorage.getItem("token");
}

export function removerToken() {
  localStorage.removeItem("token");
}

export async function api(caminho, opcoes = {}) {
  const headers = { "Content-Type": "application/json", ...opcoes.headers };
  const token = lerToken();
  if (token) headers.Authorization = `Bearer ${token}`;

  let resposta;
  try {
    resposta = await fetch(`${API_URL}${caminho}`, { ...opcoes, headers });
  } catch {
    throw new Error("Não foi possível conectar à API. Verifique se o back-end está rodando.");
  }

  let dados = null;
  try {
    dados = await resposta.json();
  } catch {
    // resposta sem corpo
  }

  if (!resposta.ok) {
    const erro = new Error(dados?.mensagem || "Erro ao chamar a API.");
    erro.status = resposta.status;
    erro.campos = dados?.campos;
    throw erro;
  }
  return dados;
}