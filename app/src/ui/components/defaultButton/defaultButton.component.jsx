import "./defaultButton.style.css";

export function DefaultButton({ children, ...props }) {
  return (
    <button className="default-button" {...props}>
      {children}
    </button>
  );
}
