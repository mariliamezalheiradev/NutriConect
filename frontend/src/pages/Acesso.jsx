import { useEffect, useState } from "react";
import Topo from "../components/Topo";
import Icone from "../components/Icone";
import LoginForm from "../components/LoginForm";
import CadastroForm from "../components/CadastroForm";

const INGREDIENTES = [
  "Arroz", "Feijão", "Cenoura", "Cebola", "Batata", "Tomate",
  "Banana", "Leite", "Ovos", "Frango", "Macarrão", "Alho",
];

const EQUIPE = [
  ["Marília", "mariliamezalheiradev"],
  ["Matheus", "matheusferreiradev"],
  ["Heloiza", "helogomes25"],
  ["Diana", "dianabritodev"],
  ["Marcelo", "marcelosantosdevv"],
  ["Grazielle", "graziellepatrocinio"],
  ["Pedro", "Ferreira573"],
  ["Lucas", "lucascostas-pixel"],
  ["Guilherme", "GuilhermeFernandes2006"],
];

function descer(id, bloco = "start") {
  document.getElementById(id)?.scrollIntoView({ behavior: "smooth", block: bloco });
}

// O conteúdo é repetido para a faixa rolar sem emendas.
function Trilha({ nomes, className = "trilha" }) {
  return (
    <div className={className}>
      {[...nomes, ...nomes].map((nome, i) => (
        <span key={i}>{nome}</span>
      ))}
    </div>
  );
}

function Acesso() {
  // null (fechada) ou { aba: "log" | "cad", tipo: null | "doador" | "ong" }
  const [modal, setModal] = useState(null);
  const aberto = modal !== null;

  useEffect(() => {
    if (!aberto) return undefined;

    document.body.style.overflow = "hidden";
    function aoApertarTecla(evento) {
      if (evento.key === "Escape") setModal(null);
    }
    document.addEventListener("keydown", aoApertarTecla);

    return () => {
      document.body.style.overflow = "";
      document.removeEventListener("keydown", aoApertarTecla);
    };
  }, [aberto]);

  const abrir = (aba, tipo = null) => setModal({ aba, tipo });
  const irAba = (aba) => setModal((atual) => ({ ...atual, aba }));
  const escolherTipo = (tipo) => setModal((atual) => ({ ...atual, tipo }));
  const fechar = () => setModal(null);

  return (
    <>
      <Topo>
        <span className="acoes">
          <button className="btn sm" type="button" onClick={() => abrir("log")}>
            Entrar
          </button>
          <button className="btn sm" type="button" onClick={() => abrir("cad")}>
            Cadastrar
          </button>
        </span>
      </Topo>

      <main>
        <section className="tela">
          <div className="acesso">
            <div className="wrap hero">
              <div className="hero-txt">
                <span className="selo">ODS 2 · Fome Zero</span>
                <h1>Nenhum alimento perdido.</h1>
                <p className="lead">
                  Doe o que sobra, encontre ONGs perto de você e descubra receitas com o que tem em casa.
                </p>
                <div className="par-botoes">
                  <button className="btn baixo" type="button" onClick={() => descer("como-funciona")}>
                    Saiba mais
                  </button>
                </div>
                <ul className="pontos">
                  <li>
                    <span className="ic"><Icone nome="coracao" /></span>
                    Doe ingredientes em poucos toques
                  </li>
                  <li>
                    <span className="ic"><Icone nome="pino" /></span>
                    Veja as ONGs mais próximas do seu CEP
                  </li>
                  <li>
                    <span className="ic"><Icone nome="panela" /></span>
                    Receitas de aproveitamento total com IA
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <div className="wrap">
            <h2 className="faixa-titulo">Do arroz ao tomate, tudo pode virar prato.</h2>
          </div>
          <div className="marquee" aria-hidden="true">
            <Trilha nomes={INGREDIENTES} />
            <Trilha nomes={[...INGREDIENTES].reverse()} className="trilha volta" />
          </div>

          <div className="ondas" aria-hidden="true">
            <svg viewBox="0 0 1440 160" preserveAspectRatio="none">
              <path d="M0 70 C240 20 480 110 720 70 C960 30 1200 100 1440 55 L1440 160 L0 160Z" fill="var(--colina-2)" />
              <path d="M0 105 C300 65 520 135 800 100 C1060 70 1260 125 1440 95 L1440 160 L0 160Z" fill="var(--colina-1)" />
            </svg>
          </div>

          <div className="faixa-verde">
            <div className="wrap recurso" id="como-funciona">
              <div>
                <span className="selo">Como funciona</span>
                <h2>Da sobra de hoje ao prato de quem precisa.</h2>
                <p className="texto">Em três passos, o que ia para o lixo vira ajuda e vira comida.</p>
                <ol className="passos">
                  <li>
                    <span className="num">1</span>
                    <div>
                      <b>Cadastre o que vai doar</b>
                      <small>Escolha os ingredientes e as quantidades de uma vez.</small>
                    </div>
                  </li>
                  <li>
                    <span className="num">2</span>
                    <div>
                      <b>Encontre a ONG mais perto</b>
                      <small>Informe o CEP e veja a lista, da mais próxima à mais distante.</small>
                    </div>
                  </li>
                  <li>
                    <span className="num">3</span>
                    <div>
                      <b>Aproveite o que sobrou</b>
                      <small>A IA sugere uma receita de aproveitamento total.</small>
                    </div>
                  </li>
                </ol>
                <button className="btn baixo" type="button" onClick={() => descer("convite", "center")}>
                  Como participar
                </button>
              </div>

              <div className="palco" aria-hidden="true">
                <svg className="folha-deco" viewBox="0 0 200 200">
                  <path d="M16 184C16 82 88 18 184 16 184 112 118 184 16 184Z" fill="currentColor" />
                  <path d="M16 184C66 124 108 84 150 48" fill="none" stroke="var(--surface-2)" strokeWidth="4" strokeLinecap="round" />
                </svg>
                <span className="bolha b1"><Icone nome="pino" /></span>
                <span className="bolha b2"><Icone nome="coracao" /></span>
                <span className="bolha b3"><Icone nome="panela" /></span>
                <div className="pilha">
                  <div className="mini mini-a">
                    <b>ONGs perto de você</b>
                    <div className="linha"><span>Mesa Cheia</span><em>0,0 km</em></div>
                    <div className="linha"><span>Cozinha Solidária</span><em>2,5 km</em></div>
                  </div>
                  <div className="mini mini-b">
                    <span className="chip sol" style={{ justifySelf: "start" }}>Receita pronta</span>
                    <b>Arroz cremoso com feijão</b>
                    <small>35 minutos</small>
                    <div className="mini-chips"><span>Arroz</span><span>Feijão</span><span>Cenoura</span></div>
                  </div>
                  <div className="mini mini-c">
                    <div className="ok-linha"><span className="ok">✓</span><b>Doação registrada</b></div>
                    <small>2 itens · Mesa Cheia</small>
                  </div>
                </div>
              </div>
            </div>

            <div className="giga" aria-hidden="true">
              <Trilha nomes={["Doe", "Receba", "Aproveite", "Doe", "Receba", "Aproveite"]} />
            </div>

            <div className="wrap cta-grade" id="convite">
              <div className="cta escura">
                <h2>Tem comida sobrando?</h2>
                <p>Cadastre-se como doador e encontre quem pode receber.</p>
                <button className="btn" type="button" onClick={() => abrir("cad", "doador")}>
                  Quero doar
                </button>
              </div>
              <div className="cta viva">
                <h2>Sua ONG recebe doações?</h2>
                <p>Cadastre a instituição com o CEP e apareça para os doadores da região.</p>
                <button className="btn" type="button" onClick={() => abrir("cad", "ong")}>
                  Sou uma ONG
                </button>
              </div>
            </div>

            <div className="wrap">
              <footer className="rodape">
                <svg className="rodape-folha" viewBox="0 0 200 200" aria-hidden="true">
                  <path d="M16 184C16 82 88 18 184 16 184 112 118 184 16 184Z" fill="currentColor" />
                </svg>
                <div className="rodape-topo">
                  <span className="selo">O grupo</span>
                  <h2>Quem faz o NutriConect</h2>
                  <p>
                    Nove pessoas, um objetivo: nenhum alimento perdido. Toque em um nome para ver o perfil no GitHub.
                  </p>
                </div>
                <ul className="equipe">
                  {EQUIPE.map(([nome, usuario]) => (
                    <li key={usuario}>
                      <a
                        className="membro"
                        href={`https://github.com/${usuario}`}
                        target="_blank"
                        rel="noopener noreferrer"
                      >
                        <span className="avatar">{nome[0]}</span>
                        <span className="nome">
                          <b>{nome}</b>
                          <small>@{usuario}</small>
                        </span>
                        <span className="ir" aria-hidden="true">↗</span>
                      </a>
                    </li>
                  ))}
                </ul>
                <div className="rodape-base">
                  <span className="rodape-marca">
                    <svg viewBox="0 0 34 34" aria-hidden="true">
                      <rect width="34" height="34" rx="10" fill="#6CCB8C" />
                      <path d="M9 22c0-7 5-12 15-13-1 9-6 14-13 14" fill="none" stroke="#0B2216" strokeWidth="2.2" strokeLinecap="round" />
                      <path d="M9 25c3-6 7-9 11-11" fill="none" stroke="#0B2216" strokeWidth="2" strokeLinecap="round" />
                    </svg>
                    NutriConect
                  </span>
                  <span>Projeto acadêmico em apoio ao ODS 2: Fome Zero e Agricultura Sustentável.</span>
                  <span className="rodape-ods">ODS 2 · Fome Zero</span>
                </div>
              </footer>
            </div>
          </div>
        </section>
      </main>

      {modal && (
        <div className="modal" onClick={(evento) => evento.target === evento.currentTarget && fechar()}>
          <div className="cartao" role="dialog" aria-modal="true" aria-labelledby="modal-titulo">
            <div className="modal-topo">
              <b id="modal-titulo">Acesse sua conta</b>
              <button className="fechar" type="button" aria-label="Fechar" onClick={fechar}>
                ×
              </button>
            </div>

            <div className="abas" data-aba={modal.aba} role="tablist">
              <span className="marca-aba" />
              <button className="aba" type="button" role="tab" aria-selected={modal.aba === "log"} onClick={() => irAba("log")}>
                Entrar
              </button>
              <button className="aba" type="button" role="tab" aria-selected={modal.aba === "cad"} onClick={() => irAba("cad")}>
                Cadastro
              </button>
            </div>

            {modal.aba === "log" && (
              <>
                <LoginForm />
                <p className="link-alternativo">
                  Não tem uma conta?{" "}
                  <button className="link-botao" type="button" onClick={() => irAba("cad")}>
                    Se cadastre
                  </button>
                </p>
              </>
            )}

            {modal.aba === "cad" && !modal.tipo && (
              <div className="escolha">
                <p>Como você quer participar do NutriConect?</p>
                <button className="op" type="button" onClick={() => escolherTipo("doador")}>
                  <b>Sou doador</b>
                  <span>Tenho comida ou ingredientes para doar</span>
                </button>
                <button className="op" type="button" onClick={() => escolherTipo("ong")}>
                  <b>Sou ONG</b>
                  <span>Minha instituição recebe doações</span>
                </button>
              </div>
            )}

            {modal.aba === "cad" && modal.tipo && (
              <CadastroForm key={modal.tipo} tipoInicial={modal.tipo} />
            )}
          </div>
        </div>
      )}
    </>
  );
}

export default Acesso;