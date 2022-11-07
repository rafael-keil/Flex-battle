import "./menuContainer.style.css";
import { DONUT_BACKGROUND } from "../../../assets";

export function MenuContainer({ children }) {
  return (
    <div className="menu-container__container">
      <img
        src={DONUT_BACKGROUND}
        className="menu-container__image"
        alt="Donut background"
      />
      <div className="menu-container__div">{children}</div>
    </div>
  );
}
