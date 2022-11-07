import "./donut.style.css";
export function Donut({ color, size }) {
  return (
    <>
      <div className={`donut__order-${size}`}>
        <img
          className={`donut__image-${size}`}
          src={color}
          alt="imagem de donut do jogo"
        />
      </div>
    </>
  );
}
