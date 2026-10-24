
import { useState } from "react";
import { api } from "../services/api";

function CadastroForm() {
  const [tipo, setTipo] = useState("doador");
  const [form, setForm] = useState({
    nome: "",
    email: "",
    senha: "",
    telefone: "",
    documento: "",
    cnpj: "",
    endereco: "",
    cep: "",
  });
  const [mensagem, setMensagem] = useState("");
  const [erros, setErros] = useState({});
  const [carregando, setCarregando] = useState(false);

  function atualizarCampo(evento) {
    setForm({
      ...form,
      [evento.target.name]: evento.target.value,
    });
  }

  async function cadastrar(evento) {
    evento.preventDefault();
    setMensagem("");
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
      await api(
        tipo === "doador"
          ? "/api/doadores"
          : "/api/receptores",
        {
          method: "POST",
          body: JSON.stringify(dados),
        }
      );

      setMensagem("Conta criada com sucesso!");
      setForm({
        nome: "",
        email: "",
        senha: "",
        telefone: "",
        documento: "",
        cnpj: "",
        endereco: "",
        cep: "",
      });
    } catch (erro) {
      if (erro.status === 400) {
        setErros(erro.campos || {});
        setMensagem("Confira os campos informados.");
      } else if (erro.status === 422) {
        setMensagem("Este e-mail já está cadastrado.");
      } else if (erro.status === 404) {
        setMensagem("CEP não encontrado.");
      } else if (erro.status === 503) {
        setMensagem("Serviço indisponível. Tente novamente.");
      } else {
        setMensagem(erro.message || "Erro ao criar a conta.");
      }
    } finally {
      setCarregando(false);
    }
  }

  function erroDo(campo) {
    const erro = erros[campo];
    if (!erro) return null;

    return (
      <small role="alert">
        {Array.isArray(erro) ? erro.join(", ") : erro}
      </small>
    );
  }

  return (
    <section>
      <h2>Criar conta</h2>

      <div>
        <button
          type="button"
          disabled={carregando}
          onClick={() => {
            setTipo("doador");
            setMensagem("");
            setErros({});
          }}
        >
          Sou doador
        </button>

        <button
          type="button"
          disabled={carregando}
          onClick={() => {
            setTipo("ong");
            setMensagem("");
            setErros({});
          }}
        >
          Sou ONG
        </button>
      </div>

      <form onSubmit={cadastrar}>
        <label htmlFor="nome">Nome</label>
        <input
          id="nome"
          name="nome"
          value={form.nome}
          onChange={atualizarCampo}
          required
        />
        {erroDo("nome")}

        <label htmlFor="email">E-mail</label>
        <input
          id="email"
          name="email"
          type="email"
          value={form.email}
          onChange={atualizarCampo}
          required
        />
        {erroDo("email")}

        <label htmlFor="senha">Senha</label>
        <input
          id="senha"
          name="senha"
          type="password"
          value={form.senha}
          onChange={atualizarCampo}
          required
        />
        {erroDo("senha")}

        <label htmlFor="telefone">Telefone</label>
        <input
          id="telefone"
          name="telefone"
          value={form.telefone}
          onChange={atualizarCampo}
          required
        />
        {erroDo("telefone")}

        {tipo === "doador" ? (
          <>
            <label htmlFor="documento">Documento</label>
            <input
              id="documento"
              name="documento"
              value={form.documento}
              onChange={atualizarCampo}
              required
            />
            {erroDo("documento")}
          </>
        ) : (
          <>
            <label htmlFor="cnpj">CNPJ</label>
            <input
              id="cnpj"
              name="cnpj"
              value={form.cnpj}
              onChange={atualizarCampo}
              required
            />
            {erroDo("cnpj")}

            <label htmlFor="endereco">Endereço</label>
            <input
              id="endereco"
              name="endereco"
              value={form.endereco}
              onChange={atualizarCampo}
              required
            />
            {erroDo("endereco")}

            <label htmlFor="cep">CEP</label>
            <input
              id="cep"
              name="cep"
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

      {mensagem && <p role="status">{mensagem}</p>}
    </section>
  );
}

export default CadastroForm;
