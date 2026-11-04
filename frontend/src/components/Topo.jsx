import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function lerTemaInicial() {
  try {
    const guardado = localStorage.getItem("nc-tema");
    if (guardado === "dark" || guardado === "light") return guardado;
  } catch {
    // sem localStorage: segue o tema do sistema
  }
  return window.matchMedia?.("(prefers-color-scheme: dark)").matches ? "dark" : "light";
}

export default function Topo({ logado = false, children }) {
  const [tema, setTema] = useState(lerTemaInicial);

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", tema);
  }, [tema]);

  function alternarTema() {
    const novo = tema === "dark" ? "light" : "dark";
    setTema(novo);
    try {
      localStorage.setItem("nc-tema", novo);
    } catch {
      // sem localStorage: o tema vale só nesta visita
    }
  }

  return (
    <header className="topo">
      <div className="wrap">
        <Link className="marca" to={logado ? "/painel" : "/"} aria-label="NutriConect, voltar ao início">
          <svg viewBox="0 0 34 34" aria-hidden="true">
            <rect width="34" height="34" rx="10" fill="var(--brand)" />
            <path d="M9 22c0-7 5-12 15-13-1 9-6 14-13 14" fill="none" stroke="var(--brand-ink)" strokeWidth="2.2" strokeLinecap="round" />
            <path d="M9 25c3-6 7-9 11-11" fill="none" stroke="var(--brand-ink)" strokeWidth="2" strokeLinecap="round" />
          </svg>
          <span className="marca-txt">NutriConect</span>
        </Link>

        <div className="acoes">
          <button
            className={"tema" + (tema === "dark" ? " escuro" : "")}
            type="button"
            onClick={alternarTema}
            aria-label="Alternar entre tema claro e escuro"
          >
            <svg className="sol" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
              <circle cx="12" cy="12" r="4" />
              <path d="M12 2v2M12 20v2M4.9 4.9l1.4 1.4M17.7 17.7l1.4 1.4M2 12h2M20 12h2M4.9 19.1l1.4-1.4M17.7 6.3l1.4-1.4" />
            </svg>
            <svg className="lua" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M21 12.8A9 9 0 1111.2 3 7 7 0 0021 12.8z" />
            </svg>
          </button>
          {children}
        </div>
      </div>
    </header>
  );
}