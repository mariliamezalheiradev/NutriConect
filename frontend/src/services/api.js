const API_URL = import.meta.env.VITE_API_URL;

export async function api(path, options = {}) {
  const resposta = await fetch(`${API_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  return resposta;
}