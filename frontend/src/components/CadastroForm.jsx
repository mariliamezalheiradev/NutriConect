import { useState } from "react";
import { api } from "../services/api";
import "./CadastroForm.css";

const FORM_VAZIO = {
  nome: "",
  email: "",
  senha: "",
  telefone: "",
  documento: "",
  cnpj: "",
  endereco: "",
  cep: "",
};

function CadastroForm() {
  const [tipo, setTipo] = useState("doador");
  const [form, setForm] = useState(FORM_VAZIO);
  const [mensagem, setMensagem] = useState(null);
  const [erros, setErros] = useState({});
  const [carregando, setCarregando] = useState(false);

  function atualizarCampo(evento) {
    setForm({
      ...form,
      [evento.target.name]: evento.target.value,
    });
  }

  function escolherTipo(novoTipo) {
    setTipo(novoTipo);
    setMensagem(null);
    setErros({});
  }

  async function cadastrar(evento) {
    evento.preventDefault();
    setMensagem(null);
    setErros({});
    setCarregando(true);

    const dados = {
      nome: form.nome,
      email: form.email,
      senha: form.senha,
      telefone: form.telefone,
    };

    if (tipo === "doador") {
      dados.documento = form.documento;
    } else {
      dados.cnpj = form.cnpj;
      dados.endereco = form.endereco;
      dados.cep = form.cep.replace(/\D/g, "");
    }

    try {
      await api(tipo === "doador" ? "/api/doadores" : "/api/receptores", {
        method: "POST",
        body: JSON.stringify(dados),
      });

      setMensagem({
        tipo: "sucesso",
        texto: "Conta criada com sucesso! Agora entre com seu e-mail e senha.",
      });
      setForm(FORM_VAZIO);
    } catch (erro) {
      if (erro.status === 400) {
        setErros(erro.campos || {});
        setMensagem({ tipo: "erro", texto: "Confira os campos informados." });
      } else {
        // 404 (CEP), 422 (ex.: e-mail já cadastrado), 503 e demais: a mensagem vem do back-end.
        setMensagem({
          tipo: "erro",
          texto: erro.message || "Erro ao criar a conta.",
        });
      }
    } finally {
      setCarregando(false);
    }
  }

  function erroDo(campo) {
    const erro = erros[campo];
    if (!erro) return null;

    return (
      <small className="cadastro__erro-campo" role="alert">
        {Array.isArray(erro) ? erro.join(", ") : erro}
      </small>
    );
  }

  return (
    <section>
      <h2>Criar conta</h2>

      <div className="cadastro__tipos" role="group" aria-label="Tipo de cadastro">
        <button
          type="button"
          aria-pressed={tipo === "doador"}
          disabled={carregando}
          onClick={() => escolherTipo("doador")}
        >
          Sou doador
        </button>

        <button
          type="button"
          aria-pressed={tipo === "ong"}
          disabled={carregando}
          onClick={() => escolherTipo("ong")}
        >
          Sou ONG
        </button>
      </div>

      <form onSubmit={cadastrar}>
        <label htmlFor="cad-nome">Nome</label>
        <input
          id="cad-nome"
          maxLength={100}
          name="nome"
          autoComplete="name"
          value={form.nome}
          onChange={atualizarCampo}
          required
        />
        {erroDo("nome")}

        <label htmlFor="cad-email">E-mail</label>
        <input
          id="cad-email"
          name="email"
          type="email"
          autoComplete="email"
          value={form.email}
          onChange={atualizarCampo}
          required
        />
        {erroDo("email")}

        <label htmlFor="cad-senha">Senha</label>
        <input
          id="cad-senha"
          name="senha"
          type="password"
          autoComplete="new-password"
          value={form.senha}
          onChange={atualizarCampo}
          required
        />
        {erroDo("senha")}

        <label htmlFor="cad-telefone">Telefone</label>
        <input
          id="cad-telefone"
          maxLength={20}
          name="telefone"
          type="tel"
          autoComplete="tel"
          value={form.telefone}
          onChange={atualizarCampo}
          required
        />
        {erroDo("telefone")}

        {tipo === "doador" ? (
          <>
            <label htmlFor="cad-documento">Documento</label>
            <input
              id="cad-documento"
              maxLength={18}
              name="documento"
              autoComplete="off"
              value={form.documento}
              onChange={atualizarCampo}
              required
            />
            {erroDo("documento")}
          </>
        ) : (
          <>
            <label htmlFor="cad-cnpj">CNPJ</label>
            <input
              id="cad-cnpj"
              maxLength={18}
              name="cnpj"
              autoComplete="off"
              value={form.cnpj}
              onChange={atualizarCampo}
              required
            />
            {erroDo("cnpj")}

            <label htmlFor="cad-endereco">Endereço</label>
            <input
              id="cad-endereco"
              name="endereco"
              autoComplete="street-address"
              value={form.endereco}
              onChange={atualizarCampo}
              required
            />
            {erroDo("endereco")}

            <label htmlFor="cad-cep">CEP</label>
            <input
              id="cad-cep"
              name="cep"
              inputMode="numeric"
              autoComplete="postal-code"
              placeholder="00000-000"
              pattern="\d{5}-?\d{3}"
              title="Digite os 8 números do CEP"
              value={form.cep}
              onChange={atualizarCampo}
              required
            />
            {erroDo("cep")}
          </>
        )}

        <button type="submit" disabled={carregando}>
          {carregando ? "Cadastrando..." : "Criar conta"}
        </button>
      </form>

      {mensagem && (
        <p
          className={
            mensagem.tipo === "erro" ? "mensagem-erro" : "mensagem-sucesso"
          }
          role={mensagem.tipo === "erro" ? "alert" : "status"}
        >
          {mensagem.texto}
        </p>
      )}
    </section>
  );
}

export default CadastroForm;
