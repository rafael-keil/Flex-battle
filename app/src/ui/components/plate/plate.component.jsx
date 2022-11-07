import "./plate.style.css";

export function Plate({ color, size }) {
  return (
    <div className={`plate__order-${size}`}>
      <img
        className={`plate__image-${size}`}
        src={color}
        alt="imagem de prato do jogo"
      />
    </div>
  );
}
