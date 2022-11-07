import "./optionsButton.style.css";

export function OptionsButton({ children, ...props }) {
  return (
    <button className="options-button" {...props}>
      {children}
    </button>
  );
}
