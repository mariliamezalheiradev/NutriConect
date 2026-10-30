const FORMAS = {
  pino: (
    <>
      <path d="M12 21s7-6.2 7-11a7 7 0 10-14 0c0 4.8 7 11 7 11z" />
      <circle cx="12" cy="10" r="2.5" />
    </>
  ),
  coracao: <path d="M12 21s-7-4.5-7-10a4 4 0 017-2.6A4 4 0 0119 11c0 5.5-7 10-7 10z" />,
  mais: <path d="M12 5v14M5 12h14" />,
  panela: <path d="M6 13h12a6 6 0 01-12 0zM9 9a3 3 0 016 0M5 20h14" />,
};

export default function Icone({ nome }) {
  return (
    <svg
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
    >
      {FORMAS[nome]}
    </svg>
  );
}