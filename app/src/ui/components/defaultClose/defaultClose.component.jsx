import "./defaultClose.style.css";

export function DefaultClose({ handleClose, img }) {
  return (
    <button
      className="default-close"
      onClick={handleClose}
      style={{ backgroundImage: `url(${img})` }}
    />
  );
}
